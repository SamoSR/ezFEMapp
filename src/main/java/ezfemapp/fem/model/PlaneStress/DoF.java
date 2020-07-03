package ezfemapp.fem.model.PlaneStress;
/**
 *
 * @author GermanSR
 */
public class DoF {
    
    int index;
    boolean restricted=false;
    int countBefore=0;
    
    public DoF(int index){
        this.index=index;
    }
    public int getReducedIndex(){
        return index-countBefore;
    }
    
    
}
