/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandResult;


/**
 *
 * @author GermanSR
 */
public abstract class Property {
    
    //CHANGE TO ENUMERATIONS? MAYBE SOME DAY... xD
    public static final int TYPE_INTEGER=0;
    public static final int TYPE_DOUBLE=1;
    public static final int TYPE_STRING=2;
    public static final int TYPE_DIMENSION=3;
    public static final int TYPE_BOOLEAN=4; 
    public static final int TYPE_OBJECTLIST=5;
    public static final int TYPE_OBJECT=6;
    public static final int TYPE_REFERENCE=7;
    public static final int TYPE_STRINGID=12;
    public static final int TYPE_STRINGUNEDITABLE=13;
    public static final int TYPE_OBJECTLIST_REFID=15;

    //SERIALIZABLE FIELDS
    private final int property_type;
    private final String name;
    
    //NO NEED TO BE SERIALIZED BECAUSE THEY ARE CREATED ON EACH ELEMENT CONSTRUCTOR!
    //OR THEY ARE ASSIGNED ON RUN TIME  
    private String shortName;  //property short name
    private String description; //description (shows as tool tip in the GUI) 
    private SerializableObject parent; //a reference to the object that owns this property
    private boolean editable; //force a property to be un-editable
    private boolean serializable; //property that is not stored in the xml project tree
    
    public Property(String name, int type){
        this.name = name;
        this.property_type = type;
        this.shortName="";
        this.description="";
        this.editable = true;
        this.serializable = true;
    }
    
    public void unserializableProp(){
        serializable = false;
    }
    
    public SerializableObject getParent(){
        return parent;
    }  
    public void referenceParent(SerializableObject parent){
        if(parent.properties.get(name)==this){
             this.parent=parent;
        }
    }

    public boolean isSerializable() {
        return serializable;
    }


    public SerializableObject getProject(){
        return parent;
    }
     
    public Property setShortName(String shortName){
        this.shortName = shortName;
        return this;
    }
    
    public String getShortName(){
        if (shortName.equals("")){
            return name;
        }else{
            return shortName;
        }
    }

    public PropertyDimension castoToPropertyDimension(){
        if(getPropertyType()==TYPE_DIMENSION){
              return (PropertyDimension)this;
        }else{
            return null;
        }
    }
    
    public PropertyStringID castoToPropertyStringID(){
        if(getPropertyType()==TYPE_STRINGID){
              return (PropertyStringID)this;
        }else{
            return null;
        }
    }
    
    public PropertyString castoToPropertyString(){
        if(getPropertyType()==TYPE_STRING){
              return (PropertyString)this;
        }else{
            return null;
        }
    }
    
    public PropertyObjectList castoToPropertyObjectList(){
        if(getPropertyType()==TYPE_OBJECTLIST){
              return (PropertyObjectList)this;
        }else{
            return null;
        }
    }
    
    
    public PropertyObject castoToPropertyObject(){
        if(getPropertyType()==TYPE_OBJECT){
              return (PropertyObject)this;
        }else{
            return null;
        }
    }
    
    public PropertyInteger castoToPropertyInteger(){
        if(getPropertyType()==TYPE_INTEGER){
            return (PropertyInteger)this;
        }else{
            return null;
        }
    }
    
    public PropertyReference castToPropertyRef(){
        if(getPropertyType()==TYPE_REFERENCE){
              return (PropertyReference)this;
        }else{
            return null;
        }
    }
    
    public PropertyDouble castToPropertyDouble(){
        if(getPropertyType()==TYPE_DOUBLE){
              return (PropertyDouble)this;
        }else{
            return null;
        }
    }
    
    public PropertyInteger castToPropertyInteger(){
        if(getPropertyType()==TYPE_INTEGER){
              return (PropertyInteger)this;
        }else{
            return null;
        }
    }
    
    public PropertyBoolean castToPropertyBoolean(){
        if(getPropertyType()==TYPE_BOOLEAN){
              return (PropertyBoolean)this;
        }else{
            return null;
        }
    }
    
