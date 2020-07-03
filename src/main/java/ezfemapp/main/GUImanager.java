/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.main;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXRippler.RipplerMask;
import com.jfoenix.controls.JFXRippler.RipplerPos;

import ezfemapp.blockProject.BlockProject;
import ezfemapp.gui.ezfem.AnalysisScreen;
import ezfemapp.gui.ezfem.ObjListEditScreen;
import ezfemapp.gui.ezfem.ModelingScreen;
import ezfemapp.gui.ezfem.SidePane;
import ezfemapp.gui.ezfem.SingleObjectEditScreen;
import ezfemapp.gui.mdcomponents.MyConfirmationPanel;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.gui.theme.ThemeWhite;
import java.io.File;
import java.util.HashMap;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serializableApp.objects.Project;

/**
 *
 * @author GermanSR
 */
public class GUImanager {
    
    public static ColorTheme colorTheme;
    public static double toolBoxIconSize = 30;
    public static double topBarHeight = 42;
    public static double topBarTextSize = 16;
    public static double sidePanelWidth = 250;
    public static double appBarIconSize = 28;
    
    public static final String SCREEN_SETTINGS="Project Settings";
    
    HashMap<String,AppScreen> screens = new HashMap<>();
    AnchorPane root;
    Scene mainScene;   
    String mainScreen = ModelingScreen.ID;
    String currentScreen="";
    SidePane sidePane;
    MyApp2 app;
    
    String currentProjectPath="";
   // boolean ignoreTouch=false;

    ReadOnlyDoubleProperty width;
    ReadOnlyDoubleProperty height;
    Stage primaryStage;
    
    public GUImanager(MyApp2 app,Stage primaryStage){
        this.primaryStage = primaryStage;
        this.app = app;
        initialize();
        initializeDeskTop(primaryStage);
        
        width = mainScene.widthProperty();
        height = mainScene.heightProperty(); 
    }
    
    public void newDefaultProject(){

        MyConfirmationPanel confirmPanel = new MyConfirmationPanel();
        confirmPanel.setMessage("Delete everything to create\na new default project?");
        confirmPanel.show(this);

        confirmPanel.getPopUp().setOnHidden((event)->{
            if(confirmPanel.getResult()){
                BlockProject newProject = new BlockProject("NewProject");
                newProject.createDefaultProject();
                getApp().setProject(newProject); 
                createScreens();
                
                //ModelingScreen screen = (ModelingScreen)screens.get(ModelingScreen.ID);
                //screen.resetScreen();
                
                getSidePanel().closePanel();
            }
        });
        /*
        JFXListView<String> list = new JFXListView<>();
        for(int i = 1 ; i < 5 ; i++) {
            list.getItems().add("Item " + i);
        }
        double w = getWidth().doubleValue()/2-confirmPanel.getBtnWidth()/2;
        double h = getHeight().doubleValue()/2-confirmPanel.getBtnHeight()/2;
        JFXPopup popup = new JFXPopup();
        popup.setPopupContent(confirmPanel);
        popup.show(root, PopupVPosition.TOP, PopupHPosition.LEFT,w,h);
        */
    }
    
    
    
    public void openProjectFromLocalFile(){
        switch(app.platform){
            case MyApp2.PLATFORM_DESKTOP:
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load Project");
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                   Project proy = getApp().getBlocks().deserializeProject(file.getAbsolutePath());
                   if(proy!=null){
                       if(proy instanceof BlockProject){
                           
                            currentProjectPath = file.getAbsolutePath();
                            BlockProject blocks = (BlockProject)proy;
                            getApp().setProject(blocks);
                            createScreens();
                            /*
                            ModelingScreen screen = (ModelingScreen)screens.get(ModelingScreen.ID);
                            screen.resetScreen();*/
                            getSidePanel().closePanel();
                       }
                   }
                    //getApp().loadProject(proy);
                    //return new CommandResult(true,"Project opened: "+file.getAbsolutePath());
                }
                return;
            case MyApp2.PLATFORM_ANDROID:    
                return;
            default:
                return;
        }
    }
    
    public void saveProjectToLocalFile(){
  
        switch(app.platform){
            case MyApp2.PLATFORM_DESKTOP:
                
                File currentPath = new File(currentProjectPath);
                if(currentPath.exists()){
                    getApp().getBlocks().serializeProject(currentPath.getAbsolutePath());
                }else{
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Project");
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        getApp().getBlocks().serializeProject(file.getAbsolutePath());
                        getSidePanel().closePanel();
                    }
                }
                
                return;
            case MyApp2.PLATFORM_ANDROID:    
                return;
            default:
                return;
        }
        
    }
    
    public void saveAs(){
        switch(app.platform){
            case MyApp2.PLATFORM_DESKTOP:
                
                File currentFile = new File(currentProjectPath);
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Project");
                fileChooser.setInitialDirectory(currentFile);
                File file = fileChooser.showSaveDialog(primaryStage);
                
                if (file != null) {
                    getApp().getBlocks().serializeProject(file.getAbsolutePath());
                   
                }
                
                return;
            case MyApp2.PLATFORM_ANDROID:    
                return;
            default:
                return;
        }
        
    }
    
    public void saveProjectToCurrentPath(){
        File file = new File(currentProjectPath);
        if(file.exists()){
            getApp().getBlocks().serializeProject(file.getAbsolutePath());
        }
    }
    
    public AnchorPane getRootPane(){
        return root;
    }
    
    public SidePane getSidePanel(){
        return sidePane;
    }
    
    public GUImanager(MyApp2 app){
        this.app = app;
        initialize();
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
    
 
    
 
    
    private void initialize(){
        
        
        root = new AnchorPane();
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
      
    }
    
 
    
    
    private void initializeDeskTop(Stage primaryStage){
        JFXDecorator decorator = new JFXDecorator(primaryStage,root);
        mainScene = new Scene(decorator, 600, 400);
        primaryStage.setTitle("");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    

    
    public MyApp2 getApp(){
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

    public void createScreens(){
        
        screens = new HashMap<>();
        screens.clear();
        
        AppScreen appModelingScreen = new ModelingScreen(this);
        screens.put(appModelingScreen.getID(), appModelingScreen);
        mainScreen = appModelingScreen.getID();

        AppScreen appAnalysisScreen = new AnalysisScreen(this);
        screens.put(appAnalysisScreen.getID(), appAnalysisScreen);
              
        AppScreen materialEditScreen = new ObjListEditScreen(app.getBlocks().getProperty(BlockProject.PROPNAME_MATERIAL_LIST).castoToPropertyObjectList(),this);
        screens.put(materialEditScreen.getID(), materialEditScreen);
        
        AppScreen loadCaseEditScreen = new ObjListEditScreen(app.getBlocks().getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList(),this);
        screens.put(loadCaseEditScreen.getID(), loadCaseEditScreen);
        
        AppScreen BLockProjectEditScreen = new SingleObjectEditScreen(SCREEN_SETTINGS,app.getBlocks(),this);
        screens.put(BLockProjectEditScreen.getID(), BLockProjectEditScreen);
        
        loadScreen(mainScreen);
        
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
    
    /*
    public void showSidePane(){
        if(root.getChildren().contains(sidePane)){
            root.getChildren().remove(sidePane);
        }else{
            root.getChildren().add(sidePane);
            AnchorPane.setBottomAnchor(sidePane, 0.0);
            AnchorPane.setTopAnchor(sidePane, 0.0);
        }     
        ignoreTouch = true;
    }*/

    /*
    public void hideSidePane(){
        root.getChildren().remove(sidePane);
        ignoreTouch = false;
    }
        */    


    
}
