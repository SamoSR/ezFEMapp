/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author germanso
 */
public class StressFieldComputation {
    
    public static final String DISPLAYMODE_NONE="NONE";
    public static final String DISPLAYMODE_STRESS_SXX="SXX";
    public static final String DISPLAYMODE_STRESS_SYY="SYY";
    public static final String DISPLAYMODE_STRESS_TXY="SXY";
    public static final String DISPLAYMODE_STRESS_VM="SVM";
    public static final String DISPLAYMODE_DISPX="DX";
    public static final String DISPLAYMODE_DISPY="DY";
    public static final String DISPLAYMODE_RX="RX";
    public static final String DISPLAYMODE_RY="RY";
    
    double currentMin;
    double currentMax;
    HashMap<Integer,NodeStressAverage> nodeValues = new HashMap<>();
    String comp;
 
    public String getMode(){
        return comp;
    }

    public StressFieldComputation(FEMmodelPlaneStress model, List<String> loadCase,  List<String> includeMaterials,String StressComponent){
        comp = StressComponent;
        
        LoadCaseSolutionMultiple solution = model.getLoadCaseSolutionMultiple(loadCase);
        if(solution==null){
            return;
        }
        
        for(NodeFEM node:model.getNodes()){
            nodeValues.put(node.getIndex(),new NodeStressAverage(node.getIndex()));
        }
        
        List<NodeFEM> nodesForColorScale = new ArrayList<>();
        for(ele2D4N_2DOF ele:model.getElements()){
            
            //exlude specific materials from the computation
            boolean skip = true;
            if(includeMaterials!=null){
                for(String mat:includeMaterials){
                    if(mat.equals(ele.mat.matID)){
                        skip=false;
                    }
                }
            }
            /*
            if(ele.blocksupportElement){
                skip = true;
            }*/
            
            if(skip){
                continue;
            }
            
            
            
            for(NodeFEM n:ele.nodes){
                if(!nodesForColorScale.contains(n)){
                    nodesForColorScale.add(n);
                }
            }
            
            SimpleMatrix d = new SimpleMatrix(8,1);
            d.getDDRM().setData(solution.getElementDisplacements(ele));
            
            SimpleMatrix stress = new SimpleMatrix(4,1);
            
            boolean average=true;
            double[] disp = solution.getElementDisplacements(ele);
            SimpleMatrix K = ele.getK();
            SimpleMatrix u = new SimpleMatrix(8,1);
            u.getDDRM().setData(disp);
            SimpleMatrix F = K.mult(u);
           
            
            switch(StressComponent){
                case DISPLAYMODE_STRESS_SXX:
                    stress = ele.nodalStresses(d, 0);
                    break;
                case DISPLAYMODE_STRESS_SYY:
                    stress = ele.nodalStresses(d, 1);
                    break; 
                case DISPLAYMODE_STRESS_TXY:
                    stress = ele.nodalStresses(d, 2);
                    break;   
                case DISPLAYMODE_STRESS_VM:
                    stress = ele.vonMissesStress(d);
                    break; 
                case DISPLAYMODE_RX: 
                    average=false;
                    stress.set(0,0,F.get(0));
                    stress.set(1,0,F.get(2));
                    stress.set(2,0,F.get(4));
                    stress.set(3,0,F.get(6));
                    break;
                case DISPLAYMODE_RY:   
                    average=false;
                    stress.set(0,0,F.get(1));
                    stress.set(1,0,F.get(3));
                    stress.set(2,0,F.get(5));
                    stress.set(3,0,F.get(7));    
                    break;
                default:   
            }
            

            int i=0;
            for(NodeFEM node:ele.getNodes()){
                if(average){
                   nodeValues.get(node.getIndex()).addElement(); 
                }else{
                     nodeValues.get(node.getIndex()).numElements = 1;
                }

                nodeValues.get(node.getIndex()).addValue(stress.get(i));
                i++;
            }
        
            
        }
        
       
   
        
        
        if(StressComponent.equals(DISPLAYMODE_DISPX)){
            nodeValues.clear();
            for(NodeFEM node:nodesForColorScale){
                double[] disp = solution.getNodeDisplacements(node);
                NodeStressAverage avg = new NodeStressAverage(node.getIndex());
                nodeValues.put(node.getIndex(),avg);
                avg.numElements = 1;
                avg.value = disp[0];
            }
        }else if(StressComponent.equals(DISPLAYMODE_DISPY)){
            nodeValues.clear();
            for(NodeFEM node:nodesForColorScale){
                double[] disp = solution.getNodeDisplacements(node);
                NodeStressAverage avg = new NodeStressAverage(node.getIndex());
                nodeValues.put(node.getIndex(),avg);
                avg.numElements = 1;
                avg.value = disp[1];
            }
        }

        currentMin = Double.POSITIVE_INFINITY;
        currentMax = Double.NEGATIVE_INFINITY;
        
        for(NodeStressAverage nodeValue:nodeValues.values()){
            if(nodeValue.getAvergage()>currentMax){
                //currentMax = round(nodeValue.getAvergage(), 5);
                currentMax = nodeValue.getAvergage();
            }
            if(nodeValue.getAvergage()<currentMin){
                //currentMin = round(nodeValue.getAvergage(), 5);
                currentMin = nodeValue.getAvergage();
               // currentMin = nodeValue.getAvergage();
            }
        }
        
        /*
        System.out.println("MAX STRESS: "+currentMax);
        System.out.println("MIN STRESS: "+currentMin);
        for(NodeStressAverage nv:nodeValues.values()){
            System.out.println("Node: "+nv.nodalIndex);
            System.out.println("VAL: : "+nv.getAvergage());
        }*/
        
        
    }
    
    public static List<String> getDisplayModes(){
        List<String> modes = new ArrayList<>();
        modes.add(DISPLAYMODE_NONE);
        modes.add(DISPLAYMODE_STRESS_SXX);
        modes.add(DISPLAYMODE_STRESS_SYY);
        modes.add(DISPLAYMODE_STRESS_TXY);
        modes.add(DISPLAYMODE_STRESS_VM);
        modes.add(DISPLAYMODE_DISPX);
        modes.add(DISPLAYMODE_DISPY);
        return modes;
    }
    
    public double normalize(double xreal, double min, double max){
        return (xreal-min) / (max-min);
    }
    
    public double unNormalize(double xnorm, double min, double max){
        return xnorm*(max-min)+min;
    }
    
    
    public double getMax(){
        return currentMax;
    }
    public double getMin(){
        return currentMin;
    }
    
    public double getValueAtNode(int index){
        if(nodeValues.get(index)==null){
            return Double.NaN;
        }
       return nodeValues.get(index).getAvergage();
    }
    public double getValueAtNode_Normalized(int index){
       //return normalize(nodeValues.get(index).getAvergage(),currentMin,currentMax);
       return normalize(nodeValues.get(index).getAvergage(),currentMin,currentMax);
    }
    
    /*
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/
    
}
