/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandResult;
import serializableApp.utils.RoundDouble;

/**
 *
 * @author GermanSR
 */
public class PropertyInteger extends Property{
    
    private int value;
    
    public PropertyInteger(String name){
        super(name,Property.TYPE_INTEGER);
    }
    
    public PropertyInteger(String name, int value){
        super(name, Property.TYPE_INTEGER);
        this.value=value;
    }
    
    public void setValue(int val){
        this.value = val;
    }
    public int getValue(){
        return value;
    }
    
    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.setAttribute("Value", ""+value);
        return rootEle;
    }
    
    @Override
    public void deserializeXML(Project project, Element e){
        String Value = e.getAttribute("Value");
        value = Integer.parseInt(Value);     
    }
    
    @Override
    public CommandResult editWithString(String val){
        System.out.println("EDITING INTEGER");
        try {
            int numVal = Integer.parseInt(val);
            int oldValue = value;
            value = numVal;
            return new CommandResult(true,"Edited object="+getParent().getTypeAndID()+ ", property="+getPropertyName()+", old value="+oldValue+ ", new value="+numVal);
        } catch (Exception e) {
            return new CommandResult(false,"New value for property <"+getPropertyName()+"> is not numeric");
        }   
    }
    

    
}
