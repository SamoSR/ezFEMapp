/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;


import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author GermanSR
 */
public abstract class CanvasPane2D_1 extends StackPane{
    
 
   
    boolean isTouchMoving;
    boolean isTouchScreen;
    
    Group rootNode;
    Group allGeomtry;
    
    SubScene scene;
    AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();
    
    double scale = 1;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    
    public CanvasPane2D_1(){  

        rootNode = new Group();
        allGeomtry = new Group(rootNode);
        
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        
        scene = new SubScene(allGeomtry, 0, 0);
        scene.setFill(javafx.scene.paint.Color.WHITE);
        //FIX THE SCENE SIZE TO MATCH THE PARENT PANEL
    
        //CANVAS SUB-SCENE
        //ADD SUB-SCENE TO CONTAINER PANE
        scene.heightProperty().bind(this.heightProperty());
        scene.widthProperty().bind(this.widthProperty());
     
        this.setPrefSize(0, 0);
        this.setMinSize(0, 0);
        this.getChildren().add(scene);

        
        PointLight pointlight = new PointLight(Color.WHITE); 
        allGeomtry.getChildren().add(pointlight);
        
        PerspectiveCamera camera = new PerspectiveCamera(false); 
        camera.setTranslateX(0); 
        camera.setTranslateY(0); 
        camera.setTranslateZ(0);
         
        pointlight.setTranslateZ(-1000000); 
        pointlight.setTranslateX(+0); 
        pointlight.setTranslateY(+0); 
        
        scene.setCamera(camera);
        
        handleMouse(scene);     
    }
    
    public void bindSize(ReadOnlyDoubleProperty width,ReadOnlyDoubleProperty height){
        scene.heightProperty().bind(width);
        scene.widthProperty().bind(height);
    }


    public SubScene getSubScene(){
        return scene;
    }
    
    public void clear(){
        rootNode.getChildren().clear();
        
    }

    public void addGeometry(Node geom){
        Rectangle r = new Rectangle();
        double size = 10000000;
        r.setWidth(size);
        r.setHeight(size);
        r.setTranslateX(-size/2);
        r.setTranslateY(-size/2);
        r.setFill(Color.TRANSPARENT);
        rootNode.getChildren().add(r);
        rootNode.getChildren().add(geom);
    }
    
    public Group getRootNode(){
        return rootNode;
    }
    
    public void resetCamera(){
        rootNode.setTranslateX(0);
        rootNode.setTranslateY(0);
        rootNode.setScaleX(1);
        rootNode.setScaleY(1);
    }
    
    public void computeMouseMotion(MouseEvent event){
        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        mousePosX = event.getSceneX();
        mousePosY = event.getSceneY();
        mouseDeltaX = (mousePosX - mouseOldX); 
        mouseDeltaY = (mousePosY - mouseOldY);
    }
    
    public void computeMouseMotionTouch(TouchEvent event){
        TouchPoint p = event.getTouchPoint();
        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        mousePosX = p.getSceneX();
        mousePosY = p.getSceneY();
        mouseDeltaX = (mousePosX - mouseOldX); 
        mouseDeltaY = (mousePosY - mouseOldY);
    }
    
    public void setBackgroundColor(Color background){
        scene.setFill(background);
    }
    
