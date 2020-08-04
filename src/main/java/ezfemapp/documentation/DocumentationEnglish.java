/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.documentation;

/**
 *
 * @author GermanSR
 */
public class DocumentationEnglish extends DocumentationStrings{
    
    public DocumentationEnglish(){
        
        super(LANGUAGE_ENGLISH);
        
        addString(STRING_ITEM_DOCUMENTATION, "Documentation");
        
        
        addString(ITEM_MODELING, "Modeling");
        addString(ITEM_ANLYSIS, "Analysis");
        addString(ITEM_RESULTS, "Results");
        
        addString(ITEM_MODELING_ELEMENTS, "Elements");
        addString(ITEM_MODELING_LOADCASES, "LoadCases");
        addString(ITEM_MODELING_LOADS, "Loads");
        addString(ITEM_MODELING_MATERIALS, "Materials");
        
        addString(ITEM_RESULTS_COLORFIELDS, "Color Field");
        addString(ITEM_RESULTS_GENERAL, "General");
        addString(ITEM_RESULTS_GENERAL_LOADCASES, "Active LoadCases");
        addString(ITEM_RESULTS_GENERAL_SCALE, "Deformation Scale");
    }
    
}
