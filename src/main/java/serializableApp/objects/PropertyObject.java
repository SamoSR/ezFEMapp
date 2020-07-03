/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import serializableApp.commands.CommandResult;

/**
 *
 * @author GermanSR
 */
public class PropertyObject extends Property{
    
    SerializableObject object;
    /*
    public PropertyObject(String name){
        super(name,Property.TYPE_OBJECT);
    }*/
    
    public PropertyObject(String name, SerializableObject obj){
        super(name, Property.TYPE_OBJECT);
        this.object=obj;       
        if(obj!=null){
            obj.usedAsProperty = name;
        }        
    }

    public void setObject(SerializableObject obj){
        object=obj;
    }
    public SerializableObject getObject(){
        return object;
    }
    
    @Override
    public CommandResult editWithString(String val){
        return new CommandResult();
    }
    

    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.appendChild(object.serializeObject(dom));
        return rootEle;
    }
    
    @Override
    public void deserializeXML(Project project, Element element){
        NodeList nList = element.getElementsByTagName("Object");
        Node nNode = nList.item(0);
        Element e = (Element) nNode;
        SerializableObject obj = project.createObjectFromXML(e);
        setObject(obj);
    }
    
}
