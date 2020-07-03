/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXSlider;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


/**
 *
 * @author GermanSR
 */
public class MySlider extends HBox{
    
    JFXSlider slider;
     
    public MySlider(String txt){
        
        double w = 150;
        double h = 20;

        slider = new JFXSlider(0, 1, 0.5);
        String cssSlider = this.getClass().getResource("/cssStyles/WhiteTheme/MySlider.css").toExternalForm(); 
        slider.setPadding(new Insets(0, 0, 0, 0));
        
        slider.getStylesheets().add(cssSlider);
        slider.setPrefSize(w, h);
        AnchorPane.setRightAnchor(slider, 0.0);
        //slider.setTranslateY(h/4);
        /*
        Rectangle r = new Rectangle(w, h);
        r.setFill(Color.TRANSPARENT);
        
        StackPane pane = new StackPane();
        Rectangle rlbl = new Rectangle(w,h);
        rlbl.setFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
        Text text = new Text(txt);
        text.setFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        */
        
        Label lbl = new Label(txt);
        lbl.setTextFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT));
        
        //pane.getChildren().addAll(rlbl,text);
        this.setSpacing(3);
        this.getChildren().addAll(lbl,slider);
    }
    
    public JFXSlider getSlider(){
        return slider;
    }
    
}
