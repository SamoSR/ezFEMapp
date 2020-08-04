/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.documentation;

import java.util.HashMap;

/**
 *
 * @author GermanSR
 */
public abstract class DocumentationStrings {
    
    public static final String LANGUAGE_SPANISH = "Español";
    public static final String LANGUAGE_ENGLISH = "English";
    
    public static final String STRING_ITEM_DOCUMENTATION = "documentation";
    
    /*
    public static final String STRING_ITEM_GENERAL = "general";
    public static final String STRING_ITEM_GENERAL_UNITS = "units";
    public static final String STRING_ITEM_GENERAL_GRID = "grid";
    
    public static final String STRING_ITEM_ELEMENTS = "elements";
    public static final String STRING_ITEM_ELEMENTS_MATERIALS = "materials";
    */
    
    public static final String ITEM_MODELING = "modeling";
    public static final String ITEM_ANLYSIS = "analysis";
    
    public static final String ITEM_MODELING_ELEMENTS = "elements";
    public static final String ITEM_MODELING_LOADS = "loads";
    public static final String ITEM_MODELING_MATERIALS = "materials";
    public static final String ITEM_MODELING_LOADCASES = "loadcases";
  
    public static final String ITEM_RESULTS = "results";
    
    public static final String ITEM_RESULTS_GENERAL = "resultsGeneral";
    public static final String ITEM_RESULTS_GENERAL_LOADCASES = "resultsCases";
    public static final String ITEM_RESULTS_GENERAL_SCALE = "resultsScale";
   
    public static final String ITEM_RESULTS_COLORFIELDS = "resultsGeneral";
    
 
    
    
    String language;
    HashMap<String,String> strings = new HashMap<>();
    
    
    public DocumentationStrings(String language){
        this.language=language; 
    }
    
    public String get(String key){
        return strings.get(key);
    }
            
    
    public void addString(String key, String text){
        strings.put(key, text);
    }
    
            
            
    
}
