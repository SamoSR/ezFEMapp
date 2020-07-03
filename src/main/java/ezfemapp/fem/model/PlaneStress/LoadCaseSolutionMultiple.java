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
public class LoadCaseSolutionMultiple extends LoadCaseSolution{
    
    public LoadCaseSolutionMultiple(){
        super(new SimpleMatrix(1,1),new LoadCase(""));
    }
    
}
