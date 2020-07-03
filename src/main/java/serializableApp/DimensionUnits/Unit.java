/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.DimensionUnits;

import java.util.ArrayList;
import java.util.List;

public class Unit {
	
        public static final String TYPE_FORCE="Force";
        public static final String TYPE_LENGTH="Length";
        public static final String TYPE_MASS="Mass";
    
        String type;
	String unit;
	double exp;
	
	public Unit(String name, double exp){
		this.unit=name;
		this.exp=exp;
	}
	
	public String getUnit(){
		return unit;
	}
	
	
	public double getPower(){
		return exp;
	}

	public double getConversionFactorFromKgMToUnit(){
            double factor = 1;
            switch(unit){
                case "GN":
                        factor = Math.pow( UnitUtils.kg_to_GN, exp);
                        break;
                case "N":
                        factor = Math.pow( UnitUtils.kg_to_N, exp);
                        break;
                case "kN":
                        factor = Math.pow( UnitUtils.kg_to_KN, exp);
                        break;
                case "lb":
                        factor = Math.pow( UnitUtils.kg_to_lb, exp);	
                        break;
                case "klb":
                        factor = Math.pow( UnitUtils.kg_to_Klb, exp);	
                        break;
                case "tonf":
                case "ton":
                        factor = Math.pow( UnitUtils.kg_to_ton, exp);		
                        break;
                case "slug":
                        factor = Math.pow( UnitUtils.kg_to_slug, exp);	
                        break;
                case "mm":
                        factor = Math.pow( UnitUtils.m_to_mm, exp);
                        break;
                case "cm":
                        factor = Math.pow( UnitUtils.m_to_cm, exp);
                        break;
                case "m":
                        factor = Math.pow( UnitUtils.m_to_m, exp);	
                        break;
                case "in":
                        factor = Math.pow( UnitUtils.m_to_in, exp);	
                        break;
                case "ft":
                        factor = Math.pow( UnitUtils.m_to_ft, exp);	
                        break;	
                case "kg":
                case "kgf":
                        factor = Math.pow( UnitUtils.kg_to_kg, exp);	
                        break;
                default:
                        factor = 1;
            }
            return factor;
			
	}
	
        public String unitType(){
            
            List<String> force = new ArrayList<>(); 
            List<String> mass = new ArrayList<>(); 
            List<String> length = new ArrayList<>();
            
            force.add("N");
            force.add("kN");
            force.add("GN");
            force.add("lb");
            force.add("klb");
            force.add("kgf");
            force.add("tonf");
            
            mass.add("kg");
            mass.add("ton");
            mass.add("slug");
            
            length.add("mm");
            length.add("cm");
            length.add("m");
            length.add("in");
            length.add("ft");
            length.add("");
            
            for(String unitStr:force){
                if(unitStr.equals(unit)){
                    this.type=TYPE_FORCE;
                }
            }
            for(String unitStr:mass){
                if(unitStr.equals(unit)){
                    this.type=TYPE_MASS;
                }
            }
            for(String unitStr:length){
                if(unitStr.equals(unit)){
                    this.type=TYPE_LENGTH;
                }
            }
            
            return "";
        }
        
        public boolean unitExist(){
            
            List<String> allUnits = new ArrayList<>(); 
            allUnits.add("N");
            allUnits.add("kN");
            allUnits.add("GN");
            allUnits.add("lb");
            allUnits.add("klb");
            allUnits.add("kgf");
            allUnits.add("kg");
            allUnits.add("tonf");
            allUnits.add("ton");
            allUnits.add("slug");
            allUnits.add("mm");
            allUnits.add("cm");
            allUnits.add("m");
            allUnits.add("in");
            allUnits.add("ft");
            allUnits.add("");
            
            for(String unitLoop:allUnits){
                if(unitLoop.equals(unit)){
                    return true;
                }
            }
 
            return false;
        }
        
	public double getConversionFactorFromUnitToKgM(){
            double factor = 1;
            /*
            switch(unit){
                case "N":
                        factor = Math.pow( 1/UnitUtils.kg_to_N, exp);
                        break;
                case "GN":
                        factor = Math.pow( 1/UnitUtils.kg_to_N, exp);
                        break;
                case "kN":
                        factor = Math.pow( 1/UnitUtils.kg_to_KN, exp);
                        break;
                case "lb":
                        factor = Math.pow( 1/UnitUtils.kg_to_lb, exp);	
                        break;
                case "klb":
                        factor = Math.pow( 1/UnitUtils.kg_to_Klb, exp);	
                        break;
                case "tonf":
                case "ton":
                        factor = Math.pow( 1/UnitUtils.kg_to_ton, exp);	
                        break;        
                case "slug":
                        factor = Math.pow( 1/UnitUtils.kg_to_slug, exp);	
                        break;
                case "mm":
                        factor = Math.pow( 1/UnitUtils.m_to_mm, exp);
                        break;
                case "cm":
                        factor = Math.pow( 1/UnitUtils.m_to_cm, exp);
                        break;
                case "m":
                        factor = Math.pow( 1/UnitUtils.m_to_m, exp);	
                        break;
                case "in":
                        factor = Math.pow( 1/UnitUtils.m_to_in, exp);	
                        break;
                case "ft":
                        factor = Math.pow( 1/UnitUtils.m_to_ft, exp);	
                        break;	
                case "kg":
                case "kgf":
                        factor = Math.pow( 1/UnitUtils.kg_to_kg, exp);	
                        break;        
                default:
                        factor = 1;
            }*/
            factor = 1/getConversionFactorFromKgMToUnit();
            return factor;
	}
	

	

}

