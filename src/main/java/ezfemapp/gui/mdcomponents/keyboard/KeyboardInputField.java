/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import com.jfoenix.controls.JFXRippler;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class KeyboardInputField extends StackPane{
        
        Text btnText = new Text();

        public void setText(String text){
            btnText.setText(text);
        }
        
        public void typeChar(String character){
            btnText.setText(btnText.getText().concat(character));
        }
        public void delChar(){
            
        }
        
        public KeyboardInputField(String text, double w, double h){
            
            Rectangle r = new Rectangle();

            r.setWidth(w);
            r.setHeight(h);
            r.setFill(Color.WHITE);
            
            btnText = new Text();
            btnText.setText(text);
            btnText.setFont(Font.font ("Arial", 12));
            btnText.setFill(Color.BLACK);
            
            DropShadow e = new DropShadow();
            e.setWidth(w);
            e.setHeight(h);
            e.setOffsetX(1);
            e.setOffsetY(1);
            e.setRadius(1);
            e.setColor(Color.LIGHTGREY);
            r.setEffect(e);
            r.setArcWidth(5); 
            r.setArcHeight(5);  
            this.getChildren().addAll(r,btnText);
        }
        
        public Node getRippler(){
            return new JFXRippler(this);
        }
 
    
    
}
