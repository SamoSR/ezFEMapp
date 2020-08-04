/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author GermanSR
 */
public class ele2D4N_2DOF implements FiniteElement2D{
    
    public NodeFEM[] nodes;
    public MaterialElastic mat;
    int index;
    double thickness=1;
    boolean blocksupportElement=false;
    boolean reducedIntegration = false;
    
    //GAUSS COORDINATES FOR 2 POINT RULE
    public static final double gaussCoord = 1/Math.sqrt(3);
    public static final double[] eint = new double[]{-gaussCoord,-gaussCoord,+gaussCoord,+gaussCoord};
    public static final double[] nint = new double[]{-gaussCoord,+gaussCoord,-gaussCoord,+gaussCoord};
 
    public ele2D4N_2DOF(NodeFEM node1, NodeFEM node2,NodeFEM node3,NodeFEM node4, MaterialElastic material){
        nodes = new NodeFEM[4];
        nodes[0]=node1;
        nodes[1]=node2;
        nodes[2]=node3;
        nodes[3]=node4;
        mat=material;  
    }
    
    public void setIndex(int index){
        this.index=index;
    }
    
    public int getIndex(){
        return index;
    }
   
    //SHAPE FUNCTIONS
    public double[] getN(double e, double n){  
        double[] N = new double[4];
        N[0] = 0.25*(1-e)*(1-n);
        N[1] = 0.25*(1+e)*(1-n);
        N[2] = 0.25*(1+e)*(1+n);
        N[3] = 0.25*(1-e)*(1+n);     
        return N;
    }
    
    //DERIVATIVE OF SHAPE FUNCTIONS
    public double[][] getdN(double e, double n){
        double[][] dN = new double[2][4];
        dN[0][0]=-0.25*(1-n);
        dN[1][0]=-0.25*(1-e);
        dN[0][1]= 0.25*(1-n);
        dN[1][1]=-0.25*(1+e);  
        dN[0][2]= 0.25*(1+n);
        dN[1][2]= 0.25*(1+e); 
        dN[0][3]=-0.25*(1+n);
        dN[1][3]= 0.25*(1-e); 
        return dN;
    }
    
    public double[] midPoint(){
        double midX = (nodes[0].x+nodes[2].x)/2;
        double midY = (nodes[0].y+nodes[2].y)/2;
        return new double[]{midX,midY};
    }
    
 
    public int[] getNodalIndices(int dofs){
        int[] indices = new int[4*dofs];
        for(int i=0;i<4;i++){
            for(int j=0;j<dofs;j++){
                indices[(i*dofs)+j]=(nodes[i].index*dofs) + j;
            }
        }
        return indices;
    }
    
    public SimpleMatrix getBmatrix(double e, double n){
        SimpleMatrix B = new SimpleMatrix(3,8);
        //SHAPE FUNCTION DERIVATIVES RESPECT TO LOCAL COORDS
        double[][] dN = getdN(e,n);
        //CALCULATE JACOBIAN MATRIX
        SimpleMatrix J = new SimpleMatrix(2,2);
        double j11,j12,j22,j21;
        j11=j12=j21=j22=0;
        for(int i=0;i<4;i++){
            j11+=dN[0][i]*nodes[i].x;
            j12+=dN[0][i]*nodes[i].y;
            j21+=dN[1][i]*nodes[i].x;
            j22+=dN[1][i]*nodes[i].y;
        }
        
        J.set(0, 0, j11);
        J.set(0, 1, j12);
        J.set(1, 0, j21);
        J.set(1, 1, j22);
        SimpleMatrix devN = new SimpleMatrix(dN);
        SimpleMatrix devNxy = J.invert().mult(devN);

        //CONSTRUCT MATRIX B
        for(int i=0;i<4;i++){
            B.set(0, i*2, devNxy.get(0,i));
            B.set(1, i*2, 0);
            B.set(2, i*2, devNxy.get(1,i));
            B.set(0, i*2+1, 0);
            B.set(1, i*2+1, devNxy.get(1,i));
            B.set(2, i*2+1, devNxy.get(0,i));
        }
        return B;
    }
    

