/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author GermanSR
 */
public class RoundDouble {
    
    public static String Round5(double number){
        DecimalFormat df = new DecimalFormat("#.#####");
        String NumberFormated = df.format(number);
        if(number!=0&&NumberFormated.equals("0")){		
                DecimalFormat df2 = new DecimalFormat("#.####E0");
                String NumberFormated2 = df2.format(number);
                return NumberFormated2;
        }else{
                return NumberFormated;
        }	
    }
    
    public static String Round0(double number){
        DecimalFormat df = new DecimalFormat("#");
        String NumberFormated = df.format(number);
        if(number!=0&&NumberFormated.equals("0")){		
                DecimalFormat df2 = new DecimalFormat("#.E0");
                String NumberFormated2 = df2.format(number);
                return NumberFormated2;
        }else{
                return NumberFormated;
        }	
    }
    
    public static String Round2(double number){
        DecimalFormat df = new DecimalFormat("#.##");
        String NumberFormated = df.format(number);
        if(number!=0&&NumberFormated.equals("0")){		
                DecimalFormat df2 = new DecimalFormat("#.##E0");
                String NumberFormated2 = df2.format(number);
                return NumberFormated2;
        }else{
                return NumberFormated;
        }	
     }
    
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

