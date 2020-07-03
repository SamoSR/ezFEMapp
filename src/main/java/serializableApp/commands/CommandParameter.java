/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;

/**
 *
 * @author GermanSR
 */
public class CommandParameter {
    
    boolean required;
    String name;
    String value;
    
    public CommandParameter(String name,boolean required){
        this.name=name;
        this.required=required;
    }
    public CommandParameter(String name){
        this.name=name;
        this.required=true;
    }
}
