/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXRippler;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 *
 * @author GermanSR
 */
public class MyRippler extends JFXRippler{
    
    public MyRippler(Node control,Color color){
        super(control);
        this.setRipplerFill(color);
    }
    
}
