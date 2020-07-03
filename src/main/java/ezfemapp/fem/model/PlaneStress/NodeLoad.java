package ezfemapp.fem.model.PlaneStress;
/**
 *
 * @author GermanSR
 */
public class NodeLoad {

    
    double forceX;
    double forceY;
    double forceZ;
    double momentX;
    double momentY;
    double momentZ;
    LoadCase lcase;
    
    public NodeLoad(LoadCase lcase){
        this.lcase=lcase;
    }
    
    public NodeLoad setForceX(double val){
        forceX=val;
        return this;
    }
    public NodeLoad setForceY(double val){
        forceY=val;
        return this;
    }
    public NodeLoad setForceZ(double val){
        forceZ=val;
        return this;
    }
    public NodeLoad setMomentX(double val){
        momentX=val;
        return this;
    }
    public NodeLoad setMomentY(double val){
        momentY=val;
        return this;
    }
    public NodeLoad setMomentZ(double val){
        momentZ=val;
        return this;
    }
    
}
