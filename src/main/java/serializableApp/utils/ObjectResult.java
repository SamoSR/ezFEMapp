/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;

import serializableApp.objects.SerializableObject;



/**
 *
 * @author GermanSR
 */
public class ObjectResult {
    
    SerializableObject object;
    boolean valid;
    String msg;
    
    public ObjectResult(boolean isValid, SerializableObject obj, String msg){
        this.valid = isValid;
        this.msg=msg;
        this.object = obj;
    }
    
    public boolean isValid(){
        return valid;
    }
    public SerializableObject getObject(){
        return object;
    }
    public String getMesage(){
        return msg;
    }
    
    
    
}
