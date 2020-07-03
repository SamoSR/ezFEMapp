/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author GermanSR
 */
public class ToggleManager {
    
    List<PulseIconButtonCustom> btns = new ArrayList<>();
    
    public ToggleManager(){
        
    }
    
    public void addAll(PulseIconButtonCustom... buttons){    
        for(PulseIconButtonCustom btn:buttons){
            addButton(btn);  
        } 
    }
    
    public void addButton(PulseIconButtonCustom btn){    
        
        double w, h;   
        w  = btn.getBackGroundWidth();
        h  = btn.getBackGroundHeight();
        Rectangle r = new Rectangle(w,h,new Color(0.8,0.8,0.8,0.5));
        btn.setSelectedOverlay(r); 
        btn.setOnMousePressed((event)->{
            select(btn);
        });
        btns.add(btn);   
         
    }

    private void select(PulseIconButtonCustom selectedBtn){
        for(PulseIconButtonCustom currentButton:btns){  
            if(currentButton==selectedBtn){
                currentButton.toggleSelect();
            }else{
                currentButton.toggleUnselect();
            } 
        }
    }
    
    public void selectByID(String id){
        for(PulseIconButtonCustom currentButton:btns){ 
            if(currentButton.getBtnID().equals(id)){
                select(currentButton);
                //System.out.println("selecting: "+id);
                return;
            }
        }
        unselectAll();
    }
    
    public void unselectAll(){
       // System.out.println("unselecting all");
        btns.forEach((i)->i.toggleUnselect());
    }
}
