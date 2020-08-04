/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import serializableApp.commands.CommandResult;



import serializableApp.utils.StringResult;

/**
 *
 * @author GermanSR
 */
public class PropertyObjectList extends Property{
    
    String objectsType;
    List<SerializableObject> objects;
    int maxItems = Integer.MAX_VALUE;
    
    //INSTEAD OF THE "ID" PROPERTY OF THE ITEMS STORED IN THIS LIST, ANOTHER OBJECTS ID WILL BE USED
    //private boolean bindedID;
    //String bindedListName;
    //PropertyObjectList bindedList;
    
    public PropertyObjectList(String name, String objectType){
        super(name,Property.TYPE_OBJECTLIST); 
        this.objectsType = objectType;
        this.objects = new ArrayList<>();
    }
    
    /*
    public boolean hasBindedID(){
        return bindedID;
    }  
    public void bindID(String bindedList){
       this.bindedID = true;
       this.bindedListName = bindedList;
    }
    public PropertyObjectList getBindedIDpropList(){
        return bindedList;
    }*/
    
    
    public void setMaxItems(int maxItems){
       this.maxItems = maxItems;
    }
    public int getMaxItems(){
        return maxItems;
    }
    
    public SerializableObject getObjectByID(String id){ 
        /*
        if(!objects.isEmpty()){
            SerializableObject exObj = objects.get(0);
            Property prop = exObj.getProperty(PropertyStringID.PROPNAME_BINDED_TO_REFPROP);
            if(prop!=null){
               PropertyObjectList listRef = prop.getParent().getParentList();
               if(listRef!=null){
                   
               }
            }
        }*/
        for(SerializableObject obj:getObjectList()){
            if(obj.getID().equals(id)){
                return obj;
            }
        }
        return null;
    }
    
    
    
    public int getSize(){
        return objects.size();
    }
    
    public String getNameAndSize(){
        return getPropertyName()+"["+(getSize())+"]";
    }
    
    public void addObjectAnyID(SerializableObject obj){
        //DONT CHECK FOR IDS (OBJECTS WITH SAME ID ALLOWED)
        if(obj.getObjectType().equals(objectsType)){
            if(objects.size()<maxItems){
                 objects.add(obj);
            }  
        }  
    }
    
    public boolean addObjectUniqueID(SerializableObject obj){
        //DONT ADD IF THERE IS AN OBJECT WITH THAT ID ALREADY
        for(SerializableObject objcurrent:objects){
            if(objcurrent.getID().equals(obj.getID())){
                return false;
            }
        }
        if(obj.getObjectType().equals(objectsType)){
            if(objects.size()<maxItems){
                 objects.add(obj);
            }  
            return true;
        }    
        return false;
    }
    
    public void removeObjectByID(String id){
        //DONT ADD IF THERE IS AN OBJECT WITH THAT ID ALREADY
        for(SerializableObject objcurrent:objects){
            if(objcurrent.getID().equals(id)){
                objects.remove(objcurrent);
                return;
            }
        }
    }
    
    public void removeObject(SerializableObject deleteObj){
       for(SerializableObject obj:objects){
           if(obj==deleteObj){
               objects.remove(obj);
               return;
           }
       }
    }
    
    public String getObjectType(){
        return objectsType;
    }
    
    public List<SerializableObject> getObjectList(){
        return objects;
    }
    public void clearList(){
        objects.clear();
    }
    
    public boolean addNewObjectPromptID(SerializableObject obj){
        
        int size1 = objects.size();
        TextInputDialog dialog = new TextInputDialog(this.objectsType+"_"+objects.size());
        dialog.setTitle(getPropertyName());
        dialog.setHeaderText("Create New "+this.objectsType);
        dialog.setContentText("ID:");
        Optional<String> result = dialog.showAndWait();
         
        if(result.isPresent()){
            obj.setID(result.get());
            addObjectUniqueID(obj);
        }
        
        if(size1==objects.size()){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(getPropertyName());
            alert.setHeaderText("A "+this.objectsType+" with the specified ID already exists in the list");
            alert.setContentText("choose another ID for the new "+this.objectsType);
            alert.show();
            return false;
        }else{
            return true;
        }
        
    } 

