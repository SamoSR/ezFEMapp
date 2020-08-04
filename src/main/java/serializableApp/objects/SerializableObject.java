/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import serializableApp.utils.StringResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import serializableApp.utils.ObjectResult;
import serializableApp.utils.PropertyResult;


/**
 *
 * @author GermanSR
 */
public class SerializableObject {
    
    public static final String PROPNAME_OBJTYPE = "ObjType";
    public static final String PROPNAME_ID = "ID";
    public static final String PROPNAME_CODEID = "codeID";
    public static final String PROPNAME_DELETABLE = "deletable";

    //store properties (
    HashMap<String,Property> properties;

    //THIS IS COMPUTED IN THE CONSTRUCTOR OF EACH OBJECT AND ITS DONE BY THE PROGRAMMER,
    //DOES NOT NEED SERIALIZATION 
    List<EditPropertyGroup> propertyHierachy;
    
    //THESE PROPERTIES ARE COMPUTED IN RUN TIME AND DO NOT NEED SERIALIZATION
    PropertyObjectList parentList;
    SerializableObject parentObj;
    String usedAsProperty="";

    
    public static final String[] ignoreProps = new String[]{SerializableObject.PROPNAME_CODEID,
                                                            SerializableObject.PROPNAME_DELETABLE,
                                                            SerializableObject.PROPNAME_ID,
                                                            SerializableObject.PROPNAME_OBJTYPE};
    
    public HashMap<String,Property> getPropertyHashMap(){
        return properties;
    }
    /*
    public void setPropertiesHashMap(HashMap<String,Property> props){
        properties = props;
    }*/
    public List<EditPropertyGroup> getEditPropertyGroups(){
        return propertyHierachy;
    }
    /*
    public void setPropertyHierachy(List<EditPropertyGroup> props){
        propertyHierachy=props;
    }*/
    public boolean hasProperty(String name){
        return properties.get(name) != null;
    }
    
    

    
    
    //CONSTRUCTOR 1
    /*
    public SerializableObject(String type){
        this.properties = new HashMap<>();
        this.propertyHierachy = new ArrayList<>();
        addProperty(new PropertyString(PROPNAME_OBJTYPE, type).uneditable());  
        addProperty(new PropertyStringID("")); 
        addProperty(new PropertyString(PROPNAME_CODEID, type)); //SET MANUALLY FOR A DIFERENT VALUE
        addProperty(new PropertyBoolean(PROPNAME_DELETABLE, false)); //SET MANUALLY FOR A DIFERENT VALUE
        
        createDefaultPropGroup();

    }*/
     //CONSTRUCTOR 2
    public SerializableObject(String type,String id){
        this.properties = new HashMap<>();
        this.propertyHierachy = new ArrayList<>();
        addProperty(new PropertyString(PROPNAME_OBJTYPE,type).uneditable()); 
        addProperty(new PropertyStringID(id));
        addProperty(new PropertyString(PROPNAME_CODEID, type)); //SET MANUALLY FOR A DIFERENT VALUE
        addProperty(new PropertyBoolean(PROPNAME_DELETABLE, false)); //SET MANUALLY FOR A DIFERENT VALUE
        
        //createDefaultPropGroup();
    }
    
    public void createDefaultPropGroup(){
        addPropertyGroup(new EditPropertyGroup("General", PROPNAME_ID,PROPNAME_OBJTYPE));
    }
    
    public void clearProperties(){
        this.properties.clear();
    }
       
    public SerializableObject undeletable(){
        setValue(PROPNAME_DELETABLE, false);
        return this;
    }
    public SerializableObject uneditable(){
        for(Property prop:properties.values()){
            prop.uneditable();
        }
        return this;
    }
    public SerializableObject getParentObject(){
        return parentObj;
    }
            
    
    
