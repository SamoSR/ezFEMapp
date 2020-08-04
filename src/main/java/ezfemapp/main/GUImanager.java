/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.main;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXSnackbar;


import ezfemapp.blockProject.BlockProject;
import ezfemapp.documentation.DocumentationScreen;
import ezfemapp.gui.ezfem.AboutScreen;
import ezfemapp.gui.ezfem.AnalysisScreen;
import ezfemapp.gui.ezfem.ObjListEditScreen;
import ezfemapp.gui.ezfem.ModelingScreen;
import ezfemapp.gui.ezfem.SidePane;
import ezfemapp.gui.ezfem.SplashScreen;
import ezfemapp.gui.ezfem.VariousObjectEditScreen;
import ezfemapp.gui.mdcomponents.keyboard.ScreenKeyBoard;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.gui.theme.ThemeWhite;
import java.util.HashMap;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author GermanSR
 */
public class GUImanager {
    
    public static ColorTheme colorTheme;
    
    public static double toolBoxIconSize = 32;
    public static double topBarHeight = 42;
    public static double topBarTextSize = 18;
    public static double topBarBurgerIconSize = 20;
    
    public static double sidePanelIconSize = 16;
    
    public static double hollowCircleButtonRadius = 16;
    public static double hollowCircleIconSize = 20;
    
    public static double sidePanelWidth = 250;
    public static double appBarIconSize = 28;
    
    public static String defaultFont = "Roboto";
    
    public static final String SCREEN_SETTINGS="Project Settings";
    
    HashMap<String,AppScreen> screens = new HashMap<>();
    AnchorPane root;
    Scene mainScene;   
    String mainScreen = ModelingScreen.ID;
    String currentScreen="";
    SidePane sidePane;
    BasicApp app;
    
    String currentProjectPath="";
    String currentProjectFolder = "";
   // boolean ignoreTouch=false;

    ReadOnlyDoubleProperty width;
    ReadOnlyDoubleProperty height;
    Stage primaryStage;
    
    public static ScreenKeyBoard keyboard;
    
    public GUImanager(BasicApp app,Stage primaryStage, boolean maximized){
        this.primaryStage = primaryStage;
        this.app = app;
        initialize(maximized);
        width = mainScene.widthProperty();
        height = mainScene.heightProperty();
        createScreens(SplashScreen.ID);
        
        keyboard = new ScreenKeyBoard(width.doubleValue(),height.doubleValue());
        keyboard.createOnContainer(root);
       
    }
    
    /*
    public GUImanager(BasicApp app, ReadOnlyDoubleProperty w,ReadOnlyDoubleProperty h){
        this.app = app;
        width = w;
        height = h;
        initialize();
        createScreens(SplashScreen.ID);
    }*/
    
    private void initialize(boolean maximized){
        
        root = new AnchorPane();
        
        //LOAD FONT
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/robotoFont.css").toExternalForm(); 
        root.getStylesheets().add(css);
        
        colorTheme = new ThemeWhite();
        sidePane = new SidePane(this);
        
        
        root.setOnMouseClicked((event)-> {
            double x = event.getSceneX();
            if(root.getChildren().contains(sidePane)){
                if(x>sidePanelWidth){
                    sidePane.close();
                   // ignoreTouch=false;
                }
            }
           
        });
        
        //INITIAL SIZE
        int w = 600;
        int h = 400;
        if(maximized){
            Rectangle2D r = Screen.getPrimary().getVisualBounds();
            r.getWidth();
            r.getHeight();      
        }
        
        //CREATE THE SCENE SIZE OF THE SCREEN IF ITS MAXIMIZED
        JFXDecorator decorator = new JFXDecorator(primaryStage,root);
        mainScene = new Scene(decorator, w, h);
        primaryStage.setTitle("");
        primaryStage.setScene(mainScene);
        primaryStage.show();
      
        //SET FULL SCREEN TO REMOVE THE TOP BLACK BAR
        //THIS MUST BE CALLED AFTER CREATING THE SCENE!!
        if(maximized){
          primaryStage.setFullScreen(true); 
        }
  
        
        //keyboard.show(getGUI().getRootPane());

    }
    

    
    public void reLoadGUI(){
        createScreens(ModelingScreen.ID);
        getSidePanel().closePanel();
    }

    public AnchorPane getRootPane(){
        return root;
    }
    
    public SidePane getSidePanel(){
        return sidePane;
    }

    public void setScreenWidthAndHeight(ReadOnlyDoubleProperty w,ReadOnlyDoubleProperty h){
        width=w;
        height=h;
    }
    
    public ReadOnlyDoubleProperty getWidth(){
        return width;
    }
    public ReadOnlyDoubleProperty getHeight(){
        return height;
    }
    
