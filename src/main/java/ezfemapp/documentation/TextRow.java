/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.documentation;

import ezfemapp.gui.mdcomponents.SeparatorInvisible;
import ezfemapp.gui.mdcomponents.utilsGUI;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class TextRow extends HBox{
    
    public TextRow(String text){
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(30);
        Text title = utilsGUI.create(text, "Roboto", 12, Color.BLACK);
        this.getChildren().addAll(new SeparatorInvisible(Orientation.HORIZONTAL, 10), title);
    }
     
    
}
