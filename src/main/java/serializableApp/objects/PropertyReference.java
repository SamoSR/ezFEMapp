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
 * @author GermanSR
 */
public class PropertyReference extends Property {
    
    private String listPath;
    private String objectName;
    
    //THE OBJECT THAT HAS THE LIST THAT CONTAINS THE REFERENCE!
    //private SerializableObject ref;
    private PropertyObjectList list;
    
    public PropertyReference(String name, String refName, String type, String listName){
        super(name, Property.TYPE_REFERENCE);
        this.objectName=refName;
        this.listPath = listName;
    }

    public void setObjectName(String name){
        objectName = name;
    }
    
    public void changeRefName(String newID){
        objectName=newID;
    }
    
    public void setObjectByID(String id){
        SerializableObject newSelection = list.getObjectByID(id);    
        if(newSelection!=null){
            objectName = id;
        }
    }
    
 
    
    //REFERENCE THE OBJECT WHERE THE LIST ARE STORED
    public void setReference(SerializableObject ref){
        //this.ref=ref;
        //setObjectName(ref.getID());
    }
    
    //REFERENCE THE OBJECT WHERE THE LIST ARE STORED
    public void setList(PropertyObjectList listRef){
        this.list=listRef;
    }
    public PropertyObjectList getList(){
        return list;
    }
    /*
    public SerializableObject getReference(){
        return ref;
    }*/
    public String getListName(){
        return listPath;
    }
    public String getObjectName(){
        return objectName;
    }
    /*
    public SerializableObject getObject(){
       System.out.println("ref: "+ref.getID());
       PropertyObjectList list = (PropertyObjectList)ref.getProperty(listName);
       if(list!=null){
           return list.getObjectByID(objectName);
       }else{
           return null;
       }
    }*/
    
    public SerializableObject getObject2(){
        //System.out.println("returning object: "+getPropertyPathString());
       if(list!=null){
          // System.out.println("returning object: "+getObjectName());
           return list.getObjectByID(objectName);
       }else{
          // System.out.println("list is null");
           return null;
       }
    }
    
    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.setAttribute("ListPath", ""+list.getPropertyPathString());
        rootEle.setAttribute("ObjectName", objectName);
        return rootEle;
    }
    
    
    @Override
    public void deserializeXML(Project project, Element e){
        listPath = e.getAttribute("ListPath");
        objectName = e.getAttribute("ObjectName");     
    }
    
    
    @Override
    public CommandResult editWithString(String val){
        if(list==null){
            return new CommandResult(false,"Cannot edit property, the reference to the list is null");
        }
        
        boolean requestUpdate=false;
        if(getParent()!=null){
            if(getParent().getProperty(PropertyStringID.PROPNAME_BINDED_TO_REFPROP)!=null){
                if(getParent().getParentList()!=null){
                    PropertyObjectList parentList = getParent().getParentList();
                    if(parentList.getObjectByID(val)!=null){
                        return new CommandResult(false,"Cannot edit property, a reference to the object <"+val+"> already exist in the list <"+getParent().getParentList().getPropertyName()+">. The reference can only appear once in the list");
                    }else{
                        requestUpdate = true;
                    }
                }
            }           
        }
        
        //PropertyObjectList list = (PropertyObjectList)ref.getProperty(listName);
        
        if(list!=null){
            SerializableObject obj = list.getObjectByID(val);
            if(obj!=null){
                String oldValue = objectName;
                objectName = val;              
                CommandResult cmdResult = new CommandResult(true,"Edited object="+getParent().getTypeAndID()+", property="+getPropertyName()+", "+" old value="+oldValue+", new value="+val+""); 
                //THE PROPERTY EDITED WAS A REFERENCE PROPERTY THAT IS BEING USED AS ID, 
                //SO WE ARE ALSO UDATING THAT OBJECT ID
                if(requestUpdate){
                    getParent().getProperty(SerializableObject.PROPNAME_ID).castoToPropertyStringID().setValue(val);
                    cmdResult.requestGUIupdate();
                }
                return cmdResult;
            }else{
                return new CommandResult(false,"Cannot edit reference, the object <"+val+"> does not exist in the list <"+listPath+">");
            }    
       }else{
           return new CommandResult(false,"Cannot edit reference, the list <"+listPath+"> doest not exist");
       }
        
        
        
    }
    

}
