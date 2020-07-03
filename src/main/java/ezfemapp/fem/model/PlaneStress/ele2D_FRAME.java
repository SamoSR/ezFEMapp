/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author germanso
 */
public class ele2D_FRAME  implements FiniteElement2D{
    
    double A;
    double E;
    double I;
    NodeFEM ni;
    NodeFEM nj;
    
    public SimpleMatrix getRotationMatrix(){
        double L = getLength();
        double cos=(nj.x-ni.x)/L;
        double sin=(nj.y-ni.y)/L;
        SimpleMatrix matR = new SimpleMatrix(6,6);
        matR.set(0, 0, cos) ;matR.set(0, 1,-sin);matR.set(0, 2, 0) ;matR.set(0, 3, 0)   ;matR.set(0, 4, 0)   ;matR.set(0, 5, 0);
        matR.set(1, 0, sin) ;matR.set(1, 1, cos);matR.set(1, 2, 0) ;matR.set(1, 3, 0)   ;matR.set(1, 4, 0)   ;matR.set(1, 5, 0);
        matR.set(2, 0, 0)   ;matR.set(2, 1, 0);  matR.set(2, 2, 1) ;matR.set(2, 3, 0)   ;matR.set(2, 4, 0)   ;matR.set(2, 5, 0);
        matR.set(3, 0, 0)   ;matR.set(3, 1, 0);  matR.set(3, 2, 0) ;matR.set(3, 3, cos) ;matR.set(3, 4,-sin) ;matR.set(3, 5, 0);
        matR.set(4, 0, 0)   ;matR.set(4, 1, 0);  matR.set(4, 2, 0) ;matR.set(4, 3, sin) ;matR.set(4, 4, cos) ;matR.set(4, 5, 0);
        matR.set(5, 0, 0)   ;matR.set(5, 1, 0);  matR.set(5, 2, 0) ;matR.set(5, 3, 0)   ;matR.set(5, 4, 0)   ;matR.set(5, 5, 1);
        return new SimpleMatrix(1,1);
    }
    
    public SimpleMatrix getLocalStiffness(){
        double L = getLength();
        double k = (E*I)/(L*L*L);
        SimpleMatrix matK = new SimpleMatrix(6,6);
        double a = ((A*L*L)/I) * k;
        double b = 12 * k;
        double c = 6*L * k;
        double d = 4*L*L * k;
        double e = 2*L*L * k;
        matK.set(0, 0, a) ;matK.set(0, 1, 0);matK.set(0, 2, 0) ;matK.set(0, 3,-a) ;matK.set(0, 4, 0) ;matK.set(0, 5, 0);
        matK.set(1, 0, 0) ;matK.set(1, 1, b);matK.set(1, 2, c) ;matK.set(1, 3, 0) ;matK.set(1, 4,-b) ;matK.set(1, 5, c);
        matK.set(2, 0, 0) ;matK.set(2, 1, c);matK.set(2, 2, d) ;matK.set(2, 3, 0) ;matK.set(2, 4,-c) ;matK.set(2, 5, e);
        matK.set(3, 0,-a) ;matK.set(3, 1, 0);matK.set(3, 2, 0) ;matK.set(3, 3, a) ;matK.set(3, 4, 0) ;matK.set(3, 5, 0);
        matK.set(4, 0, 0) ;matK.set(4, 1,-b);matK.set(4, 2,-c) ;matK.set(4, 3, 0) ;matK.set(4, 4, b) ;matK.set(4, 5,-c);
        matK.set(5, 0, 0) ;matK.set(5, 1, c);matK.set(5, 2, e) ;matK.set(5, 3, 0) ;matK.set(5, 4,-c) ;matK.set(5, 5, d);
        return matK;
    }
    
    @Override
    public SimpleMatrix getK(){
        SimpleMatrix T = getRotationMatrix();
        return T.transpose().mult(getLocalStiffness()).mult(T);
    }
    
    public double getLength(){
        return Math.sqrt(Math.pow((nj.x-ni.x),2) + Math.pow((nj.y-ni.y),2));
    }
    
    public int[] getNodalIndices(){
        int[] indices = new int[6];
        indices[0] =  ni.index * 2;
        indices[1] = (ni.index * 2) +1;
        indices[2] = (ni.index * 2) +2;
        indices[3] =  nj.index * 2;
        indices[4] = (nj.index * 2) +1;
        indices[5] = (nj.index * 2) +2; 
        return indices;
    }
    
    @Override
    public int getDoF(){
        return 3;
    }
    
}
