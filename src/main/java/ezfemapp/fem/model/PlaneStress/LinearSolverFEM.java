
package ezfemapp.fem.model.PlaneStress;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.interfaces.linsol.LinearSolver;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.factory.LinearSolverFactory_DSCC;


/**
 *
 * @author GermanSR
 */
public class LinearSolverFEM {
    

    public static void solve2D_2DOF(FEMmodelPlaneStress model){
        model.analysisOK = false;
        
        int[] activeDOFs = new int[]{0,1};
        
        model.solutions.clear();
        
        int nActiveDoF = activeDOFs.length;
	int n = model.nodes.size()*nActiveDoF;
	     
        boolean[] restrictions = new boolean[n];
        DoF[] dofs = new DoF[n];

        //GENERATE RESTRICTIONS
        int rest=0;
        for(NodeFEM node:model.nodes){
            for(int i=0;i<nActiveDoF;i++){
                boolean restiction = node.getRestrictions()[activeDOFs[i]];
                restrictions[node.index*nActiveDoF + i] = restiction;   
                DoF dof = new DoF(node.index*nActiveDoF +i);
                dofs[node.index*nActiveDoF+i] = dof; 
                if(restiction){
                    rest++;
                    dof.restricted = true;
                }
                dof.countBefore=rest;
            }   
            
        }

        int dof=n-rest;
        
        //INITIALIZE MATRICES
        SimpleMatrix F = new SimpleMatrix(n,model.loadCases.size());
        DMatrixSparseCSC  Ksparse = new DMatrixSparseCSC(dof,dof); 

       // Ksparse.print();
        
       if(dof==0){

           return;
       }
        
        //ASSEMBLY GLOBAL STIFFNESS MATRIX 
        for(ele2D4N_2DOF elem:model.finiteElements){

           SimpleMatrix kelem = elem.getK();
           //kelem = kelem.scale(model.modelThicknessFactor);
           
           int[] indexs = elem.getNodalIndices(nActiveDoF);
           for (int i = 0; i < 8; i++) {
                //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                if(!dofs[indexs[i]].restricted){
                    for (int j = 0; j < 8; j++) {	
                        //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                        if(!dofs[indexs[j]].restricted){
                            int reducenIindex_i=dofs[indexs[i]].getReducedIndex();
                            int reducenIindex_j=dofs[indexs[j]].getReducedIndex();
                            double  val = Ksparse.get(reducenIindex_i,reducenIindex_j);
                            //RETRIEVE THE RESTRICTED DEEGREE OF FREEDOM
                            Ksparse.set(reducenIindex_i,reducenIindex_j, kelem.get(i, j)+val);
                        }       
                    }
                }
           }	
        }
        
        
         
        int count1=0;
        for(LoadCase lcase:model.loadCases){
            //ASSEMBLY GLOBAL NODAL LOAD VECTOR FOR EACH LOAD CASE
            for(NodeFEM node:model.nodes){
                F.set(node.index*nActiveDoF, count1, node.getLoadX(lcase));
                F.set((node.index*nActiveDoF)+1, count1, node.getLoadY(lcase));
            }
            count1++;
        }
      //  F.print();
         
       // System.out.println("DOF: "+dof);
        
        count1=0;
        for(LoadCase lcase:model.loadCases){
        	 
            SimpleMatrix Fred = new SimpleMatrix(dof,1);
             
            //REDUCE THE GLOBAL STIFFNESS MATRIX ACCORDINGLY TO THE RESTRICTION VECTOR GENERATED
            int count=0;
            for(int i =0;i<n;i++){
                if(!restrictions[i]){
                     double newVal = Fred.get(count, 0) + F.get(i,count1);
                     Fred.set(count, 0,newVal);
                     count++;
                 }
            }
             
            //SOLVE THE SYSTEM USING LIBRARY EJEML
            DMatrixRMaj Result = new DMatrixRMaj(dof,1);
            
           // Ksparse.print();
           // Fred.getDDRM().print();
            
            LinearSolver<DMatrixSparseCSC,DMatrixRMaj>  solver = LinearSolverFactory_DSCC.cholesky(FillReducing.IDENTITY);
            solver.setA(Ksparse);
            
            
            
            try {
                model.analysisOK=true;
                Fred.getDDRM().print();
                solver.solve(Fred.getDDRM(),Result);
                System.out.println("Model solved");
            } catch (Exception e) {
                model.analysisOK=false;
                System.out.println("Error, check the stability of the structure "+e.getMessage());
                return;
            }
            
            
            //BEFORE SAVING SOLUTION, ADD THE CEROS FROM THE RESTRICTED NODES IN ORDER TO GET THE COMPLETE VECTOR
            SimpleMatrix D = new SimpleMatrix(n,1);
            count=0;
            for(int i=0;i<n;i++){
                if(restrictions[i]){
                    D.set(i,0);
                }else{
                    D.set(i,Result.get(count));
                    count++;
                }
            } 
            
            //   D.print();
            
            LoadCaseSolution loadcasesolution = new LoadCaseSolution(D,lcase);
            loadcasesolution.calcMaxDisp();
            model.solutions.add(loadcasesolution);
        	 
            count1++;
            
        }
         
         
    }
    
    public static void solve2D_3DOF(FEMmodelPlaneStress model){
            
        int[] activeDOFs = new int[]{0,1};
        
        model.solutions.clear();
        
        int nActiveDoF = activeDOFs.length;
	int n = model.nodes.size()*nActiveDoF;
	     
        boolean[] restrictions = new boolean[n];
        DoF[] dofs = new DoF[n];

        //GENERATE RESTRICTIONS
        int rest=0;
        for(NodeFEM node:model.nodes){
            for(int i=0;i<nActiveDoF;i++){
                boolean restiction = node.getRestrictions()[activeDOFs[i]];
                restrictions[node.index*nActiveDoF + i] = restiction;   
                DoF dof = new DoF(node.index*nActiveDoF +i);
                dofs[node.index*nActiveDoF+i] = dof; 
                if(restiction){
                    rest++;
                    dof.restricted = true;
                }
                dof.countBefore=rest;
            }   
            
        }

        int dof=n-rest;
        
        //INITIALIZE MATRICES
        SimpleMatrix F = new SimpleMatrix(n,model.loadCases.size());
        DMatrixSparseCSC  Ksparse = new DMatrixSparseCSC(dof,dof); 

        //ASSEMBLY GLOBAL STIFFNESS MATRIX 
        for(ele2D4N_2DOF elem:model.finiteElements){

           SimpleMatrix kelem = elem.getK();

           int[] indexs = elem.getNodalIndices(nActiveDoF);
           for (int i = 0; i < 8; i++) {
                //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                if(!dofs[indexs[i]].restricted){
                    for (int j = 0; j < 8; j++) {	
                        //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                        if(!dofs[indexs[j]].restricted){
                            int reducenIindex_i=dofs[indexs[i]].getReducedIndex();
                            int reducenIindex_j=dofs[indexs[j]].getReducedIndex();
                            double  val = Ksparse.get(reducenIindex_i,reducenIindex_j);
                            //RETRIEVE THE RESTRICTED DEEGREE OF FREEDOM
                            Ksparse.set(reducenIindex_i,reducenIindex_j, kelem.get(i, j)+val);
                        }       
                    }
                }
           }	
        }
        
        //ASSEMBLY GLOBAL STIFFNESS MATRIX 
        for(ele2D_TRUSS elem:model.frameElements){
           SimpleMatrix kelem = elem.getK();
           int[] indexs = elem.getNodalIndices();
           for (int i = 0; i < 4; i++) {
                //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                if(!dofs[indexs[i]].restricted){
                    for (int j = 0; j < 4; j++) {	
                        //DO NOT ENTER TO RESTRICTED DEEGRES OF FREEDOM
                        if(!dofs[indexs[j]].restricted){
                            int reducenIindex_i=dofs[indexs[i]].getReducedIndex();
                            int reducenIindex_j=dofs[indexs[j]].getReducedIndex();
                            double  val = Ksparse.get(reducenIindex_i,reducenIindex_j);
                            //RETRIEVE THE RESTRICTED DEEGREE OF FREEDOM
                            Ksparse.set(reducenIindex_i,reducenIindex_j, kelem.get(i, j)+val);
                        }       
                    }
                }
           }	
        }
         
        int count1=0;
        for(LoadCase lcase:model.loadCases){
            //ASSEMBLY GLOBAL NODAL LOAD VECTOR FOR EACH LOAD CASE
            for(NodeFEM node:model.nodes){
                F.set(node.index*nActiveDoF, count1, node.getLoadX(lcase));
                F.set((node.index*nActiveDoF)+1, count1, node.getLoadY(lcase));
            }
            count1++;
        }
        //F.print();
         
        count1=0;
        for(LoadCase lcase:model.loadCases){
        	 
            SimpleMatrix Fred = new SimpleMatrix(dof,1);
             
            //REDUCE THE GLOBAL STIFFNESS MATRIX ACCORDINGLY TO THE RESTRICTION VECTOR GENERATED
            int count=0;
            for(int i =0;i<n;i++){
                if(!restrictions[i]){
                     double newVal = Fred.get(count, 0) + F.get(i,count1);
                     Fred.set(count, 0,newVal);
                     count++;
                 }
            }
             
            //SOLVE THE SYSTEM USING LIBRARY EJEML
            DMatrixRMaj Result = new DMatrixRMaj(dof,1);
            LinearSolver<DMatrixSparseCSC,DMatrixRMaj>  solver = LinearSolverFactory_DSCC.cholesky(FillReducing.IDENTITY);
            solver.setA(Ksparse);

            try {
                solver.solve(Fred.getDDRM(),Result);
                System.out.println("Model solved");
            } catch (Exception e) {
                System.out.println("Error, check the stability of the structure "+e.getMessage());
                return;
            }
            
            
            //BEFORE SAVING SOLUTION, ADD THE CEROS FROM THE RESTRICTED NODES IN ORDER TO GET THE COMPLETE VECTOR
            SimpleMatrix D = new SimpleMatrix(n,1);
            count=0;
            for(int i=0;i<n;i++){
                if(restrictions[i]){
                    D.set(i,0);
                }else{
                    D.set(i,Result.get(count));
                    count++;
                }
            } 
            
         
            
            LoadCaseSolution loadcasesolution = new LoadCaseSolution(D,lcase);
            loadcasesolution.calcMaxDisp();
            model.solutions.add(loadcasesolution);
        	 
            count1++;
            
        }
         
         
    }
    
  
    
    
    
}
