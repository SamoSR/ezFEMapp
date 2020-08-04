package ezfemapp.fem.model.PlaneStress;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author GermanSR
 */
public class MaterialElastic {
    
    String matID;
    double E;
    double v;
    double density;
    
    public MaterialElastic( String id,double elasticModulus, double poissonRatio, double density){
        this.E=elasticModulus;
        this.v=poissonRatio;
        this.matID = id;
        this.density = density;
    }
    
    public MaterialElastic(double elasticModulus, double poissonRatio){
        this.E=elasticModulus;
        this.v=poissonRatio;
        this.matID = "";
    }
    
    public SimpleMatrix getElasticityMatrix(){
        SimpleMatrix D = new SimpleMatrix(3,3);
        double f = E/(1-v*v);
        D.set(0, 0, 1);
        D.set(1, 0, v);
        D.set(0, 1, v);
        D.set(1, 1, 1);
        D.set(2, 2, (1-v)/2);
        return D.scale(f);
    }
    
    public SimpleMatrix getElasticityMatrix3D(){
        SimpleMatrix D = new SimpleMatrix(6,6);
        D.zero();
        
        double lamb = v*E/((1+v)*(1-2*v));
        double mu = E/(2*(1+v));
        
        D.set(0, 0, lamb+2*mu);
        D.set(1, 0, lamb);
        D.set(2, 0, lamb);
        
        D.set(0, 1, lamb);
        D.set(1, 1, lamb+2*mu);
        D.set(2, 1, lamb);
        
        D.set(0, 2, lamb);
        D.set(1, 2, lamb);
        D.set(2, 2, lamb+2*mu);
        
        D.set(3, 3, mu);
        D.set(4, 4, mu);
        D.set(5, 5, mu);
        
        return D;
    }
    
    
    public double getG(){
        return E*0.5f*(1+v);
    }
    
    
}
