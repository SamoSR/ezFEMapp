/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.documentation.DocumentationScreen;
import ezfemapp.gui.mdcomponents.MyRippler;
import ezfemapp.gui.mdcomponents.TextIconButton;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.main.BasicApp;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
        
        ImageView imageView = null;
        String iconPath = this.getClass().getResource("/icons/zf250x250.png").toExternalForm(); 
        try {
            Image image = new Image(iconPath);
            imageView = new ImageView(image); 
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        } catch (Exception e) {
            System.out.println("FAILLED TO LOAD IAMGE");
        }
       
       
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
        Text txt = utilsGUI.create(BasicApp.APP_NAME, GUImanager.defaultFont, 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND));
        pane.getChildren().add(txt);
        txt.setTranslateY(50+5);
        
        pane.setPrefHeight(logoPanelSize);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        if(imageView!=null){
            pane.getChildren().add(imageView);
            //imageView.setTranslateX(-50);
            //imageView.setTranslateY(-20);
        }
        pane.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_MAIN)+";");
        leftDrawerPane.getChildren().add(pane);
        
        double spacingPaneHeight = 5;
        StackPane spacingPane = new StackPane();
        spacingPane.setPrefHeight(spacingPaneHeight);
        AnchorPane.setTopAnchor(spacingPane, logoPanelSize);
        AnchorPane.setLeftAnchor(spacingPane, 0.0);
        AnchorPane.setRightAnchor(spacingPane, 0.0);
        spacingPane.setStyle("-fx-background-color: white;");
        leftDrawerPane.getChildren().add(spacingPane);
        
        double offset = 2;
        double btnWidth= GUImanager.sidePanelWidth-offset*2;
        double btnHeight= 40;
        double iconSize = GUImanager.sidePanelIconSize;
        int textSize = 12;
        String font = GUImanager.defaultFont;
        Color btnColor =  new Color(0.1,0.1,0.1,1);
        Color iconColor = new Color(0.2,0.2,0.2,1);
        Color textColor = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT);
        
        
        VBox box = new VBox();   
        
        /*Line l = new Line(0,0,btnWidth-50,0);
        l.setTranslateX(25);
        l.setStrokeWidth(5);
        l.setStroke(Color.TRANSPARENT);
        box.getChildren().add(l);*/
        
        //SAVE ICON
        TextIconButton btn = new TextIconButton(); 
        btn.setTextElement("Home Screen", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.HOME, (iconSize+5)+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{

            gui.loadScreen(SplashScreen.ID);
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        
        //SAVE ICON
        btn = new TextIconButton(); 
        btn.setTextElement("New Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.FILE, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.getApp().newDefaultProject();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Open Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.FOLDER_OPEN, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.getApp().openProjectFromLocalFile();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Save Project", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.SAVE, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.getApp().saveProjectToLocalFile();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("Save Project As", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.COPY, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.getApp().saveAs();
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        
        btn = new TextIconButton(); 
        btn.setTextElement("Documentation", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.BOOK, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //gui.loadScreen(DocumentationScreen.ID);
            gui.showNotification("Coming soon!", GUImanager.NOTIFICATION_YELLOWALERT, 250, 30);
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        btn = new TextIconButton(); 
        btn.setTextElement("About", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.INFO, iconSize+"px",iconColor); 
        btn.setBtnWidth(btnWidth);
        btn.setBtnWidth(btnWidth);
        btn.setBtnHeight(btnHeight);
        btn.setEventHandler((event)->{
            //getGUI().getApp().getBlocks().clearAll();
            //canvas.renderAllBlocks();
            gui.loadScreen(AboutScreen.ID);
        });
        btn.construct();
        btn.setTranslateX(offset);
        box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
       
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(box);
        scroll.fitToHeightProperty().set(true);
        scroll.fitToWidthProperty().set(true);
        AnchorPane.setTopAnchor(scroll, logoPanelSize+spacingPaneHeight);
        AnchorPane.setBottomAnchor(scroll, 0.0);
        leftDrawerPane.getChildren().add(scroll);
        
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/ScrollPaneTransparent.css").toExternalForm(); 
        scroll.getStylesheets().add(css);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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
