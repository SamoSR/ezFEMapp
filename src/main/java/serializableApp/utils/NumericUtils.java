/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;



/**
 *
 * @author GermanSR
 */
public class NumericUtils {
    
    public static final double[] getMaxMin(double... values){
        double max, min;
        max = Double.NEGATIVE_INFINITY;
        min = Double.POSITIVE_INFINITY;
        for(double num:values){
            if(num>max){
                max = num;
            }
            if(num<min){
                min=num;
            }
        }
        return new double[]{max,min};
    }
    
    public static final double[] getMaxMinArray(double[] values){
        double max, min;
        max = Double.NEGATIVE_INFINITY;
        min = Double.POSITIVE_INFINITY;
        for(double num:values){
            if(num>max){
                max = num;
            }
            if(num<min){
                min=num;
            }
        }
        return new double[]{max,min};
    }
    
    public static final NumericResult isNumeric(String val){
        try {
            double number = Double.parseDouble(val);
            return new NumericResult(number);
        } catch (Exception e){
            return new NumericResult(false);
        }
    }
    
    public static final double normalize(double xreal, double min, double max){
        return (xreal-min) / (max-min);
    }

    
}
