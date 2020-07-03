/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static javafx.scene.text.Font.font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class utilsGUI {
    
    public static Color getColorDouble(double r, double g, double b, double alpha){
        return new Color(r,g,b,alpha);
    }

    public static Text create(String txt, String font, int size, Color color){     
        Text text = new Text(txt);
        text.setFont(new Font(font, size));
        text.setFill(color);
        return text;
    }
    
}
