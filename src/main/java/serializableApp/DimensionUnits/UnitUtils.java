/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.DimensionUnits;

import java.util.ArrayList;
import java.util.List;
import serializableApp.utils.RoundDouble;


/**
 *
 * @author GermanSR
 */
public class UnitUtils {
    
    	//CONVERSION FROM THE STORED UNIT OF LENGTH TO THE OUTPUT UNIT OF LENGTH
	static double m_to_mm = 1000;
	static double m_to_cm = 100;
	static double m_to_m = 1;
	static double m_to_in = 39.3700787;
	static double m_to_ft = 3.2808399;
	
	//CONVERSION FROM THE STORED UNIT OF FORCE TO THE OUTPUT UNIT OF FORCE
        static double kg_to_g=1000;
	static double kg_to_kg=1;
	static double kg_to_ton=0.001;
	static double kg_to_N=9.80665002864;

	static double kg_to_lb=2.20462262;
	static double kg_to_Klb=0.00220462262;
        
        static double kg_to_KN=9.80665002864e-3;
        static double kg_to_GN=9.80665002864e-9;
        static double kg_to_MN=9.80665002864e-6;
        
	static double kg_to_slug=.0685218;
	
	public static ArrayList<Unit> splitMultiplication(String inputString){
			
		String[] unitsMult = inputString.split("\\*");
		ArrayList<Unit> multUnits = new ArrayList<>();	
		
		for(String unitPower:unitsMult){
			
			String[] unitAndPower = unitPower.split("\\^");
			
			if(unitAndPower.length==1){
				multUnits.add(new Unit(unitPower,1.0));
			}else if(unitAndPower.length==2){
				String unit = unitAndPower[0];
				String power = unitAndPower[1];
				double powerNum = 1;
				try {
					powerNum = Double.parseDouble(power);
				} catch (Exception e) {
					 powerNum = 1;
				}
				multUnits.add(new Unit(unit,powerNum));
			}else{
				multUnits.add(new Unit("null",0));
			}
			
		}
		
		return multUnits;	
	}
        
	public static boolean compatibleUnits(String units1, String units2){
            
            if(units1.equals("psi")){
                units1 ="lb/in^2";
            }
            if(units1.equals("ksi")){
                units1 ="klb/in^2";
            }
            if(units1.equals("GPa")){
                units1 ="GN/m^2";
            }
            if(units1.equals("MPa")){
                units1 ="MN/m^2";
            }
            if(units1.equals("kPa")){
                units1 ="kN/m^2";
            }
            if(units1.equals("Pa")){
                    units1 ="N/m^2";
            }
            if(units2.equals("psi")){
                units2 ="lb/in^2";
            }
            if(units2.equals("ksi")){
                units2 ="klb/in^2";
            }
            if(units2.equals("GPa")){
                units2 ="GN/m^2";
            }
            if(units2.equals("MPa")){
                units2 ="MN/m^2";
            }
            if(units2.equals("kPa")){
                units2 ="kN/m^2";
            }
            if(units2.equals("Pa")){
                    units2 ="N/m^2";
             }
            
            String[] parts = units1.split("/");
            String topString="";
            String botString="";
            if(parts.length==0){
                    return false;
            }	
            if(parts.length>2){
                    return false;
            }
            if(parts.length==1){
                    topString = parts[0];
                    botString = "";
            }else{
                    topString = parts[0];
                    botString = parts[1];
            }
            ArrayList<Unit> unitsTop1 = new ArrayList<>();	
            ArrayList<Unit> unitsBot1 = new ArrayList<>();	
            unitsTop1 = splitMultiplication(topString);
            unitsBot1 = splitMultiplication(botString);
            
            parts = units2.split("/");
            topString="";
            botString="";
            if(parts.length==0){
                    return false;
            }	
            if(parts.length>2){
                    return false;
            }
            if(parts.length==1){
                    topString = parts[0];
                    botString = "";
            }else{
                    topString = parts[0];
                    botString = parts[1];
            }
            ArrayList<Unit> unitsTop2;	
            ArrayList<Unit> unitsBot2;	
            unitsTop2 = splitMultiplication(topString);
            unitsBot2 = splitMultiplication(botString);
     
            List<String> topUnitsString1 = getAllUnitList(unitsTop1);
            List<String> topUnitsString2 = getAllUnitList(unitsTop2);
            List<String> botUnitsString1 = getAllUnitList(unitsBot1);
            List<String> botUnitsString2 = getAllUnitList(unitsBot2);
            
            /*
            System.out.println("COMPARING ARRAYS");
            
            for(String str:topUnitsString1){
                System.out.println("tunits 1: "+str);
            }
            for(String str:topUnitsString2){
                System.out.println("tunits 2: "+str);
            }
            
            for(String str:botUnitsString1){
                System.out.println("bunits 1: "+str);
            }
            for(String str:botUnitsString2){
                System.out.println("bunits 2: "+str);
            }
            */
            if(!containsSameItems(topUnitsString1,topUnitsString2)){
                return false;
            }
            if(!containsSameItems(botUnitsString1,botUnitsString2)){
                return false;
            }
            
            return true;
        }
        
