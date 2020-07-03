/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;


import ezfemapp.gui.ezfem.AnalysisScreen;
import javafx.scene.control.Slider;

/**
 *
 * @author GermanSR
 */
public class ChangeScaleAnimation extends MyAnimation{
    
    AnalysisScreen analysisScreen;
    Slider slider;
    
    int speed=0;
    int iterWait=5;
    double stepMagnitude = 0.05;
    double stepSign = 1;
    
    double upperBound = 1;
    double lowerBound = 0;

    double initial = 0.0;
    double current = 0.0;
    
    
    public ChangeScaleAnimation(AnalysisScreen analysisScreen){
        this.analysisScreen=analysisScreen;
        this.speed=0;
        this.initial = 0;
        this.setRuning(false);
    }
    
    public void setInitialValue(double current ){
        this.current=current;
    }
    
    @Override
    public double getCurrentStep(){
        return current;
    }
    
    @Override
    public void setCurrentStep(double val){
        current = val;
    }
    
    @Override
    public void handle(long now) {
        if(speed<iterWait){
            speed++;
            return;
        }
        speed=0;
        current += initial+(stepMagnitude*stepSign);
        if(current>upperBound){
            stepSign = -1;
        }
        if(current<lowerBound){
            stepSign = 1;
        }
        
        analysisScreen.updateScale(current);
        
        this.setRuning(true);
    }
    
}
