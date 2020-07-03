/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GermanSR
 */
public class PropertiesList {
    
    List<Property> props;   
    String name;
    
    public PropertiesList(SerializableObject obj){
        props = new ArrayList<>();
        this.name=obj.getID();
        for(Property prop:obj.getPropertyHashMap().values()){
            props.add(prop);
        }        
    }
    
    public PropertiesList(String name){
        this.name=name;
        props = new ArrayList<>();
    }
    
    public String getName(){
        return name;
    }
    public void addPropsFromObject(SerializableObject obj){
        for(Property prop:obj.getPropertyHashMap().values()){
            props.add(prop);
        } 
    }
    public List<Property> getProperties(){
        return props;
    }
    public void addProp(Property prop){
        props.add(prop);
    }
}
