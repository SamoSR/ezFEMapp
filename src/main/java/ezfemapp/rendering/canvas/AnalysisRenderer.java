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
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.rendering.shapes.PolygonMesh4Node;
import ezfemapp.rendering.shapes.ShapeDrawer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static javafx.scene.text.Font.font;
import javafx.scene.text.Text;
import org.fxyz3d.geometry.Point3D;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitUtils;
import serializableApp.DimensionUnits.UnitsManagerPro;
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
        
    }

    @Override
    public void singleTapMouse(MouseEvent event) {
        
    }
    
    public void clearAndRender(){
        clear();
        int nrows = analysis.getGUI().getApp().getBlocks().getNumRows();
        int ncols = analysis.getGUI().getApp().getBlocks().getNumCols();
        grid = ShapeDrawer.draw2Dmesh(0, ncols*gridRenderSize, 0, nrows*gridRenderSize, gridRenderSize, gridRenderSize,Color.GRAY);
        addGeometry(grid);  
    }
 
    
    
    public void renderAllBlocks(){ 
        
        
        analysis.getCentralPane().getChildren().remove(colorScalePanel);
        blocksGeometry.getChildren().clear();
        loadGeometry.getChildren().clear();
        
        getRootNode().getChildren().remove(blocksGeometry);
        getRootNode().getChildren().remove(loadGeometry);
        if(!analysis.getFEMModel().getModel().isAnalysisOK()){
            return;
        }
        if(analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_NONE)){
            renderDeformedBlocksNormal(analysis.getLoadCases());    
        }else{
            renderDeformedBlocksColorMap(analysis.getLoadCases());
        }
        
        getRootNode().getChildren().addAll(loadGeometry,blocksGeometry);
    }
    
    private void renderDeformedBlocksNormal(List<String> lcases){
        double min = analysis.getScaleMin();
        double max = analysis.getScaleMax();
        double perc = analysis.getScalePercetage();
        
        for(SerializableObject obj:analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){
            double[] u = analysis.getFEMModel().getNodalDisplacements_Scaled((Block)obj, lcases, min, max, perc);
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
    
    private void renderDeformedBlocksColorMap(List<String> lcases){
        
        double min = analysis.getScaleMin();
        double max = analysis.getScaleMax();
        double perc = analysis.getScalePercetage();
        
        StressFieldComputation colorField = analysis.getFEMModel().computeColorField(lcases, analysis.getSelectedDisplayMode());   
        showColorScale(colorField);
        for(SerializableObject obj:analysis.getGUI().getApp().getBlocks().getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){ 
            Block block = (Block)obj;
            double[] u = analysis.getFEMModel().getNodalDisplacements_Scaled(block, lcases, min,max, perc);
            if(obj instanceof SupportBlock){
                 blocksGeometry.getChildren().add(BlockRenderer.createSupportGeometry((SupportBlock)obj,gridRenderSize,u));  
            }else{     
                ele2D4N_2DOF feElement = analysis.getFEMModel().getElement(block.getIndex());
                double[] colors = new double[]{colorField.getValueAtNode(feElement.getNodes()[0].getIndex()),
                                               colorField.getValueAtNode(feElement.getNodes()[1].getIndex()),
                                               colorField.getValueAtNode(feElement.getNodes()[2].getIndex()),
                                               colorField.getValueAtNode(feElement.getNodes()[3].getIndex())};
                blocksGeometry.getChildren().add(BlockRenderer.createBlockGeometryDeformed_ColorMap(block,gridRenderSize,u,colors,colorField.getMin(),colorField.getMax()));
            }
        }
    }
    
    public void removeColorScale(){
        
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
    
    public void showColorScale(StressFieldComputation colorField){
         
        analysis.getCentralPane().getChildren().remove(colorScalePanel);
        
        if(colorField==null){
            return;
        }
        
        double containerHeight =  analysis.getCentralPane().heightProperty().doubleValue();
        double offsetTop=30;
        double offsetLeft=15;
        double offsetBot=15;
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

        double stepH = ((int)containerHeight-offsetTop-offsetBot)/numberOfLabels;
        
        DimensionUnit displayUnit;
        
        if(analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_DX)||
           analysis.getSelectedDisplayMode().equals(AnalysisScreen.MODE_DY)){
            displayUnit = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS);
        }else{
            displayUnit = analysis.getGUI().getApp().getBlocks().getUnitsManager().getUnits(UnitsManagerPro.UNITS_FEM_STRESS);
        }
        
        for(int i=0;i<numberOfLabels+1;i++){
            double step = (colorField.getMax() - colorField.getMin())/numberOfLabels;
            double valueKgM = colorField.getMax()-step*i;
            double unitValue = UnitUtils.convertFromKgMToUnit(valueKgM, displayUnit.getRealUnits());
            
            String text = RoundDouble.Round2(unitValue);
            
            if(Math.abs(unitValue)<Math.abs(1)){
                double treashold = 9E-5;
                if(Math.abs(unitValue)<treashold){
                    DecimalFormat formatter = new DecimalFormat("0.###E0");
                    text = formatter.format(unitValue);
                } 
            }
            
            Label lbl = new Label(text);
            labalesPane.getChildren().add(lbl);
            lbl.setFont(new Font("Arial", fontSize));
            lbl.setAlignment(Pos.CENTER_RIGHT);
            lbl.setTranslateY(-fontSize/2);
            AnchorPane.setTopAnchor(lbl, stepH*i);
        }

       
        
        String unitDisplay = UnitUtils.convertToPrettyFormat(displayUnit.getRealUnits());
        Text txt = utilsGUI.create("["+unitDisplay+"]", "Arial", (int)fontSize, Color.BLACK);
        colorScalePane.getChildren().add(txt);
        
        AnchorPane.setLeftAnchor(txt, 0.0);
        AnchorPane.setTopAnchor(txt, -offsetTop+5.0);
        
        AnchorPane.setLeftAnchor(colorScalePane, offsetLeft);
        AnchorPane.setTopAnchor(colorScalePane, offsetTop);
  
        colorScalePanel = colorScalePane;
        
        analysis.getCentralPane().getChildren().add(colorScalePanel);
    }
    
}
