/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.DimensionUnits;


import java.util.ArrayList;
import java.util.List;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.SerializableObject;




/**
 *
 * @author GermanSR
 */
public class UnitsManagerPro extends SerializableObject{
    
    public static final String OBJECT_TYPE="UnitsManager";
    
    //ALL UNIT CODES
    public static final String UNITS_GLOBAL_POSITION = "Position";
    public static final String UNITS_ELEMENT_DIMENSIONS = "ElementDimension";  
    public static final String UNITS_DENSITY = "Density";
    public static final String UNITS_CONCRETE_STRENGTH = "ConcStrength";
    public static final String UNITS_STEEL_YIELD_STRENGTH = "SteelYieldStrength";
    public static final String UNITS_REACTION_FORCE = "ReacForce";
    public static final String UNITS_REACTION_MOMENT = "ReacMoment";
    public static final String UNITS_SOIL_PRESSURE= "SoilPressure";
    public static final String UNITS_REBAR_DIAMETER="RebarDiameter";
    public static final String UNITS_REBAR_SPACING="RebarSpacing";
    public static final String UNITS_REBAR_AREA="RebarArea";
    public static final String UNITS_ELASTIC_MODULUS="ElasticModulus";
    public static final String UNITS_FEM_STRESS="FEMStress";
     
    public static final String UNITS_LOAD_POINT_FORCE = "LoadForce";
    public static final String UNITS_LOAD_LINEAR_FORCE = "LoadLinear";
    
    public static final String UNITS_WEIGHT= "Weight";
    public static final String UNITS_AREA_BIG= "AreaLarge";
    public static final String UNITS_AREA_SMALL= "AreaSmall";
     
    public static final String PROPNAME_UNITSLIST_DEF="UnitsListDefault";
    public static final String PROPNAME_UNITSLIST_USER="UnitsListUser";
    
   
    
    public UnitsManagerPro(){
       super(OBJECT_TYPE,OBJECT_TYPE);
       addProperty(new PropertyObjectList(PROPNAME_UNITSLIST_DEF,DimensionUnit.OBJECT_TYPE));
       addProperty(new PropertyObjectList(PROPNAME_UNITSLIST_USER,DimensionUnit.OBJECT_TYPE)); 
       setDefault();
       getProperty(PROPNAME_UNITSLIST_DEF).unserializableProp();
    }
    
    /*
    private static List<DimensionUnit> getDefaultUnits(){
       List<DimensionUnit> unitList = new ArrayList<>();
       unitList.add(new DimensionUnit(UNITS_GLOBAL_POSITION, "m"));
       unitList.add(new DimensionUnit(UNITS_ELEMENT_DIMENSIONS, "cm"));
       unitList.add(new DimensionUnit(UNITS_DENSITY, "ton/m^3"));
       unitList.add(new DimensionUnit(UNITS_CONCRETE_STRENGTH, "kgf/cm^2"));
       unitList.add(new DimensionUnit(UNITS_STEEL_YIELD_STRENGTH, "kgf/cm^2"));
       unitList.add(new DimensionUnit(UNITS_REACTION_FORCE, "tonf"));
       unitList.add(new DimensionUnit(UNITS_REACTION_MOMENT, "tonf*m"));
       unitList.add(new DimensionUnit(UNITS_SOIL_PRESSURE, "tonf/m^2"));
       unitList.add(new DimensionUnit(UNITS_WEIGHT, "ton"));
       unitList.add(new DimensionUnit(UNITS_AREA_BIG, "m^2"));
       unitList.add(new DimensionUnit(UNITS_AREA_SMALL, "cm^2"));
       unitList.add(new DimensionUnit(UNITS_REBAR_DIAMETER, "mm"));
       unitList.add(new DimensionUnit(UNITS_REBAR_SPACING, "cm"));
       unitList.add(new DimensionUnit(UNITS_REBAR_AREA, "cm^2"));
       return unitList;
    }*/
    
    private static List<DimensionUnit> getDefaultUnits(){
       List<DimensionUnit> unitList = new ArrayList<>();
       unitList.add(new DimensionUnit(UNITS_GLOBAL_POSITION, "m"));
       unitList.add(new DimensionUnit(UNITS_ELEMENT_DIMENSIONS, "cm"));
       unitList.add(new DimensionUnit(UNITS_DENSITY, "ton/m^3"));
       unitList.add(new DimensionUnit(UNITS_CONCRETE_STRENGTH, "kN/cm^2"));
       unitList.add(new DimensionUnit(UNITS_STEEL_YIELD_STRENGTH, "kN/cm^2"));
       unitList.add(new DimensionUnit(UNITS_REACTION_FORCE, "kN"));
       unitList.add(new DimensionUnit(UNITS_REACTION_MOMENT, "kN*m"));
       unitList.add(new DimensionUnit(UNITS_SOIL_PRESSURE, "kN/m^2"));
       unitList.add(new DimensionUnit(UNITS_WEIGHT, "ton"));
       unitList.add(new DimensionUnit(UNITS_AREA_BIG, "m^2"));
       unitList.add(new DimensionUnit(UNITS_AREA_SMALL, "cm^2"));
       unitList.add(new DimensionUnit(UNITS_REBAR_DIAMETER, "mm"));
       unitList.add(new DimensionUnit(UNITS_REBAR_SPACING, "cm"));
       unitList.add(new DimensionUnit(UNITS_REBAR_AREA, "cm^2"));
       unitList.add(new DimensionUnit(UNITS_ELASTIC_MODULUS, "kgf/cm^2"));
       unitList.add(new DimensionUnit(UNITS_LOAD_POINT_FORCE, "kN"));
       unitList.add(new DimensionUnit(UNITS_LOAD_LINEAR_FORCE, "kgf/cm"));
       unitList.add(new DimensionUnit(UNITS_FEM_STRESS, "kgf/cm^2"));
       return unitList;
    }
    
    
    public void setDefault(){
       PropertyObjectList unitList = (PropertyObjectList)getProperty(PROPNAME_UNITSLIST_DEF);
       unitList.clearList();
       for(DimensionUnit unit:getDefaultUnits()){
           unitList.addObjectUniqueID(unit);
       }
    }
    
    public DimensionUnit getUnits(String unitCode){
        List<DimensionUnit> allUnits = new ArrayList();
        PropertyObjectList unitList1 = (PropertyObjectList)getProperty(PROPNAME_UNITSLIST_DEF);
        PropertyObjectList unitList2 = (PropertyObjectList)getProperty(PROPNAME_UNITSLIST_DEF);
        for(SerializableObject obj:unitList1.getObjectList()){
           DimensionUnit unit = (DimensionUnit)obj;
           allUnits.add(unit);
        }
        for(SerializableObject obj:unitList2.getObjectList()){
           DimensionUnit unit = (DimensionUnit)obj;
           allUnits.add(unit);
        }
        for(DimensionUnit unit:allUnits){
            if(unit.getID().equals(unitCode)){
                return unit;
            }
        }
        return null;
    }
    
    
    
    public static DimensionUnit getDefault(String code){
        for(DimensionUnit unit:getDefaultUnits()){
            if(unit.getID().equals(code)){
                return unit;
            }
       }
       return null;
    }
    
}