        private static boolean containsSameItems(List<String> list1, List<String> list2){
            for(String item1:list1){
                boolean contains=false;
                for(String item2:list2){
                    if(item1.equals(item2)){
                        contains=true;
                    }
                }
                if(!contains){
                    return false;
                }
            }
            return true;
        }
       
        private static List<String> getAllUnitList( ArrayList<Unit> units){
            List<String> list = new ArrayList<>();
            ArrayList<Unit> force = forceUnits(units);
            ArrayList<Unit> length = lengthUnits(units);
            ArrayList<Unit> mass = massUnits(units);
            for(Unit u:force){
                list.add(u.type+u.exp);
            }
            for(Unit u:length){
                list.add(u.type+u.exp);
            }
            for(Unit u:mass){
                list.add(u.type+u.exp);     
            }
            return list;
        }
        
        private static ArrayList<Unit> forceUnits( ArrayList<Unit> units){
            ArrayList<Unit> unitsReturn = new ArrayList();
            for(Unit u:units){
                u.unitType();
                if(u.type.equals(Unit.TYPE_FORCE)){
                    unitsReturn.add(u);
                }
            }
            return unitsReturn;
        }
        private static ArrayList<Unit> massUnits( ArrayList<Unit> units){
            ArrayList<Unit> unitsReturn = new ArrayList();
            for(Unit u:units){
                if(u.type.equals(Unit.TYPE_MASS)){
                    unitsReturn.add(u);
                }
            }
            return unitsReturn;
        }
        private static ArrayList<Unit> lengthUnits( ArrayList<Unit> units){
            ArrayList<Unit> unitsReturn = new ArrayList();
            for(Unit u:units){
                if(u.type.equals(Unit.TYPE_LENGTH)){
                    unitsReturn.add(u);
                }
            }
            return unitsReturn;
        }
        
        public static boolean validateUnit(String units){
            
            if(units.equals("psi")){
                units ="lb/in^2";
            }
            if(units.equals("ksi")){
                units ="klb/in^2";
            }
             if(units.equals("GPa")){
                units ="GN/m^2";
            }
            if(units.equals("MPa")){
                units ="MN/m^2";
            }
            if(units.equals("kPa")){
                units ="kN/m^2";
            }
            if(units.equals("Pa")){
                    units ="N/m^2";
                }
            
            String[] parts = units.split("/");
            String topString="";
            String botString="";
            if(parts.length==0){
                    return false;
            }	
            if(parts.length>2){
                    return false;
            }
            if(parts.length==1){
                    topString = parts[0];
                    botString = "";
            }else{
                    topString = parts[0];
                    botString = parts[1];
            }
            ArrayList<Unit> unitsTop = new ArrayList<>();	
            ArrayList<Unit> unitsBot = new ArrayList<>();	
            unitsTop = splitMultiplication(topString);
            unitsBot = splitMultiplication(botString);
            for(Unit u:unitsTop){
                if(!u.unitExist()){
                    return false;
                }
            }
            for(Unit u:unitsBot){
                if(!u.unitExist()){
                    return false;
                }
            }
            return true;
        }
        
