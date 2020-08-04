/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import ezfemapp.blockProject.Block;
import ezfemapp.blockProject.BlockDistLoad;
import ezfemapp.blockProject.BlockProject;
import ezfemapp.blockProject.SupportBlock;
import java.util.List;
import javafx.geometry.Point2D;
import org.ejml.simple.SimpleMatrix;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class FEMblockModel {
    
    
    FEMmodelPlaneStress model;
    BlockProject blocks;
    
    public FEMmodelPlaneStress getModel(){
        return model;
    }
    
    public FEMblockModel(BlockProject blocks){
        this.blocks=blocks;
    }
    
    public void createModel(){
        model = new FEMmodelPlaneStress();
        PropertyObjectList loadCasesList = blocks.getProperty(BlockProject.PROPNAME_LOADCASE_LIST).castoToPropertyObjectList();
        model.addLoadCase(new LoadCase("$SW"));
        for(SerializableObject obj :loadCasesList.getObjectList()){
            model.addLoadCase(new LoadCase(obj.getID()));
        }
        
        double rows = blocks.getNumRows();
        double cols = blocks.getNumCols();
        double ymax = blocks.getNumRows()*blocks.getGridSize();
        
        double blockLength = blocks.getGridSize();
        
        int eIndex=0;
        System.out.println("BlockSIze: "+blockLength);
        
        //CREATE THE MODEL
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){ 
                
                Block b = blocks.getBlockMatrix()[i][j];
                
                if(b==null){
                    continue;
                }
                     
                NodeFEM n1 = model.newNonRepeatedNode(b.getColumn()*blockLength, ymax - (b.getRow()*blockLength), 0, blockLength*0.1);
                NodeFEM n2 = model.newNonRepeatedNode(b.getColumn()*blockLength, ymax - (b.getRow()*blockLength+blockLength), 0, blockLength*0.1);
                NodeFEM n3 = model.newNonRepeatedNode(b.getColumn()*blockLength+blockLength, ymax - (b.getRow()*blockLength+blockLength), 0, blockLength*0.1);
                NodeFEM n4 = model.newNonRepeatedNode(b.getColumn()*blockLength+blockLength, ymax - (b.getRow()*blockLength), 0, blockLength*0.1);
  
                ele2D4N_2DOF element = new ele2D4N_2DOF(n1, n2, n3, n4, new MaterialElastic(b.getMaterial().getID(),b.getMaterial().getElasticModulus(),
                                                                                            b.getMaterial().getPoissonRatio(),b.getMaterial().getDensity()));
                element.thickness = blocks.getAnalyticalThickness();
 
             // System.out.println("p1: "+(ymax - (b.getRow()*blockLength)));
               // System.out.println("p2: "+(ymax - (b.getRow()*blockLength+blockLength)));
               // System.out.println("ElasticMod: "+b.getMaterial().getElasticModulus());
              //  System.out.println("ElasticMod: "+b.getMaterial().getPoissonRatio());
                
                model.getElements().add(element);
                b.setIndex(eIndex);
                element.setIndex(eIndex);
                
                if(b instanceof SupportBlock){
                   String type = b.getProperty(SupportBlock.PROPNAME_TYPE).castoToPropertyString().getValue();
                   element.blocksupportElement = true;
                   switch(type){
                       case SupportBlock.SUPPORT_FIXED:
                            n1.restAll(true);
                            n2.restAll(true);
                            n3.restAll(true);
                            n4.restAll(true);
                       break; 
                       case SupportBlock.SUPPORT_PINNED_HORZ:
                            n1.set2Drestrictions(true, false);
                            n2.set2Drestrictions(true, false);
                            n3.set2Drestrictions(true, false);
                            n4.set2Drestrictions(true, false);
                       break;   
                       case SupportBlock.SUPPORT_PINNED_VERT:
                            n1.set2Drestrictions(false, true);
                            n2.set2Drestrictions(false, true);
                            n3.set2Drestrictions(false, true);
                            n4.set2Drestrictions(false, true);
                       break;   
                   }
                }
                eIndex++;
            }
        }
        
        //CALCULTE THE WEIGHT OF EACH FINITE ELEMENT AND TRANSFORM IT INTO NODAL LOADS IN A LOAD CASE SPECIFIC FOR THE SELF WEIGHT
        //THE WEIGHT IS DIVIDED ACCORDINGLY TO THE NUMBER OF NODES THAT ARE NOT RESTRICTED IN THE Y DIRECTION
        for(ele2D4N_2DOF ele:model.getElements()){
            int countRestrictionsY=0;
            for(NodeFEM n:ele.getNodes()){
                if(n.restY){
                    countRestrictionsY++;
                }
            }
            int nodesWithLoad = 4-countRestrictionsY;
            if(nodesWithLoad>0){
                
                //SELF WEIGHT
                double weight = blocks.getAnalyticalThickness()*blockLength*blockLength*ele.mat.density ;
                double force = (weight/nodesWithLoad);
                
                NodeLoad selflWeightForce = new NodeLoad(model.getLoadCase("$SW"));
                selflWeightForce.setForceY(-force);
                for(NodeFEM n:ele.getNodes()){
                    if(!n.restY){
                        n.addLoad(selflWeightForce);
                    }
                }
            }  
        }
        
        
        
        for(SerializableObject obj:blocks.getProperty(BlockProject.PROPNAME_LINEARLOAD_LIST).castoToPropertyObjectList().getObjectList()){
         
            BlockDistLoad load = (BlockDistLoad)obj;
            double magnitude = ((load.getLoadCase().getLinearForceMagnitude()*blockLength* blocks.getAnalyticalThickness())/2);
            
            int row = load.getRow();
            int col = load.getColumn();
            Point2D p1 = new Point2D(col*blockLength,ymax - (row*blockLength));
            Point2D p2 = new Point2D(col*blockLength,ymax - (row*blockLength+blockLength));
            Point2D p3 = new Point2D(col*blockLength+blockLength,ymax - (row*blockLength+blockLength));
            Point2D p4 = new Point2D(col*blockLength+blockLength,ymax - (row*blockLength));
            Point2D pA,pB;
            LoadCase lcase = model.getLoadCase(load.getLoadCase().getID());
            if(lcase==null){
                System.out.println("null load case");
                continue;
            }             

            switch(load.getDirection()){
                case BlockDistLoad.LOAD_DIRECTION_UP:
                    pA = p1;
                    pB = p4;
                    NodeFEM n1 = model.getNodeByCoords(pA.getX(), pA.getY(), blockLength*0.1);
                    NodeFEM n2 = model.getNodeByCoords(pB.getX(), pB.getY(), blockLength*0.1);
                    NodeLoad nodalForce = new NodeLoad(lcase);
                    nodalForce.setForceY(magnitude);
                    if(n1!=null&&n2!=null){
                        n1.addLoad(nodalForce);
                        n2.addLoad(nodalForce);
                        //System.out.println("yeah baby");
                    }
                    break;
                case BlockDistLoad.LOAD_DIRECTION_DOWN:
                    pA = p2;
                    pB = p3;
                    n1 = model.getNodeByCoords(pA.getX(), pA.getY(), blockLength*0.1);
                    n2 = model.getNodeByCoords(pB.getX(), pB.getY(), blockLength*0.1);
                    nodalForce = new NodeLoad(lcase);
                    nodalForce.setForceY(-magnitude);
                    if(n1!=null&&n2!=null){
                        n1.addLoad(nodalForce);
                        n2.addLoad(nodalForce);
                        //System.out.println("yeah baby");
                    }
                    break;
                case BlockDistLoad.LOAD_DIRECTION_RIGHT:
                    pA = p3;
                    pB = p4;
                    n1 = model.getNodeByCoords(pA.getX(), pA.getY(), blockLength*0.1);
                    n2 = model.getNodeByCoords(pB.getX(), pB.getY(), blockLength*0.1);
                    nodalForce = new NodeLoad(lcase);
                    nodalForce.setForceX(magnitude);
                   // System.out.println("dawer force: "+magnitude);
                    if(n1!=null&&n2!=null){
                        n1.addLoad(nodalForce);
                        n2.addLoad(nodalForce);
                        
                        //System.out.println("yeah baby");
                    }
                    break;
                case BlockDistLoad.LOAD_DIRECTION_LEFT:
                    pA = p1;
                    pB = p2;
                    n1 = model.getNodeByCoords(pA.getX(), pA.getY(), blockLength*0.1);
                    n2 = model.getNodeByCoords(pB.getX(), pB.getY(), blockLength*0.1);
                    nodalForce = new NodeLoad(lcase);
                    nodalForce.setForceX(-magnitude);
                    if(n1!=null&&n2!=null){
                        n1.addLoad(nodalForce);
                        n2.addLoad(nodalForce);
                        //System.out.println("yeah baby");
                    }
                    break;
            }   
        }

        
    }
    
 
    
    public StressFieldComputation computeColorField(List<String> loadCases, List<String> materials,String which){
        return new StressFieldComputation(model, loadCases, materials,which);
    }

    
   public void solveModel(){
        if(model==null){
            return;
        }
        LinearSolverFEM.solve2D_2DOF(model);
    }

    public double[] getNodalDisplacements_Scaled(Block block, List<String> loadCases, double minScale, double maxScale, double scalePercentage){
        LoadCaseSolution storedSolution = model.getLoadCaseSolutionMultiple(loadCases);
        SimpleMatrix disp = storedSolution.getDisplacements();
        double max = storedSolution.getMax();
        double realScale =linInterpolation(0, minScale, 1, maxScale, scalePercentage);
        ele2D4N_2DOF element = model.getElements().get(block.getIndex());
        double[] nodalDisp = new double[8];
        int count=0;
        for(int i=0;i<4;i++){
            double dx = disp.get(element.getNodes()[i].getIndex()*2);
            double dy = disp.get(element.getNodes()[i].getIndex()*2+1);
            nodalDisp[count++]=(dx/max)*realScale;
            nodalDisp[count++]=-(dy/max)*realScale;
        }      
        return nodalDisp;      
    }
    
    public ele2D4N_2DOF getElement(int index){
        return model.getElements().get(index);
    }
    
    public double linInterpolation(double x0,double y0,double x1,double y1, double x){
        return y0 + (x-x0)*((y1-y0)/(x1-x0));
    }
    
    
}
