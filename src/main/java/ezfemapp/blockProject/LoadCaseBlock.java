/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;


import javafx.scene.paint.Color;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.objects.EditPropertyGroup;
import serializableApp.objects.PropertyDimension;
import serializableApp.objects.PropertyObject;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class LoadCaseBlock extends SerializableObject{
    
    public static final String OBJECT_TYPE="LoadCase";
    public static final String PROPNAME_MAG_POINT="MagnitudePoint";
    public static final String PROPNAME_MAG_LINE="MagnitudeLinear";
    public static final String PROPNAME_COLOR="Color";
    
    public LoadCaseBlock(String id){
        super(OBJECT_TYPE,id);
        DimensionUnit forcePoint = UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_LOAD_POINT_FORCE);
        DimensionUnit forceLinear = UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_LOAD_LINEAR_FORCE);  
        addProperty(new PropertyDimension(PROPNAME_MAG_POINT,10,forcePoint.getRealUnits(),forcePoint.getID())); 
        addProperty(new PropertyDimension(PROPNAME_MAG_LINE,10,forceLinear.getRealUnits(),forceLinear.getID()));
        addProperty(new PropertyObject(PROPNAME_COLOR, new ColorObject("colorCase"+id, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255))));
        
        addPropertyGroup(new EditPropertyGroup("Load Magnitudes", PROPNAME_MAG_POINT, PROPNAME_MAG_LINE ));
        addPropertyGroup(new EditPropertyGroup("Color", PROPNAME_COLOR ));
    }
    
    
    public LoadCaseBlock setPointForceMagnitude(double val, String unit){
        setValue(PROPNAME_MAG_POINT, val,unit);
        return this;
    }
    public LoadCaseBlock setLinearForceMagnitude(double val, String unit){
        setValue(PROPNAME_MAG_LINE, val, unit);
        return this;
    }
    public double getPointForceMagnitude(){
        return getProperty(PROPNAME_MAG_POINT).castoToPropertyDimension().getValue();
    }
    public double getLinearForceMagnitude(){
        return getProperty(PROPNAME_MAG_LINE).castoToPropertyDimension().getValue();
    }
    public LoadCaseBlock setColor(Color color){
        getColor().setColor(color.getRed()*255, color.getGreen()*255, color.getBlue()*255,color.getOpacity());
        return this;
    }
    public ColorObject getColor(){
        return (ColorObject)getProperty(PROPNAME_COLOR).castoToPropertyObject().getObject();
    }
}
