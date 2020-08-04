/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.shapes;


import ezfemapp.blockProject.SupportBlock;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.rendering.canvas.BlockRenderer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.fxyz3d.geometry.Point3D;


/**
 *
 * @author GermanSR
 */
public class ShapeDrawer {
    
    
    public static Group drawArrowIcon(Color color, double size){
        double stroke = 2;
        double head = 6;
        Group icon = new Group();
        Point2D arrow1A  = new Point2D(-size/2,size);
        Point2D arrow1B  = new Point2D(-size/2,0);
        Point2D arrow2A  = new Point2D(size/2,size);
        Point2D arrow2B  = new Point2D(size/2,0);
        icon.getChildren().add(drawArrowDown(arrow1A,arrow1B,stroke,head,color));
        icon.getChildren().add(drawArrowDown(arrow2A,arrow2B,stroke,head,color));
        Line lineTop = new Line(arrow1B.getX(),arrow1B.getY(),arrow2B.getX(),arrow2B.getY());
        lineTop.setStrokeWidth(stroke);
        //lineTop.setTranslateY(-1);
        lineTop.setStroke(color);
        icon.getChildren().add(lineTop);
        return icon;
    }
    
    
     public static Group drawArrowLoad(Color color, double size){
        double stroke = 2;
        double head = 8;
        Group icon = new Group();
        Point2D arrow1A  = new Point2D(-size/2,size);
        Point2D arrow1B  = new Point2D(-size/2,size/2);
        Point2D arrow2A  = new Point2D(size/2,size);
        Point2D arrow2B  = new Point2D(size/2,size/2);
        icon.getChildren().add(drawArrowDown(arrow1A,arrow1B,stroke,head,color));
        icon.getChildren().add(drawArrowDown(arrow2A,arrow2B,stroke,head,color));
        Line lineTop = new Line(arrow1B.getX(),arrow1B.getY(),arrow2B.getX(),arrow2B.getY());
        lineTop.setStrokeWidth(stroke);
        //lineTop.setTranslateY(-1);
        lineTop.setStroke(color);
        icon.getChildren().add(lineTop);
        return icon;
    }
    
    public static Group drawSupportIcon(Color color, double size){
        double stroke = 3;
        double head = size;
        Group icon = new Group();
        Point2D p = new Point2D(0,0);
        icon.getChildren().add(drawTriangleUp(p, head, color));   
        Line l = new Line(p.getX()-head, p.getY()+head+5, p.getX()+head, p.getY()+head+5);
        l.setStrokeWidth(stroke);
        l.setStroke(color);
        icon.getChildren().add(l); 
        return icon;
    }
    
    public static Group drawArrowDown(Point2D pA, Point2D pB, double linewidth, double arrowSize, Color color){
        
        Group arrow = new Group();
        Line l = new Line(pA.getX(), pA.getY()-arrowSize, pB.getX(), pB.getY());
        l.setFill(color);
        l.setStrokeWidth(linewidth);
        l.setStroke(color);
        
        Point2D pt1 = new Point2D(pA.getX(),pA.getY());
        Point2D pt2 = new Point2D(pA.getX()+arrowSize,pA.getY()-arrowSize);
        Point2D pt3 = new Point2D(pA.getX()-arrowSize,pA.getY()-arrowSize);
  
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(pt1.getX(),pt1.getY(),
                                           pt2.getX(),pt2.getY(),
                                           pt3.getX(),pt3.getY());
        polygonGeometry.setFill(color);
        
        arrow.getChildren().addAll(l,polygonGeometry); 
        return arrow;
    }
    
