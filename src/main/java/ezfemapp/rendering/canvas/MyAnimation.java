/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;

import javafx.animation.AnimationTimer;

/**
 *
 * @author GermanSR
 */
public abstract class MyAnimation extends AnimationTimer {
 
    boolean runing;

    public boolean isRuning() {
        return runing;
    }

    public void setRuning(boolean runing) {
        this.runing = runing;
    }
    
    
    
    @Override
    public void handle(long now) {
        
    }
    
    public double getCurrentStep(){
        return 0;
    }
    
    public void setCurrentStep(double val){

    }
    
    
}
