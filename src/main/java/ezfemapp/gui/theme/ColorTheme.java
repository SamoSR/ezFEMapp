/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.theme;

import java.util.HashMap;
import javafx.scene.paint.Color;

/**
 *
 * @author GermanSR
 */
public class ColorTheme {
    
    public static final String COLOR_MAIN = "main";
    public static final String COLOR_BACKGROUND = "background";
    public static final String COLOR_MAIN_TEXT = "main_text";
    public static final String COLOR_BACKGROUND_TEXT = "background_text";
    public static final String COLOR_PANEL_BACKGROUND = "panel_background";
    
    HashMap<String,ColorCode> colors = new HashMap<>();
    
    public void addColor(ColorCode color){
        colors.put(color.id, color);
    }

    
    public String getColor(String id){
        ColorCode color = colors.get(id);
        return "rgb("+color.color[0]+","+color.color[1]+","+color.color[2]+""+color.color[3]+")";
    }
    public Color getColorFX(String id){
        ColorCode color = colors.get(id);
        return new Color(color.color[0]/255,color.color[1]/255,color.color[2]/255,color.color[3]);     
    }
    
    public double[] getColorDouble(String id){
        return colors.get(id).color;
    }
    
}
