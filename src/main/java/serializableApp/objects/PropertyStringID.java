/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandManager;
import serializableApp.commands.CommandResult;

import serializableApp.utils.StringResult;


/**
 * @author GermanSR
 */
public class PropertyStringID extends Property{
    
    public static final String PROPNAME_BINDED_TO_REFPROP="Bindedid_";
    private String value;
     
    public PropertyStringID(String value){
        super(SerializableObject.PROPNAME_ID, Property.TYPE_STRINGID);
        this.value=value;
    }
    
    public String getRealValue(){
        return value;
    }
    
    public String getValue(){
        if(getParent()!=null){
            PropertyString prop = (PropertyString)getParent().getProperty(PROPNAME_BINDED_TO_REFPROP);
            if(prop!=null){
                PropertyReference propRef = (PropertyReference)getParent().getProperty(prop.getValue());
                if(propRef!=null){
                    return propRef.getObjectName();
                }  
            }
        }
        return value;
    }
     
    public void setValue(String id){
        /*
        if(getParent()!=null){
            PropertyString prop = (PropertyString)getParent().getProperty(PROPNAME_BINDED_TO_REFPROP);
            if(prop!=null){
                return;
            }
        }*/
        this.value=id;
    }
     
    public CommandResult validateID(String id){
        
        if(id.equals("default")){
            return new CommandResult(false, "The ID <default> is reserved and cannot be used");
        }
        
        
        return new CommandResult(true,"");
    }
    
    CommandManager mng;
    public void setCommandManagerRef(CommandManager mng){
        this.mng=mng;
    }
    
    @Override
    public CommandResult editWithString(String val){       
        if(getParent()!=null){
            PropertyString prop = (PropertyString)getParent().getProperty(PROPNAME_BINDED_TO_REFPROP);
            if(prop!=null){
                return new CommandResult(false,"Cannot edit the ID of referenced objects");
            }
        }
        CommandResult validate = validateID(val);
        if(validate.getResult()){
            String oldValue = value;
            this.setValue(val);
            CommandResult result = new CommandResult(true,"Edited object="+getParent().getTypeAndID()+", property="+getPropertyName()+", old value="+oldValue+", new value="+val+"");
            
            //EDIT REFERENCES
            if(mng!=null){
                if(this.getParent()!=null){
                    if(this.getParent().getParentList()!=null){
                        PropertyObjectList listProp = this.getParent().getParentList();
                        mng.getProject().changeRef(oldValue, val, listProp.getPropertyName());
                    }
                }    
            }
            
            return result;
        }else{
            return validate;
        }       
    }
    
    
    public StringResult promptCheckDuplicatedName(){
        if(getParent().parentList!=null){
           return getParent().parentList.promptCheckDuplicatedName(value);
        }
        return new StringResult(false,"Error editing object ID. Unable to find object parent");
    }
    
    public StringResult checkDuplicatedName(String newID){
        if(getParent().parentList!=null){
           return getParent().parentList.checkDuplicatedName(newID);
        }
        return new StringResult(false,"Error editing object ID. Unable to find object parent");
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
        value = e.getAttribute("Value"); 
    }
    
}
