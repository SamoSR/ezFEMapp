/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import ezfemapp.rendering.canvas.BlockRenderer;
import ezfemapp.rendering.canvas.ModelRenderer;
import javafx.scene.Group;
import javafx.scene.Node;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.PropertyReference;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class Block extends SerializableObject{
    
    public static final String OBJECT_TYPE="Block";
    public static final String PROPNAME_ROW="Row";
    public static final String PROPNAME_COLUMN="Col";
    public static final String PROPNAME_MATERIAL="Material";
    public static final String PROPNAME_BLOCKINDEX="Index";
    
    public Node geometry;
    
    public Block(int row, int col, BlockMaterial material){
        super(OBJECT_TYPE,"b:"+row+","+col);
        addProperty(new PropertyInteger(PROPNAME_ROW, row));
        addProperty(new PropertyInteger(PROPNAME_COLUMN, col));
        addProperty(new PropertyInteger(PROPNAME_BLOCKINDEX, 0));
        String id="";
        if(material!=null){
            id = material.getID();
        }
        addProperty(new PropertyReference(PROPNAME_MATERIAL,id,BlockMaterial.OBJECT_TYPE, BlockProject.PROPNAME_MATERIAL_LIST));
    }
    
    public BlockMaterial getMaterial(){
        return (BlockMaterial)getProperty(PROPNAME_MATERIAL).castToPropertyRef().getObject2();
    }
    
    public int getRow(){
        return getProperty(PROPNAME_ROW).getValueInteger();
    }
    public int getColumn(){
        return getProperty(PROPNAME_COLUMN).getValueInteger();
    }
    
    public void setIndex(int index){
        getProperty(PROPNAME_BLOCKINDEX).castToPropertyInteger().setValue(index);
    }
    
    public int getIndex(){
        return getProperty(PROPNAME_BLOCKINDEX).getValueInteger(); 
    }
    
    
    public void generateGeometry(){
        geometry = BlockRenderer.createBlockGeometry_Polygon3D(this,ModelRenderer.gridRenderSize);
    }
    
    public Node getGometry(){
        return geometry;
    }
   
}
