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
import serializableApp.objects.PropertyDouble;
import serializableApp.objects.PropertyObject;
import serializableApp.objects.SerializableObject;


/**
 *
 * @author GermanSR
 */
public class BlockMaterial extends SerializableObject{
    
    public static final String OBJECT_TYPE="BlockMaterial"; 
    public static final String PROPNAME_ELASTICMOD="ElasticModulus";
    public static final String PROPNAME_POISSON="PoissonRatio";
    public static final String PROPNAME_DENSITY="Density";
    public static final String PROPNAME_COLOR="Color";

    public BlockMaterial(String id){
        super(OBJECT_TYPE,id);
        DimensionUnit dimE =  UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_ELASTIC_MODULUS);
        DimensionUnit dimDensity =  UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_DENSITY);  
        addProperty(new PropertyDimension(PROPNAME_ELASTICMOD,210000,dimE.getRealUnits(),dimE.getID()));
        addProperty(new PropertyDimension(PROPNAME_DENSITY,50,dimDensity.getRealUnits(),dimDensity.getID()));
        addProperty(new PropertyDouble(PROPNAME_POISSON,0.25,0,0.5));
        addProperty(new PropertyObject(PROPNAME_COLOR, new ColorObject("colorBlock"+id, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255))));
        addPropertyGroup(new EditPropertyGroup("General", PROPNAME_ID,  PROPNAME_OBJTYPE ));
        addPropertyGroup(new EditPropertyGroup("Mechanics", PROPNAME_ELASTICMOD,  PROPNAME_POISSON, PROPNAME_DENSITY ));
        addPropertyGroup(new EditPropertyGroup("Color", PROPNAME_COLOR));
        getProperty(SerializableObject.PROPNAME_DELETABLE).castToPropertyBoolean().setValue(true);
    }
    
    public BlockMaterial setElasticModulus(double val, String unit){
        setValue(PROPNAME_ELASTICMOD, val, unit);
        return this;
    }
    public BlockMaterial setPoissonRatio(double val){
        setValue(PROPNAME_POISSON, val);
        return this;
    }
    public BlockMaterial setDensity(double val, String unit){
        setValue(PROPNAME_DENSITY, val, unit);
        return this;
    }
    public BlockMaterial setColor(Color color){
        getColor().setColor(color.getRed()*255, color.getGreen()*255, color.getBlue()*255,color.getOpacity());
        return this;
    }
    public BlockMaterial setColor(Color color, double opacity){
        getColor().setColor(color.getRed()*255, color.getGreen()*255, color.getBlue()*255,opacity);
        return this;
    }
    public BlockMaterial setColor(double r255, double g255, double b255, double opacity){
        getColor().setColor(r255, g255, b255,opacity);
        return this;
    }
    public ColorObject getColor(){
        return (ColorObject)getProperty(PROPNAME_COLOR).castoToPropertyObject().getObject();
    }
    
    public double getElasticModulus(){
        return getProperty(PROPNAME_ELASTICMOD).castoToPropertyDimension().getValue();
    }
    public double getPoissonRatio(){
        return getProperty(PROPNAME_POISSON).castToPropertyDouble().getValue();
    }
    public double getDensity(){
        return getProperty(PROPNAME_DENSITY).castoToPropertyDimension().getValue();
    }
    
}
