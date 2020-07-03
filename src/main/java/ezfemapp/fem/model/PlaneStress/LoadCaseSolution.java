package ezfemapp.fem.model.PlaneStress;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author GermanSR
 */
public class LoadCaseSolution {
    
    SimpleMatrix U;
    LoadCase lcase;
    double maxDisp; 
    
    public LoadCaseSolution(SimpleMatrix displacements, LoadCase loadCase){
        lcase=loadCase;
        U=displacements;
    }
    
    public double[] getNodeDisplacements(NodeFEM node){
        double[] disp = new double[2];
        disp[0] = U.get(node.index*2);
        disp[1] = U.get((node.index*2)+1);
        return disp;
    }
    
    public double[] getElementDisplacements(ele2D4N_2DOF ele){
        double[] disp = new double[8];
        disp[0] = U.get(ele.getNodes()[0].index*2);
        disp[1] = U.get(ele.getNodes()[0].index*2 +1);
        disp[2] = U.get(ele.getNodes()[1].index*2);
        disp[3] = U.get(ele.getNodes()[1].index*2 +1);
        disp[4] = U.get(ele.getNodes()[2].index*2);
        disp[5] = U.get(ele.getNodes()[2].index*2 +1);
        disp[6] = U.get(ele.getNodes()[3].index*2);
        disp[7] = U.get(ele.getNodes()[3].index*2 +1);
        return disp;
    }
    
    public SimpleMatrix getDisplacements(){
        return U; 
    }
    
    public void calcMaxDisp(){
        double max=Double.NEGATIVE_INFINITY;
        double min=Double.POSITIVE_INFINITY;
        for(int i=0; i<U.getNumElements(); i++){
            double xc = U.get(i);
            if(xc>max){
                max=xc;
            }
            if(xc<min){
                min=xc;
            }
        }
        maxDisp= Math.max(Math.abs(max), Math.abs(min));
    }
    
    public double getMax(){
        return maxDisp;
    }
    
}
