/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import static ezfemapp.blockProject.Block.PROPNAME_COLUMN;
import static ezfemapp.blockProject.Block.PROPNAME_ROW;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class ChunkSize extends SerializableObject{
    
    public static final String OBJECT_TYPE="ChunkSize";
    public static final String PROPNAME_ROW="Row";
    public static final String PROPNAME_COLUMN="Col";
    
    public ChunkSize(int rows, int cols){
        super(OBJECT_TYPE,rows+"x"+cols);
        addProperty(new PropertyInteger(PROPNAME_ROW, rows));
        addProperty(new PropertyInteger(PROPNAME_COLUMN, cols));
    }
    
    public int getRows(){
        return getProperty(PROPNAME_ROW).getValueInteger();
    }
    public int getColumn(){
        return getProperty(PROPNAME_COLUMN).getValueInteger();
    }
    
}