    @Override
    public SimpleMatrix getK(){
        
        SimpleMatrix C = mat.getElasticityMatrix();
        SimpleMatrix K = new SimpleMatrix(8,8);

        
        int nPoints = 4;
        if(reducedIntegration){
            nPoints=1;
        }
        
        //NUMBER OF INTEGRATION POINTS
        for(int j=0;j<nPoints;j++){
          //local coordinates of the integration point being evaluated  
          double e=eint[j];  
          double n=nint[j];      
          if(reducedIntegration){
              e = 0;
              n = 0;
          }
          //SHAPE FUNCTION DERIVATIVES RESPECT TO LOCAL COORDS
          double[][] dN = getdN(e,n);
          //CALCULATE JACOBIAN MATRIX
          SimpleMatrix J = new SimpleMatrix(2,2);
          double j11,j12,j22,j21;
          j11=j12=j21=j22=0;
          for(int i=0;i<4;i++){
              j11+=dN[0][i]*nodes[i].x;
              j12+=dN[0][i]*nodes[i].y;
              j21+=dN[1][i]*nodes[i].x;
              j22+=dN[1][i]*nodes[i].y;
          }
          J.set(0, 0, j11);
          J.set(0, 1, j12);
          J.set(1, 0, j21);
          J.set(1, 1, j22);
          SimpleMatrix devN = new SimpleMatrix(dN);
          SimpleMatrix devNxy = J.invert().mult(devN);
          //CONSTRUCT MATRIX B
          SimpleMatrix B = new SimpleMatrix(3,8);
          for(int i=0;i<4;i++){
              B.set(0, i*2, devNxy.get(0,i));
              B.set(1, i*2, 0);
              B.set(2, i*2, devNxy.get(1,i));
              B.set(0, i*2+1, 0);
              B.set(1, i*2+1, devNxy.get(1,i));
              B.set(2, i*2+1, devNxy.get(0,i));
          }
          double Jdet = J.determinant();
          double weights = 1;
          if(reducedIntegration){
              //2x2=4 - one for each dimension
              weights = 4;
          }
          //GAUSSEAN MULTIPLICATION Bt * K * B * dV
          K=K.plus(B.transpose().mult(C).mult(B).scale(Jdet*weights));
        }
        return K.scale(thickness);
    }

    
    public SimpleMatrix nodalStresses(SimpleMatrix Ue, int component){
        SimpleMatrix stressComp = new SimpleMatrix(4,1);
        SimpleMatrix stressesIntPoints = stressIntPoints(Ue);
        SimpleMatrix Li = extrapolationMatrix();
        
        if(reducedIntegration){
            for(int i=0;i<4;i++){
                stressComp.set(i,0,stressIntPointsReduced(Ue).get(i,component));
            }
        }else{
            for(int i=0;i<4;i++){
                stressComp.set(i,0,stressesIntPoints.get(i,component));
            }
        }
        return Li.mult(stressComp);
    }
    
    
    public SimpleMatrix vonMissesStress(SimpleMatrix Ue){
        SimpleMatrix stressComp = new SimpleMatrix(4,1);
        SimpleMatrix stressesIntPoints = stressIntPoints(Ue);
        double THIRD = 1.0/3.0;
        double SQ3 = Math.sqrt(3.0);
        for(int i=0;i<4;i++){
            //STRESSES
            double sx = stressesIntPoints.get(i,0);
            double sy = stressesIntPoints.get(i,1);
            double sz = 0;
            double sxy = stressesIntPoints.get(i,2);
            double syz = 0;
            double szx = 0;
            //MEAN STRESS
            double sm = THIRD*(sx + sy + sz);
            //DEVIATORIC STRESSES
            double dx = sx - sm;
            double dy = sy - sm;
            double dz = sz - sm;
            //SECOND AND THIRD INVARIANTS
            double J2 = 0.5*(dx*dx + dy*dy + dz*dz)+ sxy*sxy + syz*syz + szx*szx;
            double J3 = dx*dy*dz + 2*sxy*syz*szx- dx*syz*syz - dy*szx*szx - dz*sxy*sxy;
            
            double vm = Math.sqrt(sx*sx+sy*sy+sz*sz - (sx*sy +sy*sz +sz*sx) + 3*(sxy*sxy+syz*syz+szx*szx));
            
            //ANGLE
            double psi = THIRD*Math.acos(1.5*SQ3*J3/Math.sqrt(J2*J2*J2));
            
            //EQUIVALENT STRESS OR VON MISSES STRESS
            double si = Math.sqrt(3*J2);
            
            //STORE THE VM STRESS OF THE NODE i
            stressComp.set(i,0,vm);
        }
        
        SimpleMatrix Li = extrapolationMatrix();
        return Li.mult(stressComp);
    }
    
