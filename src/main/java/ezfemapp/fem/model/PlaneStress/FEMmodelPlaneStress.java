/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author germanso
 */
public class FEMmodelPlaneStress {
    
    int nodeCount=0;
    int eleCount=0;

    String name;
    ArrayList<ele2D4N_2DOF> finiteElements;
    ArrayList<ele2D_TRUSS> frameElements;

    ArrayList<NodeFEM> nodes;
    ArrayList<LoadCase> loadCases;
    ArrayList<LoadCaseSolution> solutions;
    
    boolean analysisOK=false;

    public FEMmodelPlaneStress(){
        finiteElements = new ArrayList<>();
        frameElements = new ArrayList<>();
        nodes = new ArrayList<>();
        loadCases = new ArrayList<>();
        solutions = new ArrayList<>();
    }
    
    public boolean isAnalysisOK(){
        return analysisOK;
    }
    
    public void addLoadCase(LoadCase lcase){
        loadCases.add(lcase);
    }
    
    public LoadCaseSolution getLoadCaseSolution(String loadCase){
        for(LoadCaseSolution lcase:solutions){
            if(lcase.lcase.name.equals(loadCase)){
                return lcase;
            }            
        }
        return null;
    }
    
    public LoadCaseSolutionMultiple getLoadCaseSolutionMultiple(List<String> loadCases){
        LoadCaseSolutionMultiple solution = new LoadCaseSolutionMultiple();
        SimpleMatrix U = new SimpleMatrix(nodes.size()*2,1);
        U.zero();
        for(String lcase:loadCases){
            LoadCaseSolution sol = getLoadCaseSolution(lcase);
            if(sol==null){
                continue;
            }
            U=U.plus(sol.U);
        }
        solution.U = U;
        solution.calcMaxDisp();
        return solution;
    }
    
    public LoadCase getLoadCase(String loadCase){
        for(LoadCase lcase:loadCases){
            if(lcase.name.equals(loadCase)){
                return lcase;
            }            
        }
        return null;
    }
    
    public ArrayList<ele2D4N_2DOF> getElements(){
        return finiteElements;
    }
    
    public ArrayList<ele2D_TRUSS> getFrameElements(){
        return frameElements;
    }
    
