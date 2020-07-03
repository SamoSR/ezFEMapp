/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;

import ezfemapp.blockProject.Block;
import ezfemapp.blockProject.BlockDistLoad;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.SupportBlock;
import ezfemapp.gui.ezfem.ModelingScreen;
import ezfemapp.rendering.shapes.ShapeDrawer;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.paint.Color;
import serializableApp.objects.SerializableObject;

/**
 * @author GermanSR
 */
public class ModelRenderer extends CanvasPane2D_1{
    
    public static double gridRenderSize = 50;
    ModelingScreen modeling;
    BlockProject blocks;

    Group grid=new Group();
    Group firstSelectionGeometry = new Group();
    Group blocksGeometry = new Group();
    Group loadGeometry = new Group();
    
    Group allBlocks = new Group();
    
    public ModelRenderer(ModelingScreen screen){
        this.modeling = screen;
        this.blocks = screen.getGUI().getApp().getBlocks();
        //ZOOM TO THE SCREEN SIZE
        double paneWidth = screen.getGUI().getWidth().doubleValue();
        double w=(blocks.getNumCols()+4)*gridRenderSize;
        double factor = paneWidth/w;
        zoomOut(factor,0, 0);
        translateRoot((2*gridRenderSize)*factor, (2*gridRenderSize)*factor);
    }
    
    public void clearAndRender(){
        clear();
        this.blocks = modeling.getGUI().getApp().getBlocks();
        int nrows = blocks.getNumRows();
        int ncols = blocks.getNumCols();
        grid = ShapeDrawer.draw2Dmesh(0, ncols*gridRenderSize, 0, nrows*gridRenderSize, gridRenderSize, gridRenderSize, Color.GRAY);
        addGeometry(grid); 
        
        getRootNode().getChildren().remove(allBlocks);
        allBlocks.getChildren().clear();
        getRootNode().getChildren().add(allBlocks);
        
        reRenderAllBlocks();
        
    }

    TouchPoint doubleTouchP1;
    TouchPoint doubleTouchP2;
    
    @Override
    public void doubleTapTouch(TouchEvent event) {   
        if(modeling.getGUI().isSidePanelOpened()){
            return;
        }
        if(modeling.getDrawMode().equals(ModelingScreen.DRAW_MODE_RECTANGLE)){
            return;
        }
        doubleTouchP2 = event.getTouchPoint();
        
        TapToRemoveBlock_ByDoubleTap(event.getTouchPoint().getX(),event.getTouchPoint().getY());
    }

    @Override
    public void singleTapTouch(TouchEvent event) {
        if(modeling.getGUI().isSidePanelOpened()){
            return;
        }
        doubleTouchP1 = event.getTouchPoint();
        TapToAddBlock(event.getTouchPoint().getX(),event.getTouchPoint().getY());
    }

    @Override
    public void singleTapMouse(MouseEvent event) {
        if(modeling.getGUI().isSidePanelOpened()){
            return;
        }
        if(event.isPrimaryButtonDown()){
            TapToAddBlock(event.getX(),event.getY());
        }else if(event.isSecondaryButtonDown()){
            TapToRemoveBlock(event.getX(),event.getY());
        }
    }
    
    private void TapToRemoveBlock(double x, double y){
        Point2D p = this.getRootNode().parentToLocal(x, y);
        int col = (int)(p.getX()/gridRenderSize);
        int row = (int)(p.getY()/gridRenderSize);
        
        String chunkSize = modeling.getSelectedChunkSize(); 
        if(modeling.getDrawMode().equals(ModelingScreen.DRAW_MODE_RECTANGLE)){
            if(modeling.isFirstTouchConsumed()){
               boolean secondTouchConsumed = modeling.setSecondTouch(row, col);
               if(secondTouchConsumed){
                   chunkSize = modeling.getSelectedChunkSize();
                   row = modeling.getFirstTouch()[0];
                   col = modeling.getFirstTouch()[1];
                   modeling.cancelFirstTouch();
               }else{
                   return;
               } 
            }else{
                modeling.setFirstTouch(row, col,true);
                return;
            }
        }
        
        switch(modeling.getMode()){
            case ModelingScreen.MODE_BLOCKS:
                setBlockAt(col,row,chunkSize,null);
                break;
            case ModelingScreen.MODE_LOADS:  
                removeLoadAt(col,row,chunkSize,modeling.getSelectedLoadCase()); 
                break;
            default:    
        }

        updateBlockGeometries();
    }
    
