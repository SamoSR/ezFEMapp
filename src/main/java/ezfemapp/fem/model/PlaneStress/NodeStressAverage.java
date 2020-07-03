/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

/**
 *
 * @author germanso
 */
public class NodeStressAverage {
    
    int nodalIndex;
    int numElements = 0;
    double value = 0;
    
    public NodeStressAverage(int node){
        nodalIndex = node;
    }
    
    public void addValue(double val){
        value+=val;
    }
    
    public void addElement(){
        numElements++;
    }
    
    public double getAvergage(){
        return value/numElements;
    }
    
}
