/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import static ezfemapp.blockProject.LoadCaseBlock.PROPNAME_COLOR;
import static ezfemapp.blockProject.LoadCaseBlock.PROPNAME_MAG_LINE;
import static ezfemapp.blockProject.LoadCaseBlock.PROPNAME_MAG_POINT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.scene.paint.Color;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.objects.EditPropertyGroup;
import serializableApp.objects.Project;
import serializableApp.objects.PropertyDimension;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.SerializableObject;


public class BlockProject extends Project {
    
    public static final String PROPNAME_BLOCK_LIST = "BlockList";
    public static final String PROPNAME_MATERIAL_LIST = "MaerialList";
    public static final String PROPNAME_CHUNKSIZE_LIST = "ChunkSizeList";
    public static final String PROPNAME_LOADCASE_LIST = "LoadCaseList";
    public static final String PROPNAME_LINEARLOAD_LIST = "LinearLoadList";
    
    public static final String PROPNAME_GRID_SIZE = "CellSize"; 
    public static final String PROPNAME_ANALYTICAL_THICKNESS = "AnalyticalThickness"; 
    public static final String PROPNAME_NUMBER_ROWS = "NumRows";
    public static final String PROPNAME_NUMBER_COLS = "NumCols";
    
    
    HashMap<String,ArrayList<BlockDistLoad>> loads;
    Block[][] blockMatrix;
    

