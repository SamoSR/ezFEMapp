/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.scene.paint.Color;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.Unit;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.commands.CommandManager;
import serializableApp.objects.EditPropertyGroup;
import serializableApp.objects.Project;
import serializableApp.objects.PropertyDimension;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.PropertyObject;
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
    
    List<ModelingAction> executedActions = new ArrayList<>();
    List<ModelingAction> undoActions = new ArrayList<>();
    CommandManager cmdMng;
    
    public BlockProject(String id){
        super(id);
        DimensionUnit dimGlobal =  UnitsManagerPro.getDefault(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS);           
        addProperty(new PropertyDimension(PROPNAME_ANALYTICAL_THICKNESS,1,dimGlobal.getRealUnits(),dimGlobal.getID()));  
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
        
        getProperty(PROPNAME_MATERIAL_LIST).castoToPropertyObjectList().setMaxItems(10);
        getProperty(PROPNAME_LOADCASE_LIST).castoToPropertyObjectList().setMaxItems(10);
        
        getProperty(PROPNAME_NUMBER_ROWS).uneditable();
        getProperty(PROPNAME_NUMBER_COLS).uneditable();
        addPropertyGroup(new EditPropertyGroup("Grid System", PROPNAME_GRID_SIZE,PROPNAME_NUMBER_ROWS,PROPNAME_NUMBER_COLS ));
        addPropertyGroup(new EditPropertyGroup("Analysis", PROPNAME_ANALYTICAL_THICKNESS));
        
        cmdMng = new CommandManager(this);
    }
    
    public CommandManager getCmdManager(){
        return cmdMng;
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
    
    public List<LoadCaseBlock> getLoadCases(){
       List<LoadCaseBlock> loadCases = new ArrayList<>();
        PropertyObjectList lcaseList = getProperty(PROPNAME_LOADCASE_LIST).castoToPropertyObjectList();
        for(SerializableObject obj:lcaseList.getObjectList()){
            loadCases.add((LoadCaseBlock)obj);
        }
        return loadCases; 
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
    
    public ModelingAction getLastAction(){
        if(executedActions.isEmpty()){
            System.out.println("null executed action");
            return null;
        }
        return executedActions.get(executedActions.size()-1);
    }
    public ModelingAction getLastUndoAction(){
        if(undoActions.isEmpty()){
            System.out.println("null undo action");
            return null;
        }
        return undoActions.get(undoActions.size()-1);
    }
    
    public void undoLastAction(){
        ModelingAction undoAction = new ModelingAction();
        ModelingAction action = getLastAction();
        if(action==null){
            System.out.println("undo error: null action");
            return;
        }
        for(Block b:action.addedBlocks){
            blockMatrix[b.getRow()][b.getColumn()] = null;  
            //System.out.println("removed: "+b.getID());
            undoAction.removedBlocks.add(b);
        }
        for(Block b:action.removedBlocks){
            blockMatrix[b.getRow()][b.getColumn()] = b;
            undoAction.addedBlocks.add(b);
        }
        for(BlockDistLoad load:action.removedLoads){
            addLoad(load);
            undoAction.addedLoads.add(load);
        }
        for(BlockDistLoad load:action.addedLoads){
            removeLoad(load);
            undoAction.removedLoads.add(load);
        }
        undoActions.add(undoAction);
        executedActions.remove(action);
        updateBlockList();
    }
    

    
    public void redoLastUndoAction(){
        ModelingAction newAction = new ModelingAction();
        ModelingAction action = getLastUndoAction();
        if(action==null){
            System.out.println("undo error: null action");
            return;
        }
        for(Block b:action.addedBlocks){
            blockMatrix[b.getRow()][b.getColumn()] = null;  
            newAction.removedBlocks.add(b);
        }
        for(Block b:action.removedBlocks){
            blockMatrix[b.getRow()][b.getColumn()] = b;
            newAction.addedBlocks.add(b);
        }
        for(BlockDistLoad load:action.removedLoads){
            addLoad(load);
            newAction.addedLoads.add(load);
        }
        for(BlockDistLoad load:action.addedLoads){
            removeLoad(load);
            newAction.removedLoads.add(load);
        }
        undoActions.remove(action);
        executedActions.add(newAction);
        updateBlockList();
    }
    
    private void addLoad(BlockDistLoad load){
        PropertyObjectList list = getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList();
        loads.get(load.getLoadCase().getID()).add(load);
        list.addObjectUniqueID(load);    
    }
    private void removeLoad(BlockDistLoad load){
        PropertyObjectList list = getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList();
        loads.get(load.getLoadCase().getID()).remove(load);
        list.removeObject(load);    
    }
    
    public void setBlockAt(int r, int c, int sizeR,int sizeC,String material){
        ModelingAction action = new ModelingAction();

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
                   // lastRremoved.add(blockMatrix[r+i][c+j]);
                    action.removedBlocks.add(blockMatrix[r+i][c+j]);
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
                   // lastAdded.add(newBlock);
                    action.addedBlocks.add(newBlock);
                }
                
            }
        }
        
        executedActions.add(action);
        updateBlockList();
    }
    
    
    
    public void setSupportAt(int r, int c, int sizeR,int sizeC, String type){
        ModelingAction action = new ModelingAction();
        
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
                   // lastRremoved.add(blockMatrix[r+i][c+j]);
                    action.removedBlocks.add(blockMatrix[r+i][c+j]);
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
                  //  lastAdded.add(newBlock);
                    action.addedBlocks.add(newBlock);
                }
            }
        }
        
        executedActions.add(action);
        updateBlockList();
    }
    
    public void setLoadAt(int r, int c, int sizeR,int sizeC,String loadCase,String direction){
        ModelingAction action = new ModelingAction();
        
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
            removeAllLoadsAt(r, c, sizeR, sizeC,action);
            executedActions.add(action);
            return;
        }
        
        ArrayList<BlockDistLoad> distLoads = loads.get(lcase.getID());
        if(distLoads==null){
            loads.put(lcase.getID(), new ArrayList<>());
        }        
            
        PropertyObjectList list = getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList();
        
        
        for(int i=0;i<sizeR;i++){
            for(int j=0;j<sizeC;j++){
                BlockDistLoad load = new BlockDistLoad(r+i, c+j, lcase.getID(), direction);  
                list.removeObjectByID(load.getID());
                list.addObjectAnyID(load);
                loads.get(lcase.getID()).add(load);
                action.addedLoads.add(load);
            }
        }
        
        executedActions.add(action);
        //updateBlockList();
        //updateReferenceProperties();
        referenceParentToProperties();
        initializeReferenceProperties(); 
    }
    
    
    public void clearAll(){
        ModelingAction action = new ModelingAction();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    //lastRremoved.add(blockMatrix[i][j]);
                    action.removedBlocks.add(blockMatrix[i][j]);
                    blockMatrix[i][j]=null;
                }
                
            }
        }
        executedActions.add(action);
        updateBlockList();
       // referenceParentToProperties();
       // initializeReferenceProperties();
    }
    
    public void clearMaterial(String mat){
        ModelingAction action = new ModelingAction();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    if(blockMatrix[i][j] instanceof SupportBlock){
                        continue;
                    }
                    if(blockMatrix[i][j].getMaterial().getID().equals(mat)){
                        action.removedBlocks.add(blockMatrix[i][j]);
                        blockMatrix[i][j]=null;
                        
                    }
                }                  
            }
        }
        executedActions.add(action);
        updateBlockList();
    }
    public void clearSupport(String type){
        ModelingAction action = new ModelingAction();
        int r = getNumRows();
        int c = getNumCols();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                if(blockMatrix[i][j]!=null){
                    if(blockMatrix[i][j] instanceof SupportBlock){
                        SupportBlock sup = (SupportBlock)blockMatrix[i][j];
                        if(sup.getSupportType().equals(type)){
                            action.removedBlocks.add(blockMatrix[i][j]);
                            blockMatrix[i][j]=null;
                        }
                    }

                }                  
            }
        }
        executedActions.add(action);
        updateBlockList();
    }
      
    
    private void removeAllLoadsAt(int r, int c, int sizeR,int sizeC, ModelingAction action){
           
        //System.out.println("Removing loads");
        
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
                            action.removedLoads.add(load);
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
        ModelingAction action = new ModelingAction();
        for(ArrayList<BlockDistLoad> cList:loads.values()){
            for(BlockDistLoad load:cList){
                action.removedLoads.add(load);
            }
        }
        loads.clear();
        getProperty(PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().clearList();
        executedActions.add(action);
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
                .setElasticModulus(210e4, "kg/cm^2")
                .setDensity(2, "ton/m^3")
                .setPoissonRatio(0.3)
                .setColor(Color.GRAY,0.9); 
   
        BlockMaterial mat2 = new BlockMaterial("mat2")
                .setElasticModulus(210e4*0.10, "kg/cm^2")
                .setDensity(1.5, "ton/m^3")
                .setPoissonRatio(0.3)
                .setColor(139,69,19,0.9); 
        
        

        
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
                .setLinearForceMagnitude(10, "kg/cm^2")
                .setPointForceMagnitude(1000, "kN")
                .setColor(Color.GREEN);
        
        LoadCaseBlock lcase3 = new LoadCaseBlock("LoadCase2")
                .setLinearForceMagnitude(10, "kg/cm^2")
                .setPointForceMagnitude(1000, "kN")
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
                 case "Units":
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
                return new BlockDistLoad(0, 0, "", "");
            case Project.OBJECT_TYPE:
                return new BlockProject("");
        }
        System.out.println("returning null default object for type: "+type);
        return null;
    }
    
    public static final String[] stressUnits = new String[]{"Pa","kPa","MPa","GPa","N/mm^2","N/cm^2","kgf/m^2","kgf/cm^2","ksi","psi"};
    public static final String[] lengthUnits = new String[]{"m","cm","mm","in","ft"};
    public static final String[] forceUnits = new String[]{"N","kN","MN","GN","kgf","tonf"};
    public static final String[] densityUnits = new String[]{"g/cm^3","kg/m^3","ton/m^3","lb/ft^3","slug/ft^3",};
    
    public List<SerializableObject> getProjectSettings(){
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_FEM_STRESS).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(stressUnits);

        getUnitsManager().getUnits(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(lengthUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_DISPLACEMENTS).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(lengthUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_ELASTIC_MODULUS).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(stressUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_LOAD_PRESSURE).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(stressUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_LOAD_POINT_FORCE).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(forceUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_DENSITY).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(densityUnits);
        
        getUnitsManager().getUnits(UnitsManagerPro.UNITS_REACTION_FORCE).getProperty(DimensionUnit.PROPNAME_UNITSTRING).castoToPropertyString()
        .setAllowableValues(forceUnits);
        
        List<SerializableObject> settings=new ArrayList<>();
        
        SerializableObject analysisSettings = new SerializableObject("Settings", "Analysis");
        analysisSettings.addProperty(getProperty(PROPNAME_ANALYTICAL_THICKNESS));
        analysisSettings.addPropertyGroup(new EditPropertyGroup("Analysis", PROPNAME_ANALYTICAL_THICKNESS ));
        
        SerializableObject gridSettings = new SerializableObject("Settings", "Grid");
        gridSettings.addProperty(getProperty(PROPNAME_GRID_SIZE));
        gridSettings.addProperty(getProperty(PROPNAME_NUMBER_ROWS));
        gridSettings.addProperty(getProperty(PROPNAME_NUMBER_COLS));
        gridSettings.addPropertyGroup(new EditPropertyGroup("Grid Size", PROPNAME_GRID_SIZE,PROPNAME_NUMBER_ROWS,PROPNAME_NUMBER_COLS ));
        
        
        
        SerializableObject unitSettings = new SerializableObject("Settings", "Units");
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_FEM_STRESS, getUnitsManager().getUnits(UnitsManagerPro.UNITS_FEM_STRESS)));
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS, getUnitsManager().getUnits(UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS)));
        
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_LOAD_POINT_FORCE, getUnitsManager().getUnits(UnitsManagerPro.UNITS_LOAD_POINT_FORCE)));
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_LOAD_PRESSURE, getUnitsManager().getUnits(UnitsManagerPro.UNITS_LOAD_PRESSURE)));
        
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_ELASTIC_MODULUS, getUnitsManager().getUnits(UnitsManagerPro.UNITS_ELASTIC_MODULUS)));
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_DENSITY, getUnitsManager().getUnits(UnitsManagerPro.UNITS_DENSITY)));
        
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_DISPLACEMENTS, getUnitsManager().getUnits(UnitsManagerPro.UNITS_DISPLACEMENTS)));
        unitSettings.addProperty(new PropertyObject(UnitsManagerPro.UNITS_REACTION_FORCE, getUnitsManager().getUnits(UnitsManagerPro.UNITS_REACTION_FORCE)));
        
        unitSettings.addPropertyGroup(new EditPropertyGroup("Length", UnitsManagerPro.UNITS_ELEMENT_DIMENSIONS));
        unitSettings.addPropertyGroup(new EditPropertyGroup("Load", UnitsManagerPro.UNITS_LOAD_PRESSURE,UnitsManagerPro.UNITS_LOAD_POINT_FORCE));
        unitSettings.addPropertyGroup(new EditPropertyGroup("Material", UnitsManagerPro.UNITS_ELASTIC_MODULUS,UnitsManagerPro.UNITS_DENSITY));
        unitSettings.addPropertyGroup(new EditPropertyGroup("Displacement", UnitsManagerPro.UNITS_DISPLACEMENTS));
        unitSettings.addPropertyGroup(new EditPropertyGroup("Stress", UnitsManagerPro.UNITS_FEM_STRESS));
        unitSettings.addPropertyGroup(new EditPropertyGroup("Reactions", UnitsManagerPro.UNITS_REACTION_FORCE));
        
        settings.add(analysisSettings);
        settings.add(gridSettings);
        settings.add(unitSettings);
        return settings;
    }
    
}
