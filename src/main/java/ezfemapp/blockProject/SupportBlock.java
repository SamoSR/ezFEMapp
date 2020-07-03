/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import ezfemapp.rendering.canvas.BlockRenderer;
import ezfemapp.rendering.canvas.ModelRenderer;
import javafx.scene.Node;
import serializableApp.objects.PropertyString;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class SupportBlock extends Block{
    

    public static final String SUPPORT_FIXED="SUP_FIXED";
    public static final String SUPPORT_PINNED_HORZ="SUP_PIN_HOR";
    public static final String SUPPORT_PINNED_VERT="SUP_PIN_VER";
    
    public static String[] getSupportTypes(){
        return new String[]{SUPPORT_FIXED,SUPPORT_PINNED_HORZ,SUPPORT_PINNED_VERT};
    }
    
    public static final String PROPNAME_TYPE="SupportType";
    
    public SupportBlock(int row, int col, BlockMaterial material,String type){
        super(row,col,material);
        addProperty(new PropertyString(PROPNAME_TYPE, type));
        setID("bs:"+row+","+col);
    }
    
    public String getSupportType(){
        return getProperty(PROPNAME_TYPE).getValueString();
    }

    @Override
    public void generateGeometry(){
        geometry = BlockRenderer.createSupportGeometry((SupportBlock)this,ModelRenderer.gridRenderSize,new double[]{0,0,0,0,0,0,0,0});
    }
    @Override
    public Node getGometry(){
        return geometry;
    }
}
