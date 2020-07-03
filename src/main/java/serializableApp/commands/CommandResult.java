/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GermanSR
 */
public class CommandResult {
    
    boolean result;
    List<String> textLines = new ArrayList<>();
     private boolean requestGUIupdate = false;
    
    public CommandResult(){
        
    }
    
    public void requestGUIupdate(){
        requestGUIupdate = true;
    }
    
    public boolean updateGUI(){
        return requestGUIupdate;
    }
    
    public List<String> getTextLines(){
        return textLines;
    }
    
    public String getFirstLine(){
        if(textLines.size()==1){
            return textLines.get(0);
        }
        return "";
    }
    
    public CommandResult(boolean val,String msg){
        result = val;
        addLine(msg);
    }
    
    public void clear(){
        textLines.clear();
    }
    
    public void addLine(String text){
        textLines.add(text);
    }
    
    public void setResult(boolean val){
        result=val;
    }
    public boolean getResult(){
        return result;
    }
}
