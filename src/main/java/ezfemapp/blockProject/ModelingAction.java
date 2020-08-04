/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import ezfemapp.gui.ezfem.ModelingScreen;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GermanSR
 */
public class ModelingAction {
    
    //ModelingScreen modeling;
    public List<Block> addedBlocks ;
    public List<Block> removedBlocks;
    public List<BlockDistLoad> addedLoads;
    public List<BlockDistLoad> removedLoads;
    
    public ModelingAction(){
        addedBlocks = new ArrayList<>();
        removedBlocks = new ArrayList<>();
        addedLoads = new ArrayList<>();
        removedLoads = new ArrayList<>();
    }
    
}