    private void TapToRemoveBlock_ByDoubleTap(double x, double y){
        Point2D p = this.getRootNode().parentToLocal(x, y);
        
        Point2D p1 = this.getRootNode().parentToLocal(doubleTouchP1.getX(),doubleTouchP1.getY());
        Point2D p2 = this.getRootNode().parentToLocal(doubleTouchP2.getX(),doubleTouchP2.getY());
        
        int col1 = (int)(p1.getX()/gridRenderSize);
        int row1 = (int)(p1.getY()/gridRenderSize);
        
        int col2 = (int)(p2.getX()/gridRenderSize);
        int row2 = (int)(p2.getY()/gridRenderSize);
        
        if(col1!=col2){
            return;
        }
        if(row2!=row1){
            return;
        }

        String chunkSize = modeling.getSelectedChunkSize(); 
        switch(modeling.getMode()){
            case ModelingScreen.MODE_BLOCKS:
                setBlockAt(col1,row1,chunkSize,null);
                break;
            case ModelingScreen.MODE_LOADS:  
                removeLoadAt(col1,row1,chunkSize,modeling.getSelectedLoadCase()); 
                break;
            default:    
        }
       
        updateBlockGeometries();
        //renderAllBlocks();
    }
    /*
    long totalTime = System.currentTimeMillis()-doubleClickTimer;
                doubleClickTimer = System.currentTimeMillis();
                if(totalTime<clickTimerMilis){
                    
                    doubleTapTouch(event);
                    
                    doubleClick = true;
      */       
    
    long firstTapTimer;
    private void TapToAddBlock(double x, double y){
        Point2D p = this.getRootNode().parentToLocal(x, y);
        int col = (int)(p.getX()/gridRenderSize);
        int row = (int)(p.getY()/gridRenderSize);
        String chunkSize = modeling.getSelectedChunkSize();
        if(modeling.getDrawMode().equals(ModelingScreen.DRAW_MODE_RECTANGLE)){
            long totalTime = System.currentTimeMillis();      
            if(modeling.isFirstTouchConsumed()&&totalTime>210){
               boolean secondTouchConsumed = modeling.setSecondTouch(row, col);
               if(secondTouchConsumed){
                   chunkSize = modeling.getSelectedChunkSize();
                   row = modeling.getFirstTouch()[0];
                   col = modeling.getFirstTouch()[1];
                   modeling.cancelFirstTouch();
               }else{
                   return;
               } 
            }else{
                firstTapTimer = System.currentTimeMillis();
                modeling.setFirstTouch(row, col,false);
                return;
            }
        }
       
        switch(modeling.getMode()){
            case ModelingScreen.MODE_BLOCKS:
                if(modeling.getSelectedBlockType().equals(ModelingScreen.BLOCK_TYPE_SOLID)){
                    setBlockAt(col,row,chunkSize,modeling.getSelectedMaterial());
                }else if(modeling.getSelectedBlockType().equals(ModelingScreen.BLOCK_TYPE_SUPPORT)){
                    setSupportAt(col,row,chunkSize,modeling.getSelectedSupport());
                }   
                break;
            case ModelingScreen.MODE_LOADS:  
                setLoadAt(col,row,chunkSize,modeling.getSelectedLoadCase(),modeling.getSelectedLoadDirection());
                break;
            default:    
        }
        updateBlockGeometries();
        //renderAllBlocks();
    }
    
