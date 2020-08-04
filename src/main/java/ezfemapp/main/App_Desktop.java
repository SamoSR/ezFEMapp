/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.main;

import com.jfoenix.controls.JFXSnackbar;
import ezfemapp.blockProject.BlockProject;
import java.io.File;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serializableApp.commands.CommandManager;
import serializableApp.objects.Project;

/**
 *
 * @author GermanSR
 */
public class App_Desktop extends BasicApp{

    
    public App_Desktop(Stage primaryStage){
        BasicApp.platform = PLATFORM_DESKTOP;
        initializeGUI(primaryStage,false);
    }

    @Override
    public boolean openProjectFromLocalFile() {
        boolean result = false;
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Project");
        File file = fileChooser.showOpenDialog(gui.primaryStage);
        if (file != null) {
           Project proy = getBlocks().deserializeProject(file.getAbsolutePath());
           if(proy!=null){
               if(proy instanceof BlockProject){
                    setProjectPath(file.getAbsolutePath());
                    setProjectDirectory(file.getParent());
                    BlockProject blocks = (BlockProject)proy;
                    setProject(blocks);
                    result = true;
               }
           }
        }
        if(result){
            getGUImanager().showNotification("File opened: "+getProjectPath(),GUImanager.NOTIFICATION_SUCCESS,350,75);
        }else{
            getGUImanager().showNotification("Could not open selected file!",GUImanager.NOTIFICATION_WARNING,350,25);
        }
        getGUImanager().getSidePanel().closePanel();
        return result;
    }

    @Override
    public boolean saveProjectToLocalFile() {
        boolean result = false;
        File currentPath = new File(projectPath);
        if(currentPath.exists()){
            getBlocks().serializeProject(currentPath.getAbsolutePath());
        }else{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project");
            File file = fileChooser.showSaveDialog(gui.primaryStage);
            if (file != null) {
                getBlocks().serializeProject(file.getAbsolutePath());
                setProjectPath(file.getAbsolutePath());
                setProjectDirectory(file.getParent());
                getGUImanager().getSidePanel().closePanel();
                result = true;
            }
        }
        if(result){
            getGUImanager().showNotification("File saved to: "+getProjectPath(),GUImanager.NOTIFICATION_SUCCESS,350,75);
        }else{
            getGUImanager().showNotification("Could not save file!",GUImanager.NOTIFICATION_WARNING,350,25);
        }
        getGUImanager().getSidePanel().closePanel();
        return result;
    }

    @Override
    public boolean saveAs() {
        
        boolean result = false;
        File currentFile = new File(getProjectDirectory());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Project");
        if(currentFile.exists()){
            fileChooser.setInitialDirectory(currentFile);
        }
        File file = fileChooser.showSaveDialog(gui.primaryStage);

        if (file != null) {
            getBlocks().serializeProject(file.getAbsolutePath());
            setProjectPath(file.getAbsolutePath());
            setProjectDirectory(file.getParent());
            result = true;
        }  
        
        if(result){
            getGUImanager().showNotification("File saved to: "+getProjectPath(),GUImanager.NOTIFICATION_SUCCESS,350,75);
        }else{
            getGUImanager().showNotification("Could not save file!",GUImanager.NOTIFICATION_WARNING,350,25);
        }
        getGUImanager().getSidePanel().closePanel();
        
        return result;        
    }

    @Override
    public boolean saveProjectToCurrentPath() {
        boolean result = false;
        File file = new File(getProjectPath());
        if(file.exists()){
            try {
                getBlocks().serializeProject(file.getAbsolutePath());
                result = true;
            } catch (Exception e) {
                result = false;
            }   
        }
        if(result){
            getGUImanager().showNotification("File saved to: "+getProjectPath(),GUImanager.NOTIFICATION_SUCCESS,350,75);
        }else{
            getGUImanager().showNotification("Could not save file!",GUImanager.NOTIFICATION_WARNING,350,25);
        }
        getGUImanager().getSidePanel().closePanel();
        return result;
    }
    
    
    
}
