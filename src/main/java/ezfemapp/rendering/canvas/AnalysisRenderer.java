/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;

import ezfemapp.blockProject.Block;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.SupportBlock;
import ezfemapp.fem.model.PlaneStress.StressFieldComputation;
import ezfemapp.fem.model.PlaneStress.ele2D4N_2DOF;
import ezfemapp.gui.ezfem.AnalysisScreen; 
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.rendering.shapes.PolygonMesh4Node;
import ezfemapp.rendering.shapes.ShapeDrawer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.fxyz3d.geometry.Point3D;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitUtils;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.commands.CommandResult;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.PropertyString;
import serializableApp.objects.SerializableObject;
import serializableApp.utils.RoundDouble;


/**
 *
 * @author GermanSR
 */
public class AnalysisRenderer extends CanvasPane2D_1 {
    
    final double gridRenderSize = 50;
    AnalysisScreen analysis;
    //BlockProject blocks;
    
    Group grid=new Group();
    Group blocksGeometry = new Group();
    Group loadGeometry = new Group();
    
    public AnalysisRenderer(AnalysisScreen screen){
        this.analysis = screen;
       
        
        //ZOOM TO THE SCREEN SIZE
        double paneWidth = screen.getGUI().getWidth().doubleValue();
        double w=(screen.getGUI().getApp().getBlocks().getNumCols()+4)*gridRenderSize;
        double factor = paneWidth/w;
        zoomOut(factor,0, 0);
        translateRoot((2*gridRenderSize)*factor, (2*gridRenderSize)*factor);
    }

    @Override
    public void doubleTapTouch(TouchEvent event) {
        
    }

    @Override
    public void singleTapTouch(TouchEvent event) {
        Node n = event.getTouchPoint().getPickResult().getIntersectedNode();
            if(n!=null){
                if(n.getId()!=null){
                    String id = n.getId();
                   // System.out.println("Selected 1: "+n.getId());
                    if(id.startsWith("b")){

                        PropertyObjectList listBlocks = analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList();
                        Block b = (Block)listBlocks.getObjectByID(id);
                        analysis.showElementResults(b,event.getTouchPoint().getScreenX(),event.getTouchPoint().getScreenY());
                        return;
                    }

                }
        }
        hideSelectedElementHighlight();
    }

    @Override
    public void singleTapMouse(MouseEvent event) {
        //System.out.println("Click...");
        if(event.isPrimaryButtonDown()){
            Node n = event.getPickResult().getIntersectedNode();
            if(n!=null){
                if(n.getId()!=null){
                    String id = n.getId();
                    //System.out.println("Selected 1: "+n.getId());
                    if(id.startsWith("b")){

                        PropertyObjectList listBlocks = analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList();
                        Block b = (Block)listBlocks.getObjectByID(id);
                        analysis.showElementResults(b,event.getScreenX(),event.getScreenY());
                        return;
                    }

                }
            }
        }
        hideSelectedElementHighlight();
    }
    
    Node selectedBlockGeometry;
    public void highlightSelectedBlock( Block b){
        if(selectedBlockGeometry!=null){
            getRootNode().getChildren().remove(selectedBlockGeometry);
        }
        double min = analysis.getScaleMin();
        double max = analysis.getScaleMax();
        double perc = analysis.getScalePercetage();
        double[] u = analysis.getFEMModel().getNodalDisplacements_Scaled(b, analysis.getLoadCases(), min, max, perc);
        selectedBlockGeometry = BlockRenderer.createDeformedElementFrame(b,gridRenderSize,u);
        getRootNode().getChildren().add(selectedBlockGeometry);
    }
    
    public void hideSelectedElementHighlight(){
        if(selectedBlockGeometry!=null){
            getRootNode().getChildren().remove(selectedBlockGeometry);
        }  
    }
    
