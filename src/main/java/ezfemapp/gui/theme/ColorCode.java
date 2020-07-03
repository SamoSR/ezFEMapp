/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.theme;

/**
 *
 * @author GermanSR
 */
public class ColorCode {
    
    double[] color;
    String id;
    
    public ColorCode(String id, double r, double g, double b, double alpha){
        this.id=id;
        color = new double[]{r,g,b,alpha};
    }
}