    public ObjectResult getObjectByCode(String code){ 
        
        SerializableObject searchObject = this; 
        String[] split = code.split("\\.");
        
        for(int i=0;i<split.length;i++){ 
            
            String currentProp = split[i];
            
            //AVOID LOOKING FOR "ITSELF" AS PROPERTY/OBJECT
            if(currentProp.equals(getID())){
                continue;
            }
            
            Property prop = searchObject.getPropertyObject(currentProp);
            if(prop==null){
                return new ObjectResult(false,null,"Undefined property <"+currentProp+"> for object "+searchObject.getTypeAndID());
            }else{
                if(prop instanceof PropertyObjectList){
                    PropertyObjectList listProp = prop.castoToPropertyObjectList();
                    if(i+1>=split.length){
                        break;
                    }
                    i++;
                    String  objID = split[i]; //NAME OF THE OBJECT IN THE LIST
                    boolean found=false;
                    for(int k=0;k<listProp.getSize();k++){
                        SerializableObject obj = listProp.getObjectList().get(k);
                        if(obj.getID().equals(objID)){
                            searchObject = obj;
                            found=true;
                            break;
                        }               
                    }
                    if(!found){
                         return new ObjectResult(false,null,"Object <"+objID+"> was not found in the list <"+listProp.getPropertyName()+">");
                    }
                }else if(prop instanceof PropertyObject){
                    PropertyObject objProp = prop.castoToPropertyObject();
                    searchObject = objProp.getObject();
                    
                }else{

                    //return new ObjectResult(false,null,"Object <"+objID+"> was not found in the list <"+listProp.getPropertyName()+">");    
                }
            }   
        }
        
        
        
        return new ObjectResult(true,searchObject,"");
    }
    
    
   
   //THIS WILL RETURN THE VALUE OF A GIVE PROPERTY IN THE FORM OF
    // ObjectID.PropList.ObjectID.Property.ObjectID.Property
    public PropertyResult getPropertyByCode(String code){ 
        
        SerializableObject searchObject = this; 
        String[] split = code.split("\\.");
        
        for(int i=0;i<split.length;i++){ 
            
            String currentProp = split[i]; 
            
            //AVOID LOOKING FOR "ITSELF" AS PROPERTY/OBJECT
            if(currentProp.equals(getID())){
                continue;
            }
            
            Property prop = searchObject.getPropertyObject(currentProp);
            //System.out.println("searching on: "+searchObject.getTypeAndID());
            //System.out.println("property: "+currentProp);
            
            if(prop==null){    
                return new PropertyResult(false,null,"Undefined property <"+currentProp+"> for object "+searchObject.getTypeAndID());
            }else{
                if(prop instanceof PropertyObjectList){
                    PropertyObjectList listProp = prop.castoToPropertyObjectList();
                    if(i==split.length-1){
                        return new PropertyResult(true,prop,"");  
                    }
                    if(i+1>=split.length){
                        break;
                    }
                    i++;
                    String  objID = split[i]; //NAME OF THE OBJECT IN THE LIST
                    boolean found = false;
                    for(SerializableObject obj:listProp.getObjectList()){
                        if(obj.getID().equals(objID)){
                            searchObject = obj;
                            found=true;
                            break;
                        }  
                    }
                    if(!found){
                        return new PropertyResult(false,null,"Object <"+objID+"> was not found in the list <"+listProp.getPropertyName()+">");    
                    }
                }else if(prop instanceof PropertyObject){
                    PropertyObject objProp = prop.castoToPropertyObject();
                    searchObject = objProp.getObject();
                }else{
                    return new PropertyResult(true,prop,"");  
                }
            }   
        }
        return new PropertyResult(false,null,"Failed to edit property. The specified path '"+code+"' leads to an object and not a property");
    }
    
    private Property getPropertyObject(String property){
        Property prop = getPropertyByShortName(property);
        if(prop!=null){
            return prop;
        }else{
            return null;
        }
    }

    /*
    private SerializableObject getNestedObject(Property prop){
        if(prop.getPropertyType()==Property.TYPE_REFERENCE){
            PropertyReference propRef = (PropertyReference)prop;
            SerializableObject obj = propRef.getObject();
                if(obj==null){
                    return null;
                }else{
                   return obj;
                }
        }
        if(prop.getPropertyType()==Property.TYPE_OBJECT){
            PropertyObject propObj = (PropertyObject)prop;
            SerializableObject obj = propObj.getObject();
                if(obj==null){
                       return null;
                }else{
                   return obj;
                }
        }
        return null;
    }*/

    
    
    public String getID(){
        if(getProperty(PROPNAME_ID)==null){
            return getProperty(PROPNAME_OBJTYPE).getValueString();
        }
        if(getProperty(PROPNAME_ID).getValueString().equals("")){
            return getProperty(PROPNAME_OBJTYPE).getValueString()+"";
        }
        return getProperty(PROPNAME_ID).getValueString();
    }
    
    
    public void setID(String val){
        setValue(PROPNAME_ID, val);
    }
    
    public final void addProperty(Property prop){
        if(prop==null){
            System.out.println("null property attempt");
            return;
        }
        //ENSURE THAT ONLY UNIQUE NAMES ARE USED FOR THE PROPERTIES OF THIS OBJECT
        if(properties.containsKey(prop.getPropertyName())){
            System.out.println("duplicated property attempt");
            return;
        }    
        properties.put(prop.getPropertyName(), prop);
    }
    