    private void handleKeyboard(SubScene scene){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                
               switch (event.getCode()) {
                   case ESCAPE:    
                       
                       break;
                   case X:     
                       
                        break;
                    case V:
                       break;
                       
                    case SPACE:
                        resetCamera();
                        break;
               } 
            } 
        }); 
        
    }//  handleKeyboard()
    
    boolean screenPressedSingle=true;
    boolean onePointOnly;
    boolean ignoreTouch;
    boolean isZooming;
    Point2D firstTouch;
    
    //total distance since first touch
    double cumulativeTouchDistance=0;
    
    double twoPointDistance=0;
    boolean zoomingOUT;
    long clickTimerMilis = 200;
    //boolean doubleClick=false;
    long doubleClickTimer=System.currentTimeMillis();
  
    TouchPoint first;
    
    private void handleMouse(SubScene scene) {
        
        
        //MOUSE CLICK EVENT
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent me) {
                if(isTouchScreen){
                    return;
                }
                singleTapMouse(me);
            }
        });
        

        
        scene.setOnTouchReleased(new EventHandler<TouchEvent>() {
            @Override 
            public void handle(TouchEvent event) {
                if(isTouchMoving){
                    if(event.getTouchCount()==1){
                        isTouchMoving=false;   
                        cumulativeTouchDistance=0;
                    }
                    return;
                }
       
                if(screenPressedSingle==false){
                    return;
                }
                long totalTime = System.currentTimeMillis()-doubleClickTimer;
                doubleClickTimer = System.currentTimeMillis();
                if(totalTime<clickTimerMilis){
                    doubleTapTouch(event);
                }else{
                   
                    first =     event.getTouchPoint();  
                    singleTapTouch(event);
                    
                }
                

                
            }
        });
        
        scene.setOnTouchPressed(new EventHandler<TouchEvent>() {
            @Override 
            public void handle(TouchEvent e) {
                isTouchScreen=true;
                onePointOnly=false;
                ignoreTouch=false;  
                if(e.getTouchPoints().size()==1){
                    firstTouch=new Point2D(e.getTouchPoint().getX(),e.getTouchPoint().getY());
                    onePointOnly=true;
                    screenPressedSingle=true;    
                }else if(e.getTouchPoints().size()==2){
                    Point2D p1 =new Point2D(e.getTouchPoints().get(0).getX(),e.getTouchPoints().get(0).getY());
                    Point2D p2 =new Point2D(e.getTouchPoints().get(1).getX(),e.getTouchPoints().get(1).getY());
                    firstTouch = p1.midpoint(p2);
                    twoPointDistance = p1.distance(p2);
                    screenPressedSingle=false;
                }else{
                   ignoreTouch=true;
                } 
            }
        });
        

        
        //MOUSE MOVE EVENT
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
               computeMouseMotion(me);     
            }
        });
       

        
        //ZOOMING WITH TOUCH (PINCH GESTURE)
        scene.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override 
            public void handle(ZoomEvent event) {
                if(ignoreTouch){
                    return;
                }
                screenPressedSingle=false;
                isTouchMoving=true;
                double zoomFactor = 1.015;
                if (event.getZoomFactor()<1) {
                    zoomFactor = 1 / zoomFactor;
                }    
                zoomOperator.zoom(rootNode, zoomFactor, firstTouch.getX(), firstTouch.getY()); 
                event.consume();
            }
        });
        
        //ZOOMING WITH TOUCH (PINCH GESTURE)
        scene.setOnZoomStarted(new EventHandler<ZoomEvent>() {
            @Override 
            public void handle(ZoomEvent event) {
                isZooming = true;
            }
        });
        //ZOOMING WITH TOUCH (PINCH GESTURE)
        scene.setOnZoomFinished(new EventHandler<ZoomEvent>() {
            @Override 
            public void handle(ZoomEvent event) {
                isZooming = false;
            }
        });
       
        //ZOOMING WITH MOUSE  
        scene.setOnScroll((ScrollEvent event) -> {  
            if(isTouchScreen){
                return;
            }
            double zoomFactor = 1.1;
            if (event.getDeltaY() <= 0) {
                zoomFactor = 1 / zoomFactor;
            }
            zoomOperator.zoom(rootNode, zoomFactor, event.getX(), event.getY());     
        });

        
        //PANNING WITH MOUSE
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent me) {  
                if(isTouchScreen){           
                    return;
                }
                computeMouseMotion(me); 
                if(me.isMiddleButtonDown()){
                    rootNode.setTranslateX((rootNode.getTranslateX() + mouseDeltaX*0.5));
                    rootNode.setTranslateY((rootNode.getTranslateY() + mouseDeltaY*0.5)); 
                    me.consume();
                } 
           }
        });
        
        //PANNING WITH TOUCH
        scene.setOnTouchMoved(new EventHandler<TouchEvent>() {
            @Override 
            public void handle(TouchEvent e) {
                
                Point2D current =new Point2D(e.getTouchPoint().getX(),e.getTouchPoint().getY());
                cumulativeTouchDistance += current.distance(firstTouch);
                if(cumulativeTouchDistance>100){
                    isTouchMoving=true; 
                }
                
                if(isZooming){
                    return;
                }
                if(ignoreTouch){
                    return;
                }
                if(!onePointOnly){
                    if(e.getTouchPoints().size()==2){
                        Point2D p1 =new Point2D(e.getTouchPoints().get(0).getX(),e.getTouchPoints().get(0).getY());
                        Point2D p2 =new Point2D(e.getTouchPoints().get(1).getX(),e.getTouchPoints().get(1).getY());
                        double newDistance = p1.distance(p2);
                        if(newDistance>twoPointDistance){
                            zoomingOUT=false;
                        }else{
                            zoomingOUT=true;
                        }
                        twoPointDistance = newDistance;
                    }
                    return;
                }
                computeMouseMotionTouch(e);
                rootNode.setTranslateX((rootNode.getTranslateX() + mouseDeltaX));
                rootNode.setTranslateY((rootNode.getTranslateY() + mouseDeltaY));
                e.consume();
            }
        });

    } //handleMouse
    
    
    public void translateRoot(double dx, double dy){
        rootNode.setTranslateX((rootNode.getTranslateX() + dx));
        rootNode.setTranslateY((rootNode.getTranslateY() + dy));  
    }
    
    public void zoomOut(double factor, double x, double y){
       zoomOperator.zoom(rootNode,factor,x,y);
    }
    public void resetScale(){
        rootNode.setScaleX(1.0);
        rootNode.setScaleY(1.0);
    }
    
    public abstract void doubleTapTouch(TouchEvent event);
    public abstract void singleTapTouch(TouchEvent event);
    public abstract void singleTapMouse(MouseEvent event);
    
    public class AnimatedZoomOperator {
        
   
        
    public AnimatedZoomOperator() {         
       
    }
    
    public void zoom(Node node, double factor, double x, double y) {    
        // determine scale
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        
        node.setTranslateX(node.getTranslateX()-f*dx);
        node.setTranslateY(node.getTranslateY()-f*dy);
        node.setScaleX(scale);
        node.setScaleY(scale);
   
        /*
        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
            new KeyFrame(Duration.millis(0), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
            new KeyFrame(Duration.millis(0), new KeyValue(node.scaleXProperty(), scale)),
            new KeyFrame(Duration.millis(0), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();*/
    }

}
    
    
}
