/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandResult;


/**
 *
 * @author GermanSR
 */
public class PropertyBoolean extends Property{
    
    private boolean value;
    
    public PropertyBoolean(String name){
        super(name,Property.TYPE_BOOLEAN);
    }
    
    public PropertyBoolean(String name, boolean value){
        super(name, Property.TYPE_BOOLEAN);
        this.value=value;
    }
    
    public void setValue(boolean val){
        this.value = val;
    }
    public boolean getValue(){
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
        value = Boolean.parseBoolean(Value);
    }
    
    @Override
    public CommandResult editWithString(String val){        
        if(val.equals("true")){
            this.value = true;
            return new CommandResult(true,"edited"+" object=<"+getParent().getObjectType()+">, id=<"+getParent().getID()+">, property=<"+getPropertyName()+">"+", new value=<"+val+">");
        }
        if(val.equals("false")){
            this.value = false;
            return new CommandResult(true,"edited"+" object=<"+getParent().getObjectType()+">, id=<"+getParent().getID()+">, property=<"+getPropertyName()+">"+", new value=<"+val+">");
        }
        return new CommandResult(false, "invalid property value <"+val+">");
    }
    

}
