/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.blockProject.Block;
import ezfemapp.blockProject.BlockMaterial;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.LoadCaseBlock;
import ezfemapp.fem.model.PlaneStress.FEMblockModel;
import ezfemapp.fem.model.PlaneStress.StressFieldComputation;
import static ezfemapp.gui.ezfem.ModelingScreen.DRAW_MODE_SINGLE;
import static ezfemapp.gui.ezfem.ModelingScreen.MODE_BLOCKS;
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
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    public static final String MODE_RX= StressFieldComputation.DISPLAYMODE_RX;
    public static final String MODE_RY= StressFieldComputation.DISPLAYMODE_RY;
    
    String playBtnSize = "20px";
    
    List<String> loadCases = new ArrayList<>();
    List<String> materialColorScale = new ArrayList<>();
    
    public static String ID = "Analysis Results";
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
    
    Node materialsColorScaleBtn;
    Node materialsColorScalePane;
    ToggleManager stateToggle;
    
    public AnalysisScreen(GUImanager gui){
        super(ID,gui); 

        
        //BUTTON GO BACK
        PulseIconButtonCustom btnBack = new PulseIconButtonCustom("btnGoBack");
        btnBack.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnBack.setIconFontawesome(FontAwesomeIcon.CHEVRON_LEFT, GUImanager.topBarBurgerIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
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
        
         //BUTTON FX
        PulseIconButtonCustom btnFX = new PulseIconButtonCustom(MODE_RX);
        btnFX.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnFX.setIconCustom(ShapeDrawer.MathSubscriptIcon("F","x"));
        btnFX.setEventHandler((event)->{  
            if(mode.equals(MODE_RX)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_RX; 
            }
            updateMode();
        });
        btnFX.construct();
        
        //BUTTON FY
        PulseIconButtonCustom btnFY = new PulseIconButtonCustom(MODE_RY);
        btnFY.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnFY.setIconCustom(ShapeDrawer.MathSubscriptIcon("F","y"));
        btnFY.setEventHandler((event)->{  
            if(mode.equals(MODE_RY)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_RY; 
            }
            updateMode();
        });
        btnFY.construct();
        
        //BUTTON SETTINGS
        PulseIconButtonCustom btnCOG = new PulseIconButtonCustom(MODE_BLOCKS);
        btnCOG .setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        //btnGrid.setIconCustom(ShapeDrawer.createGridIcon(18,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnCOG .setIconFontawesome(FontAwesomeIcon.COG, (GUImanager.toolBoxIconSize-5)+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnCOG .setEventHandler((event)->{
            gui.loadScreen(GUImanager.SCREEN_SETTINGS);
        });
        btnCOG .construct();
        
        
        getAppBar().setRightBox(btnSx,btnSy,btnSXY,btnSVM,btnDX,btnDY,btnFX,btnFY);
        getAppBar().construct();
        
        stateToggle = new ToggleManager();
        stateToggle.addAll(btnSx,btnSy,btnSXY,btnSVM,btnDX,btnDY,btnFX,btnFY);
        
        HBox box = new HBox(5);
        
        sliderScale = new MySlider("Scale");
        sliderScale.getSlider().setValue(scalePercentage);

        
        sliderScale.getSlider().setOnMouseReleased(event -> {
            scalePercentage = sliderScale.getSlider().getValue();
            renderBlocks();
        });
        
        currentAnimation = new ChangeScaleAnimation(this);
        
        btnPlayAnimation = new PulseIconButtonCustom("btnPlayAnimation");
        btnPlayAnimation.setBackGroundCircle(50, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), true);
        btnPlayAnimation.setIconFontawesome(FontAwesomeIcon.PLAY, playBtnSize,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlayAnimation.setEventHandler((event)->{   
            System.out.println("TOGGLE ANIMATION");
            mode = MODE_NONE; 
            stateToggle.unselectAll();
            toggleAnimation(); 
        });
        btnPlayAnimation.construct();
        
        
        
        //ICON ZOOM IN
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BTN_CIRCLE_HOLLOW)); 
        PulseIconButtonCustom btnZoomExtend = new PulseIconButtonCustom("btnZoom");
        btnZoomExtend.setIconFontawesome(FontAwesomeIcon.EXPAND, GUImanager.toolBoxIconSize-12+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BTN_CIRCLE_HOLLOW_ICON)); 
        btnZoomExtend.setBackGroundCustom(c);
        btnZoomExtend.setEventHandler((event)->{        
               canvas.zoomExtends();
        });
        btnZoomExtend.construct();   
        getCentralPane().getChildren().add(btnZoomExtend);
        AnchorPane.setLeftAnchor(btnZoomExtend, 5.0);
        AnchorPane.setBottomAnchor(btnZoomExtend, 5.0);
        
        createLoadCaseList();
        createMaterialList();
        
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
        
        updateData();
    }
    
    StressFieldComputation currentColorField;
    public StressFieldComputation getCurrentColorField(){
        return currentColorField;
    }
    
    Node currentElementResults;
    public void showElementResults(Block b, double x, double y){    
        //String css = this.getClass().getResource("/cssStyles/WhiteTheme/MyContextMenuInvisible.css").toExternalForm(); 
        //getCentralPane().getStylesheets().add(css);
        getCentralPane().getChildren().remove(currentElementResults); 
        double h = getCentralPane().getHeight();
        if(mode!=MODE_NONE){
            if(canvas.isMaterialInColorScale(b.getMaterial().getID())){
                SelectedElementResultPane pane = new SelectedElementResultPane(b,currentColorField,getFEMModel().getModel(),180,80,getGUI().getApp().getBlocks().getUnitsManager());  
                currentElementResults = pane;
                getCentralPane().getChildren().add(currentElementResults);
                AnchorPane.setTopAnchor(pane, 5.0);
                AnchorPane.setRightAnchor(pane, 5.0);
                canvas.highlightSelectedBlock(b);
                pane.visibleProperty().addListener((event)->{
                    canvas.hideSelectedElementHighlight();
                });
            }
           
        }
    }
    
    public void hideElementResultsPanel(){
        getCentralPane().getChildren().remove(currentElementResults);  
    }
            
    
    
    public  List<String> getMaterialsInColorScale(){
        return materialColorScale;
    }
    
    public void createLoadCaseList(){
        loadCases.clear();
        getCentralPane().getChildren().remove(loadCaseBtn);
        getCentralPane().getChildren().remove(loadCasePane);
        VBox loadCaseBox = new VBox(10);
        loadCaseBox.setVisible(false);
        
        //BUTTON BLOCKS
        /*
        PulseIconButtonCustom btnLoadCase = new PulseIconButtonCustom("LoadCase");
        btnLoadCase.setBackGroundRectangle(110, 20, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), false);
        //btnBlocks.setIconFontawesome(FontAwesomeIcon.TH_LARGE, "24px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnLoadCase.setIconCustom(utilsGUI.create("Active Load Cases", GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnLoadCase.setEventHandler((event)->{
           loadCaseBox.setVisible(!loadCaseBox.isVisible());
        });
        btnLoadCase.construct();
        AnchorPane.setRightAnchor(btnLoadCase, 5.0);
        AnchorPane.setTopAnchor(btnLoadCase, 5.0);
         */
        
        //ContextMenu loadCasesMenu = new ContextMenu(); 

        /*
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        PulseIconButtonCustom btnLCase = new PulseIconButtonCustom("btnDrawModeSingle");
            btnLCase.setIconCustom(utilsGUI.create("LC", GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnLCase.setBackGroundCustom(c);
            btnLCase.setEventHandler((event)->{
                loadCaseBox.setVisible(!loadCaseBox.isVisible());
                materialsColorScalePane.setVisible(false);
                        
                
            });
            btnLCase.construct();
         */   
            
        PulseIconButtonCustom btnLoadCase = new PulseIconButtonCustom("LoadCase");
        btnLoadCase.setBackGroundRectangle(70, 20, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), false);
        //btnBlocks.setIconFontawesome(FontAwesomeIcon.TH_LARGE, "24px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnLoadCase.setIconCustom(utilsGUI.create("Load Cases", GUImanager.defaultFont, 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnLoadCase.setEventHandler((event)->{ 
            loadCaseBox.setVisible(!loadCaseBox.isVisible());
            //materialsColorScalePane.setVisible(false);
        });
        btnLoadCase.construct();
            
            
            
            
            
            
        AnchorPane.setRightAnchor(btnLoadCase, 5.0);
        AnchorPane.setTopAnchor(btnLoadCase, 5.0);
        /*
        PulseIconButtonCustom btnModeSingle = new PulseIconButtonCustom("btnDrawModeSingle");
            btnModeSingle.setIconCustom(ShapeDrawer.createBlockIcon(10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnModeSingle.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
            btnModeSingle.setEventHandler((event)->{
                drawMode = DRAW_MODE_SINGLE;
                canvas.removeFirstTouch();
                btnSelectedChunkSize.setVisible(true);
                btnDrawMode.setIconCustom(ShapeDrawer.createBlockIcon(10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
                btnDrawMode.construct();
                cancelFirstTouch();
            });
            btnModeSingle.construct();
            CustomMenuItem item1 = new CustomMenuItem(btnModeSingle);
            drawModeMenu.getItems().add(item1); 
        */
        
        PropertyObjectList listCases = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList();
        loadCases.add("$SW");
        
        //loadCaseBox.getChildren().add(new Label("Active Load Cases"));
        
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
            updateMode();
        });

        
        for(SerializableObject obj:listCases.getObjectList()){
            LoadCaseBlock blockCase = (LoadCaseBlock)obj;
            hbox = new HBox();
            //Text txt=  utilsGUI.create(blockCase.getID(), GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
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
                updateMode();
            }); 
            hbox.getChildren().add(b);
            loadCaseBox.getChildren().add(hbox);
        }
        AnchorPane.setRightAnchor(loadCaseBox, 5.0);
        AnchorPane.setTopAnchor(loadCaseBox, 30.0+5+5);
  
        loadCaseBtn=btnLoadCase;
        loadCasePane = loadCaseBox;
        getCentralPane().getChildren().add(loadCaseBtn);
        getCentralPane().getChildren().add(loadCasePane);

    }
    
    public void createMaterialList(){
        materialColorScale.clear();
        getCentralPane().getChildren().remove(materialsColorScaleBtn);
        getCentralPane().getChildren().remove(materialsColorScalePane);
        
        VBox materialBox = new VBox(10);
        materialBox.setVisible(false);
        double offset = 90;
      
        //materialBox.getChildren().add(new Label("Included Materials"));
/*
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        PulseIconButtonCustom btnMaterials = new PulseIconButtonCustom("btnMaterials");
            btnMaterials.setIconCustom(utilsGUI.create("M", GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnMaterials.setBackGroundCustom(c);
            btnMaterials.setEventHandler((event)->{
                materialBox.setVisible(!materialBox.isVisible());
                loadCasePane.setVisible(false);
            });
            btnMaterials.construct();
         */   
        
        PulseIconButtonCustom btnMaterials = new PulseIconButtonCustom("Materials");
        btnMaterials.setBackGroundRectangle(70, 20, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), false);
        //btnBlocks.setIconFontawesome(FontAwesomeIcon.TH_LARGE, "24px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnMaterials.setIconCustom(utilsGUI.create("Materials", GUImanager.defaultFont, 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnMaterials.setEventHandler((event)->{ 
            materialBox.setVisible(!materialBox.isVisible());
            //loadCasePane.setVisible(false);
        });
        btnMaterials.construct();  
        AnchorPane.setLeftAnchor(btnMaterials, 70.0+20);
        AnchorPane.setTopAnchor(btnMaterials, 5.0);
        
        PropertyObjectList listCases = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_MATERIAL_LIST).castoToPropertyObjectList();
        
   
        HBox hbox = new HBox();    
        for(SerializableObject obj:listCases.getObjectList()){
            BlockMaterial material = (BlockMaterial)obj;
            hbox = new HBox();
            //Text txt=  utilsGUI.create(blockCase.getID(), GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
            MyColoredCheckBox b = new MyColoredCheckBox(material.getID(),material.getColor().getColorFX());
            b.setSelected(true);
            materialColorScale.add(material.getID());
            b.setFocusTraversable(false);
            b.setOnAction((event)->{
                if(b.isSelected()){
                    if(!materialColorScale.contains(b.getText())){
                        materialColorScale.add(b.getText());
                    } 
                }else{
                    materialColorScale.remove(b.getText());
                }  
                updateMode();
            });
            hbox.getChildren().add(b);
            materialBox.getChildren().add(hbox);
        }
        AnchorPane.setLeftAnchor(materialBox, 70.0+25+5);
        AnchorPane.setTopAnchor(materialBox, 40.0);
        
        materialsColorScaleBtn=btnMaterials;
        materialsColorScalePane = materialBox;
        

    }
    
    public void showMaterialList(){
        getCentralPane().getChildren().add(materialsColorScaleBtn);
        getCentralPane().getChildren().add(materialsColorScalePane);
    }
    public void hideMaterialList(){
        getCentralPane().getChildren().remove(materialsColorScaleBtn);
        getCentralPane().getChildren().remove(materialsColorScalePane);
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
        mode = MODE_NONE;
        stateToggle.unselectAll();
        canvas.hideSelectedElementHighlight();
        canvas.zoomExtends();
        hideElementResultsPanel();
        hideMaterialList();
        updateData();
        createLoadCaseList();
        createMaterialList();
        stopAnimation();
        createAndSolveModel();
        renderBlocks();   
        
    }
    
    private void updateData(){
        loadCases.clear();
        materialColorScale.clear();
        
        for(BlockMaterial mat:getGUI().getApp().getBlocks().getMaterials()){
            materialColorScale.add(mat.getID());
        }
        for(LoadCaseBlock lc:getGUI().getApp().getBlocks().getLoadCases()){
            loadCases.add(lc.getID());
        }
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
        if(mode!=MODE_NONE){
            System.out.println("color field computed");
            currentColorField = getFEMModel().computeColorField(getLoadCases(), getMaterialsInColorScale(), getSelectedDisplayMode());   
        }
        renderBlocks();
    }
    
    @Override
    public void update(String... args){
        createLoadCaseList();
    }
    
    MyAnimation currentAnimation;
    public void toggleAnimation(){
        hideElementResultsPanel();
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
        btnPlayAnimation.adjustIconPosition(3, 0);
        btnPlayAnimation.construct();
        currentAnimation.setRuning(false);
        currentAnimation.stop();
        System.gc();   
    }
}