    public void clearAndRender(){
        clear();
        int nrows = analysis.getGUI().getApp().getBlocks().getNumRows();
        int ncols = analysis.getGUI().getApp().getBlocks().getNumCols();
        grid = ShapeDrawer.draw2Dmesh(0, ncols*gridRenderSize, 0, nrows*gridRenderSize, gridRenderSize, gridRenderSize,Color.LIGHTGREY);
        addGeometry(grid);  
    }
 
    public void zoomExtends(){
        resetCamera();
        double paneWidth = analysis.getGUI().getWidth().doubleValue();
        double w=(analysis.getGUI().getApp().getBlocks().getNumCols()+4)*gridRenderSize;
        double factor = paneWidth/w;
        zoomOut(factor,0,0);
        translateRoot((2*gridRenderSize)*factor, (2*gridRenderSize)*factor);
    }
    
    public void renderAllBlocks(){ 
        analysis.hideElementResultsPanel();
        
        analysis.hideMaterialList();
        analysis.getCentralPane().getChildren().remove(colorScalePanel);
        blocksGeometry.getChildren().clear();
        loadGeometry.getChildren().clear();
        
        getRootNode().getChildren().remove(blocksGeometry);
        getRootNode().getChildren().remove(loadGeometry);
        if(!analysis.getFEMModel().getModel().isAnalysisOK()){
            return;
        }
        if(analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_NONE)){
            renderDeformedBlocksNormal();    
        }else{
            renderDeformedBlocksColorMap();
        }
        