    public StringResult promptCheckDuplicatedName(String id){
        
        TextInputDialog dialog = new TextInputDialog(id);
        dialog.setTitle("ID Property Editor");
        dialog.setHeaderText("current ID="+id);
        dialog.setContentText("new ID:");
        Optional<String> result = dialog.showAndWait();
         
        if(result.isPresent()){
            String newID = result.get();
            if(getObjectByID(newID)!=null){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("ID Property Editor");
                String errorText = "An object with the specified ID <"+id+"> already exists in the list <"+getPropertyName()+">";
                alert.setHeaderText(errorText); 
                alert.setContentText("choose another ID");
                alert.show();
                return new StringResult(false, errorText );
                
            }else{
                return new StringResult(true, newID);   
            }
 
        }
        
       return new StringResult(false, "");
    }
       
    
    public StringResult checkDuplicatedName(String newID){   
        if(getObjectByID(newID)!=null){
            return new StringResult(false, "An object with the specified ID <"+newID+"> already exists in the list <"+getPropertyName()+">" );

        }else{
            return new StringResult(true, newID);   
        }
    }
    

    
    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        for(SerializableObject obj:getObjectList()){
            rootEle.appendChild(obj.serializeObject(dom));
        }
        return rootEle;
    }
    
    @Override
    public void deserializeXML(Project project, Element element){
        NodeList nList = element.getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element e = (Element) nNode;
            SerializableObject obj = project.createObjectFromXML(e);
            objects.add(obj);
        }    
    }
    
    /*
    public StringResult prompChangeObjectID(String id){
        
        TextInputDialog dialog = new TextInputDialog(id);
        dialog.setTitle("ID Property Editor");
        dialog.setHeaderText("current ID="+id);
        dialog.setContentText("new ID:");
        Optional<String> result = dialog.showAndWait();
         
        if(result.isPresent()){
            String newID = result.get();
            if(getObjectByID(newID)!=null){
                
      
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("ID Property Editor");
                alert.setHeaderText("An object with the specified ID <"+id+" already exists in the list <"+getPropertyName()+">"); 
                alert.setContentText("choose another ID");
                alert.show();
                return new StringResult(false, "" );
                
            }else{
                
                SerializableObject objChange = getObjectByID(id);
                
                CommandResult validationID = PropertyStringID.validateID(newID);
                if(!validationID.getResult()){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("ID Property Editor");
                    alert.setHeaderText("Invalid ID"); 
                    alert.setContentText(""+validationID.getFirstLine());
                    alert.show();
                    return new StringResult(false, "" );
                }
                
                
                if(objChange!=null){
                    objChange.setID(newID);
                    return new StringResult(true, newID);   
                }
                
            }
 
        }
        
       return new StringResult(false, "");
        
    } 
*/

    public CommandResult changeObjectID(String newID, String oldID){
        
    
        if(getObjectByID(newID)!=null){
            return new CommandResult(false,"An object with the specified ID <"+newID+" already exists in the list <"+getPropertyName()+">"); 
        }else{
            SerializableObject objChange = getObjectByID(oldID);
            
            if(objChange!=null){
                objChange.setID(newID);
                return new CommandResult(true,"Object ID changed, old value="+oldID+", new value="+newID); 
            }

        }

        return new CommandResult(false, "unexpected error changing objects ID"); 
        
    }      
    
    public boolean promtDeleteObjectByID(List<String> objects){
        if(objects.isEmpty()){
            return false;
        }
        String allCombos="{";
        for(String combo:objects){
            allCombos+=combo+",";
        }
        allCombos=allCombos.substring(0, allCombos.length()-1);
        allCombos+="}";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(getPropertyName());
        alert.setHeaderText("Delete all selected "+this.objectsType+"?");
        alert.setContentText("selected: "+allCombos);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait();
        
        if(alert.getResult() == okButton) {
            for(String id:objects){
                removeObjectByID(id);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public CommandResult editWithString(String val){
        return new CommandResult();
    }
    

    
    
}
