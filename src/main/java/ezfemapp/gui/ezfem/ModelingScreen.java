/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import ezfemapp.rendering.canvas.ModelRenderer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.blockProject.BlockDistLoad;
import ezfemapp.blockProject.BlockMaterial;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.ChunkSize;
import ezfemapp.blockProject.LoadCaseBlock;
import ezfemapp.blockProject.SupportBlock;
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.ToggleManager;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.rendering.shapes.ShapeDrawer;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
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
public class ModelingScreen extends AppScreen{
    
    public static String ID = "Modeling";
    
    public static final String MODE_NONE = "none";
    public static final String MODE_LOADS = "Loads";
    public static final String MODE_BLOCKS = "Blocks";

    public static final String DRAW_MODE_SINGLE = "Single";
    public static final String DRAW_MODE_RECTANGLE = "Rect";
    
    public static final String BLOCK_TYPE_SOLID = "SolidBlock";
    public static final String BLOCK_TYPE_SUPPORT = "Support";
    
    public String selectedBlockType = BLOCK_TYPE_SOLID;
    
    Node blockTools = new Group();
    Node blockToolsScroll= new Group();
    Node loadTools= new Group();
    Node loadToolsScroll= new Group();
    Node supportTools= new Group();
    Node supportToolsScroll= new Group();
    AnchorPane topRightTools = new AnchorPane();
 
    String mode=MODE_NONE;
    String drawMode = DRAW_MODE_SINGLE;
    boolean firstTouchConsumed=false;
    boolean firstTouchDelete=false;
    
    String selectedBlock="mat1";
    String selectedLoadCase="LoadCase1";
    String selectedLoadDirection=BlockDistLoad.LOAD_DIRECTION_DOWN;
    String selectedSupport=SupportBlock.SUPPORT_FIXED;
    String selectedChunk="1x1";
    String customChunk="1x1";
        
    ModelRenderer canvas;
   // ToggleManager stateToggle = new ToggleManager();
    ToggleManager stateToggle;
    