    public void renderFirstTouch(boolean delete){
        if(modeling.isFirstTouchConsumed()){
            getRootNode().getChildren().remove(firstSelectionGeometry);
            if(delete){
               firstSelectionGeometry = new Group(BlockRenderer.createFirstTouchGeometry(modeling.getFirstTouch()[0], modeling.getFirstTouch()[1], gridRenderSize,Color.RED));
            }else{
               firstSelectionGeometry = new Group(BlockRenderer.createFirstTouchGeometry(modeling.getFirstTouch()[0], modeling.getFirstTouch()[1], gridRenderSize,Color.GREEN)); 
            }
            
            getRootNode().getChildren().addAll(firstSelectionGeometry);
            firstSelectionGeometry.toFront();
        }   
    }
    public void removeFirstTouch(){
        if(firstSelectionGeometry!=null){
            getRootNode().getChildren().remove(firstSelectionGeometry);
        }       
    }
    
    private void setBlockAt(int col, int row, String chunkSize,String material){
        int[] chunkSizeInt = getChunkSize(chunkSize);
        blocks.setBlockAt(row, col, chunkSizeInt[0],chunkSizeInt[1],material);
    }
    
    private void setLoadAt(int col, int row, String chunkSize, String loadCase,String loadDirection){ 
        int[] chunkSizeInt = getChunkSize(chunkSize);
        blocks.setLoadAt(row, col,chunkSizeInt[0],chunkSizeInt[1],loadCase,loadDirection);
    }
    
    private void setSupportAt(int col, int row, String chunkSize, String type){
        int[] chunkSizeInt = getChunkSize(chunkSize);
        blocks.setSupportAt(row, col, chunkSizeInt[0],chunkSizeInt[1],type);
    }
    
    public int[] getChunkSize(String chunkSize){
        String[] sizeRC = chunkSize.split("x");
        String rows = sizeRC[0];
        String cols = sizeRC[1];
        int chunkRows;
        int chunkCols;
        try {
            chunkRows = Integer.parseInt(rows);
            chunkCols = Integer.parseInt(cols);
        } catch (Exception e) {
            chunkRows = 1;
            chunkCols = 1;
        }
        return new int[]{chunkRows,chunkCols};
    }
    
    private void removeLoadAt(int col, int row, String chunkSize, String loadCase){
        int[] chunkSizeInt = getChunkSize(chunkSize);
        //System.out.println("rows: "+chunkSizeInt[0]);
        //System.out.println("cols: "+chunkSizeInt[1]);
        blocks.removeAllLoadsAt(row, col,chunkSizeInt[0],chunkSizeInt[1]);
    }

    
    public void renderLoads(){  
        loadGeometry.getChildren().clear();
        getRootNode().getChildren().remove(loadGeometry);
        for(SerializableObject obj:blocks.getProperty(BlockProject.PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().getObjectList()){
            loadGeometry.getChildren().add(BlockRenderer.createLoadGeometry((BlockDistLoad)obj,gridRenderSize));
        }
        
        getRootNode().getChildren().addAll(loadGeometry);
        loadGeometry.toBack();
        grid.toBack();
    }
    
    private void reRenderAllBlocks(){  
        getRootNode().getChildren().remove(loadGeometry);
        for(SerializableObject obj:blocks.getProperty(BlockProject.PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){
            Block b = (Block)obj;
            b.generateGeometry();
            allBlocks.getChildren().add(b.getGometry());
 
        }
        updateBlockGeometries();
        renderLoads();
        grid.toBack();
        
    }
    
  
    
    public void updateBlockGeometries(){ 
        for(Block b:blocks.getLastAdded()){
            if(!allBlocks.getChildren().contains(b.getGometry())){
                allBlocks.getChildren().add(b.getGometry());
            }
          
        }
        for(Block b :blocks.getLastRemoved()){
            allBlocks.getChildren().remove(b.getGometry());
        }
        renderLoads();
    }

    
}
