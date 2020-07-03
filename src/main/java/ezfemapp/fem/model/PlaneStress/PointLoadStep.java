/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

/**
 *
 * @author GermanSR
 */
public class PointLoadStep {
    
    public double x;
    public double y;
    
    public double fx;
    public double fy;
    public double fz;
    
    public PointLoadStep(double posX, double posY, double fx, double fy, double fz){
        this.x=posX;
        this.y=posY;
        this.fx=fx;
        this.fy=fy;
        this.fz=fz;
    }
    
}