        getRootNode().getChildren().addAll(loadGeometry,blocksGeometry);
    }
    
    private void renderDeformedBlocksNormal(){
        double min = analysis.getScaleMin();
        double max = analysis.getScaleMax();
        double perc = analysis.getScalePercetage();
        
        for(SerializableObject obj:analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){
            double[] u = analysis.getFEMModel().getNodalDisplacements_Scaled((Block)obj, analysis.getLoadCases(), min, max, perc);
            if(obj instanceof SupportBlock){
                blocksGeometry.getChildren().add(BlockRenderer.createSupportGeometry((SupportBlock)obj,gridRenderSize,u));  
            }else{
                blocksGeometry.getChildren().add(BlockRenderer.createBlockGeometryDeformed_Polygon3D((Block)obj,gridRenderSize,u));   
            }
        }
        loadGeometry.toBack();
        blocksGeometry.toBack();
        grid.toBack();
    }
    
   
    private void renderDeformedBlocksColorMap(){
        
        double min = analysis.getScaleMin();
        double max = analysis.getScaleMax();
        double perc = analysis.getScalePercetage();
        
        StressFieldComputation currentColorField = analysis.getCurrentColorField();
        showColorScale();
        
        analysis.showMaterialList();
        for(SerializableObject obj:analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){ 
            Block block = (Block)obj;
            double[] u = analysis.getFEMModel().getNodalDisplacements_Scaled(block, analysis.getLoadCases(), min,max, perc);
            if(obj instanceof SupportBlock){
                
                if( analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_RX)||analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_RY)){
                    ele2D4N_2DOF feElement = analysis.getFEMModel().getElement(block.getIndex());
                    double[] colors = new double[]{currentColorField.getValueAtNode(feElement.getNodes()[0].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[1].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[2].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[3].getIndex())};
                    blocksGeometry.getChildren().add(BlockRenderer.createBlockGeometryDeformed_ColorMap(block,gridRenderSize,u,colors,currentColorField.getMin(),currentColorField.getMax(),true));
                }else{
                    blocksGeometry.getChildren().add(BlockRenderer.createSupportGeometry((SupportBlock)obj,gridRenderSize,u));  
                }
                
                 
            }else{  
                //IS THE MATERIAL IS CONSIDERED FOR THE COLOR SCALE?
                if(!isMaterialInColorScale(block.getMaterial().getID())){
                    //NO
                    blocksGeometry.getChildren().add(BlockRenderer.createBlockGeometryDeformed_Polygon3D((Block)obj,gridRenderSize,u)); 
                }else{
                    //YES
                    ele2D4N_2DOF feElement = analysis.getFEMModel().getElement(block.getIndex());
                    double[] colors = new double[]{currentColorField.getValueAtNode(feElement.getNodes()[0].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[1].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[2].getIndex()),
                                                   currentColorField.getValueAtNode(feElement.getNodes()[3].getIndex())};
                    blocksGeometry.getChildren().add(BlockRenderer.createBlockGeometryDeformed_ColorMap(block,gridRenderSize,u,colors,currentColorField.getMin(),currentColorField.getMax(),true));
                }
            }
        }
    }
    
    public boolean isMaterialInColorScale(String mat){
        for(String cmat:analysis.getMaterialsInColorScale()){
            if(cmat.equals(mat)){
                return true;
            }
        }
        return false;
    }
    
    private PolygonMesh4Node colorScalePolygon(float w, float h){
        Point3D p1 = new Point3D( 0,h,0,1);
        Point3D p2 = new Point3D( w,h,0,1);
        Point3D p3 = new Point3D( w,0,0,0);
        Point3D p4 = new Point3D( 0,0,0,0);
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        PolygonMesh4Node mesh = new PolygonMesh4Node(points);
        mesh.setTextureModeVertices3D(1530, p -> p.f,0, 1.5 );
        return mesh;
    }
    
    Node colorScalePanel;
    
    
    public void showColorScale(){
        
        StressFieldComputation colorField = analysis.getCurrentColorField();
        
        analysis.getCentralPane().getChildren().remove(colorScalePanel);
        
        if(colorField==null){
            return;
        }
        if(colorField.getValueAtNode(0)==Double.NaN){
            
        }
        if(colorField.getMax()==Double.NaN){
            return;
        }
        if(colorField.getMin()==Double.NaN){
            return;
        }
        
        double containerHeight =  analysis.getCentralPane().heightProperty().doubleValue();
        double offsetTop=30;
        double offsetLeft=15;
        double offsetBot=15+40;
        double fontSize=10;
        int numberOfLabels= 4;
        int width=35;
        
        AnchorPane colorScalePane = new AnchorPane();
        colorScalePane.setPrefWidth(width);
        
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        AnchorPane labalesPane = new AnchorPane();
        hbox.getChildren().add(colorScalePolygon(width, (int)containerHeight-(int)offsetTop-(int)offsetBot));
        hbox.getChildren().add(labalesPane);
        colorScalePane.getChildren().add(hbox);

        double stepH = ((int)containerHeight-10-offsetTop-offsetBot)/numberOfLabels;
        
        DimensionUnit displayUnit;
        
        if(analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_DX)||
           analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_DY)){
            displayUnit = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(UnitsManagerPro.UNITS_DISPLACEMENTS);
        }else if(analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_RX)||
           analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_RY)){
            displayUnit = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(UnitsManagerPro.UNITS_REACTION_FORCE);
        }else{
            displayUnit = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(UnitsManagerPro.UNITS_FEM_STRESS);
        }
        
        //System.out.println("converting: "+displayUnit.getRealUnits());
        //double zeroTressHold = 1e-10;
        for(int i=0;i<numberOfLabels+1;i++){
            double step = (colorField.getMax() - colorField.getMin())/numberOfLabels;
            double valueKgM = colorField.getMax()-step*i;
            
            
            double unitValue = UnitUtils.convertFromKgMToUnit(valueKgM, displayUnit.getRealUnits());
           // if(unitValue<zeroTressHold){
            //    unitValue = 0;
           // }
            String text = RoundDouble.Round5(unitValue);
            
            if(Math.abs(unitValue)<Math.abs(1)){
                double treashold = 1E-3;
                if(Math.abs(unitValue)<treashold){
                    DecimalFormat formatter = new DecimalFormat("0.###E0");
                    text = formatter.format(unitValue);
                } 
            }
            
            Text txt = utilsGUI.create(text, GUImanager.defaultFont, (int)fontSize, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT));
            labalesPane.getChildren().add(txt);
            AnchorPane.setTopAnchor(txt, stepH*i);
        }


        String unitDisplay = UnitUtils.convertToPrettyFormat(displayUnit.getRealUnits());
       // Text txt = utilsGUI.create("["+unitDisplay+"]", GUImanager.defaultFont, (int)fontSize+1, Color.BLACK);
       // colorScalePane.getChildren().add(txt);
        
        ContextMenu menu = new ContextMenu(); 
        String unitType = "";
        String[] options = new String[]{};

        if(analysis.getSelectedDisplayMode().equals(StressFieldComputation.DISPLAYMODE_DISPX)||
           analysis.getSelectedDisplayMode().equals(StressFieldComputation.DISPLAYMODE_DISPY)){ 
           unitType = UnitsManagerPro.UNITS_DISPLACEMENTS;
           options = BlockProject.lengthUnits;
        }else if(analysis.getSelectedDisplayMode().equals(StressFieldComputation.DISPLAYMODE_RX)||
                 analysis.getSelectedDisplayMode().equals(StressFieldComputation.DISPLAYMODE_RY)){
           unitType = UnitsManagerPro.UNITS_REACTION_FORCE;
           options = BlockProject.forceUnits;
        }else{
           unitType = UnitsManagerPro.UNITS_FEM_STRESS;
           options = BlockProject.stressUnits;
        }

        for(String unit:options){
            //StackPane pane = new StackPane();
           // pane.setStyle("-fx-background-color: white;");
           // pane.getChildren().add(createTextButton(unit,unitType));
            CustomMenuItem item1 = new CustomMenuItem(createTextButton(unit,unitType));
            menu.getItems().add(item1); 
        }
       
       
        PulseIconButtonCustom btnUnits = new PulseIconButtonCustom("LoadCase");
        btnUnits.setBackGroundRectangle(70, 20, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), false);
        btnUnits.setIconCustom(utilsGUI.create(""+unitDisplay+"", GUImanager.defaultFont, 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT)));
        btnUnits.setEventHandler((event)->{
                Bounds b = btnUnits.localToScreen(btnUnits.getBoundsInLocal());
                menu.show(btnUnits,(int)b.getMinX()-5,(int)b.getMaxY()+2); 
            
        });
        btnUnits.construct();
        colorScalePane.getChildren().add(btnUnits);
        AnchorPane.setLeftAnchor(btnUnits, 0.0);
        AnchorPane.setTopAnchor(btnUnits, -offsetTop+5);
        
        AnchorPane.setLeftAnchor(colorScalePane, offsetLeft);
        AnchorPane.setTopAnchor(colorScalePane, offsetTop);
  
        colorScalePanel = colorScalePane;
        
        analysis.getCentralPane().getChildren().add(colorScalePanel);
    }
    
    
    private PulseIconButtonCustom createTextButton(String unit, String unitType){
        PulseIconButtonCustom btnUnits = new PulseIconButtonCustom(unit);
        btnUnits.setBackGroundRectangle(70, 20, Color.TRANSPARENT, false);
        btnUnits.setIconCustom(utilsGUI.create(unit, GUImanager.defaultFont, 11, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT)));
        btnUnits.setEventHandler((event)->{        
            changeColorScaleDisplayUnits(unit,unitType);
        });
        btnUnits.construct();
        return btnUnits;
    }
    
    private void changeColorScaleDisplayUnits(String unit, String unitType){
        DimensionUnit unitObj = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(unitType);
        PropertyString stringProp = unitObj.getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString();
 
        CommandResult r = stringProp.editWithString(unit);
        System.out.println("command1: "+r.getFirstLine());
        
        showColorScale();
        
    }
    
}
