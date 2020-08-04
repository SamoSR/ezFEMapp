/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;




public class TextIconButton extends HBox{

    double w,h;
    Node backGround;
    Node icon;
    Node selectedOverLay;
    Text text;
    boolean toggleSelected=false;
    EventHandler<MouseEvent> eventHandler;
    
    public TextIconButton(){
        setSpacing(5);
    }
 
    public void setEventHandler(EventHandler<MouseEvent> mouseEvent){
        this.eventHandler = mouseEvent;
    }
    public Node getIcon(){
        return icon;
    }
    public void setIconFontawesome(FontAwesomeIcon iconID, String numSize, Color color){
        Text iconNode = GlyphsDude.createIcon(iconID,numSize);
        iconNode.setFill(color);
        icon = iconNode;
    }
    
    public void setIconCustom(Node n){
        icon = n;
    }
    
    public void setBackGroundCustom(Node n){
        backGround = n;
    }
    
    public void setSelectedOverlay(Node n){
        selectedOverLay = n;
    }
    
    public void toggleUnselect(){
        ScaleTransition st = new ScaleTransition(Duration.millis(100), selectedOverLay);
        st.setToX(0f);
        st.setToY(0f);   
        st.play();
        st.setOnFinished((event) -> { 
            this.getChildren().remove(selectedOverLay);
        });
        toggleSelected=false;
    }
    
    public void toggleSelect(){
        if(toggleSelected){
            toggleUnselect();
            return;
        }
        this.getChildren().remove(selectedOverLay);
        this.getChildren().add(selectedOverLay);
        selectedOverLay.setScaleX(0);
        selectedOverLay.setScaleY(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(100), selectedOverLay);
        st.setToX(1f);
        st.setToY(1f);   
        st.play();
        toggleSelected=true;
    }
    
    public void setBtnWidth(double w){
        this.w=w;
    }
    public void setBtnHeight(double h){
        this.h=h;
    }
    
    public void setBackGroundRectangle(double w, double h, Color color, boolean shadow){
        this.w=w;
        this.h=h;
        Rectangle c = new Rectangle(w, h, color);
        if(shadow){
           DropShadow e = new DropShadow();
           e.setWidth(w);
           e.setHeight(h);
           e.setOffsetX(3);
           e.setOffsetY(3);
           e.setRadius(5);
           e.setColor(Color.GRAY);
           c.setEffect(e); 
        }
        backGround = c;
    }
    
    public void setBackGroundRectangleDouble(double w1, double h1, double w2,double h2, Color color1, Color color2, boolean shadow){
        this.w=Math.max(w1, w2);
        this.h=Math.max(h1, h2);
        StackPane g = new StackPane();
        Rectangle c1 = new Rectangle(w1, h1, color1);
        Rectangle c2 = new Rectangle(w2, h2, color2);
        g.getChildren().addAll(c1,c2);
        if(shadow){
           DropShadow e = new DropShadow();
           e.setWidth(w);
           e.setHeight(h);
           e.setOffsetX(3);
           e.setOffsetY(3);
           e.setRadius(5);
           e.setColor(Color.GRAY);
           c2.setEffect(e); 
        }
        backGround = g;
    }
    
    public void setBackGroundCircle(double d, Color color, boolean shadow){
        this.w=d;
        this.h=d;
        Circle c = new Circle(d/2, color);
        if(shadow){
           DropShadow e = new DropShadow();
           e.setWidth(w);
           e.setHeight(h);
           e.setOffsetX(3);
           e.setOffsetY(3);
           e.setRadius(5);
           e.setColor(Color.GRAY);
           c.setEffect(e); 
        }
        backGround = c;
    }
    
    public double getBackGroundWidth(){
        return w;
    }
    public double getBackGroundHeight(){
        return h;
    }
    
    public void construct(){  
        this.getChildren().clear();
        StackPane iconcontainer = new StackPane();
        Rectangle r = new Rectangle(30,30,Color.WHITE);
        iconcontainer.getChildren().addAll(r,icon);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(iconcontainer);

        setMinHeight(h);
        setPrefHeight(h);
        setMaxHeight(h);
        
        setMinWidth(w);
        setMaxWidth(w);
        setPrefWidth(w);
        
        setSpacing(10);
        if(text!=null){
            this.getChildren().addAll(text);
        }
        addPulseAnimation(this,eventHandler);  
    }
    
    
    public void adjustIconPosition(double x, double y){
        icon.setTranslateX(x);
        icon.setTranslateY(y);
    }
    public void adjustTextPosition(double x, double y){
        text.setTranslateX(x);
        text.setTranslateY(y);
    }


    MouseEvent me;
    Point2D p;
    public void addPulseAnimation(Node n, EventHandler<MouseEvent> eventHandler){
        
        ScaleTransition st = new ScaleTransition(Duration.millis(50), n);
        st.setToX(1.05f);
        st.setToY(1.05f);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(50), n);
        st2.setToX(1f);
        st2.setToY(1f);


        n.setOnMousePressed((event)->{
            p = new Point2D(event.getScreenX(),event.getScreenY());     
        });
        
        n.setOnMouseReleased((event) -> { 
            Point2D p2 = new Point2D(event.getScreenX(),event.getScreenY());   
            //IF THE FINGER OR COURSOR IS MOVED DURING THE PRESSING OPERATION, DO NOT FIRE! 
            //NOT FOR ME: THIS "TRICK" COULD BE IMPROVED BUT MEEH
            if(p.distance(p2)>40){
                return;
            }
            me = (MouseEvent)event; 
            st.play(); 
        });
        
        st.setOnFinished((event) -> { 
            st2.play();
        });
        
        st2.setOnFinished((event) -> { 
            if(this.eventHandler!=null){
                eventHandler.handle(me);
            }     
            
        });
  
    }
    
    public void setTextElement(String txt, String font, int size, Color color){
        text = new Text(txt);
        text.setFont(new Font(font, size));
        text.setFill(color);
    }
    


    
}