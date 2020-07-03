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
public class EditPropertyGroup {
    
    final String name;
    List<String> properties;
    List<EditPropertyGroup> propGroups;
    
    public EditPropertyGroup(String name){
        properties = new ArrayList<>();
        propGroups = new ArrayList<>();
        this.name=name;
    }
 
    public EditPropertyGroup(String name, String... props){
        properties = new ArrayList<>();
        propGroups = new ArrayList<>();
        this.name=name;
        for(String propName:props){
            properties.add(propName);
        }
    }
    
    public String getName(){
        return name;
    }
    
    public void addProperty(String prop){
        properties.add(prop);
    }
    
     public void addPropertyGroup(EditPropertyGroup propGroup){
        propGroups.add(propGroup);
    }
     
    public List<String> getProperties(){
        return properties;
    }
    public List<EditPropertyGroup> getPropertyGroups(){
        return propGroups;
    }
    
    
    public ArrayList<String> getNestedProperties(){
        ArrayList<String> allProps = new ArrayList<>();
        for(EditPropertyGroup group:propGroups){
            for(String prop:group.getNestedProperties()){
                allProps.add(prop);
            }
        }
        for(String prop:properties){
            allProps.add(prop);
        }
        return allProps;
    }
     
    
    

    
    
}
