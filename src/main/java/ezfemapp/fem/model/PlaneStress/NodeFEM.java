package ezfemapp.fem.model.PlaneStress;

import java.util.ArrayList;

/**
 *
 * @author GermanSR
 */
public class NodeFEM {
    
    int index;
    double x;
    double y;
    double z;
    
    boolean restX=false;
    boolean restY=false;
    boolean restZ=false;
    boolean rotX=false;
    boolean rotY=false;
    boolean rotZ=false;
    
    double storedOutputField;
    int connectivityCount=0;
    ArrayList<NodeLoad> loads;
    

    
    public boolean[] getRestrictions(){
        return new boolean[]{restX,restY,restZ,rotX,rotY,rotZ};
    }
    
    public void set2Drestrictions(boolean dx, boolean dy){
        restX=dx;
        restY=dy;
    }
    
    
    public NodeFEM(double x, double y, double z){
        loads = new ArrayList<>();
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public void addLoad(NodeLoad load){
        loads.add(load);
    }
    public double distance(NodeFEM node){
        return Math.sqrt((node.x-x)*(node.x-x) + (node.y-y)*(node.y-y) + (node.z-z)*(node.z-z));
    }
    
    public double distance(double p2X, double p2Y, double p2Z){
        return Math.sqrt((p2X-x)*(p2X-x) + (p2Y-y)*(p2Y-y)+(p2Z-z)*(p2Z-z));
    }
    
    public void setStoredOutputField(double val){
        storedOutputField=val;
    }
    public double getStoredOutputField(){
        return storedOutputField;
    }
    
    public void connectivityCount(){
        connectivityCount++;
    }
    public int connectedElements(){
        return connectivityCount;
    }
    
    public double getLoadX(LoadCase lcase){
        double magnitude=0;
        for(NodeLoad load:loads){
            if(lcase.name.equals(load.lcase.name)){
                magnitude+=load.forceX;
            }
        }
        return magnitude;
    }
    
    public double getLoadY(LoadCase lcase){
         double magnitude=0;
        for(NodeLoad load:loads){
            if(lcase.name.equals(load.lcase.name)){
                magnitude+=load.forceY;
            }
        }
        return magnitude;       
    }
    
    public double getLoadZ(LoadCase lcase){
        double magnitude=0;
        for(NodeLoad load:loads){
            if(lcase.name.equals(load.lcase.name)){
                magnitude+=load.momentZ;
            }
        }
        return magnitude;       
    }
   
    public void restAll(boolean value){
        restX=value;
        restY=value;
        restZ=value;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    
    public boolean hasRestriction(){
       return (restX||restY||restZ);
    }
    
    public int getIndex(){
        return index;
    }
    
    public String printCoords(){
        return ""+x+", "+y;
    }

}
