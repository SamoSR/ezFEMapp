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
public class StringResultInvalid extends StringResult{
    
     public StringResultInvalid(String error){
        super(false,"");
        this.error = error;
    }
    
}
