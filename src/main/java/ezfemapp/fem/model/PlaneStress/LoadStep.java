/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.fem.model.PlaneStress;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GermanSR
 */
public class LoadStep {
    
    List<PointLoadStep> pointLoads;
    
    public LoadStep(){
        pointLoads = new ArrayList<>();
    }
    
    
    
    public List<PointLoadStep> getPointLoads(){
        return pointLoads;
    }
    
}
