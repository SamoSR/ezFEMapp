/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import javafx.geometry.Orientation;
import javafx.scene.control.Separator;

/**
 *
 * @author GermanSR
 */
public class SeparatorInvisible extends Separator{
    
    public SeparatorInvisible(Orientation orientation, double size){
        this.setPrefSize(size, size);
        this.setOrientation(orientation);
        this.setVisible(false);
    }
    
}