    public String getObjectType(){
       return properties.get(PROPNAME_OBJTYPE).getValueString();
    }
    
    /*
    public void printPropertiesToConsole(){
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property)pair.getValue();
            System.out.println(prop.getPropertyName() + " = " + prop.getValueString());
        }
    }*/
    
    public void deleteProperty(String name){
        if(properties.containsKey(name)){
            properties.remove(name);
        }
    }
    
    public Property getProperty(String propertyName){
       return properties.get(propertyName);
    }
    
    private Property getPropertyByShortName(String propertyName){
        for(Property prop:properties.values()){
            if(prop.getShortName().equals(propertyName)){
                return prop;
            }
            if(prop.getPropertyName().equals(propertyName)){
                return prop;
            }
        }
       return null;
    }
           
    public void createPropertyGroup(String name, String... props){
        EditPropertyGroup propGroup = new EditPropertyGroup(name);
        for(String prop:props){
            if(properties.containsKey(prop)){
                propGroup.addProperty(prop);
            }  
        }
        propertyHierachy.add(propGroup);
    }
    
    public void addPropertyGroup(EditPropertyGroup group){
        propertyHierachy.add(group);
    }
    
    public List<EditPropertyGroup> getPropertyGroups(){
        return propertyHierachy;
    }
    
    public List<PropertyObjectList> getObjectListProperties(){
        List<PropertyObjectList> listProperties = new ArrayList<>();
        for(Property prop:getPropertyHashMap().values()){ 
            if( prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                listProperties.add((PropertyObjectList)prop);
            }
        }
        return listProperties;
    }
    
    public ArrayList<Property> getPropertiesList(){
        return new ArrayList<Property>(properties.values());
    }
    
