/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;


public class NumericResult{
    
        boolean isNumber;
        double value;

        public NumericResult(boolean val){
            isNumber = val;
            value = Double.NaN;
        }
        public NumericResult(double val){
            isNumber = true;
            value = val;
        }
}