    public ArrayList<NodeFEM> getNodes(){
        return nodes;
    }
    public ele2D4N_2DOF getElementByIndex(int index){
        //System.out.println("looking for: "+index);
        for(ele2D4N_2DOF e:finiteElements){
            //System.out.println("int; "+e.index);
            if(e.index==index){
               // System.out.println("found it! ");
                return e;
            }
        }
        return null;
    }
    private int nx,ny;
    public void createRectangle(double dimX, double dimY, int nx, int ny, double E, double v, double pressureX, double pressureY,boolean reducedInt){
        
        this.nx=nx;
        this.ny=ny;
        MaterialElastic mat = new MaterialElastic(E, v);
        double eleLengthX = dimX/nx;
        double eleLengthY = dimY/ny;
        int count = 0;
        
        for(int i=0;i<nx+1;i++){
            for(int j=0;j<ny+1;j++){
                NodeFEM n = new NodeFEM(eleLengthX*i,dimY-j*eleLengthY,0);
               // System.out.println("X: "+(eleLengthX*i)+", Y:"+(dimY-j*eleLengthY));
                n.index = count;
                nodes.add(n);
                count++;
            }
        }
        
        count=0;
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                NodeFEM n1;
                NodeFEM n2,n3,n4;
                n1 = nodes.get(count+i);
                n2 = nodes.get(count+i+1);
                n3 = nodes.get(count+i+ny+2);
                n4 = nodes.get(count+i+ny+1);
                ele2D4N_2DOF e = new ele2D4N_2DOF(n1,n2,n3,n4,mat);
                e.reducedIntegration = reducedInt;
                //e.reducedIntegration = true;
                e.setIndex(count);
                finiteElements.add(e);
                count++;
            }
        } 
        
        double valX = pressureX * dimY;
        double valY = pressureY * dimY;
        double nodalValX = valX/(ny)/2;
        double nodalValY = valY/(ny)/2;
        
        LoadCase lcase = new LoadCase("DEAD");
        
        loadCases.add(lcase);
        
        /*
        for(int i=0;i<ny+1;i++){
            nodes.get(i).restAll(true); 
            if(i==0){
                NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(nodalValX);
                NodeLoad load2 = new NodeLoad(loadCases.get(0)).setForceY(nodalValY);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load1);  
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load2);  
            }else if(i==ny){
                NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(nodalValX);
                NodeLoad load2 = new NodeLoad(loadCases.get(0)).setForceY(nodalValY);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load1);  
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load2); 
            }else{
                NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(nodalValX*2);
                NodeLoad load2 = new NodeLoad(loadCases.get(0)).setForceY(nodalValY*2);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load1);  
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load2);  
            }
        }
        */
        
            
        NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(valX);
        nodes.get(nodes.size()-1).loads.add(load1);   
        load1 = new NodeLoad(loadCases.get(0)).setForceY(valY);
        nodes.get(nodes.size()-1).loads.add(load1); 
        
        
        /*
        load1 = new NodeLoad(loadCases.get(0)).setForceY(778846.154/2.76);
        nodes.get(2).loads.add(load1);
        
        load1 = new NodeLoad(loadCases.get(0)).setForceX(778846.154/9);
        nodes.get(3).loads.add(load1);  
        
        load1 = new NodeLoad(loadCases.get(0)).setForceY(778846.154/36);
        nodes.get(3).loads.add(load1);  
        */
        
    }
    
    public void checkConnectivity(){
        for(ele2D4N_2DOF ele:finiteElements){
            for(NodeFEM node:ele.nodes){
                node.connectivityCount++;
            }
        }
    }
    

    public void pureBending(double dim, int nEles, double E, double v, double moment){
        
        
        this.nx=nEles+2;
        this.ny=nEles;
        double dimY = dim;
        double dimX = dim + (dimY/ny)*2;
        
        
        MaterialElastic mat = new MaterialElastic(E, v);
        MaterialElastic mat2 = new MaterialElastic(E*0.001, v);
        
        double eleLengthX = dimX/nx;
        double eleLengthY = dimY/ny;
        int count = 0;
        
        for(int i=0;i<nx+1;i++){
            for(int j=0;j<ny+1;j++){
                NodeFEM n = new NodeFEM(eleLengthX*i,dimY-j*eleLengthY,0);
                System.out.println("X: "+(eleLengthX*i)+", Y:"+(dimY-j*eleLengthY));
                n.index = count;
                nodes.add(n);
                count++;
            }
        }
        
        count=0;
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                NodeFEM n1;
                NodeFEM n2,n3,n4;
                n1 = nodes.get(count+i);
                n2 = nodes.get(count+i+1);
                n3 = nodes.get(count+i+ny+2);
                n4 = nodes.get(count+i+ny+1);
                ele2D4N_2DOF e = new ele2D4N_2DOF(n1,n2,n3,n4,mat);
                e.setIndex(count);
                finiteElements.add(e);
                count++;
            }
        } 
        
        checkConnectivity();
        
        for(int i=0;i<ny;i++){
            finiteElements.get(i).mat = mat2;
        }
        for(int i=0;i<ny;i++){
            finiteElements.get(finiteElements.size()-1-i).mat = mat2;
        }
        
        restrictLeftEdge();
        restrictRightEdge();

      //  int midNode = ((ny+1) * (nx+1)-1) /2;
       //  nodes.get(midNode).restAll(true);
         
         
        int nodeIndex1 = ny+1;
        int nodeIndex2 = ny*2+1;
      
        int nodeIndex3 = (ny+1) * (nx+1) -2 -ny;
        int nodeIndex4 = (ny+1) * (nx+1) -2 -ny*2;
        
        double force = moment;

        LoadCase lcase = new LoadCase("DEAD");
        loadCases.add(lcase);
    
        double stepx = ((dimY/2) / (ny/2)) / 2;
        double slope = (force / (dimY/2 ));
       
        System.out.println("stepx: "+stepx);
        System.out.println("slope: "+slope);
        
        
        if(ny==1){
            force = (moment * (dimY/2)) / 2;
            NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(force);
            nodes.get(nodeIndex1).loads.add(load1);
             load1 = new NodeLoad(loadCases.get(0)).setForceX(-force);
            nodes.get(nodeIndex2).loads.add(load1);
             load1 = new NodeLoad(loadCases.get(0)).setForceX(force);
            nodes.get(nodeIndex3).loads.add(load1);
             load1 = new NodeLoad(loadCases.get(0)).setForceX(-force);
            nodes.get(nodeIndex4).loads.add(load1);
            System.out.println("force: "+force);
        }else{
        double x0 = 0;
        double x1 = 0;
        double sum=0;    
            for(int i=nodeIndex1;i<nodeIndex2+1;i++){
                double m = 2;
                if(i==nodeIndex1||nodeIndex2==i){
                    m=1;   
                }
                x1+=stepx*m;
                x0=x1-stepx*m;
                double lengthx = stepx*m;
                double y0 = x0*slope;
                double y1 = x1*slope;
                double negArea1 = lengthx * y0;
                double negArea2 = (lengthx * (y1-y0)) * 0.5;
                double posArea = lengthx * force;
                double area = posArea - negArea1 - negArea2;
                NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(area);
                nodes.get(i).loads.add(load1);
                sum+=area;
                
            }
            
            
            x0 = 0;
            x1 = 0;
            for(int i=nodeIndex4;i<nodeIndex3+1;i++){
                double m = 2;
                if(i==nodeIndex4||nodeIndex3==i){
                    m=1;   
                }
                x1+=stepx*m;
                x0=x1-stepx*m;
                double lengthx = stepx*m;
                double y0 = x0*slope;
                double y1 = x1*slope;
                double negArea1 = lengthx * y0;
                double negArea2 = (lengthx * (y1-y0)) * 0.5;
                double posArea = lengthx * force;
                double area = posArea - negArea1 - negArea2;
                NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(-area);
                nodes.get(i).loads.add(load1);
            }
        }
        
        
            /*
        NodeLoad load1 = new NodeLoad(loadCases.get(0)).setForceX(force);
        nodes.get(nodeIndex1).loads.add(load1);   
        
        load1 = new NodeLoad(loadCases.get(0)).setForceX(-force);
        nodes.get(nodeIndex2).loads.add(load1);
        
        load1 = new NodeLoad(loadCases.get(0)).setForceX(force);
        nodes.get(nodeIndex3).loads.add(load1);  
        
        load1 = new NodeLoad(loadCases.get(0)).setForceX(-force);
        nodes.get(nodeIndex4).loads.add(load1);  */
        

   
    }
    
    public void restrictLeftEdge(){
        for(int i=0;i<ny+1;i++){
            nodes.get(i).restAll(true);
        }
        //for(int i=0;i<nodes.size();i++){
        //    nodes.get(i).restX=true;
        //}
       
    }
    
    
    public NodeFEM newNonRepeatedNode(double x, double y, double z, double tol){
        NodeFEM newNode = new NodeFEM(x,y,z);
        for(NodeFEM n:nodes ){
            if(n.distance(newNode)<tol){
                return n;
            }
        }
        nodes.add(newNode);
        newNode.index = nodeCount;
        nodeCount++;       
        return newNode;
    }
    
    public void restrictRightEdge(){
        for(int i=0;i<ny+1;i++){
            nodes.get((nodes.size()-1)-i).restAll(true);
        }
        
    }
    
     public void applyPresureRightEdge(double value){
        
    }
    
    
    public void CreateCantiliverBeamExample(int nx, int ny){
        NodeFEM[] nodes1 = new NodeFEM[(nx+1)*(ny+1)];
        MaterialElastic mat = new MaterialElastic(1, 0.3);
        
        int count=0;
        for(int i=0;i<nx+1;i++){
            for(int j=0;j<ny+1;j++){
                NodeFEM n = new NodeFEM(i,ny-j,0);
                n.index = count;
                nodes1[count]=n;
                nodes.add(n);
                count++;
            }
        }
        
        count=0;
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                NodeFEM n1;
                NodeFEM n2,n3,n4;
                n1 = nodes1[count+i];
                n2 = nodes1[count+i+1];
                n3 = nodes1[count+i+ny+2];
                n4 = nodes1[count+i+ny+1];
                ele2D4N_2DOF e = new ele2D4N_2DOF(n1,n2,n3,n4,mat);
                finiteElements.add(e);
                count++;
            }
        }
        
        LoadCase lcase = new LoadCase("DEAD");
        loadCases.add(lcase);
        
        for(int i=0;i<ny+1;i++){
            nodes.get(i).restAll(true);
        }

        NodeLoad load = new NodeLoad(loadCases.get(0)).setForceY(1.0);
        nodes.get((nx+1)*(ny+1)-1).loads.add(load);  
    }
    
    public NodeFEM getNodeByCoords(double x, double y, double tolerance){
        for(NodeFEM n:nodes){
            if(n.distance(x, y,0)<=tolerance){
                return n;
            }
        }
        return null;
    }
    
    public void CreatePlaneStressExample(int nx, int ny){
        MaterialElastic mat = new MaterialElastic(1, 0.0);
        NodeFEM[] nodes1 = new NodeFEM[(nx+1)*(ny+1)];
        int count=0;
        for(int i=0;i<nx+1;i++){
            for(int j=0;j<ny+1;j++){
                NodeFEM n = new NodeFEM(i,ny-j,0);
                n.index = count;
                nodes1[count]=n;
                nodes.add(n);
                count++;
            }
        }
        
        count=0;
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                NodeFEM n1;
                NodeFEM n2,n3,n4;
                n1 = nodes1[count+i];
                n2 = nodes1[count+i+1];
                n3 = nodes1[count+i+ny+2];
                n4 = nodes1[count+i+ny+1];
                ele2D4N_2DOF e = new ele2D4N_2DOF(n1,n2,n3,n4,mat);
                finiteElements.add(e);
                count++;
            }
        }
        
        LoadCase lcase = new LoadCase("DEAD");
        loadCases.add(lcase);
        
        for(int i=0;i<ny+1;i++){
            nodes.get(i).restAll(true);
        }
        
        for(int i=0;i<ny+1;i++){
            if(i==0){
                NodeLoad load = new NodeLoad(loadCases.get(0)).setForceX(1.0);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load);  
            }else if(i==ny){
                NodeLoad load = new NodeLoad(loadCases.get(0)).setForceX(1.0);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load);  
            }else{
                NodeLoad load = new NodeLoad(loadCases.get(0)).setForceX(2.0);
                nodes.get((nx+1)*(ny+1)-1-i).loads.add(load);  
            }

        }

    }
    
    public void CreateSimpleSupportedBeamExample(int nx, int ny){
        MaterialElastic mat = new MaterialElastic(1, 0.3);
        NodeFEM[] nodes1 = new NodeFEM[(nx+1)*(ny+1)];
        int count=0;
        for(int i=0;i<nx+1;i++){
            for(int j=0;j<ny+1;j++){
                NodeFEM n = new NodeFEM(i,ny-j,0);
                n.index = count;
                nodes1[count]=n;
                nodes.add(n);
                count++;
            }
        }
        
        count=0;
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                NodeFEM n1;
                NodeFEM n2,n3,n4;
                n1 = nodes1[count+i];
                n2 = nodes1[count+i+1];
                n3 = nodes1[count+i+ny+2];
                n4 = nodes1[count+i+ny+1];
                ele2D4N_2DOF e = new ele2D4N_2DOF(n1,n2,n3,n4,mat);
                finiteElements.add(e);
                count++;
            }
        }
        
        
        
        LoadCase lcase = new LoadCase("DEAD");
        loadCases.add(lcase);
        
        nodes.get(ny).restAll(true);
        nodes.get(nodes.size()-1).restY = true;
          
        for(int i=0;i<nx+1;i++){
            NodeLoad load = new NodeLoad(loadCases.get(0)).setForceY(-1.0);
            nodes.get(i*(ny+1)).loads.add(load);  
        }
     
    }
    
    public void generateIndexes(){
        int count=0;
        for(NodeFEM n:nodes){
            n.index = count++;
        }
        count = 0;
        for(ele2D4N_2DOF e:finiteElements){
            e.index = count++;
        }
                
    }
    
}
