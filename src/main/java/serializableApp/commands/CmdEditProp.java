/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;

import serializableApp.objects.Property;
import serializableApp.objects.PropertyDimension;
import serializableApp.objects.PropertyStringID;
import serializableApp.utils.PropertyResult;




/**
 *
 * @author GermanSR
 */
public class CmdEditProp extends Command{
    
    public static final String NAME = "EditProp";
    public static final String SHORT = "ep";
    
    public static final String PARAM_NAME_PROPPATH= "PropertyPath";
    public static final String PARAM_NAME_PROPVALUE= "Value";

    public CmdEditProp(){
        super(NAME);
        addParam(PARAM_NAME_PROPPATH);
        addParam(PARAM_NAME_PROPVALUE);
    }
    
    @Override
    public CommandResult execute(CommandManager mng){
        
        if(!checkParamExistence().result){
            return checkParamExistence();
        }
         
        String propertyPath = getParamValue(PARAM_NAME_PROPPATH);
        String newValue = getParamValue(PARAM_NAME_PROPVALUE);
         
        PropertyResult propResult = mng.getProject().getPropertyByCode(propertyPath);
        
        if(propResult.isValid()){
            Property prop = propResult.getProperty();
            if(prop instanceof PropertyDimension){
                prop.castoToPropertyDimension().setUnitManagerRef(mng.getProject().getUnitsManager());
            }
            if(prop instanceof PropertyStringID){
                
                prop.castoToPropertyStringID().setCommandManagerRef(mng);
            }
            CommandResult cmdResult = propResult.getProperty().editWithString(newValue);
            
            return cmdResult;
        }else{
            return new CommandResult(false, propResult.getMesage());
        }
        

    }
    
}
