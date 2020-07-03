/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.screen;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 *
 * @author GermanSR
 */
public class AppBarState {
    
    String stateName;
    String text;
    Node leftBox=null;
    Node rightBox=null;
    
    public AppBarState(String id){
        stateName = id;
    }
    
    public void setLeftBox(Node... btns){
        HBox btnBox = new HBox(5);
        leftBox = btnBox;
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        for(Node btn:btns){
            btnBox.getChildren().add(btn);
        }
    }
    
    public void setRightBox(Node... btns){
        HBox btnBox = new HBox(5);
        rightBox = btnBox;
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        for(Node btn:btns){
            btnBox.getChildren().add(btn);
        }
    }
    public void setText(String currentText){
        text = currentText;
    }
    
}
