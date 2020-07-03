/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.LoadCaseBlock;
import ezfemapp.fem.model.PlaneStress.FEMblockModel;
import ezfemapp.fem.model.PlaneStress.StressFieldComputation;
import ezfemapp.gui.mdcomponents.MyColoredCheckBox;

import ezfemapp.gui.mdcomponents.MySlider;

import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.ToggleManager;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.rendering.canvas.AnalysisRenderer;
import ezfemapp.rendering.canvas.ChangeScaleAnimation;
import ezfemapp.rendering.canvas.MyAnimation;
import ezfemapp.rendering.shapes.ShapeDrawer;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class AnalysisScreen extends AppScreen{
    
    public static final String MODE_NONE = StressFieldComputation.DISPLAYMODE_NONE;
    public static final String MODE_SX = StressFieldComputation.DISPLAYMODE_STRESS_SXX;
    public static final String MODE_SY = StressFieldComputation.DISPLAYMODE_STRESS_SYY;
    public static final String MODE_SXY= StressFieldComputation.DISPLAYMODE_STRESS_TXY;
    public static final String MODE_SVM= StressFieldComputation.DISPLAYMODE_STRESS_VM;
    public static final String MODE_DX= StressFieldComputation.DISPLAYMODE_DISPX;
    public static final String MODE_DY= StressFieldComputation.DISPLAYMODE_DISPY;
    
    String playBtnSize = "20px";
    
    List<String> loadCases = new ArrayList<>();
    public static String ID = "Analysis";
    AnalysisRenderer canvas;
    FEMblockModel FEMmodel;
    String mode=MODE_NONE;
    
    PulseIconButtonCustom btnPlayAnimation;
    double scalePercentage=0.50;
    double scaleMin=0;
    double scaleMax=100;
    MySlider sliderScale;
    
    Node loadCaseBtn;
    Node loadCasePane;
    
    public AnalysisScreen(GUImanager gui){
        super(ID,gui); 
        loadCases.add("$SW");
        loadCases.add("LoadCase1");
        loadCases.add("LoadCase2");
        //BUTTON GO BACK
        PulseIconButtonCustom btnBack = new PulseIconButtonCustom("btnGoBack");
        btnBack.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnBack.setIconFontawesome(FontAwesomeIcon.CHEVRON_LEFT, GUImanager.appBarIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnBack.setEventHandler((event)->{
            gui.loadScreen(ModelingScreen.ID);
            stopAnimation();
        });
        btnBack.construct();
        getAppBar().setLeftBox(btnBack);
        getAppBar().construct();
        
        
        //BUTTON SX
        PulseIconButtonCustom btnSx = new PulseIconButtonCustom(MODE_SX);
        btnSx.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnSx.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03c3","x"));
        btnSx.setEventHandler((event)->{  
            if(mode.equals(MODE_SX)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_SX; 
            }
            updateMode();
        });
        btnSx.construct();
        
        //BUTTON SX
        PulseIconButtonCustom btnSy = new PulseIconButtonCustom(MODE_SY);
        btnSy.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnSy.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03c3","y"));
        btnSy.setEventHandler((event)->{  
            if(mode.equals(MODE_SY)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_SY; 
            }
            updateMode();
        });
        btnSy.construct();
        
        
        //BUTTON TXY
        PulseIconButtonCustom btnSXY = new PulseIconButtonCustom(MODE_SXY);
        btnSXY.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnSXY.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03c3","xy"));
        btnSXY.setEventHandler((event)->{  
            if(mode.equals(MODE_SXY)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_SXY; 
            }
            updateMode();
        });
        btnSXY.construct();
        
        //BUTTON SVM
        PulseIconButtonCustom btnSVM = new PulseIconButtonCustom(MODE_SVM);
        btnSVM.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnSVM.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03c3","vm"));
        btnSVM.setEventHandler((event)->{  
            if(mode.equals(MODE_SVM)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_SVM; 
            }
            updateMode();
        });
        btnSVM.construct();
        
        //BUTTON DX
        PulseIconButtonCustom btnDX = new PulseIconButtonCustom(MODE_DX);
        btnDX.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnDX.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03b4","x"));
        btnDX.setEventHandler((event)->{  
            if(mode.equals(MODE_DX)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_DX; 
            }
            updateMode();
        });
        btnDX.construct();
        
        //BUTTON DY
        PulseIconButtonCustom btnDY = new PulseIconButtonCustom(MODE_DY);
        btnDY.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnDY.setIconCustom(ShapeDrawer.MathSubscriptIcon("\u03b4","y"));
        btnDY.setEventHandler((event)->{  
            if(mode.equals(MODE_DY)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_DY; 
            }
            updateMode();
        });
        btnDY.construct();
        
        getAppBar().setRightBox(btnSx,btnSy,btnSXY,btnSVM,btnDX,btnDY);
        getAppBar().construct();
        
        ToggleManager stateToggle = new ToggleManager();
        stateToggle.addAll(btnSx,btnSy,btnSXY,btnSVM,btnDX,btnDY);
        
        HBox box = new HBox(5);
        
        sliderScale = new MySlider("Scale");
        sliderScale.getSlider().setValue(scalePercentage);

        
        sliderScale.getSlider().setOnMouseReleased(event -> {
            scalePercentage = sliderScale.getSlider().getValue();
            renderBlocks();
        });
        
        currentAnimation = new ChangeScaleAnimation(this);
        
        btnPlayAnimation = new PulseIconButtonCustom("btnPlayAnimation");
        //btnPlayAnimation.setBackGroundCircle(GUImanager.toolBoxIconSize, Color.TRANSPARENT, true);
        btnPlayAnimation.setBackGroundCircle(40, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), true);
       // btnPlayAnimation.setIconFontawesome(FontAwesomeIcon.PLAY_CIRCLE, (GUImanager.toolBoxIconSize+5)+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
        btnPlayAnimation.setIconFontawesome(FontAwesomeIcon.PLAY, playBtnSize,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlayAnimation.setEventHandler((event)->{   
            System.out.println("TOGGLE ANIMATION");
            
            mode = MODE_NONE; 
            stateToggle.unselectAll();
            //updateMode();
            toggleAnimation(); 
        });
        btnPlayAnimation.construct();
        
        createLoadCaseList();
        
        sliderScale.setTranslateY(15);
        box.getChildren().addAll(sliderScale,btnPlayAnimation);
        getCentralPane().getChildren().add(box);
        box.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setRightAnchor(box, 5.0);
        AnchorPane.setBottomAnchor(box, 5.0);
        
        canvas = new AnalysisRenderer(this);   
        getCentralPane().getChildren().add(canvas);
        canvas.toBack();
        canvas.addGeometry(new Rectangle(200,200,Color.BLUEVIOLET));
        canvas.clearAndRender();
        
    }
    
    
    public void createLoadCaseList(){
        loadCases.clear();
        getCentralPane().getChildren().remove(loadCaseBtn);
        getCentralPane().getChildren().remove(loadCasePane);
        VBox loadCaseBox = new VBox(5);
        loadCaseBox.setVisible(false);
        //BUTTON BLOCKS
        PulseIconButtonCustom btnLoadCase = new PulseIconButtonCustom("LoadCase");
        btnLoadCase.setBackGroundRectangle(110, 20, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), false);
        //btnBlocks.setIconFontawesome(FontAwesomeIcon.TH_LARGE, "24px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnLoadCase.setIconCustom(utilsGUI.create("Active Load Cases", "Arial", 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnLoadCase.setEventHandler((event)->{
           loadCaseBox.setVisible(!loadCaseBox.isVisible());
        });
        btnLoadCase.construct();
        AnchorPane.setRightAnchor(btnLoadCase, 5.0);
        AnchorPane.setTopAnchor(btnLoadCase, 5.0);
         
        PropertyObjectList listCases = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList();
        loadCases.add("$SW");
        //CheckBox cbSelfWeight = new CheckBox("Self weight");
        
        MyColoredCheckBox cbSelfWeight = new MyColoredCheckBox("Self Weight",Color.BLACK);
        cbSelfWeight.setFocusTraversable(false);
        cbSelfWeight.setSelected(true);
        HBox hbox = new HBox();
        hbox.getChildren().add(cbSelfWeight);
        loadCaseBox.getChildren().add(hbox);
        cbSelfWeight.setOnAction((event)->{
            if(cbSelfWeight.isSelected()){
                if(!loadCases.contains("$SW")){
                    loadCases.add("$SW");
                } 
            }else{
                loadCases.remove("$SW");
            } 
            renderBlocks();
        });
            
        for(SerializableObject obj:listCases.getObjectList()){
            LoadCaseBlock blockCase = (LoadCaseBlock)obj;
            hbox = new HBox();
            //Text txt=  utilsGUI.create(blockCase.getID(), "Arial", 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
            MyColoredCheckBox b = new MyColoredCheckBox(blockCase.getID(),blockCase.getColor().getColorFX());
            b.setSelected(true);
            loadCases.add(blockCase.getID());
            b.setFocusTraversable(false);
            b.setOnAction((event)->{
                if(b.isSelected()){
                    if(!loadCases.contains(b.getText())){
                        loadCases.add(b.getText());
                    } 
                }else{
                    loadCases.remove(b.getText());
                }  
                renderBlocks();
            });
            hbox.getChildren().add(b);
            loadCaseBox.getChildren().add(hbox);
        }
        AnchorPane.setRightAnchor(loadCaseBox, 25.0);
        AnchorPane.setTopAnchor(loadCaseBox, 25+5+5.0);
        
        loadCaseBtn=btnLoadCase;
        loadCasePane = loadCaseBox;
        getCentralPane().getChildren().add(loadCaseBtn);
        getCentralPane().getChildren().add(loadCasePane);
    }
    
    public void updateScale(double value){
        sliderScale.getSlider().setValue(value);
        scalePercentage = sliderScale.getSlider().getValue();
        renderBlocks();
    }
    
    public List<String> getLoadCases(){
        return loadCases;
    }
    
    public double getScaleMin(){
        return scaleMin;
    }
    
    public double getScaleMax(){
        return scaleMax;
    }
    
    public double getScalePercetage(){
        return scalePercentage;
    }
    
    public String getSelectedDisplayMode(){
        return mode;
    }
    
    public FEMblockModel getFEMModel(){
        return FEMmodel;
    }
    
    @Override
    public void loadScreen(){
        createLoadCaseList();
        stopAnimation();
        createAndSolveModel();
        renderBlocks();   
    }
    
    private void createAndSolveModel(){
        FEMmodel = new FEMblockModel(getGUI().getApp().getBlocks());
        FEMmodel.createModel();
        FEMmodel.solveModel();
    }
    
    private void renderBlocks(){
        canvas.renderAllBlocks();
    }
    
    private void updateMode(){
        if(currentAnimation.isRuning()){
            stopAnimation();
        }
        renderBlocks();
    }
    
    @Override
    public void update(String... args){
        createLoadCaseList();
    }
    
    MyAnimation currentAnimation;
    public void toggleAnimation(){
        if(currentAnimation.isRuning()){
            stopAnimation();
        }else{
            currentAnimation.setCurrentStep(scalePercentage);
            currentAnimation.start();
            btnPlayAnimation.setIconFontawesome(FontAwesomeIcon.PAUSE, "15px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
            btnPlayAnimation.construct();
        }
    }
    
    public void stopAnimation(){
        System.out.println("STOPING ANIMATION");
        btnPlayAnimation.setIconFontawesome(FontAwesomeIcon.PLAY, playBtnSize,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlayAnimation.construct();
        currentAnimation.setRuning(false);
        currentAnimation.stop();
        System.gc();   
    }
}