    private PropertyObjectList helperList;
    private void storeList(String listName){
        if(helperList!=null){
                return;
        }
        for(Property prop:properties.values()){
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                if(prop.getPropertyName().equals(listName)){
                    helperList = prop.castoToPropertyObjectList();
                    break;
                }else{
                    for(SerializableObject obj:prop.castoToPropertyObjectList().getObjectList()){
                        obj.storeList(listName);
                    }
                }
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject objProp = (PropertyObject)prop;
                SerializableObject obj = objProp.getObject();   
                obj.storeList(listName);
            }
        }
    }
    
    
    public PropertyObjectList findListByName_Nested(String id){
        helperList = null;
        storeList(id);
        return helperList;
    }
    
    public void setValue(String propName, Object... value){
        if(!properties.containsKey(propName)){
            return;
        }
        if(value.length<1){
            return;
        }
        Property prop = getProperty(propName);
        
        switch(prop.getPropertyType()){
            
            case Property.TYPE_STRING:     
                String newValue = (String)value[0];
                PropertyString propString = (PropertyString)prop;
                propString.setValue(newValue);       
            break;
            
            case Property.TYPE_DIMENSION: 
                double valNum = (double)value[0];
                String valUnits = (String)value[1];
                PropertyDimension propDim = (PropertyDimension)prop;
                propDim.setValue(valNum,valUnits);  
            break;
            
            case Property.TYPE_OBJECT:
                SerializableObject obj = null;
                if(value[0] instanceof SerializableObject){
                    obj = (SerializableObject)value[0];
                }
                PropertyObject propObj = (PropertyObject)prop;
                propObj.setObject(obj);
            break;   
            
            case Property.TYPE_BOOLEAN:
                boolean booleanVal = (boolean)value[0];
                PropertyBoolean propBool = (PropertyBoolean)prop;
                propBool.setValue(booleanVal);   
            break;
            
            case Property.TYPE_STRINGID:
                String stringIDval = (String)value[0];
                PropertyStringID idProp = (PropertyStringID)prop;
                idProp.setValue(stringIDval);   
            break;
            

        }
        
    }
    
    public boolean isDeletable(){
        PropertyBoolean bol = (PropertyBoolean)getProperty(PROPNAME_DELETABLE);
        return bol.getValue();
    }
    
     
    //BECAREFUL WITH RECURSIVITY
    public void referenceParentToProperties(){
        for(Property prop:properties.values()){
            prop.referenceParent(this);
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList listProp = (PropertyObjectList)prop;
                for(SerializableObject obj:listProp.getObjectList()){
                    obj.referenceParentToProperties();
                }
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject objProp = (PropertyObject)prop;
                SerializableObject obj = objProp.getObject();
                if(obj!=null){
                    obj.referenceParentToProperties();
                }
                
            }
        }
    }
    
    //BECAREFUL WITH RECURSIVITY
    public TreeItem getTree(){
        TreeItem<String> rootItem = new TreeItem(""+this.getID());
        //System.out.println("id: "+getID());
        for(Property prop:properties.values()){
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList listProp = (PropertyObjectList)prop;
                TreeItem<String> branch = new TreeItem(listProp.getNameAndSize());
                for(SerializableObject obj:listProp.getObjectList()){
                    branch.getChildren().add(obj.getTree());
                }
                rootItem.getChildren().add(branch);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject objProp = (PropertyObject)prop;
                SerializableObject obj = objProp.getObject();
                rootItem.getChildren().add(obj.getTree());
            } 
            
        }
        return rootItem;
    }

    
    
    /*
    public SerializableObject searchObject_Recursive(String id, String objectType, String parent){
        searchObject = null;
        findObjByIDandParent(id,objectType,parent);
        if(searchObject!=null){
            return (SerializableObject)searchObject;  
        }
        return null;
    }*/
    /*
    private SerializableObject findObjByIDandParent(String id, String type, String parent){  
        for(Property prop:properties.values()){
            if(prop instanceof PropertyObject){
                PropertyObject pobj = (PropertyObject)prop;
               // pobj.getObject().findListByID(id, type);
              
                if(pobj.getObject().getObjectType().equals(type)&&
                   pobj.getObject().getID().equals(id)&&
                        
                   pobj.getObject().getp.equals(id)){
                    
                }
            }
            if(prop instanceof PropertyObjectList){
                PropertyObjectList pobjList = (PropertyObjectList)prop;  
                if(pobjList.getObjectType().equals(type)&&pobjList.getPropertyName().equals(id)){
                    searchObject = (PropertyObjectList)prop;
                }else{
                    for(SerializableObject obj:pobjList.getObjectList()){
                       obj.findListByID(id, type);  
                    } 
                }
                
            }
        }
        return null;
    }
    */
    
    
    /*
    
    //helper class to search with recursivity
    private Object searchObject;
    
    public PropertyObjectList searchList_Recursive(String parent,String objectType, String listName){
        searchObject = null;
        findListByID_parent(listName,objectType,parent);
        if(searchObject!=null){
            return (PropertyObjectList)searchObject;  
        }
        return null;
    }
    
    
    
    private PropertyObjectList findListByID(String id, String type){  
        for(Property prop:properties.values()){
            if(prop instanceof PropertyObject){
                PropertyObject pobj = (PropertyObject)prop;
                pobj.getObject().findListByID(id, type);
            }
            if(prop instanceof PropertyObjectList){
                PropertyObjectList pobjList = (PropertyObjectList)prop;  
                if(pobjList.getObjectType().equals(type)&&pobjList.getPropertyName().equals(id)){
                    searchObject = (PropertyObjectList)prop;
                }else{
                    for(SerializableObject obj:pobjList.getObjectList()){
                       obj.findListByID(id, type);  
                    } 
                }
                
            }
        }
        return null;
    }
    
    private PropertyObjectList findListByID_parent(String id, String type,String parentID){  
        for(Property prop:properties.values()){
            if(prop instanceof PropertyObject){
                PropertyObject pobj = (PropertyObject)prop;
                pobj.getObject().findListByID(id, type);
            }
            if(prop instanceof PropertyObjectList){
                PropertyObjectList pobjList = (PropertyObjectList)prop;  
                if(pobjList.getObjectType().equals(type)&&pobjList.getPropertyName().equals(id)){
                    if(pobjList.getParent().getID().equals(parentID)){
                        searchObject = (PropertyObjectList)prop;
                    } 
                }else{
                    for(SerializableObject obj:pobjList.getObjectList()){
                       obj.findListByID(id, type);  
                    } 
                }
                
            }
        }
        return null;
    }
    
    */  
    
    
    public PropertyObjectList getParentList(){
        return parentList;
    }
    
    public void updateReferenceProperties(){
        addReferenceToRefProperties(this);
    }
    
  
    
    
    public String getTypeAndID(){
        return getObjectType()+"<"+getID()+">";
    }
    
    //BECAREFUL WITH RECURSIVITY
    private void addReferenceToRefProperties(SerializableObject refObject){
        Iterator it = properties.entrySet().iterator();
        //System.out.println("Object: "+this.getObjectType());
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property)pair.getValue();
            //System.out.println("  property: "+prop.getPropertyName());
            if(prop.getPropertyType()==Property.TYPE_REFERENCE){
                PropertyReference ref = (PropertyReference)prop;
                ref.setReference(refObject);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject pobj = (PropertyObject)prop;
                if(pobj.getObject()!=null){
                    pobj.getObject().addReferenceToRefProperties(refObject);
                    pobj.getObject().parentObj=this;
                }      
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList pobjList = (PropertyObjectList)prop;
                for(SerializableObject obj:pobjList.getObjectList()){
                    obj.addReferenceToRefProperties(refObject);
                    obj.parentList=pobjList;
                    //System.out.println(obj.getID());
                    //System.out.println(obj.parentList.getPropertyName());
                }
            }
        }
    }
    
    
    public void intializeReferencedObjects(SerializableObject root){
        //System.out.println("root: "+root.getTypeAndID());
        for(Property prop:properties.values()){
           // System.out.println("prop type: "+prop.getPropertyType());
           // System.out.println("reading prop: "+prop.getPropertyName());
            if(prop.getPropertyType()==Property.TYPE_REFERENCE){
                PropertyReference refProp = (PropertyReference)prop;
               // System.out.println("refProp: "+refProp.getListName());
                PropertyObjectList list = root.getPropertyByCode(refProp.getListName()).getProperty().castoToPropertyObjectList();
                refProp.setList(list);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject objProp = (PropertyObject)prop;
                objProp.getObject().intializeReferencedObjects(root);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList propList = (PropertyObjectList)prop;
                for(SerializableObject obj:propList.getObjectList()){
                    obj.intializeReferencedObjects(root);
                }
            }
        }
    }
    
    //CHANGES THE REFERENCES OF OBJECTS WITH A NEW NAME
    //BECAREFUL WITH RECURSIVITY
    public void changeReference(String oldID, String newID, String listName,SerializableObject refObject){
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property)pair.getValue();
            if(prop.getPropertyType()==Property.TYPE_REFERENCE){
                PropertyReference ref = (PropertyReference)prop;
                //System.out.println("REF FOUND");
                if(ref.getListName().equals(listName)){
                    if(ref.getObjectName().equals(oldID)){
                        //System.out.println("...updated in object: "+this.getID());
                        ref.changeRefName(newID);
                    }
                }
                ref.setReference(refObject);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject pobj = (PropertyObject)prop;
                pobj.getObject().changeReference(oldID,newID,listName,refObject);
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList pobjList = (PropertyObjectList)prop;
                for(SerializableObject obj:pobjList.getObjectList()){
                    obj.changeReference(oldID,newID,listName,refObject);
                }
            }
        }
    }
    
    //CHECK IF THE GIVEN REF IS UED IN ANY OBJECT
    //BECAREFUL WITH RECURSIVITY
    public StringResult isReferenceUsed(String refName, String listName){
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property)pair.getValue();
            if(prop.getPropertyType()==Property.TYPE_REFERENCE){
                PropertyReference ref = (PropertyReference)prop;
                if(ref.getListName().equals(listName)){
                    if(ref.getObjectName().equals(refName)){
                        return new StringResult(true, ref.getParent().getID());
                    }
                }
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject pobj = (PropertyObject)prop;
                if( pobj.getObject().isReferenceUsed(refName,listName).getResult()){
                     return new StringResult(true, ""+pobj.getObject().getID());
                }
            }
            if(prop.getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList pobjList = (PropertyObjectList)prop;
                for(SerializableObject obj:pobjList.getObjectList()){
                    if(obj.isReferenceUsed(refName,listName).getResult()){
                         return new StringResult(true, ""+obj.getID());
                    }
                }
            }
        }
        return new StringResult(false, "");
    }

    
    public String getObjectPath(){  
        
        List<String> fullPath = new ArrayList<>(); 
        SerializableObject currentObj = this;
        SerializableObject currentParentObj = currentObj.parentObj;
        PropertyObjectList currentParentList = currentObj.parentList; 
        
        //fullPath.add(this.getPropertyName()); 
        
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
    
    
    
    public Element serializeObject(Document dom){
        Element rootEle = dom.createElement("Object");
        rootEle.setAttribute(PROPNAME_OBJTYPE, getObjectType());
        rootEle.setAttribute(PROPNAME_ID, getID());
        
        for(Property prop:getPropertiesList()){
            boolean ignore = false;
            for(String nonSerializable:ignoreProps){
                if(prop.getPropertyName().equals(nonSerializable)){
                    ignore=true;
                }
            }
            if(ignore){
                continue;
            }
            if(!prop.isSerializable()){
                continue;
            }
            rootEle.appendChild(prop.serializeXML(dom));
        }
        return rootEle;
    }
    
    
   
    

    
}
