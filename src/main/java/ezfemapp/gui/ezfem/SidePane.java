/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.gui.mdcomponents.MyRippler;
import ezfemapp.gui.mdcomponents.TextIconButton;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author GermanSR
 */
public class SidePane extends JFXDrawer{
    

    GUImanager gui;
    SidePane refThis;
    double logoPanelSize = 175;
    
    public SidePane(GUImanager gui){
        
        this.gui=gui;
        refThis=this;
        
        AnchorPane leftDrawerPane = new AnchorPane();
       // leftDrawerPane.setStyle(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_PANEL_BACKGROUND));
        
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);
        setSidePane(leftDrawerPane);
        setDefaultDrawerSize(GUImanager.sidePanelWidth);
        setOverLayVisible(false);
        setResizableOnDrag(false);
        
        setOnDrawerClosed(new EventHandler<JFXDrawerEvent>() {
            @Override 
            public void handle(JFXDrawerEvent event) {
                //setDefaultDrawerSize(0);
                gui.getRootPane().getChildren().remove(refThis);
            }
        });
      
        StackPane pane = new StackPane();
        pane.setPrefHeight(logoPanelSize);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        pane.setStyle("-fx-background-color: rgb(225, 245, 254,1);");
        leftDrawerPane.getChildren().add(pane);
        
        double offset = 2;
        double btnWidth= GUImanager.sidePanelWidth-offset*2;
        double btnHeight= 40;
        double iconSize = 20;
        int textSize = 12;
        String font = "Arial";
        Color btnColor = Color.TRANSPARENT;
        Color iconColor = Color.DARKGRAY;
        Color textColor = Color.BLACK;
        
        VBox box = new VBox();   
        //SAVE ICON
        TextIconButton btn = new TextIconButton(); 
        btn.setTextElement("New Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.FILE, iconSize+"px",iconColor); 
        btn.adjustIconPosition(-btnWidth/2+15, 0);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.newDefaultProject();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Open Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.FOLDER_OPEN, iconSize+"px",iconColor); 
        btn.adjustIconPosition(-btnWidth/2+15, 0);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.openProjectFromLocalFile();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Save Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.SAVE, iconSize+"px",iconColor); 
        btn.adjustIconPosition(-btnWidth/2+15, 0);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.saveProjectToLocalFile();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Save Project As", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.COPY, iconSize+"px",iconColor); 
        btn.adjustIconPosition(-btnWidth/2+15, 0);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.saveAs();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("About", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.INFO, iconSize+"px",iconColor); 
        btn.adjustIconPosition(-btnWidth/2+15, 0);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        AnchorPane.setTopAnchor(box, logoPanelSize);
        leftDrawerPane.getChildren().add(box);

    }
    
   
    
    public void closePanel(){
        close();
        gui.getRootPane().getChildren().remove(this);
    }
    
    public void openPanel(){
        if(!gui.getRootPane().getChildren().contains(this)){
            gui.getRootPane().getChildren().add(this);
        }    
        open();
    }
    

}