    //MATRIX 4x3, EVERY ROW (i) STORES THE STRESS COMPONENTS OF THE (i)th NODE
    //sx1 sy1 Txy1
    //sx2 sy2 Txy2
    //sx3 sy3 Txy3
    //sx4 sy4 Txy4
    public SimpleMatrix stressIntPoints(SimpleMatrix Ue){ 
        SimpleMatrix strain;
        SimpleMatrix stress;
        SimpleMatrix stresses = new SimpleMatrix(4,3);   
        //INTEGRATION POINTS 2x2 RULE
        for(int i=0;i<4;i++){ 
           //CURRENT INTEGRATION POINT COORDINATES
           double e=eint[i];
           double n=nint[i];
           strain = getBmatrix(e, n).mult(Ue);
           stress = mat.getElasticityMatrix().mult(strain);
           stresses = stresses.combine(i, 0, stress.transpose());
        }     
        return stresses;
    }
    
    public SimpleMatrix stressIntPointsReduced(SimpleMatrix Ue){ 
        SimpleMatrix strain;
        SimpleMatrix stress;
        SimpleMatrix stresses = new SimpleMatrix(4,3);   
        for(int i=0;i<4;i++){ 
           //CURRENT INTEGRATION POINT COORDINATES
           double e=0;
           double n=0;
           strain = getBmatrix(e, n).mult(Ue);
           stress = mat.getElasticityMatrix().mult(strain);
           stresses = stresses.combine(i, 0, stress.transpose());
        }     
        return stresses;
    }
    
   
    
    
    private SimpleMatrix extrapolationMatrix(){
        
        SimpleMatrix Li = new SimpleMatrix(4,4);
        double A,B,C;
        
        A = (1+(Math.sqrt(3)/2));
        B = -0.5;
        C = (1-(Math.sqrt(3)/2));
           
        Li.set(0,0,A);
        Li.set(0,1,B);
        Li.set(0,2,B);
        Li.set(0,3,C);
 
        Li.set(1,0,B);
        Li.set(1,1,C);
        Li.set(1,2,A);
        Li.set(1,3,B);

        Li.set(2,0,C);
        Li.set(2,1,B);
        Li.set(2,2,B);
        Li.set(2,3,A);

        Li.set(3,0,B);
        Li.set(3,1,A);
        Li.set(3,2,C);
        Li.set(3,3,B);
        
        return Li;  
    }
    
    public NodeFEM[] getNodes(){
        return nodes;
    }
    
    public double strainEnergyUnitDensity(SimpleMatrix Ue){
       SimpleMatrix strainE = Ue.transpose().mult(getK()).mult(Ue);
       return strainE.get(0);
    }
    
    @Override
    public int getDoF(){
        return 2;
    }

    
}