    public ModelingScreen(GUImanager gui){
        super(ID,gui);   
        
        canvas = new ModelRenderer(this);
        
        getCentralPane().getChildren().add(topRightTools);
        AnchorPane.setTopAnchor(topRightTools, 5.0);
        AnchorPane.setRightAnchor(topRightTools, 5.0);

        //BUTTON SHOW LEFT PANE
        PulseIconButtonCustom btnHamburger = new PulseIconButtonCustom("btnHamburger");
        btnHamburger.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnHamburger.setIconFontawesome(FontAwesomeIcon.REORDER, GUImanager.appBarIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnHamburger.setEventHandler((event)->{
           
            gui.getSidePanel().openPanel();
            
        });
        btnHamburger.construct();
        getAppBar().setLeftBox(btnHamburger);

        //BUTTON LOADS
        PulseIconButtonCustom btnLoads = new PulseIconButtonCustom(MODE_LOADS);
        btnLoads.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnLoads.setIconCustom(ShapeDrawer.drawArrowIcon(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT),18));
        btnLoads.setEventHandler((event)->{  
            if(mode.equals(MODE_LOADS)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_LOADS; 
            }
            updateMode();
        });
        btnLoads.construct();
        
        //BUTTON BLOCKS
        PulseIconButtonCustom btnBlocks = new PulseIconButtonCustom(MODE_BLOCKS);
        btnBlocks.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        //btnBlocks.setIconFontawesome(FontAwesomeIcon.TH_LARGE, "24px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnBlocks.setIconCustom(ShapeDrawer.createBlockIcon(18, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnBlocks.setEventHandler((event)->{
            if(mode.equals(MODE_BLOCKS)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_BLOCKS; 
            }
            updateMode();
        });
        btnBlocks.construct();
        
        
        //BUTTON SETTINGS
        PulseIconButtonCustom btnGrid = new PulseIconButtonCustom(MODE_BLOCKS);
        btnGrid.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        //btnGrid.setIconCustom(ShapeDrawer.createGridIcon(18,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnGrid.setIconFontawesome(FontAwesomeIcon.COG, (GUImanager.toolBoxIconSize-2)+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnGrid.setEventHandler((event)->{
            gui.loadScreen(GUImanager.SCREEN_SETTINGS);
        });
        btnGrid.construct();
        
        
        /*
        //BUTTON SUPPORTS
        PulseIconButtonCustom btnSupports = new PulseIconButtonCustom(MODE_SUPPORTS);
        btnSupports.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnSupports.setIconCustom(ShapeDrawer.drawSupportIcon(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT),10));
        btnSupports.setEventHandler((event)->{
           if(mode.equals(MODE_SUPPORTS)){
               mode = MODE_NONE; 
            }else{
               mode = MODE_SUPPORTS; 
            }
            updateMode();
        });
        btnSupports.construct();
        */
        getAppBar().setRightBox(btnBlocks,btnLoads,btnGrid);
        getAppBar().construct();
        
         stateToggle = new ToggleManager();
        stateToggle.addAll(btnLoads,btnBlocks);
        
        //BUTTON PLAY
        PulseIconButtonCustom btnPlay = new PulseIconButtonCustom("btnAnalysis");
        //btnPlay.setBackGroundCircle(40, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), true);
        //btnPlay.setIconFontawesome(FontAwesomeIcon.PLAY, "20px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlay.setBackGroundCircle(50, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), true);
 
       // btnPlay.setIconFontawesome(FontAwesomeIcon.PLAY, "20px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlay.setIconCustom(utilsGUI.create("Run", "Arial", 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        //btnPlay.getIcon().setTranslateX(3);
        btnPlay.setEventHandler((event)->{
            gui.loadScreen(AnalysisScreen.ID);
        });
        btnPlay.construct();
        getCentralPane().getChildren().add(btnPlay);
        AnchorPane.setBottomAnchor(btnPlay, 5.0);
        AnchorPane.setRightAnchor(btnPlay, 5.0);   
        
        
        
  
       
        createDrawModeButtons();
        createBlockEditTools();
        createLoadEditTools();  

        getCentralPane().getChildren().add(canvas);
        canvas.toBack();
        canvas.addGeometry(new Rectangle(200,200,Color.BLUEVIOLET));
        canvas.clearAndRender();
    }
    
    PulseIconButtonCustom cancelFirstTouchBtn;
    
    public void createDrawModeButtons(){
        
        PropertyObjectList chunkList = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_CHUNKSIZE_LIST).castoToPropertyObjectList();
        ChunkSize selectedChunkSize = (ChunkSize)chunkList.getObjectByID(selectedChunk);    
        ContextMenu contextMenuBlockChunkSize = new ContextMenu();  
        
        if(selectedChunkSize==null){
            selectedChunk="1x1";
        }
        
        PropertyObjectList listChunks = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_CHUNKSIZE_LIST).castoToPropertyObjectList();
        PulseIconButtonCustom btnSelectedChunkSize = new PulseIconButtonCustom("btnChunkSizeList");
        btnSelectedChunkSize.createTextElement(selectedChunkSize.getID(), "Arial", 10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
        btnSelectedChunkSize.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnSelectedChunkSize.setIconCustom(c);
        btnSelectedChunkSize.setEventHandler((event)->{
               Bounds b = btnSelectedChunkSize.localToScreen(btnSelectedChunkSize.getBoundsInLocal());
               contextMenuBlockChunkSize.show(btnSelectedChunkSize,(int)b.getMinX()-5,(int)b.getMaxY());      
        });
        btnSelectedChunkSize.construct();    
            for(SerializableObject obj:listChunks.getObjectList()){
                PulseIconButtonCustom btn = new PulseIconButtonCustom("btnChunk"+obj.getID());
                btn.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
                btn.setIconCustom(new Rectangle(20,20,Color.TRANSPARENT));
                btn.createTextElement(obj.getID(), "Arial", 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
                btn.setEventHandler((event)->{
                    btnSelectedChunkSize.setText(obj.getID());
                    selectedChunk = obj.getID();
                });
                btn.construct();
                CustomMenuItem item = new CustomMenuItem(btn);
                contextMenuBlockChunkSize.getItems().add(item);
            }
        
            
            
        c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        cancelFirstTouchBtn = new PulseIconButtonCustom("cancelFirstToch");
        cancelFirstTouchBtn.setIconFontawesome(FontAwesomeIcon.REMOVE, GUImanager.toolBoxIconSize-8+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        cancelFirstTouchBtn.setBackGroundCustom(c);
        cancelFirstTouchBtn.setEventHandler((event)->{           
               cancelFirstTouch();
        });
        cancelFirstTouchBtn.construct();    
            
        //DRAW MODE
        c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        ContextMenu drawModeMenu = new ContextMenu(); 
        
        PulseIconButtonCustom btnDrawMode = new PulseIconButtonCustom("btnDrawMode");
        btnDrawMode.setIconCustom(ShapeDrawer.createBlockIcon(10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnDrawMode.setBackGroundCustom(c);
        btnDrawMode.setEventHandler((event)->{        
               Bounds b = btnDrawMode.localToScreen(btnDrawMode.getBoundsInLocal());
               drawModeMenu.show(btnDrawMode,(int)b.getMinX()-5,(int)b.getMaxY()+2);    
               
        });
        btnDrawMode.construct();
        
        
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
            
            PulseIconButtonCustom btnModeRectangle = new PulseIconButtonCustom("btnDrawModeRactangle");
            btnModeRectangle.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);    
            btnModeRectangle.setIconCustom(ShapeDrawer.createDrawModeDoubleIcon(5, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnModeRectangle.setEventHandler((event)->{
                drawMode = DRAW_MODE_RECTANGLE;
                btnSelectedChunkSize.setVisible(false);
                btnDrawMode.setIconCustom(ShapeDrawer.createDrawModeDoubleIcon(5, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
                btnDrawMode.construct();
            });
            btnModeRectangle.construct();
            CustomMenuItem item2 = new CustomMenuItem(btnModeRectangle);
            drawModeMenu.getItems().add(item2);  
        
        
        
        HBox box = new HBox(5);
        box.getChildren().addAll(btnDrawMode, btnSelectedChunkSize);
        getCentralPane().getChildren().addAll(box);
        AnchorPane.setTopAnchor(box, 5.0);
        AnchorPane.setLeftAnchor(box, 5.0);  
    }
    
    public void showCancelButton(boolean v){
        if(v){
            getCentralPane().getChildren().addAll(cancelFirstTouchBtn);
            AnchorPane.setTopAnchor(cancelFirstTouchBtn, 5.0);
            double w = getGUI().getWidth().getValue()/2 - GUImanager.toolBoxIconSize/2;
            AnchorPane.setLeftAnchor(cancelFirstTouchBtn,  w);  
        }else{
            getCentralPane().getChildren().remove(cancelFirstTouchBtn);
        }
    }
    
    public boolean isFirstTouchConsumed(){
        return firstTouchConsumed;
    }
    public boolean isFistTouchForDelete(){
        return firstTouchDelete;
    }
    
    int firstTouchRow, firstTouchCol;
    public void setFirstTouch(int r, int c, boolean delete){
        if(mode.equals(MODE_NONE)){
            return;
        }
        firstTouchDelete = delete;
        int nRows = getGUI().getApp().getBlocks().getNumRows();
        int nCols = getGUI().getApp().getBlocks().getNumCols();
        if(r>=nRows||r<0){
            return;
        }
        if(c>=nCols||c<0){
            return;
        }
        firstTouchRow=r;
        firstTouchCol=c;
        firstTouchConsumed = true;
        canvas.renderFirstTouch(firstTouchDelete);
        showCancelButton(true);
    }
    
    public int[] getFirstTouch(){
        return new int[]{firstTouchRow,firstTouchCol};
    }
    
    public void cancelFirstTouch(){
        showCancelButton(false);
        firstTouchConsumed=false;
        firstTouchRow = -1;
        firstTouchCol = -1;
        //selectedChunk = "1x1";
        canvas.removeFirstTouch();
    }
    
    public boolean setSecondTouch(int r, int c){
        int nRows = getGUI().getApp().getBlocks().getNumRows();
        int nCols = getGUI().getApp().getBlocks().getNumCols();
        if(r>=nRows||r<0){
            cancelFirstTouch();
            return false;
        }
        if(c>=nCols||c<0){
            cancelFirstTouch();
            return false;
        }
        /*
        if(r<firstTouchRow){
            cancelFirstTouch();
            return false;
        }
        if(c<firstTouchCol){
            cancelFirstTouch();
            return false;
        }*/
        int higherRow;
        int higherCol;
        int lowerRow;
        int lowerCol;
        higherRow = Math.max(firstTouchRow, r);
        higherCol = Math.max(firstTouchCol, c);
        lowerRow = Math.min(firstTouchRow, r);
        lowerCol = Math.min(firstTouchCol, c);
        firstTouchRow = lowerRow;
        firstTouchCol = lowerCol;
        customChunk = ""+(1+(higherRow-lowerRow))+"x"+(1+(higherCol-lowerCol));
        showCancelButton(false);
        return true;
    }
    
    public String getDrawMode(){
        return drawMode;
    }
    public String getMode(){
        return mode;
    }
    public String getSelectedMaterial(){
        return selectedBlock;
    }
    public String getSelectedLoadCase(){
        return selectedLoadCase;
    }
    public String getSelectedSupport(){
        return selectedSupport;
    }
    public String getSelectedChunkSize(){
        if(drawMode.equals(DRAW_MODE_RECTANGLE)){
            return customChunk;
        }else{
            return selectedChunk;
        }
        
    }
    public String getSelectedLoadDirection(){
        return selectedLoadDirection;
    }
    
    PulseIconButtonCustom btnSelectedMaterial;
    private void createBlockEditTools(){ 
        
        Group allBtns = new Group();
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/MyContextMenu.css").toExternalForm(); 
        getCentralPane().getStylesheets().add(css);
        
        PropertyObjectList materialList = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_MATERIAL_LIST).castoToPropertyObjectList();
        BlockMaterial selectedMaterial = (BlockMaterial)materialList.getObjectByID(selectedBlock);
 
        PropertyObjectList chunkList = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_CHUNKSIZE_LIST).castoToPropertyObjectList();
        ChunkSize selectedChunkSize = (ChunkSize)chunkList.getObjectByID(selectedChunk);

        HBox boxBlockTools = new HBox(3);
        
        
        PulseIconButtonCustom btnConfigBlock = new PulseIconButtonCustom("btnConfigMaterials");
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnConfigBlock.setBackGroundCustom(c);
        btnConfigBlock.setIconFontawesome(FontAwesomeIcon.EDIT, GUImanager.toolBoxIconSize-9+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnConfigBlock.setEventHandler((event)->{
            getGUI().loadScreen(BlockProject.PROPNAME_MATERIAL_LIST);
        });
        btnConfigBlock.construct();
        
        PulseIconButtonCustom deleteCurrent = new PulseIconButtonCustom("btnClearBlocks"); 
        
        ContextMenu deleteOptions = new ContextMenu();
        PulseIconButtonCustom btnClearBlocks = new PulseIconButtonCustom("btnClearBlocks"); 
        c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnClearBlocks.setBackGroundCustom(c);
        btnClearBlocks.setIconFontawesome(FontAwesomeIcon.TRASH, GUImanager.toolBoxIconSize-8+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnClearBlocks.setEventHandler((event)->{
            String txt = "";
            if(selectedBlockType.equals(BLOCK_TYPE_SOLID)){
                txt = selectedBlock;
            }else if(selectedBlockType.equals(BLOCK_TYPE_SUPPORT)){
                txt = selectedSupport;
            }
            deleteCurrent.setIconCustom(utilsGUI.create(txt, "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteCurrent.construct();
            Bounds b = btnClearBlocks.localToScreen(btnClearBlocks.getBoundsInLocal());
            deleteOptions.show(btnClearBlocks,(int)b.getMinX()-GUImanager.toolBoxIconSize,(int)b.getMaxY());
        });
        btnClearBlocks.construct();
        
        
            CustomMenuItem it = new CustomMenuItem(utilsGUI.create("Delete Blocks...", "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT)));
            deleteOptions.getItems().add(it); 
            PulseIconButtonCustom deleteAll = new PulseIconButtonCustom("btnClearBlocks"); 
            deleteAll.setBackGroundCustom(new Rectangle(80,20,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            deleteAll.setIconCustom(utilsGUI.create("All", "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteAll.setEventHandler((event)->{
                getGUI().getApp().getBlocks().clearAll();
                canvas.updateBlockGeometries();
                //canvas.renderAllBlocks();
            });
            deleteAll.construct();
             it = new CustomMenuItem(deleteAll);
            deleteOptions.getItems().add(it); 
            
            
            deleteCurrent.setBackGroundCustom(new Rectangle(80,20,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            deleteCurrent.setIconCustom(utilsGUI.create(selectedBlock, "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteCurrent.setEventHandler((event)->{
                if(selectedBlockType.equals(BLOCK_TYPE_SOLID)){
                    getGUI().getApp().getBlocks().clearMaterial(selectedBlock);
                    canvas.updateBlockGeometries();
                   // canvas.renderAllBlocks();
                }else{
                    getGUI().getApp().getBlocks().clearSupport(selectedSupport);
                    canvas.updateBlockGeometries();
                   // canvas.renderAllBlocks();
                }
                
            });
            deleteCurrent.construct();
            it = new CustomMenuItem(deleteCurrent);
            deleteOptions.getItems().add(it); 
            
        ContextMenu contextMenuMaterial = new ContextMenu();
        Circle c2 = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c2.setStrokeWidth(1);
        c2.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnSelectedMaterial = new PulseIconButtonCustom("btnMaterialList");
        if(selectedBlockType.equals(BLOCK_TYPE_SOLID)){
            if(selectedMaterial==null){
                btnSelectedMaterial.setIconCustom(ShapeDrawer.createNullBlockIcon(GUImanager.toolBoxIconSize-15, Color.LIGHTGREY,
                        GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            }else{
                btnSelectedMaterial.setIconCustom(ShapeDrawer.createBlockIcon(GUImanager.toolBoxIconSize-15, selectedMaterial.getColor().getColorFX()));
            }
            
        }else{
            btnSelectedMaterial.setIconCustom(ShapeDrawer.createSupportGeometry(selectedSupport, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        }
        
        btnSelectedMaterial.setBackGroundCustom(c2);
        
        btnSelectedMaterial.setEventHandler((event)->{
                Bounds b = btnSelectedMaterial.localToScreen(btnSelectedMaterial.getBoundsInLocal());
                contextMenuMaterial.show(btnSelectedMaterial,(int)b.getMinX(),(int)b.getMaxY());
            });
        btnSelectedMaterial.construct();

             
        List<BlockMaterial> materials = getGUI().getApp().getBlocks().getMaterials();
        int nMats = 3;

        HBox matRow = new HBox();
        matRow.getChildren().add(createNullMaterialButton());
        
        it = new CustomMenuItem(new HBox(new Label("Materials:")));
        contextMenuMaterial.getItems().add(it); 
        it = new CustomMenuItem(matRow);
        contextMenuMaterial.getItems().add(it); 

        
        for(BlockMaterial mat:materials){  
            matRow.getChildren().add(createMaterialButton(mat));
            //nm++;
            if(matRow.getChildren().size()==nMats){
                //avoid adding an empty row at the last material
                if(mat==materials.get(materials.size()-1)){
                    continue;
                }
                matRow = new HBox();
                it = new CustomMenuItem(matRow);
                contextMenuMaterial.getItems().add(it); 
            } 
        }
        
        it = new CustomMenuItem(new HBox(new Label("Supports:")));
        contextMenuMaterial.getItems().add(it); 
        
        matRow = new HBox();
        for(String support:SupportBlock.getSupportTypes()){  
            PulseIconButtonCustom btnBlock = new PulseIconButtonCustom(support);
            btnBlock.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
            btnBlock.setIconCustom(ShapeDrawer.createSupportGeometry(support, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnBlock.setEventHandler((event)->{
                selectedSupport = support;
                btnSelectedMaterial.setIconCustom(ShapeDrawer.createSupportGeometry(selectedSupport, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
                btnSelectedMaterial.construct();
                selectedBlockType = BLOCK_TYPE_SUPPORT;
            });
            btnBlock.construct();
            matRow.getChildren().add(btnBlock);
        }

        it = new CustomMenuItem(matRow);
        contextMenuMaterial.getItems().add(it); 

        AnchorPane ScrollContent = new AnchorPane();
        boxBlockTools.getChildren().addAll(btnSelectedMaterial,btnClearBlocks,btnConfigBlock);
        AnchorPane.setRightAnchor(btnConfigBlock, 70.0);
        AnchorPane.setRightAnchor(btnSelectedMaterial, 35.0);
        
        allBtns.getChildren().add(boxBlockTools);
        blockToolsScroll = ScrollContent;
        blockTools = allBtns;
        
    }
    
    public String getSelectedBlockType(){
        return selectedBlockType;
    }
 
    private Node createMaterialButton(BlockMaterial mat){
        
        PulseIconButtonCustom btnBlock = new PulseIconButtonCustom(mat.getID());
        btnBlock.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
        btnBlock.setIconCustom(ShapeDrawer.createBlockIcon(GUImanager.toolBoxIconSize-15, mat.getColor().getColorFX()));
        btnBlock.setEventHandler((event)->{
            btnSelectedMaterial.setIconCustom(ShapeDrawer.createBlockIcon(GUImanager.toolBoxIconSize-15, mat.getColor().getColorFX()));
            btnSelectedMaterial.construct();
            selectedBlock = mat.getID();
            selectedBlockType = BLOCK_TYPE_SOLID;
        });
        btnBlock.construct();
        return btnBlock;
    }
    
    private Node createNullMaterialButton(){
       
        PulseIconButtonCustom btnBlock = new PulseIconButtonCustom("null");
        btnBlock.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
        btnBlock.setIconCustom(ShapeDrawer.createNullBlockIcon(GUImanager.toolBoxIconSize-15, Color.LIGHTGREY,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnBlock.setEventHandler((event)->{
            btnSelectedMaterial.setIconCustom(ShapeDrawer.createNullBlockIcon(GUImanager.toolBoxIconSize-15, Color.LIGHTGREY,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnSelectedMaterial.construct();
            selectedBlock = "null";
            selectedBlockType = BLOCK_TYPE_SOLID;
        });
        btnBlock.construct();
        return btnBlock;
    }
    
    private void createLoadEditTools(){
        
        HBox hbox = new HBox(3);
        
        
        PulseIconButtonCustom btnConfigLoads = new PulseIconButtonCustom("btnConfigLoad");
        Circle c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnConfigLoads.setBackGroundCustom(c);
        btnConfigLoads.setIconFontawesome(FontAwesomeIcon.EDIT, GUImanager.toolBoxIconSize-9+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnConfigLoads.setEventHandler((event)->{
            getGUI().loadScreen(BlockProject.PROPNAME_LOADCASE_LIST);
        });
        btnConfigLoads.construct();
        
        PulseIconButtonCustom deleteCurrent = new PulseIconButtonCustom("btnClearBlocks"); 
        ContextMenu deleteOptions = new ContextMenu();
        PulseIconButtonCustom btnClearBlocks = new PulseIconButtonCustom("btnClearBlocks"); 
        c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnClearBlocks.setBackGroundCustom(c);
        btnClearBlocks.setIconFontawesome(FontAwesomeIcon.TRASH, GUImanager.toolBoxIconSize-8+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnClearBlocks.setEventHandler((event)->{
            deleteCurrent.setIconCustom(utilsGUI.create(selectedLoadCase, "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteCurrent.construct();
            Bounds b = btnClearBlocks.localToScreen(btnClearBlocks.getBoundsInLocal());
            deleteOptions.show(btnClearBlocks,(int)b.getMinX()-GUImanager.toolBoxIconSize,(int)b.getMaxY());
        });
        btnClearBlocks.construct();
        
            CustomMenuItem it = new CustomMenuItem(utilsGUI.create("Delete Loads...", "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT)));
            deleteOptions.getItems().add(it); 
        
            PulseIconButtonCustom deleteAll = new PulseIconButtonCustom("btnClearBlocks"); 
            deleteAll.setBackGroundCustom(new Rectangle(80,20,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            deleteAll.setIconCustom(utilsGUI.create("All", "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteAll.setEventHandler((event)->{
                getGUI().getApp().getBlocks().removeLoadsAll();
                //canvas.renderAllBlocks();
            });
            deleteAll.construct();
            it = new CustomMenuItem(deleteAll);
            deleteOptions.getItems().add(it); 
            
            
            deleteCurrent.setBackGroundCustom(new Rectangle(80,20,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            deleteCurrent.setIconCustom(utilsGUI.create(selectedLoadCase, "Arial", 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
            deleteCurrent.setEventHandler((event)->{
                getGUI().getApp().getBlocks().removeLoadsLoadCase(selectedLoadCase);
                //canvas.renderAllBlocks();
            });
            deleteCurrent.construct();
            it = new CustomMenuItem(deleteCurrent);
            deleteOptions.getItems().add(it); 

        
        PropertyObjectList loadCasesList = getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList();
        LoadCaseBlock selectedCase = (LoadCaseBlock)loadCasesList.getObjectByID(this.selectedLoadCase);
        
        //BUTTON FOR LOAD CASE ////////////////////////////////////////////
        Circle c2 = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c2.setStrokeWidth(1);
        c2.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        ContextMenu loadCasesMenu = new ContextMenu();

        
        PulseIconButtonCustom btnSelectedLoadCase = new PulseIconButtonCustom("btnLoadList");
        btnSelectedLoadCase.setBackGroundCustom(c2);
        if(selectedCase==null){
            btnSelectedLoadCase.setIconCustom(ShapeDrawer.createNullLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, Color.LIGHTGRAY, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        }else{
            btnSelectedLoadCase.setIconCustom(ShapeDrawer.createLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, selectedCase.getColor().getColorFX()));
        }
       
        btnSelectedLoadCase.setEventHandler((event)->{
            Bounds b = btnSelectedLoadCase.localToScreen(btnSelectedLoadCase.getBoundsInLocal());
            loadCasesMenu.show(btnSelectedLoadCase,(int)b.getMinX()-5,(int)b.getMaxY()+2);
        });
        btnSelectedLoadCase.construct();
        
        
        // NULL LOAD CASE BUTTON
        PulseIconButtonCustom btnLcaseNull = new PulseIconButtonCustom("BtnNullLoadCase");
        btnLcaseNull.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
        btnLcaseNull.setIconCustom(ShapeDrawer.createNullLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, Color.LIGHTGRAY, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnLcaseNull.setEventHandler((event)->{
            btnSelectedLoadCase.setIconCustom(ShapeDrawer.createNullLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, Color.LIGHTGRAY, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
            btnSelectedLoadCase.construct();
            selectedLoadCase = "null";
        });
        btnLcaseNull.construct();
        CustomMenuItem item = new CustomMenuItem(btnLcaseNull);
        loadCasesMenu.getItems().add(item);
        
        ////////////////////////////////////////////////////////////////////////////////////////
        
            for(SerializableObject obj:loadCasesList.getObjectList()){  
                LoadCaseBlock lcase = (LoadCaseBlock)obj;
                PulseIconButtonCustom btnBlock = new PulseIconButtonCustom(obj.getID());
                btnBlock.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
                btnBlock.setIconCustom(ShapeDrawer.createLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, lcase.getColor().getColorFX()));
                btnBlock.setEventHandler((event)->{
                    btnSelectedLoadCase.setIconCustom(ShapeDrawer.createLoadCaseIcon(GUImanager.toolBoxIconSize/2-7, lcase.getColor().getColorFX()));
                    btnSelectedLoadCase.construct();
                    selectedLoadCase = obj.getID();
                });
                btnBlock.construct();
                item = new CustomMenuItem(btnBlock);
                loadCasesMenu.getItems().add(item);  
            }
        
        
        ContextMenu loadDirections = new ContextMenu();
        
        //BUTTON FOR LOAD DIRECTION ////////////////////////////////////////////
        PulseIconButtonCustom btnLoadDirection = new PulseIconButtonCustom("btnDirection");
        c = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnLoadDirection.setBackGroundCustom(c);
        btnLoadDirection.setIconFontawesome(getDirectionIcon(selectedLoadDirection), 
                (GUImanager.toolBoxIconSize-12)+"px",
                 GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnLoadDirection.setEventHandler((event)->{
            Bounds b = btnLoadDirection.localToScreen(btnLoadDirection.getBoundsInLocal());
            loadDirections.show(btnLoadDirection,(int)b.getMinX()-5,(int)b.getMaxY());
        });
        btnLoadDirection.construct();
        ////////////////////////////////////////////////////////////////////////
        
            //CustomMenuItem item = new CustomMenuItem(utilsGUI.create("Direction", "Arial", 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT)));
            //loadDirections.getItems().add(item); 
            
            String[] directions = new String[]{BlockDistLoad.LOAD_DIRECTION_UP,
                                            BlockDistLoad.LOAD_DIRECTION_RIGHT,
                                            BlockDistLoad.LOAD_DIRECTION_DOWN,
                                            BlockDistLoad.LOAD_DIRECTION_LEFT};
            for(String dir:directions){
                //DIRECTION BUTTONS
                PulseIconButtonCustom btn = new PulseIconButtonCustom(dir);
                btn.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
                btn.setIconFontawesome(getDirectionIcon(dir), (GUImanager.toolBoxIconSize-12)+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
                btn.setEventHandler((event)->{
                    selectedLoadDirection = dir;
                    //System.out.println("direction: "+dir);
                    btnLoadDirection.setIconFontawesome(getDirectionIcon(selectedLoadDirection), 
                           (GUImanager.toolBoxIconSize-12)+"px",
                            GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
                    btnLoadDirection.construct();
                });
                btn.construct();
                item = new CustomMenuItem(btn);
                loadDirections.getItems().add(item); 
            }
            

        hbox.getChildren().addAll(btnSelectedLoadCase,btnLoadDirection,btnClearBlocks,btnConfigLoads);
        loadTools = hbox; 
    }
    
    
    private void createSupportEditTools(){ 
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/MyContextMenu.css").toExternalForm(); 
        getCentralPane().getStylesheets().add(css); 
        HBox hbox = new HBox(3);
        /*
        PulseIconButtonCustom btnConfigLoads = new PulseIconButtonCustom("btnConfigLoad");
        btnConfigLoads.setBackGroundCircle(GUImanager.toolBoxIconSize+5, Color.TRANSPARENT, true);
        btnConfigLoads.setIconFontawesome(FontAwesomeIcon.COG, GUImanager.toolBoxIconSize+5+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN));
        btnConfigLoads.construct();   */
        
        ContextMenu supportMenu = new ContextMenu();
        //BUTTON FOR LOAD CASE ////////////////////////////////////////////
        Circle circle2 = new Circle((GUImanager.toolBoxIconSize/2),Color.TRANSPARENT);
        circle2.setStrokeWidth(1);
        circle2.setStroke(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        Circle circle = new Circle(GUImanager.toolBoxIconSize/2-10,Color.ALICEBLUE);
        PulseIconButtonCustom btnSelectedSupport = new PulseIconButtonCustom("btnSupportList");
        btnSelectedSupport.setBackGroundCustom(circle2);
        btnSelectedSupport.setIconCustom(ShapeDrawer.createSupportGeometry(selectedSupport, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnSelectedSupport.setEventHandler((event)->{
            Bounds b = btnSelectedSupport.localToScreen(btnSelectedSupport.getBoundsInLocal());
            supportMenu.show(btnSelectedSupport,(int)b.getMinX()-5,(int)b.getMaxY()+2);
        });
        btnSelectedSupport.construct();
        ////////////////////////////////////////////////////////////////////////////////////////
        
        
         for(String support:SupportBlock.getSupportTypes()){  
                PulseIconButtonCustom btnBlock = new PulseIconButtonCustom(support);
                btnBlock.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
                btnBlock.setIconCustom(ShapeDrawer.createSupportGeometry(support, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
                btnBlock.setEventHandler((event)->{
                    selectedSupport = support;
                    btnSelectedSupport.setIconCustom(ShapeDrawer.createSupportGeometry(support, 16,2,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
                    btnSelectedSupport.construct();
                });
                btnBlock.construct();
                CustomMenuItem item = new CustomMenuItem(btnBlock);
                supportMenu.getItems().add(item);  
            }
        
        hbox.getChildren().addAll(btnSelectedSupport);
        supportTools = hbox;
    }
    
    
    
    
    private FontAwesomeIcon getDirectionIcon(String dir){
        switch (dir){
            case BlockDistLoad.LOAD_DIRECTION_UP:
                return FontAwesomeIcon.ARROW_UP;
            case BlockDistLoad.LOAD_DIRECTION_RIGHT:
                return FontAwesomeIcon.ARROW_RIGHT;
            case BlockDistLoad.LOAD_DIRECTION_DOWN:
                return FontAwesomeIcon.ARROW_DOWN;
            case BlockDistLoad.LOAD_DIRECTION_LEFT:
                return FontAwesomeIcon.ARROW_LEFT;
        }
        return null;
    }
    
    
    private void setTopRightCornerTools(Node n, Node scrollableTools){
        //topRightScroll.setContent(null);
       // topRightScroll.setContent(scrollableTools);
        //topRightScroll.setVisible(true);
        topRightTools.getChildren().add(n);
       // topRightScroll.toFront();
        topRightTools.toFront();
    }
    
    @Override
    public void loadScreen(){
        cancelFirstTouch();
        //this screen could have been updated in an other screen, so updated these elements when loading this screen
        createBlockEditTools(); 
        createLoadEditTools();
        updateMode();
        canvas.clearAndRender();
       
        //canvas.renderAllBlocks();
    }
    
    /*
    public void resetScreen(){
        cancelFirstTouch();
        mode = MODE_NONE;
        stateToggle.unselectAll();
        //this screen could have been updated in an other screen, so updated these elements when loading this screen
        createBlockEditTools(); 
        createLoadEditTools();

        updateMode();
        canvas.clearAndRender();  
    }*/
    
    private void updateMode(){
        cancelFirstTouch();
        topRightTools.getChildren().clear();
        switch(mode){
            case MODE_BLOCKS:
                getAppBar().setText(ID+" - "+MODE_BLOCKS);
                setTopRightCornerTools(blockTools,blockToolsScroll);
                break;
            case MODE_LOADS:
                getAppBar().setText(ID+" - "+MODE_LOADS);
                setTopRightCornerTools(loadTools,loadToolsScroll);
            break;
            default:
                mode = MODE_NONE;
                getAppBar().setText(ID);
        }
    }
    
    @Override
    public void update(String... args){
        
    }
   
}
