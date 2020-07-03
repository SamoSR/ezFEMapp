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
public class ele2D_TRUSS implements FiniteElement2D{
    
    MaterialElastic mat;
    double A;
    NodeFEM ni;
    NodeFEM nj;
    
    public ele2D_TRUSS(double area, MaterialElastic mat){
        this.A=area;
        this.mat = mat;
    }
    
    public SimpleMatrix getRotationMatrix(){
        double L = getLength();
        double cos=(nj.x-ni.x)/L;
        double sin=(nj.y-ni.y)/L;
        SimpleMatrix matR = new SimpleMatrix(4,4);
        matR.set(0, 0, cos) ;matR.set(0, 1,-sin);matR.set(0, 2, 0);   matR.set(0, 3, 0);
        matR.set(1, 0, sin) ;matR.set(1, 1, cos);matR.set(1, 2, 0);   matR.set(1, 3, 0);
        matR.set(2, 0, 0)   ;matR.set(2, 1, 0);  matR.set(2, 2, cos); matR.set(2, 3,-sin);
        matR.set(3, 0, 0)   ;matR.set(3, 1, 0);  matR.set(3, 2, sin); matR.set(3, 3, cos);
        return new SimpleMatrix(1,1);
    }
    
    public SimpleMatrix getLocalStiffness(){
        double E = mat.E;
        double L = getLength();
        double a = (E*A)/L;
        SimpleMatrix matK = new SimpleMatrix(4,4);
        matK.set(0, 0, a) ;matK.set(0, 1, 0);matK.set(0, 2,-a) ;matK.set(0, 3, 0) ;
        matK.set(1, 0, 0) ;matK.set(1, 1, 0);matK.set(1, 2, 0) ;matK.set(1, 3, 0) ;
        matK.set(2, 0,-a) ;matK.set(2, 1, 0);matK.set(2, 2, a) ;matK.set(2, 3, 0) ;
        matK.set(3, 0, 0) ;matK.set(3, 1, 0);matK.set(3, 2, 0) ;matK.set(3, 3, 0) ;
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
        int[] indices = new int[4];
        indices[0] = ni.index * 2;
        indices[1] =(ni.index * 2) +1;
        indices[2] = nj.index * 2;
        indices[3] =(nj.index * 2) +1;  
        return indices;
    }
    
    @Override
    public int getDoF(){
        return 2;
    }
    
}
