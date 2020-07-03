/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;

import serializableApp.objects.Property;


/**
 *
 * @author GermanSR
 */
public class PropertyResult {
    
    Property property;
    boolean valid;
    String msg;
    
    public PropertyResult(boolean isValid, Property prop, String msg){
        this.valid = isValid;
        this.msg=msg;
        this.property = prop;
    }
    
    
    
    
    public boolean isValid(){
        return valid;
    }
    public Property getProperty(){
        return property;
    }
    public String getMesage(){
        return msg;
    }
}
