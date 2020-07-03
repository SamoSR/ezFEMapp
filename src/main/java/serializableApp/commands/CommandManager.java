/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.commands;



import serializableApp.objects.Project;



/**
 *
 * @author GermanSR
 */
public class CommandManager {
    
    
    Project project;
  
    public CommandManager(Project project){
        this.project=project;
    }
    
    public Project getProject(){
        return project;
    }
    
    
    public CommandResult processCommand(String text){
        
        String[] params = text.split(",");
         //COMMANDS WITHOUT PARAMETERS
        if(params.length<=0){
            return new CommandResult(false,"null command");  
        }  

        //REMOVE WHITE SPACES
        for(int i=0;i<params.length;i++){
            params[i] = params[i].trim();
        }
        
        String command=params[0];
        Command cmd;
        
        switch(command){
            
            case "regui":
                
                //reInitializeGUI("command regui");
                //return new CommandResult(true,"Graphical User Interface re-loaded");
               
            
            case "save":
                
                //CommandResult cmdResult1 = app.getGUImng().saveProjectDialog();
                //updateProject("command save executed");
                //updateRenderer("command save executed");
                //return cmdResult1;

                
            case "load":   
                
                //CommandResult cmdResult2 = app.getGUImng().loadProjectDialog();
                //updateProject("command load executed");
                //updateRenderer("command load executed");
                //return cmdResult2;
                
            case "render":

                //updateRenderer("command render");
                //return new CommandResult(true,"current view re-rendered");
               
            
            case "refresh":
            case "re":
                
                //updateGUI("command refresh");
               // return new CommandResult(true,"project refreshed");
             
            case "update":
            case "uptd":
                 
                updateProject("command update");
                return new CommandResult(true,"references updated");
  
            case CmdNewObject.NAME:
            case CmdNewObject.SHORT:
                
                cmd = new CmdNewObject();
                break;
           
            case CmdDeleteObject.NAME:
            case CmdDeleteObject.SHORT:
               cmd = new CmdDeleteObject();
               break;
               
            case CmdEditProp.NAME:
            case CmdEditProp.SHORT:
                
                cmd = new CmdEditProp();
                break;
                
            default:
                
              return new CommandResult(false,"invalid command");
    
        }
        
        if(cmd==null){
            return new CommandResult(false,"unexpected command error");
        }
        
        CommandResult result = cmd.setParameters(params);
        if(!result.result){
           return result;
        }else{
           result = cmd.execute(this);
        }
        
        //IF THE COMMAND IS EXECUTED SUCCESFULY
        if(result.result){
            
            if(cmd instanceof CmdDeleteObject){
                
                updateProject("command delete executed");
                //updateGUI("command delete executed");
               
            }else if(cmd instanceof CmdNewObject){
                
                updateProject("command new object executed");
                //updateGUI("command new object executed");
                
            }else if(cmd instanceof CmdEditProp){
                
                updateProject("command edit executed");
                //updateRenderer("command edit executed");
                   
            }  
        }
        
        
        if(result.updateGUI()){
           // updateGUI("requested by command: "+cmd.textCmd);
        }
        
        return result;
        
    }
    
    public void updateProject(String calledBy){
        project.initializeReferenceProperties();
        project.referenceParentToProperties();
        project.updateReferenceProperties();
        System.out.println("UPDATING PROJECT REFERENCES, called by <"+calledBy+">");
    }




}