    public String getPropertyName(){
        return name;
    }
  
    public Label getLabelName(){
        
        Label lblName= new Label(getShortName());
        lblName.setPrefWidth(90);
        lblName.setAlignment(Pos.CENTER_RIGHT);
        
        Tooltip tooltip = new Tooltip();
   
        tooltip.setText(getDescription());
        lblName.setTooltip(tooltip);
        
        return lblName;
    }
    
 
    public abstract CommandResult editWithString(String val);

    
    public Element serializeXML(Document dom){
        return null;
    }
    
    
    
    public void deserializeXML(Project project, Element e){

    }

    public int getValueInteger(){
        if(property_type==TYPE_INTEGER){
            PropertyInteger p = (PropertyInteger)this;
            return p.getValue();
        }
        return Integer.MIN_VALUE;
    }
    public double getValueDouble(){
        if(property_type==TYPE_DIMENSION){
            PropertyDimension p = (PropertyDimension)this;
            return p.getValue();
        }
        if(property_type==TYPE_DOUBLE){
            PropertyDouble p = (PropertyDouble)this;
            return p.getValue();
        }
        return Double.NaN;
    }
    public String getValueString(){
        if(property_type==TYPE_STRING){
            PropertyString p = (PropertyString)this;
            return p.getValue();
        }
        if(property_type==TYPE_STRINGID){
            PropertyStringID p = (PropertyStringID)this;
            return p.getValue();
        }
        if(property_type==TYPE_OBJECTLIST){
            PropertyObjectList po = (PropertyObjectList)this;
            return "list of objects: "+po.getObjectType();
        }
        if(property_type==TYPE_DIMENSION){
            PropertyDimension d = (PropertyDimension)this;
            return ""+d.getValue();
        }
        if(property_type==TYPE_BOOLEAN){
            PropertyBoolean d = (PropertyBoolean)this;
            return ""+d.getValue();
        }
        if(property_type==TYPE_REFERENCE){
            PropertyReference d = (PropertyReference)this;
            return ""+d.getObjectName();
        }
        if(property_type==TYPE_DOUBLE){
            PropertyDouble d = (PropertyDouble)this;
            return ""+d.getValue();
        }
        return "";
    }
    
    public String getDescription(){
        if(shortName.equals("")){
            return "["+name+"] "+description;
        }else{
            return "["+name+","+shortName+"] "+description;
        }    
    }

    //"BACK PROPAGATE THE PROPERTY TO FIND ITS OWN PATH INSIDE THE PROJECT TREE"
    public String getPropertyPathString(){  
        
        List<String> fullPath = new ArrayList<>(); 
        SerializableObject currentObj = this.parent;
        SerializableObject currentParentObj = currentObj.parentObj;
        PropertyObjectList currentParentList = currentObj.parentList; 
        
        fullPath.add(this.getPropertyName()); 
        
        while(currentParentObj==null||currentParentList==null){ 
   
            //EL PARENT ES UNA LISTA
            if(currentParentList!=null){
                fullPath.add(currentObj.getID());
                fullPath.add(currentParentList.getPropertyName()); 
                currentObj = currentParentList.getParent();
            //EL PARENT ES UN OBJETO    
            }else if(currentParentObj!=null){      
                fullPath.add(currentObj.usedAsProperty);
                currentObj = currentParentObj;
            }
            currentParentObj = currentObj.parentObj;
            currentParentList = currentObj.parentList;
            
            if(currentParentObj==null&&currentParentList==null){
                fullPath.add(currentObj.getID());
                break;
            }  
        }
        String path="";
        for(int i=0;i<fullPath.size();i++){
            path+=fullPath.get(fullPath.size()-1-i);
            if(i!=fullPath.size()-1){
                path+=".";
            }    
        }
        return path;
    }
 
    
    public Property setDescription(String desc){
        description=desc;
        return this;
    }
    
    public boolean isEditable() {
        return editable;
    }

    public int getPropertyType(){
        return property_type;
    }
    
    public Property uneditable(){
       editable = false;
       return this;
    }
    
    
    
    
    
}
