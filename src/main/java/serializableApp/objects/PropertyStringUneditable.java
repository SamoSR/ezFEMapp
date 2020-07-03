/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author GermanSR
 */
public class PropertyStringUneditable extends PropertyString{
    
  
    public PropertyStringUneditable(String name,String value){
        super(name,value,false);     
        
    }
    
    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.setAttribute("Value", ""+getValue());
        return rootEle;
    }
    
    @Override
    public boolean isEditable(){
        return false;
    }
    

  
    
}
