/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;

/**
 *
 * @author GermanSR
 */
public class StringResult {
    
    public boolean valid;
    public String stringValue;
    public double doubleValue;
    public String error;
    public boolean isNumeric = false;
    
    private Object storedObj;
    
    public StringResult(boolean val,String str){
        valid=val;
        stringValue=str;   
    }
    
     public StringResult(boolean val,String str, Object obj){
        valid=val;
        stringValue=str;  
        storedObj=obj;
    }
    
    public void setStoredObject(Object obj){
        this.storedObj=obj;
    }
    
    public Object getStoredObject(){
        return storedObj;
    }
    
    public void convertToNumber(){
        try {
            double val = Double.parseDouble(stringValue);
            doubleValue = val;
            isNumeric = true;
        } catch (Exception e) {
            isNumeric = false;
            doubleValue = Double.NaN;
        }
    }
    
    public boolean getResult(){
        return valid;
    }
    
    public String getStringValue(){
        return stringValue;
    }
    
    public void PrintToConsole(){
        System.out.println("string results");
        System.out.println(" valid: "+valid);
        System.out.println(" value: "+stringValue);
        System.out.println(" errorMsg: "+error);
    }
    
}
