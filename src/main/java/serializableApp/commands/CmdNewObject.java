/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;



import serializableApp.objects.Project;
import serializableApp.objects.Property;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.PropertyStringID;
import serializableApp.objects.SerializableObject;
import serializableApp.utils.PropertyResult;


/**
 *
 * @author GermanSR
 */
public class CmdNewObject extends Command{
    
    public static final String NAME = "NewObject";
    public static final String SHORT = "new";
    
    public static final String PARAM_NAME_OBJTYPE = "ListPath";
    public static final String PARAM_NAME_OBJID = "ObjID";
    
    public CmdNewObject(){
        super(NAME);
        addParam(PARAM_NAME_OBJTYPE);
        addParam(PARAM_NAME_OBJID);
    }
    
    @Override
    public CommandResult execute(CommandManager mng){
        
        if(!checkParamExistence().result){
            return checkParamExistence();
        }
        
        Project proj = mng.getProject();
        
        String listPath = getParamValue(PARAM_NAME_OBJTYPE);
        String objID = getParamValue(PARAM_NAME_OBJID);
        
        PropertyObjectList list;
        
        System.out.println("list path: "+listPath);
        PropertyResult propResult = mng.getProject().getPropertyByCode(listPath);
        
        if(propResult.isValid()){
            if(propResult.getProperty().getPropertyType() == Property.TYPE_OBJECTLIST){
                list = propResult.getProperty().castoToPropertyObjectList();   
            }else{
                return new CommandResult(false, "The specified property path '"+listPath+"' does not lead to a list");
            }
        }else{
            return new CommandResult(false, propResult.getMesage()); 
        }
        
        String objType = list.getObjectType();
        SerializableObject newObj = proj.getDefaultObject(list.getObjectType());
        if(newObj==null){
            return new CommandResult(false,"The specified object type <"+objType+"> is unknown to the current project");
        }
 
        Property prop = newObj.getProperty(PropertyStringID.PROPNAME_BINDED_TO_REFPROP);
        if(prop!=null){     
            String listName = newObj.getProperty(prop.getValueString()).castToPropertyRef().getListName();
            PropertyObjectList refList = mng.getProject().findListByName_Nested(listName); 
            if(refList!=null){
                boolean containsName=false;
                for(SerializableObject obj:refList.getObjectList()){
                    if(obj.getID().equals(objID)){
                        containsName = true;
                        newObj.getProperty(prop.getValueString()).castToPropertyRef().setObjectName(objID);
                        newObj.getProperty(SerializableObject.PROPNAME_ID).castoToPropertyStringID().setValue(objID);
                    }
                }
                if(!containsName){
                    return new CommandResult(false,"Invalid ID<"+objID+">. The ID must match an object from the list "+refList.getPropertyPathString());
                }
            }  
        }
        
        newObj.setID(objID);
        int size1 = list.getSize();
        list.addObjectUniqueID(newObj);
        int size2 = list.getSize();

        if(size2>size1){
           
            return new CommandResult(true,"New object "+newObj.getTypeAndID()
                    + " was added to the list <"+list.getPropertyName()+">. Properties are set to the default values"); 
            
        }else{
            return new CommandResult(false,"An object with the specified ID <"+objID+"> already exists in the specified list <"+list.getPropertyName()+">"); 
        }
        
      
    }
}
