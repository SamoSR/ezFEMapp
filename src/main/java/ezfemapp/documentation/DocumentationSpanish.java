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
public class DocumentationSpanish extends DocumentationStrings{
    
    public DocumentationSpanish(){
        
        super(LANGUAGE_ENGLISH);
        
        addString(STRING_ITEM_DOCUMENTATION, "Documentación");
        
        
        addString(ITEM_MODELING, "Modelado");
        addString(ITEM_ANLYSIS, "Analysis");
        addString(ITEM_RESULTS, "Resultados");
        
        addString(ITEM_MODELING_ELEMENTS, "Elementos");
        addString(ITEM_MODELING_LOADCASES, "Casos de carga");
        addString(ITEM_MODELING_LOADS, "Cargas");
        addString(ITEM_MODELING_MATERIALS, "Materiales");
        
        addString(ITEM_RESULTS_COLORFIELDS, "Colores");
        addString(ITEM_RESULTS_GENERAL, "General");
        addString(ITEM_RESULTS_GENERAL_LOADCASES, "Casos de carga activos");
        addString(ITEM_RESULTS_GENERAL_SCALE, "Escala de deformación");
    }
    
}
