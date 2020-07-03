/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.DimensionUnits;


import serializableApp.objects.PropertyString;
import serializableApp.objects.SerializableObject;



/**
 *
 * @author GermanSR
 */
public class DimensionUnit extends SerializableObject{
    
    public static final String OBJECT_TYPE="DimensionUnit";
    public static final String PROPNAME_UNITSTRING="";
    
    public DimensionUnit(String name, String unit){
       super(OBJECT_TYPE,name);
       addProperty(new PropertyString(PROPNAME_UNITSTRING,unit));
    }
    public String getRealUnits(){
        return getProperty(PROPNAME_UNITSTRING).getValueString();
    }
}