    public static Polygon drawTriangleDown(Point2D pA, double arrowSize, Color color){   
        Point2D pt1 = new Point2D(pA.getX(),pA.getY());
        Point2D pt2 = new Point2D(pA.getX()+arrowSize,pA.getY()-arrowSize);
        Point2D pt3 = new Point2D(pA.getX()-arrowSize,pA.getY()-arrowSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(pt1.getX(),pt1.getY(),
                                           pt2.getX(),pt2.getY(),
                                           pt3.getX(),pt3.getY());
        polygonGeometry.setFill(color);    
        return polygonGeometry;
    }
    public static Polygon drawTriangleUp(Point2D pA, double arrowSize, Color color){   
        Point2D pt1 = new Point2D(pA.getX(),pA.getY());
        Point2D pt2 = new Point2D(pA.getX()+arrowSize,pA.getY()+arrowSize);
        Point2D pt3 = new Point2D(pA.getX()-arrowSize,pA.getY()+arrowSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(pt1.getX(),pt1.getY(),
                                           pt2.getX(),pt2.getY(),
                                           pt3.getX(),pt3.getY());
        polygonGeometry.setFill(color);    
        return polygonGeometry;
    }
    
    public static Node createBlockIcon(double size,Color color){   
        double gridRenderSize = size;
        int row = 0;
        int col = 0;
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
     
        polygonGeometry.setFill(color);
        
        blockGeometry.getChildren().add(polygonGeometry);
  
        return blockGeometry;
    }
    
    
    public static Node createNullLoadCaseIcon(double size,Color colorCircle,Color colorLine){       
        Group blockGeometry = new Group();
        Circle c = new Circle(size,colorCircle);
        blockGeometry.getChildren().add(c);  
        Line l = new Line();
        l.setStroke(colorLine);
        l.setStrokeWidth(2);
        l.setStartX(-size);
        l.setStartY(-size);
        l.setEndX(size);
        l.setEndY(size);
        blockGeometry.getChildren().add(l);  
        return blockGeometry;
    }
    
    public static Node createLoadCaseIcon(double size,Color colorCircle){       
        Circle c = new Circle(size,colorCircle); 
        return c;
    }
    
    public static Node createNullBlockIcon(double size,Color color, Color colorLine){   
        double gridRenderSize = size;
        int row = 0;
        int col = 0;
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
     
        polygonGeometry.setFill(color);
        //polygonGeometry.setStroke(color);
        blockGeometry.getChildren().add(polygonGeometry);
        double f = size*0.05;
        Line l = new Line();
        l.setStroke(colorLine);
        l.setStrokeWidth(2);
        l.setStartX(-f);
        l.setStartY(-f);
        l.setEndX(size+f);
        l.setEndY(size+f);
        blockGeometry.getChildren().add(l); 
        /*
        Point2D[] points = new Point2D[]{p1,p2,p3,p4};
        for(Point2D p:points){
            Circle c = new Circle();
            c.setCenterX(p.getX());
            c.setCenterY(p.getY());
            c.setRadius(gridRenderSize/2/6);
            c.setFill(Color.BLACK);
            blockGeometry.getChildren().add(c);
        } */
        return blockGeometry;
    }
    
    
    public static Node createDrawModeDoubleIcon(double size,Color color){   
        
        double gridRenderSize = 5;
        int row = 0;
        int col = 0;
        double offset=7;
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon b1 = new Polygon();
        b1.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
     
        b1.setFill(color);
        
        Polygon b2 = new Polygon();
        b2.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
     
        b2.setFill(color);
        //polygonGeometry.setStroke(color);
        blockGeometry.getChildren().add(b1);
        blockGeometry.getChildren().add(b2);
        
        double w = offset*2;
        Rectangle r = new Rectangle(w,w);
        r.setFill(Color.TRANSPARENT);
        r.setStrokeWidth(1);
        r.setStroke(color);
        
        
        b1.setTranslateX(-size/2);
        b1.setTranslateY(-size/2);
        b2.setTranslateX(offset+size);
        b2.setTranslateY(offset+size);
        blockGeometry.getChildren().add(r);
        /*
        Point2D[] points = new Point2D[]{p1,p2,p3,p4};
        for(Point2D p:points){
            Circle c = new Circle();
            c.setCenterX(p.getX());
            c.setCenterY(p.getY());
            c.setRadius(gridRenderSize/2/6);
            c.setFill(Color.BLACK);
            blockGeometry.getChildren().add(c);
        } */
        return blockGeometry;
    }
    
    public static Node createGridIcon(double size, Color color){   
        double n=size/2;
        return draw2Dmesh(-n,n,-n,n,n,n, color);
    }
    
    public static Group draw2Dmesh(double xmin, double xmax, double ymin, double ymax, double dx, double dy,Color color){
        
        Group grid = new Group();
        
        int nx = (int)((xmax-xmin)/dx)+1;
        int ny = (int)((ymax-ymin)/dy)+1;
        
        //lineas verticales
        for(int i=0;i<nx;i++){
            double x0 = xmin+dx*i;
            double y0 = ymin;
            double xf = xmin+dx*i;
            double yf = ymax;
            Line l = new Line();
            l.setStroke(color);
            l.setStartX(x0);
            l.setStartY(y0);
            l.setEndX(xf);
            l.setEndY(yf);
            grid.getChildren().add(l);   
        }
        //lineas horizontales
        for(int i=0;i<ny;i++){
            double x0 = xmin;
            double y0 = ymin+dy*i;
            double xf = xmax;
            double yf = ymin+dy*i;
            Line l = new Line();
            l.setStroke(color);
            l.setStartX(x0);
            l.setStartY(y0);
            l.setEndX(xf);
            l.setEndY(yf);
            grid.getChildren().add(l);    
        }
        return grid;
    }
    
    
    public static Node MathSubscriptIcon(String number, String subscript){
        StackPane pane = new StackPane();
        Text t1 = new Text(number);
        t1.setFont(Font.font(GUImanager.defaultFont, FontWeight.BOLD, 20));
        t1.setFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        t1.setTranslateX(-6);
        Text t2 = new Text(subscript);
        t2.setFont(new Font(GUImanager.defaultFont, 12));
        t2.setFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        t2.setTranslateX(+t1.getBoundsInParent().getMaxX());
        t2.setTranslateY(4);       
        pane.getChildren().addAll(t1,t2);
        return pane;
    }
    
    public static Node createSupportGeometry(String type,double gridRenderSize, double lineWdith, Color color){
        
        int row = 0;
        int col = 0;
        
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId("b,"+row+","+col);
        
        polygonGeometry.setStroke(Color.TRANSPARENT);
        polygonGeometry.setFill(Color.TRANSPARENT);
        //blockGeometry.getChildren().add(polygonGeometry);
        
        double r = gridRenderSize/2/4;
        double positions[]= new double[]{-gridRenderSize/2+r*1.5,0,gridRenderSize/2-r*1.5};
        Point2D midPoint = p1.midpoint(p3);
        
        if(type.equals(SupportBlock.SUPPORT_FIXED)){
            double sizeReduction = 6;
            Rectangle re = new Rectangle();
            re.setFill(Color.TRANSPARENT);
            re.setStroke(color);
            re.setStrokeWidth(2);
            re.setWidth(gridRenderSize);
            re.setHeight(gridRenderSize);
            re.setTranslateX(p1.getX()+gridRenderSize/2);
            re.setTranslateY(p1.getY()+gridRenderSize/2);
            blockGeometry.getChildren().add(re);
            
            re = new Rectangle();
            re.setFill(color);
            re.setWidth(gridRenderSize-sizeReduction);
            re.setHeight(gridRenderSize-sizeReduction);
            re.setTranslateX(p1.getX()+gridRenderSize/2+sizeReduction/2);
            re.setTranslateY(p1.getY()+gridRenderSize/2+sizeReduction/2);
            blockGeometry.getChildren().add(re);
        }else if(type.equals(SupportBlock.SUPPORT_PINNED_HORZ)){

            for(int i=0;i<3;i++){
                Circle c = new Circle();
                c.setCenterX(midPoint.getX()-gridRenderSize/2+r*1.0+lineWdith);
                c.setCenterY(midPoint.getY()+positions[i]);
                c.setRadius(r);
                c.setFill(color);
                blockGeometry.getChildren().add(c);
                c = new Circle();
                c.setCenterX(midPoint.getX()+gridRenderSize/2-r*1.0-lineWdith);
                c.setCenterY(midPoint.getY()+positions[i]);
                c.setRadius(r);
                c.setFill(color);
                blockGeometry.getChildren().add(c);
            }
            
            Line l1 = new Line();
            l1.setStartX(p1.getX()+lineWdith/2);
            l1.setEndX(p2.getX()+lineWdith/2);
            l1.setStartY(p1.getY());
            l1.setEndY(p2.getY());
            l1.setStrokeWidth(lineWdith);
            l1.setStroke(color);
            blockGeometry.getChildren().add(l1);
            l1 = new Line();
            l1.setStartX(p3.getX()-lineWdith/2);
            l1.setEndX(p4.getX()-lineWdith/2);
            l1.setStartY(p3.getY());
            l1.setEndY(p4.getY());
            l1.setStrokeWidth(lineWdith);
            l1.setStroke(color);
            blockGeometry.getChildren().add(l1);
            
        }else if(type.equals(SupportBlock.SUPPORT_PINNED_VERT)){
            for(int i=0;i<3;i++){
                Circle c = new Circle();
                c.setCenterX(midPoint.getX()+positions[i]);
                c.setCenterY(midPoint.getY()-gridRenderSize/2+r+lineWdith);
                c.setRadius(r);
                c.setFill(color);
                blockGeometry.getChildren().add(c);
                c = new Circle();
                c.setCenterX(midPoint.getX()+positions[i]);
                c.setCenterY(midPoint.getY()+gridRenderSize/2-r-lineWdith);
                c.setRadius(r);
                c.setFill(color);
                blockGeometry.getChildren().add(c);
            }   
            Line l1 = new Line();
            l1.setStartX(p1.getX());
            l1.setEndX(p4.getX());
            l1.setStartY(p1.getY()+lineWdith/2);
            l1.setEndY(p4.getY()+lineWdith/2);
            l1.setStrokeWidth(lineWdith);
            l1.setStroke(color);
            blockGeometry.getChildren().add(l1);
            l1 = new Line();
            l1.setStartX(p2.getX());
            l1.setEndX(p3.getX());
            l1.setStartY(p2.getY()-lineWdith/2);
            l1.setEndY(p3.getY()-lineWdith/2);
            l1.setStrokeWidth(lineWdith);
            l1.setStroke(color);
            blockGeometry.getChildren().add(l1);     
        }
        /*
        Point2D[] points = new Point2D[]{p1,p2,p3,p4};
        for(Point2D p:points){
            Circle c = new Circle();
            c.setCenterX(p.getX());
            c.setCenterY(p.getY());
            c.setRadius(gridRenderSize/2/10);
            c.setFill(Color.BLACK);
            blockGeometry.getChildren().add(c);
        }*/
        
        return blockGeometry;
    }
    
    public static Node drawPlane2(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Color colorLines){
    
        PhongMaterial matLines = new PhongMaterial();
        matLines.setDiffuseColor(colorLines);

        //FILL SHAPE
        TriangleMesh mesh2 = new TriangleMesh();
        mesh2.getPoints().addAll(
            (float)p1.x,  (float)p1.y,    (float)p1.z,          
            (float)p2.x,  (float)p2.y,    (float)p2.z,                 
            (float)p3.x,  (float)p3.y,    (float)p3.z,       
            (float)p4.x,  (float)p4.y,    (float)p4.z                
        );
        
        mesh2.getFaces().addAll(
            0,0,  1,0,  1,0,         
            1,0,  2,0,  2,0,
            2,0,  3,0,  3,0,
            3,0,  0,0,  0,0  
        ); 
        
        mesh2.getTexCoords().addAll(0,0);
        
        MeshView planeGeom2 = new MeshView(mesh2);
        planeGeom2.setDrawMode(DrawMode.LINE);
        planeGeom2.setMaterial(matLines);
 
        return planeGeom2;             
    }
    
    public static Node drawPlane(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Color colorLines){
    
        Group g = new Group();
        
        PhongMaterial matLines = new PhongMaterial();
        matLines.setDiffuseColor(colorLines);

        Line l1 = BlockRenderer.drawLine(new Point2D(p1.x,p1.y), new Point2D(p2.x,p2.y), colorLines, 1.5);
        Line l2 = BlockRenderer.drawLine(new Point2D(p2.x,p2.y), new Point2D(p3.x,p3.y), colorLines, 1.5);
        Line l3 = BlockRenderer.drawLine(new Point2D(p3.x,p3.y), new Point2D(p4.x,p4.y), colorLines, 1.5);
        Line l4 = BlockRenderer.drawLine(new Point2D(p4.x,p4.y), new Point2D(p1.x,p1.y), colorLines, 1.5);
        
        g.getChildren().addAll(l1,l2,l3,l4);
        
        /*
        //FILL SHAPE
        TriangleMesh mesh2 = new TriangleMesh();
        mesh2.getPoints().addAll(
            (float)p1.x,  (float)p1.y,    (float)p1.z,          
            (float)p2.x,  (float)p2.y,    (float)p2.z,                 
            (float)p3.x,  (float)p3.y,    (float)p3.z,       
            (float)p4.x,  (float)p4.y,    (float)p4.z                
        );
        
        mesh2.getFaces().addAll(
            0,0,  1,0,  1,0,         
            1,0,  2,0,  2,0,
            2,0,  3,0,  3,0,
            3,0,  0,0,  0,0  
        ); 
        
        mesh2.getTexCoords().addAll(0,0);
        
        MeshView planeGeom2 = new MeshView(mesh2);
        planeGeom2.setDrawMode(DrawMode.LINE);
        planeGeom2.setMaterial(matLines);*/
 
        return g;             
    }
            
            
    
}
