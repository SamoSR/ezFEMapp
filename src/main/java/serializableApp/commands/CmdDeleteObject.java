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
import serializableApp.utils.ObjectResult;
import serializableApp.utils.StringResult;






/**
 *
 * @author GermanSR
 */
public class CmdDeleteObject extends Command{
    
    public static final String NAME = "DeleteObject";
    public static final String SHORT = "del";
    public static final String PARAM_NAME_PATH = "ObjPath";

    
    public CmdDeleteObject(){
        super(NAME);
        addParam(PARAM_NAME_PATH);
    }
    
    
    @Override
    public CommandResult execute(CommandManager mng){
    
        if(!checkParamExistence().result){
            return checkParamExistence();
        }
        
        Project proj = mng.getProject();
        String objPath = getParamValue(PARAM_NAME_PATH);

        
        
        ObjectResult result = proj.getObjectByCode(objPath);
        SerializableObject obj = result.getObject();
        if(obj==null){
           return new CommandResult(false,"Oject <"+objPath+"> was not found in the project"); 
        }
        
        //returns a boolean with the result if the property is being used
        //and a string of the id of the object that is using it
        if(obj.getParentList()!=null){
            StringResult resultUsed = proj.isReferenceUsed(obj.getID(), obj.getParentList().getPropertyName());
            if(resultUsed.getResult()){
                return new CommandResult(false,"Unable to delete object "+obj.getTypeAndID()+". The object is currently used in a 'reference property' by object <"+resultUsed.getStringValue()+">"); 
            }
        }
        
        /*
        if(obj.getID().equals("default")){
            return new CommandResult(false,"Unable to delete object: " +objType+"<"+objID+">. Default objects are undeletable"); 
        }
        */
        
        if(!obj.isDeletable()){
            return new CommandResult(false,"Unable to delete object "+obj.getTypeAndID()+". The object is marked as 'undeletable'"); 
        }
        
        
        Property prop =  obj.getProperty(PropertyStringID.PROPNAME_BINDED_TO_REFPROP);
        if(prop!=null){
            String listName = obj.getProperty(prop.getValueString()).castToPropertyRef().getListName();
            PropertyObjectList refList = mng.getProject().findListByName_Nested(listName); 
            if(refList!=null){
                refList.removeObject(obj);
            } 
        }
         
        
        if(obj.getParentList()!=null){
            obj.getParentList().removeObjectByID(obj.getID());
            return new CommandResult(true,"Object "+obj.getTypeAndID()+" deleted from the project database"); 
        }
       
        return new CommandResult(false,"error deleting object");

    }
    
}
