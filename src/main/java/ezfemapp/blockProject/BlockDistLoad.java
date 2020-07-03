/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import serializableApp.objects.PropertyInteger;
import serializableApp.objects.PropertyReference;
import serializableApp.objects.PropertyString;
import serializableApp.objects.SerializableObject;

/**
 * @author GermanSR
 */
public class BlockDistLoad extends SerializableObject{
    
    public static final String OBJECT_TYPE="LinearLoad";
    public static final String PROPNAME_ROW="Row";
    public static final String PROPNAME_COLUMN="Col";
    public static final String PROPNAME_DIRECTION="Direction";
    public static final String PROPNAME_LOADCASE="LoadCase";
    
    public static final String LOAD_DIRECTION_UP = "UP";
    public static final String LOAD_DIRECTION_RIGHT = "RIGHT";
    public static final String LOAD_DIRECTION_DOWN = "DOWN";
    public static final String LOAD_DIRECTION_LEFT = "LEFT";
    
    public BlockDistLoad(int row, int col, LoadCaseBlock loadCase, String direction){
        super(OBJECT_TYPE,OBJECT_TYPE+":"+row+","+col+","+loadCase+","+direction);
        
        addProperty(new PropertyInteger(PROPNAME_ROW, row));
        addProperty(new PropertyInteger(PROPNAME_COLUMN, col));
        addProperty(new PropertyString(PROPNAME_DIRECTION, direction));
        addProperty(new PropertyReference(PROPNAME_LOADCASE, loadCase.getID(),LoadCaseBlock.OBJECT_TYPE, BlockProject.PROPNAME_LOADCASE_LIST));
    }

    
    public LoadCaseBlock getLoadCase(){
        return (LoadCaseBlock)getProperty(PROPNAME_LOADCASE).castToPropertyRef().getObject2();
    }
    
    public String getDirection(){
        return getProperty(PROPNAME_DIRECTION).getValueString();
    }
    
    public int getRow(){
        return getProperty(PROPNAME_ROW).getValueInteger();
    }
      
    public int getColumn(){
        return getProperty(PROPNAME_COLUMN).getValueInteger();
    }
    
}