	public static double convertToKgM(double value, String units){
		
                if(units.equals("psi")){
                    units ="lb/in^2";
                }
                if(units.equals("ksi")){
                    units ="klb/in^2";
                }
                if(units.equals("GPa")){
                    units ="GN/m^2";
                }
                if(units.equals("MPa")){
                    units ="MN/m^2";
                }
                if(units.equals("kPa")){
                    units ="kN/m^2";
                }
                if(units.equals("Pa")){
                    units ="N/m^2";
                }
            
		String[] parts = units.split("/");

		String topString="";
		String botString="";
		
		if(parts.length==0){
			return value;
		}
		
		if(parts.length>2){
			return value;
		}
		
		if(parts.length==1){
			topString = parts[0];
			botString = "";
		}else{
			topString = parts[0];
			botString = parts[1];
		}
			
               
                
		ArrayList<Unit> unitsTop = new ArrayList<>();	
		ArrayList<Unit> unitsBot = new ArrayList<>();	
		unitsTop = splitMultiplication(topString);
		unitsBot = splitMultiplication(botString);
		
		double factorTop = 1;
		for(Unit u:unitsTop){
			factorTop*=u.getConversionFactorFromUnitToKgM();
		}
		
		double factorBot = 1;
		for(Unit u:unitsBot){
			factorBot*=u.getConversionFactorFromUnitToKgM();
		}

		return value*(factorTop/factorBot);
	}
	
	public static double convertFromKgMToUnit(double value, String units){
            
                if(units.equals("psi")){
                    units ="lb/in^2";
                }
                if(units.equals("ksi")){
                    units ="klb/in^2";
                }
                if(units.equals("GPa")){
                    units ="GN/m^2";
                }
                if(units.equals("MPa")){
                    units ="MN/m^2";
                }
                if(units.equals("kPa")){
                    units ="kN/m^2";
                }
                if(units.equals("Pa")){
                    units ="N/m^2";
                }
            
                if(units==null){
                    return value;
                }
		if(units.isEmpty()){
                    return value;
                }
                
		String[] parts = units.split("/");

		String topString="";
		String botString="";
		
		if(parts.length==0){
			return value;
		}
		
		if(parts.length>2){
			return value;
		}
		
		if(parts.length==1){
			topString = parts[0];
			botString = "";
		}else{
			topString = parts[0];
			botString = parts[1];
		}
                
		ArrayList<Unit> unitsTop = new ArrayList<>();	
		ArrayList<Unit> unitsBot = new ArrayList<>();	
		unitsTop = splitMultiplication(topString);
		unitsBot = splitMultiplication(botString);
		
		double factorTop = 1;
		for(Unit u:unitsTop){
			factorTop*=u.getConversionFactorFromKgMToUnit();
		}
		double factorBot = 1;
		for(Unit u:unitsBot){
			factorBot*=u.getConversionFactorFromKgMToUnit();
		}
		return value*(factorTop/factorBot);
	}
        
        //ROUND TO TWO DECIMALS AND DISPLAYING THE UUNIT AT THE END
        public static String fromKgMToUnit_round2wU(double val, String units){
            return RoundDouble.Round2(convertFromKgMToUnit(val,units))+" "+units;
        }
         //ROUND TO TWO DECIMALS 
	public static String fromKgMToUnit_round2(double val, String units){
            return RoundDouble.Round2(convertFromKgMToUnit(val,units));
        }
	
	public static double convertFromUnitToUnit(double val, String inputUnit, String outputUnit){
		double valueInKgM = convertToKgM(val, inputUnit);
		double valueInUnits = convertFromKgMToUnit(valueInKgM, outputUnit);
		return valueInUnits;
	}
        
        public static String convertToPrettyFormat(String realUnits){
            switch(realUnits){
                case "GN/m^2":
                    return "GPa";
                case "MN/m^2":
                    return "MPa"; 
                case "kN/m^2":
                    return "kPa";  
                case "N/m^2":
                    return "Pa"; 
                case "lb/in^2":
                    return "psi";
                case "klb/in^2":
                    return "ksi";     
                default:
                    return realUnits;
            }
        }
        
}