    public BlockProject(String id){
        super(id);
        DimensionUnit dimGlobal =  UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS);           
        addProperty(new PropertyDimension(PROPNAME_ANALYTICAL_THICKNESS,10,dimGlobal.getRealUnits(),dimGlobal.getID()));  
        addProperty(new PropertyDimension(PROPNAME_GRID_SIZE,10,dimGlobal.getRealUnits(),dimGlobal.getID()));
        addProperty(new PropertyObjectList(PROPNAME_MATERIAL_LIST, BlockMaterial.OBJECT_TYPE));
        addProperty(new PropertyObjectList(PROPNAME_BLOCK_LIST, Block.OBJECT_TYPE));
        addProperty(new PropertyObjectList(PROPNAME_CHUNKSIZE_LIST, ChunkSize.OBJECT_TYPE));
        addProperty(new PropertyObjectList(PROPNAME_LOADCASE_LIST, LoadCaseBlock.OBJECT_TYPE));
        addProperty(new PropertyObjectList(PROPNAME_LINEARLOAD_LIST, BlockDistLoad.OBJECT_TYPE));
        addProperty(new PropertyInteger(PROPNAME_NUMBER_ROWS, 25));
        addProperty(new PropertyInteger(PROPNAME_NUMBER_COLS, 50));
        

        
        blockMatrix = new Block[getNumRows()][getNumCols()];
        loads = new HashMap<>();
        
        
        getProperty(PROPNAME_NUMBER_ROWS).uneditable();
        getProperty(PROPNAME_NUMBER_COLS).uneditable();
        addPropertyGroup(new EditPropertyGroup("Grid System", PROPNAME_GRID_SIZE,PROPNAME_NUMBER_ROWS,PROPNAME_NUMBER_COLS ));
        addPropertyGroup(new EditPropertyGroup("Analysis", PROPNAME_ANALYTICAL_THICKNESS));
    }
    
    public double getAnalyticalThickness(){
        return getProperty(PROPNAME_ANALYTICAL_THICKNESS).castoToPropertyDimension().getValue();
    }
    
    
    public List<BlockMaterial> getMaterials(){
        List<BlockMaterial> materials = new ArrayList<>();
        PropertyObjectList matList = getProperty(PROPNAME_MATERIAL_LIST).castoToPropertyObjectList();
        for(SerializableObject obj:matList.getObjectList()){
            materials.add((BlockMaterial)obj);
        }
        return materials;
    }
    
    public void generateBlockMatrix(){
        int r = getNumRows();
        int c = getNumCols();
        blockMatrix = new Block[r][c];
        for(SerializableObject obj:getProperty(PROPNAME_BLOCK_LIST).castoToPropertyObjectList().getObjectList()){
            Block block = (Block)obj;
            blockMatrix[block.getRow()][block.getColumn()] = block;
        }
    }
    
    private void updateBlockList(){
        PropertyObjectList list = getProperty(PROPNAME_BLOCK_LIST).castoToPropertyObjectList();
        list.clearList();
        int r = getNumRows();
        int c = getNumCols();
         for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]==null){
                    continue;
                }
                list.addObjectAnyID(blockMatrix[i][j]);
            }
        }
    }
    
    List<Block> lastAdded = new ArrayList<>();
    List<Block> lastRremoved = new ArrayList<>();;
    public List<Block> getLastAdded(){
        return lastAdded;
    }
    public List<Block> getLastRemoved(){
        return lastRremoved;
    }
    
    public void setBlockAt(int r, int c, int sizeR,int sizeC,String material){
        lastAdded.clear();
        lastRremoved.clear();
        
        SerializableObject mat = getProperty(PROPNAME_MATERIAL_LIST).castoToPropertyObjectList().getObjectByID(material);
        int nRows = getNumRows();
        int nCols = getNumCols();
        if(r>=nRows||r<0){
            return;
        }
        if(c>=nCols||c<0){
            return;
        }
        
        for(int i=0;i<sizeR;i++){
            for(int j=0;j<sizeC;j++){
                if((r+i)>=(nRows)){
                    continue;
                }
                if((c+j)>=(nCols)){
                    continue;
                }
                if(blockMatrix[r+i][c+j]!=null){
                    lastRremoved.add(blockMatrix[r+i][c+j]);
                }
                Block newBlock = null;
                if(mat!=null){
                    newBlock = new Block(r+i,c+j,(BlockMaterial)mat);  
                //NULL BLOCK
                }
                //ADD THE NEW BLOCK
                blockMatrix[r+i][c+j] = newBlock;  
                if(newBlock!=null){
                    newBlock.intializeReferencedObjects(this);
                    newBlock.referenceParentToProperties();
                    newBlock.generateGeometry();
                    lastAdded.add(newBlock);
                }
                
            }
        }
        
        updateBlockList();
    }
    
    public void setSupportAt(int r, int c, int sizeR,int sizeC, String type){
        lastAdded.clear();
        lastRremoved.clear();
        
        SerializableObject mat = getProperty(PROPNAME_MATERIAL_LIST).castoToPropertyObjectList().getObjectList().get(0); 
        int nRows = getNumRows();
        int nCols = getNumCols();
        
        if(r>=nRows||r<0){
            return;
        }
        if(c>=nCols||c<0){
            return;
        }
        
        for(int i=0;i<sizeR;i++){
            for(int j=0;j<sizeC;j++){
                if((r+i)>=(nRows)){
                    continue;
                }
                if((c+j)>=(nCols)){
                    continue;
                }
                if(blockMatrix[r+i][c+j]!=null){
                    lastRremoved.add(blockMatrix[r+i][c+j]);
                }
                Block newBlock = null;
                if(mat!=null){
                    newBlock = new SupportBlock(r+i,c+j,(BlockMaterial)mat,type);  
                }
                //ADD THE NEW BLOCK
                blockMatrix[r+i][c+j] = newBlock;  
                if(newBlock!=null){
                    newBlock.intializeReferencedObjects(this);
                    newBlock.referenceParentToProperties();
                    newBlock.generateGeometry();
                    lastAdded.add(newBlock);
                }
            }
        }
        
        //blockMatrix[r][c] = newBlock;
        updateBlockList();
        referenceParentToProperties();
        initializeReferenceProperties();   
    }
    
    public void setLoadAt(int r, int c, int sizeR,int sizeC,String loadCase,String direction){
        
        SerializableObject lcase = getProperty(PROPNAME_LOADCASE_LIST).castoToPropertyObjectList().getObjectByID(loadCase);
        int nRows = getNumRows();
        int nCols = getNumCols();
        if(r>=nRows||r<0){
            return;
        }
        if(c>=nCols||c<0){
            return;
        }
        if(lcase==null){
            removeAllLoadsAt(r, c, sizeR, sizeC);
            return;
        }
        
        ArrayList<BlockDistLoad> distLoads = loads.get(lcase.getID());
        if(distLoads==null){
            loads.put(lcase.getID(), new ArrayList<>());
        }        
            
        PropertyObjectList list = getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList();
        
        
        for(int i=0;i<sizeR;i++){
            for(int j=0;j<sizeC;j++){
                BlockDistLoad load = new BlockDistLoad(r+i, c+j, (LoadCaseBlock)lcase, direction);  
                list.removeObjectByID(load.getID());
                list.addObjectAnyID(load);
                loads.get(lcase.getID()).add(load);
            }
        }
        
     
        //updateBlockList();
        //updateReferenceProperties();
        referenceParentToProperties();
        initializeReferenceProperties(); 

    }
    
    
    public void clearAll(){
        lastAdded.clear();
        lastRremoved.clear();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    lastRremoved.add(blockMatrix[i][j]);
                    blockMatrix[i][j]=null;
                }
                
            }
        }
        updateBlockList();
        referenceParentToProperties();
        initializeReferenceProperties();
    }
    
    public void clearMaterial(String mat){
        lastAdded.clear();
        lastRremoved.clear();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    if(blockMatrix[i][j] instanceof SupportBlock){
                        continue;
                    }
                    if(blockMatrix[i][j].getMaterial().getID().equals(mat)){
                        lastRremoved.add(blockMatrix[i][j]);
                        blockMatrix[i][j]=null;
                        
                    }
                }                  
            }
        }
        updateBlockList();
        referenceParentToProperties();
        initializeReferenceProperties();
    }
    public void clearSupport(String type){
        lastAdded.clear();
        lastRremoved.clear();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    if(blockMatrix[i][j] instanceof SupportBlock){
                        SupportBlock sup = (SupportBlock)blockMatrix[i][j];
                        if(sup.getSupportType().equals(type)){
                            lastRremoved.add(blockMatrix[i][j]);
                            blockMatrix[i][j]=null;
                        }
                    }

                }                  
            }
        }
        updateBlockList();
        referenceParentToProperties();
        initializeReferenceProperties();
    }
      
    
    public void removeAllLoadsAt(int r, int c, int sizeR,int sizeC){
           
        //SerializableObject lcase = getProperty(PROPNAME_LOADCASE_LIST).castoToPropertyObjectList().getObjectByID(loadCase);
        //if(lcase==null){
           // return;
        //}
        
        int nRows = getNumRows();
        int nCols = getNumCols();
        
        if(r>=nRows||r<0){
            return;
        }
        if(c>=nCols||c<0){
            return;
        }
        
        if(loads.values().size()==0){
            return;
        }
    
        for(int i=0;i<sizeR;i++){
            for(int j=0;j<sizeC;j++){
                for(ArrayList<BlockDistLoad> cList:loads.values()){
                    Iterator<BlockDistLoad> itr = cList.iterator();
                    while (itr.hasNext()) {
                        BlockDistLoad load = itr.next();
                        String[] loadProps = load.getID().split(":")[1].split(",");
                        String rowStr = loadProps[0];
                        String colStr = loadProps[1];
                        int loadRow = Integer.parseInt(rowStr);
                        int loadCol = Integer.parseInt(colStr);
                        if(loadRow==(r+i)&&loadCol==(c+j)){
                            itr.remove();
                            getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().removeObjectByID(load.getID());
                        }
                    } 
                }
                  
            }
        }
        referenceParentToProperties();
        initializeReferenceProperties();
    }
    
    public void removeLoadsAll(){
        loads.clear();
        getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().clearList();
    }
    
    public void removeLoadsLoadCase(String loadCase){
        if(loads.isEmpty()){
            return;
        }
        Iterator<BlockDistLoad> itr = loads.get(loadCase).iterator();
        while (itr.hasNext()) {
            BlockDistLoad load = itr.next();
            if(load.getLoadCase().getID().equals(loadCase)){
                itr.remove();
                getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().removeObjectByID(load.getID());
            }
        }  
    }
    
    
    public Block[][] getBlockMatrix(){
        return blockMatrix;
    }
    
    
    public int getNumRows(){
        return getProperty(PROPNAME_NUMBER_ROWS).getValueInteger();
    }
    public int getNumCols(){
        return getProperty(PROPNAME_NUMBER_COLS).getValueInteger();
    }
    public double getGridSize(){
        return getProperty(PROPNAME_GRID_SIZE).getValueDouble();
    }
    
    @Override
    public void createDefaultProject(){
        
        BlockMaterial mat1 = new BlockMaterial("mat1")
                .setElasticModulus(14000*Math.sqrt(300), "kg/cm^2")
                .setDensity(2.4, "ton/m^3")
                .setPoissonRatio(0.24)
                .setColor(Color.GRAY); 
        
        BlockMaterial mat2 = new BlockMaterial("mat2")
                .setElasticModulus(14000*Math.sqrt(300)*0.10, "kg/cm^2")
                .setDensity(1.5, "ton/m^3")
                .setPoissonRatio(0.24)
                .setColor(Color.BROWN); 
        
        

        
        /*
        BlockMaterial mat3 = new BlockMaterial("mat3")
                .setElasticModulus(45e6, "kN/m^2")
                .setDensity(24, "ton/m^3")
                .setPoissonRatio(0.24)
                .setColor(Color.BLUE); */

        
        addObjectToList(mat1);
        addObjectToList(mat2);
        //addObjectToList(mat3);
        //addObjectToList(SUPPORT_BLOCK_MATERIAL);
        
        ChunkSize size1x1 = new ChunkSize(1, 1);
        ChunkSize size2x2 = new ChunkSize(2, 2);
        ChunkSize size3x3 = new ChunkSize(3, 3);
        ChunkSize size4x4 = new ChunkSize(4, 4);
        addObjectToList(size1x1);
        addObjectToList(size2x2);
        addObjectToList(size3x3);
        addObjectToList(size4x4);
        /*
        LoadCaseBlock lcase1 = new LoadCaseBlock("SW")
                .setLinearForceMagnitude(10, "kN")
                .setPointForceMagnitude(10, "kN/m")
                .setColor(Color.RED);   
        lcase1.undeletable();*/
        
        LoadCaseBlock lcase2 = new LoadCaseBlock("LoadCase1")
                .setLinearForceMagnitude(1500, "kg/m")
                .setPointForceMagnitude(10, "kg")
                .setColor(Color.GREEN);
        
        LoadCaseBlock lcase3 = new LoadCaseBlock("LoadCase2")
                .setLinearForceMagnitude(1500, "kg/m")
                .setPointForceMagnitude(10, "kg")
                .setColor(Color.BLUE);
        
        /*
       BlockMaterial fixed = new BlockMaterial("mat7")
                .setElasticModulus(45e6, "kN/m^2")
                .setDensity(24, "ton/m^3")
                .setPoissonRatio(0.24)
                .setColor(Color.BLACK); 
        */
        //addObjectToList(lcase1);
        addObjectToList(lcase2);
        addObjectToList(lcase3);

        referenceParentToProperties();
        updateReferenceProperties();
        
    }
    
    @Override
    public SerializableObject getDefaultObject(String... params){
        String type = params[0];
        String id ="";
        if(params.length==2){
            id = params[1];
        }
        switch(type){
            case UnitsManagerPro.OBJECT_TYPE:
                return new UnitsManagerPro();
            case ColorObject.OBJECT_TYPE:
                return new ColorObject(id, 0, 0, 0);
            case BlockMaterial.OBJECT_TYPE:
                return new  BlockMaterial("");
            case Block.OBJECT_TYPE:
                if(id.startsWith("bs")){
                    return new SupportBlock(0, 0, null, "");
                }else{
                    return new Block(-1,-1,null);
                }
                
            case LoadCaseBlock.OBJECT_TYPE:
                return new LoadCaseBlock("");
            case ChunkSize.OBJECT_TYPE:
                return new ChunkSize(1,1);
                
           // case SupportBlock.PROPVALUE_SUBTYPE:
               // return new SupportBlock(0, 0, null, "");
            case BlockDistLoad.OBJECT_TYPE:  
                return new BlockDistLoad(0, 0, null, "");
            case Project.OBJECT_TYPE:
                return new BlockProject("");
        }
        return null;
    }
}
