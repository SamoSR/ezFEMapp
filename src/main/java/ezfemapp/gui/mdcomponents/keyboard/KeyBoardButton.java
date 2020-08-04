/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import com.jfoenix.controls.JFXRippler;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class KeyBoardButton extends StackPane{
        
        
        KeyBoardKey key;

        public KeyBoardButton(KeyBoardKey key, double w, double h){
            this.key=key;
            Rectangle r = new Rectangle();

            r.setWidth(w);
            r.setHeight(h);
            r.setFill(Color.WHITE);
            
            Text btnText = new Text();
            btnText.setText(key.character);
            btnText.setFont(Font.font ("Roboto", 14));
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
        
        public void setListener(ScreenKeyBoard kb){
            
            ScaleTransition st = new ScaleTransition(Duration.millis(50), this);
            st.setToX(1.36f);
            st.setToY(1.36f);

            ScaleTransition st2 = new ScaleTransition(Duration.millis(25), this);
            st2.setToX(1f);
            st2.setToY(1f);

            this.setOnMouseClicked((event) -> { 
                st.play(); 
            });

            st.setOnFinished((event) -> { 
                st2.play();
            });

            st2.setOnFinished((event) -> {     
                processText(kb,key.character);
            });
        }
        
        private void processText(ScreenKeyBoard kb, String newChar){
            switch (newChar){
                case "del":
                    String str = kb.keyBoardCurrentInput.getText();
                    if (str != null && str.length() > 0) {
                        str = str.substring(0, str.length() - 1);
                        kb.keyBoardCurrentInput.setText(str);
                    }
                    break;
                case "space":
                    kb.keyBoardCurrentInput.setText(kb.keyBoardCurrentInput.getText()+" "); 
                    break;
                case "CLS":
                    kb.hide();
                    break;
                default:
                    kb.keyBoardCurrentInput.setText(kb.keyBoardCurrentInput.getText()+newChar);   
                    
            }
            kb.currentTextField.positionCaret(Integer.MAX_VALUE);
        }
        
        public Node getRippler(){
            return new JFXRippler(this);
        }
 
    }
