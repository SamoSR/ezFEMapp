/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GermanSR
 */
public abstract class Command {
    
    
    final String textCmd;
    final String shortCmd;
    List<CommandParameter> params;
    
    public Command(String textCmd){
        this.textCmd=textCmd;
        this.shortCmd="";
        params = new ArrayList<>();
    }
    
    public CommandResult getRequiredParamsInfo(){
        CommandResult result = new CommandResult();
        result.result = false;
        result.addLine("incorrect number of parameters for command <"+textCmd+">");
        String text="parameters required: ";
        for(CommandParameter param:params){
            text+="<"+param.name+">"+",";
        }
        result.addLine(text.substring(0,text.length()-1));
        return result;
    }
    
    public CommandResult setParameters(String[] parameters){  
        if(params.size()!=parameters.length-1){
            return getRequiredParamsInfo();
        }
        int i=0;
        for(CommandParameter para:params){
            para.value = parameters[i+1];
            i++;
        }
        CommandResult cmdResult = new CommandResult();
        cmdResult.result=true;
        return cmdResult;
    }
    
    public abstract CommandResult execute(CommandManager mng);
    

    public void addParam(String name){
        params.add(new CommandParameter(name));
    }
    
    public void setParamValue(String paramName, String val){
        for(CommandParameter param:params){
            if(param.name.equals(paramName)){
                param.value = val;
            }
        }
    }
    public String getParamValue(String paramName){
        for(CommandParameter param:params){
             if(param.name.equals(paramName)){
                 return param.value;
             }
        }
        return "";
    }
    
    public CommandResult checkParamExistence(){
        CommandResult result = new CommandResult(); 
        for(CommandParameter param:params){
            if(param.required){
                if(param.value.equals("")){
                    result.addLine("required param: "+param.name+" is empty");
                    result.result=false;
                }else if(param.value.isEmpty()){
                    result.addLine("required param: "+param.name+" is empty");
                    result.result=false;
                }else if(param.value==null){
                    result.addLine("required param: "+param.name+" is null");
                    result.result=false;
                }
            }
        }
        result.result=true;
        result.addLine(textCmd);
        return result;
    }
    
}
