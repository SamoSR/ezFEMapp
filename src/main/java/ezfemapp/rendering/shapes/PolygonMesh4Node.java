/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.shapes;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.shape.TriangleMesh;
import org.fxyz3d.geometry.Face3;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.TexturedMesh;

/**
 *
 * @author GermanSR
 */
public class PolygonMesh4Node extends TexturedMesh{
    
    Point3D[] vertices;
    
    public PolygonMesh4Node(Point3D... points){
        this.vertices=points;
        updateMesh();
    }
    
    public PolygonMesh4Node(){
       vertices = new Point3D[4];
    }
    
    public void setPoint(int index, double x, double y, double z){
        vertices[index] = new Point3D((float)x,(float)y,(float)z);
    }
    
    public void setPoint(int index, double x, double y, double z,double f){
        vertices[index] = new Point3D((float)x,(float)y,(float)z,(float)f);
    }
    
    public PolygonMesh4Node(ArrayList<Point3D> points){
        vertices = new Point3D[points.size()];
        for(int i=0;i<points.size();i++){
            vertices[i] = points.get(i);
        }
        updateMesh();
    }
    
    public Point2D getPoint2D(int index){
        return new Point2D(vertices[index].x,vertices[index].y);
    }
   
    
   @Override
    public final void updateMesh(){   
        setMesh(null);
        mesh = createGeometry();
        setMesh(mesh);    
    }
    
    
    
    private TriangleMesh createGeometry(){
        
        listVertices.clear();
        listTextures.clear();
        listFaces.clear();
        
        listVertices.add(vertices[0]);
        listVertices.add(vertices[1]);
        listVertices.add(vertices[2]);
        listVertices.add(vertices[3]);
        
        createTexCoords(1, 1);
        
        listFaces.add(new Face3(2,3,0));    
        listFaces.add(new Face3(0,1,2));
        
        for(int i=0;i<listFaces.size();i++){
           listTextures.add(new Face3(0,0,0));
        }
 
        return createMesh();
    }
    
}