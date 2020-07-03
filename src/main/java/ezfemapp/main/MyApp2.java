/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.main;

import ezfemapp.blockProject.BlockProject;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import serializableApp.commands.CommandManager;

/**
 *
 * @author GermanSR
 */
public class MyApp2 {
    
    public static final int PLATFORM_DESKTOP=0;
    public static final int PLATFORM_ANDROID=1;  
    int platform;
    
    CommandManager cmdManager;
    BlockProject blocks;
    GUImanager gui;
    String appName = "ezFEM";
    
    public MyApp2(){
        blocks = new BlockProject("NewProject");
        blocks.createDefaultProject();
    }

    public void initializeOnNewStage(Stage primaryStage){
        gui = new GUImanager(this,primaryStage);
        cmdManager = new CommandManager(blocks); 
        gui.createScreens();
        platform=PLATFORM_DESKTOP;
    }
    
    public void initializeOnPane(ReadOnlyDoubleProperty w,ReadOnlyDoubleProperty h){
        gui = new GUImanager(this);
        cmdManager = new CommandManager(blocks); 
        gui.setScreenWidthAndHeight(w, h);
        gui.createScreens();
        platform=PLATFORM_ANDROID;
    }
    
    public void setProject(BlockProject blocks){
        this.blocks = blocks;
        blocks.initializeReferenceProperties();
        blocks.referenceParentToProperties();
        blocks.updateReferenceProperties();
        blocks.generateBlockMatrix();
    }
    
    public GUImanager getGUImanager(){
        return gui;
    }

    
    public CommandManager getCmdManager(){
        return cmdManager;
    }
    public BlockProject getBlocks(){
        return blocks;
    }
    
}
