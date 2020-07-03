/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXCheckBox;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author GermanSR
 */
public class MyColoredCheckBox extends JFXCheckBox{
    
    String id;
    Color color;
    
    public MyColoredCheckBox(String id, Color color){
        this.id=id;
        this.color=color;
        String strColor = "rgb("+(color.getRed()*255)+","+(color.getGreen()*255)+","+(color.getBlue()*255)+")";
        List<String> styles = new ArrayList<>();
        styles.add("-jfx-checked-color: "+strColor+";");
        styles.add("-jfx-unchecked-color: "+strColor+";");
        String allStyles="";
        for(String sty:styles){
            allStyles+=sty;
        }
        setStyle(allStyles);
        setFocusTraversable(false);
        this.setText(id);
    }
    
    
}
