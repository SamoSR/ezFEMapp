/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.screen;

import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author GermanSR
 */
public abstract class AppScreen {
    
    String screenID;
    GUImanager gui;
    
    BorderPane screenMainPane;
    AppTopBar bar;
    AnchorPane centralPane;
    
    public AppScreen(String id,GUImanager gui){
        this.gui=gui;
        this.screenID = id;
        bar = new AppTopBar(this);
        centralPane = new AnchorPane();
        centralPane.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_PANEL_BACKGROUND)+";");
    }
    
    public String getID(){
        return screenID;
    }
    
    public GUImanager getGUI(){
        return gui;
    }
  
    public final Pane getMainPane(){
        return screenMainPane;
    }
    
    public AnchorPane getCentralPane(){
        return centralPane;
    }
    
    public final AppTopBar getAppBar(){
        return bar;
    }
    
    public void construct(){
        screenMainPane = new BorderPane();
        screenMainPane.setTop(bar);
        screenMainPane.setCenter(centralPane);
    }
    
    public abstract void loadScreen();
    public abstract void update(String... args);
    
    
}