    public static final String NOTIFICATION_WARNING="warning";
    public static final String NOTIFICATION_THEMED="theme";
    public static final String NOTIFICATION_WHITE="white";
    public static final String NOTIFICATION_ERROR="error";
    public static final String NOTIFICATION_SUCCESS="success";
    public static final String NOTIFICATION_YELLOWALERT="alertYellow";
    
    public void showNotification(String message, String textAreaCssFile, double w, double h){
        String colorBG;
        Color labelColor;
        
        switch(textAreaCssFile){
        case NOTIFICATION_THEMED:
            labelColor = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT);
            colorBG = GUImanager.colorTheme.getColor(ColorTheme.COLOR_MAIN);
            break;
        case NOTIFICATION_ERROR:
            labelColor = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT);
            colorBG = "indianred";
            break;
        case NOTIFICATION_SUCCESS:
            labelColor = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT);
            colorBG = "limegreen";
            break;
        case NOTIFICATION_WARNING:
            labelColor =  GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT);
            colorBG = "orange";
            break;
        case NOTIFICATION_YELLOWALERT:
            labelColor =  GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT);
            colorBG = "yellow";
            break;
        default:
            labelColor = Color.BLACK;
            colorBG = "white";
        } 
        
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: "+colorBG+";");

        Text txt = utilsGUI.create(message, "Roboto", 12, labelColor);
      
        pane.setPrefSize(w, h);
        pane.getChildren().add(txt);
        
        JFXSnackbar bar = new JFXSnackbar(getCurrentScreen().getCentralPane());
        bar.enqueue(new JFXSnackbar.SnackbarEvent(pane));
    }
    


    
    public BasicApp getApp(){
        return app;
    }

    public ColorTheme colorTheme(){
        return colorTheme;
    }
    
    public AppScreen getScreen(String id){
        return screens.get(id);
    }
    
    public AppScreen getCurrentScreen(){
        return getScreen(currentScreen);
    }

    private void createScreens(String id){
        
        screens = new HashMap<>();
        screens.clear();
        
        AppScreen appModelingScreen = new ModelingScreen(this);
        screens.put(appModelingScreen.getID(), appModelingScreen);
        mainScreen = appModelingScreen.getID();

        
        AppScreen splashScreen = new SplashScreen(this);
        screens.put(splashScreen.getID(), splashScreen);
        
        AppScreen appAnalysisScreen = new AnalysisScreen(this);
        screens.put(appAnalysisScreen.getID(), appAnalysisScreen);
         
        AppScreen documentationScreeen = new DocumentationScreen(this);
        screens.put(documentationScreeen.getID(), documentationScreeen);
        
        AppScreen aboutScreen = new AboutScreen(this);
        screens.put(aboutScreen.getID(), aboutScreen);
        
        AppScreen materialEditScreen = new ObjListEditScreen(app.getBlocks().getProperty(BlockProject.PROPNAME_MATERIAL_LIST).castoToPropertyObjectList(),this);
        screens.put(materialEditScreen.getID(), materialEditScreen);
        
        AppScreen loadCaseEditScreen = new ObjListEditScreen(app.getBlocks().getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList(),this);
        screens.put(loadCaseEditScreen.getID(), loadCaseEditScreen);
        
        //AppScreen BLockProjectEditScreen = new SingleObjectEditScreen(SCREEN_SETTINGS,app.getBlocks(),this);
        //screens.put(BLockProjectEditScreen.getID(), BLockProjectEditScreen);
        
        
        AppScreen SettingsScreen = new VariousObjectEditScreen(app.getBlocks().getProjectSettings(),SCREEN_SETTINGS,this);
        screens.put(SettingsScreen.getID(), SettingsScreen);
        
        loadScreen(id);
        
    }
       
    public void loadScreen(String screenID){
        AppScreen cScreen = screens.get(screenID);
        /*
        if(screenID.equals(currentScreen)){
            System.out.println("detected: changing GUI screen for the same screen");
            return;
        }*/
        if(cScreen==null){
            System.out.println("detected: attempt to load a null screen: "+screenID);
            return;
        }

        cScreen.loadScreen();
        cScreen.construct();
        
        currentScreen = cScreen.getID();
        root.getChildren().clear();
        
        root.getChildren().addAll(cScreen.getMainPane());
        AnchorPane.setBottomAnchor(cScreen.getMainPane(), 0.0);
        AnchorPane.setTopAnchor(cScreen.getMainPane(), 0.0);
        AnchorPane.setRightAnchor(cScreen.getMainPane(), 0.0);
        AnchorPane.setLeftAnchor(cScreen.getMainPane(), 0.0);

        //ignoreTouch=false;
    }
    
    public boolean isSidePanelOpened(){
        if(root.getChildren().contains(sidePane)){
            return true;
        }else{
            return false;
        }
    }
    


    
}
