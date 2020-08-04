/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import java.util.ArrayList;
import java.util.List;

    public class KeyBoardRow{
        
        double offset = 0;
        double height = 50;
        List<KeyBoardKey> row = new ArrayList<>();
        List<Double> widths = new ArrayList<>();
        
        public KeyBoardRow(){
            
        }
        
        public void createWidths(double... values){
            widths = new ArrayList<>();
            for(Double v:values){
                widths.add(v);
            }
        }
        
        public void createAllWidths(double val){
            height = val;
            widths = new ArrayList<>();
            for(KeyBoardKey key:row){
                widths.add(val);
            }
        }
  
    }