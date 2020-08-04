/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.rendering.canvas;

import ezfemapp.blockProject.Block;
import ezfemapp.blockProject.BlockDistLoad;
import ezfemapp.blockProject.SupportBlock;
import ezfemapp.rendering.shapes.PolygonMesh4Node;
import ezfemapp.rendering.shapes.ShapeDrawer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.fxyz3d.geometry.Point3D;
import serializableApp.utils.NumericUtils;

/**
 *
 * @author GermanSR
 */
public class BlockRenderer {
    
        
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node createSupportGeometry(SupportBlock b, double gridRenderSize, double[] u){
        int row = b.getRow();
        int col = b.getColumn();
    
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize+u[0],row*gridRenderSize+u[1]);
        Point2D p2 = new Point2D(col*gridRenderSize+u[2],row*gridRenderSize+gridRenderSize+u[3]);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize+u[4],row*gridRenderSize+gridRenderSize+u[5]);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize+u[6],row*gridRenderSize+u[7]);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setStroke(Color.TRANSPARENT);
        polygonGeometry.setFill(Color.TRANSPARENT);
        blockGeometry.getChildren().add(polygonGeometry);
        
        double r = gridRenderSize/10;
        double positions[]= new double[]{-gridRenderSize/2+r*1.5,0,gridRenderSize/2-r*1.5};
        Point2D midPoint = p1.midpoint(p3);
        
        if(b.getSupportType().equals(SupportBlock.SUPPORT_FIXED)){
            double sizeReduction = 12;
            Rectangle re = new Rectangle();
            re.setFill(Color.TRANSPARENT);
            re.setStroke(Color.BLACK);
            re.setStrokeWidth(2);
            re.setWidth(gridRenderSize);
            re.setHeight(gridRenderSize);
            re.setTranslateX(p1.getX());
            re.setTranslateY(p1.getY());
            blockGeometry.getChildren().add(re);   
            re = new Rectangle();
            re.setFill(Color.BLACK);
            re.setWidth(gridRenderSize-sizeReduction);
            re.setHeight(gridRenderSize-sizeReduction);
            re.setTranslateX(p1.getX()+sizeReduction/2);
            re.setTranslateY(p1.getY()+sizeReduction/2);
            re.setId(b.getID());
            blockGeometry.getChildren().add(re);
        }else if(b.getSupportType().equals(SupportBlock.SUPPORT_PINNED_HORZ)){

            for(int i=0;i<3;i++){
                Circle c = new Circle();
                c.setCenterX(midPoint.getX()-gridRenderSize/2+r*1.0+4);
                c.setCenterY(midPoint.getY()+positions[i]);
                c.setRadius(r);
                c.setFill(Color.BLACK);
                blockGeometry.getChildren().add(c);
                c = new Circle();
                c.setCenterX(midPoint.getX()+gridRenderSize/2-r*1.0-4);
                c.setCenterY(midPoint.getY()+positions[i]);
                c.setRadius(r);
                c.setFill(Color.BLACK);
                c.setId(b.getID());
                blockGeometry.getChildren().add(c);
            }
            
            Line l1 = new Line();
            l1.setStartX(p1.getX()+2);
            l1.setEndX(p2.getX()+2);
            l1.setStartY(p1.getY());
            l1.setEndY(p2.getY());
            l1.setStrokeWidth(4);
            l1.setStroke(Color.BLACK);
            blockGeometry.getChildren().add(l1);
            l1 = new Line();
            l1.setStartX(p3.getX()-2);
            l1.setEndX(p4.getX()-2);
            l1.setStartY(p3.getY());
            l1.setEndY(p4.getY());
            l1.setStrokeWidth(4);
            l1.setStroke(Color.BLACK);
            blockGeometry.getChildren().add(l1);
            
        }else if(b.getSupportType().equals(SupportBlock.SUPPORT_PINNED_VERT)){
            for(int i=0;i<3;i++){
                Circle c = new Circle();
                c.setCenterX(midPoint.getX()+positions[i]);
                c.setCenterY(midPoint.getY()-gridRenderSize/2+r+4);
                c.setRadius(r);
                c.setFill(Color.BLACK);
                blockGeometry.getChildren().add(c);
                c = new Circle();
                c.setCenterX(midPoint.getX()+positions[i]);
                c.setCenterY(midPoint.getY()+gridRenderSize/2-r-4);
                c.setRadius(r);
                c.setFill(Color.BLACK);
                c.setId(b.getID());
                blockGeometry.getChildren().add(c);
            } 
            
            Line l1 = new Line();
            l1.setStartX(p1.getX());
            l1.setEndX(p4.getX());
            l1.setStartY(p1.getY()+2);
            l1.setEndY(p4.getY()+2);
            l1.setStrokeWidth(4);
            l1.setStroke(Color.BLACK);
            blockGeometry.getChildren().add(l1);
            l1 = new Line();
            l1.setStartX(p2.getX());
            l1.setEndX(p3.getX());
            l1.setStartY(p2.getY()-2);
            l1.setEndY(p3.getY()-2);
            l1.setStrokeWidth(4);
            l1.setStroke(Color.BLACK);
            blockGeometry.getChildren().add(l1);     
        }

        blockGeometry.setId(b.getID());
        return blockGeometry;
    }
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node createBlockGeometry(Block b, double gridRenderSize ){
        int row = b.getRow();
        int col = b.getColumn();
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setFill(b.getMaterial().getColor().getColorFX());
        polygonGeometry.setStroke(Color.BLACK);
        blockGeometry.getChildren().add(polygonGeometry);
        
        Point2D[] points = new Point2D[]{p1,p2,p3,p4};
        for(Point2D p:points){
            Circle c = new Circle();
            c.setCenterX(p.getX());
            c.setCenterY(p.getY());
            c.setRadius(gridRenderSize/2/10);
            c.setFill(Color.BLACK);
            blockGeometry.getChildren().add(c);
        } 
        return blockGeometry;
    }
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node blockContour(Block b, double gridRenderSize,Color color, double width ){
        int row = b.getRow();
        int col = b.getColumn();
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setFill(Color.TRANSPARENT);
        polygonGeometry.setStroke(color);
        polygonGeometry.setStrokeWidth(width);
        blockGeometry.getChildren().add(polygonGeometry);
        return blockGeometry;
    }
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node createBlockGeometry_Polygon3D(Block b, float gridRenderSize ){
        Group blockGeometry = new Group();
        
        PolygonMesh4Node poly = new PolygonMesh4Node();
        int c = b.getColumn();
        int r = b.getRow();
        
        Point3D p1 = new Point3D(c*gridRenderSize,r*gridRenderSize,0,1);
        Point3D p2 = new Point3D(c*gridRenderSize,r*gridRenderSize+gridRenderSize,0,1);
        Point3D p3 = new Point3D(c*gridRenderSize+gridRenderSize,r*gridRenderSize+gridRenderSize,0,1);
        Point3D p4 = new Point3D(c*gridRenderSize+gridRenderSize,r*gridRenderSize,0,1);

        poly.setPoint(0,p1);
        poly.setPoint(1,p2);
        poly.setPoint(2,p3);
        poly.setPoint(3,p4);
        poly.updateMesh();
        
        poly.setDiffuseColor(Color.TRANSPARENT);
        poly.setBlendMode(BlendMode.SCREEN);

       //System.out.println("material: "+b.getMaterial().getID());
        
        poly.setMaterial(new PhongMaterial(b.getMaterial().getColor().getColorFX()));
        blockGeometry.getChildren().add(poly);
        
        Point2D[] thePoints = new Point2D[]{poly.getPoint2D(0),poly.getPoint2D(1),poly.getPoint2D(2),poly.getPoint2D(3)};
        
        for(Point2D p:thePoints){
            Circle node = new Circle();
            node.setCenterX(p.getX());
            node.setCenterY(p.getY());
            node.setRadius(gridRenderSize/2/7);
            node.setFill(Color.BLACK);
            blockGeometry.getChildren().add(node);
        }
        
        blockGeometry.getChildren().add(ShapeDrawer.drawPlane(p1, p2, p3, p4, Color.BLACK));
        blockGeometry.setId(b.getID());
        return blockGeometry;
    }
    
    public static Node createFirstTouchGeometry(int selectedRow, int selectedCol, double gridRenderSize, Color color ){
        int row = selectedRow;
        int col = selectedCol;
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Circle c = new Circle(gridRenderSize/4);
        c.setCenterX(p1.getX()+gridRenderSize/2);
        c.setCenterY(p1.getY()+gridRenderSize/2);
        c.setFill(Color.RED);
        blockGeometry.getChildren().add(c);
        return blockGeometry;
    }
    
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node createBlockGeometryDeformed(Block b, double gridRenderSize, double[] u){
        int row = b.getRow();
        int col = b.getColumn();
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize+u[0],row*gridRenderSize+u[1]);
        Point2D p2 = new Point2D(col*gridRenderSize+u[2],row*gridRenderSize+gridRenderSize+u[3]);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize+u[4],row*gridRenderSize+gridRenderSize+u[5]);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize+u[6],row*gridRenderSize+u[7]);
          Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setFill(b.getMaterial().getColor().getColorFX());
        polygonGeometry.setStroke(Color.BLACK);
        blockGeometry.getChildren().add(polygonGeometry);
        
        Point2D[] points = new Point2D[]{p1,p2,p3,p4};
        for(Point2D p:points){
            Circle c = new Circle();
            c.setCenterX(p.getX());
            c.setCenterY(p.getY());
            c.setRadius(gridRenderSize/2/7);
            c.setFill(Color.BLACK);
            blockGeometry.getChildren().add(c);
        } 
        return blockGeometry;
    }
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node blockCountourDeformed(Block b, double gridRenderSize, double[] u, Color color, double width){
        int row = b.getRow();
        int col = b.getColumn();
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize+u[0],row*gridRenderSize+u[1]);
        Point2D p2 = new Point2D(col*gridRenderSize+u[2],row*gridRenderSize+gridRenderSize+u[3]);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize+u[4],row*gridRenderSize+gridRenderSize+u[5]);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize+u[6],row*gridRenderSize+u[7]);
          Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setFill(Color.TRANSPARENT);
        polygonGeometry.setStroke(color);
        polygonGeometry.setStrokeWidth(width);
        blockGeometry.getChildren().add(polygonGeometry);
        return blockGeometry;
    }
    
    public static Node createBlockGeometryDeformed_Polygon3D(Block b, double size, double[] u){
 

        Group blockGeometry = new Group();
        
        PolygonMesh4Node poly = new PolygonMesh4Node();
        int c = b.getColumn();
        int r = b.getRow();
        
        
        poly.setPoint(0,c*size+u[0],r*size+u[1],0,0);
        poly.setPoint(1,c*size+u[2],r*size+size+u[3],0,0);
        poly.setPoint(2,c*size+size+u[4],r*size+size+u[5],0,0);
        poly.setPoint(3,c*size+size+u[6],r*size+u[7],0,0);
        poly.updateMesh();
        
        Point3D p1 = new Point3D((float)(c*size+u[0]),(float)(r*size+u[1]),0,0);
        Point3D p2 = new Point3D((float)(c*size+u[2]),(float)(r*size+size+u[3]),0,0);
        Point3D p3 = new Point3D((float)(c*size+size+u[4]),(float)(r*size+size+u[5]),0,0);
        Point3D p4 = new Point3D((float)(c*size+size+u[6]),(float)(r*size+u[7]),0,0);

        poly.setPoint(0,p1);
        poly.setPoint(1,p2);
        poly.setPoint(2,p3);
        poly.setPoint(3,p4);
        
        
        poly.setId(b.getID());
        
        poly.setDiffuseColor(Color.TRANSPARENT);
        poly.setBlendMode(BlendMode.SCREEN);
       

        poly.setMaterial(new PhongMaterial(b.getMaterial().getColor().getColorFX()));
        blockGeometry.getChildren().add(poly);
        Point2D[] thePoints = new Point2D[]{poly.getPoint2D(0),poly.getPoint2D(1),poly.getPoint2D(2),poly.getPoint2D(3)};
        
        for(Point2D p:thePoints){
            Circle node = new Circle();
            node.setCenterX(p.getX());
            node.setCenterY(p.getY());
            node.setRadius(size/2/7);
            node.setFill(Color.BLACK);
            blockGeometry.getChildren().add(node);
        }
        
        blockGeometry.getChildren().add(ShapeDrawer.drawPlane(p1, p2, p3, p4, Color.BLACK));
        return blockGeometry;
    }
    
    public static Node createDeformedElementFrame(Block b, double gridRenderSize, double[] u){
 
        Group blockGeometry = new Group();

        int row = b.getRow();
        int col = b.getColumn();

        Point2D p1 = new Point2D(col*gridRenderSize+u[0],row*gridRenderSize+u[1]);
        Point2D p2 = new Point2D(col*gridRenderSize+u[2],row*gridRenderSize+gridRenderSize+u[3]);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize+u[4],row*gridRenderSize+gridRenderSize+u[5]);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize+u[6],row*gridRenderSize+u[7]);

        Color color = Color.BLACK;
        double w = 3;

        blockGeometry.getChildren().add(drawLine(p1,p2,color,w));
        blockGeometry.getChildren().add(drawLine(p2,p3,color,w));
        blockGeometry.getChildren().add(drawLine(p3,p4,color,w));
        blockGeometry.getChildren().add(drawLine(p4,p1,color,w));
        
        return blockGeometry;
    }
    
    public static Line drawLine(Point2D p1, Point2D p2, Color color, double w){
        Line l = new Line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
        l.setStrokeWidth(w);
        l.setStroke(color);
        return l;
    }
    
    //P1- TOP-LEFT
    //P2- BOT-LEFT
    //P3- BOT-RIGHT
    //P4- TOP_RIGHT
    public static Node createLoadGeometry(BlockDistLoad b, double gridRenderSize ){
        int row = b.getRow();
        int col = b.getColumn();
        
        Group blockGeometry = new Group();
        Point2D p1 = new Point2D(col*gridRenderSize,row*gridRenderSize);
        Point2D p2 = new Point2D(col*gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p3 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize+gridRenderSize);
        Point2D p4 = new Point2D(col*gridRenderSize+gridRenderSize,row*gridRenderSize);
        Polygon polygonGeometry = new Polygon();
        polygonGeometry.getPoints().addAll(p1.getX(),p1.getY(),
                                           p2.getX(),p2.getY(),
                                           p3.getX(),p3.getY(),
                                           p4.getX(),p4.getY());
        polygonGeometry.setId(b.getID());
        polygonGeometry.setFill(b.getLoadCase().getColor().getColorFX());
        polygonGeometry.setStroke(Color.BLACK);
       
        
        Group g = ShapeDrawer.drawArrowLoad(b.getLoadCase().getColor().getColorFX(), gridRenderSize);
        switch(b.getDirection()){
            case BlockDistLoad.LOAD_DIRECTION_UP:
                g.setTranslateX(p1.getX()+gridRenderSize*0.50);
                g.setTranslateY(p1.getY()-(gridRenderSize/2));
                g.setRotate(180);
            break;
            case BlockDistLoad.LOAD_DIRECTION_RIGHT:
                g.setTranslateX(p1.getX()+gridRenderSize*0.75);
                g.setTranslateY(p1.getY()-gridRenderSize/4);
                g.setRotate(-90);
            break;
            case BlockDistLoad.LOAD_DIRECTION_DOWN:
                g.setTranslateX(p1.getX()+gridRenderSize*0.50);
                g.setTranslateY(p1.getY());
                g.setRotate(0);
            break;
            case BlockDistLoad.LOAD_DIRECTION_LEFT:
                g.setTranslateX(p1.getX()+gridRenderSize*0.25);
                g.setTranslateY(p1.getY()-gridRenderSize/4);
                g.setRotate(90);
            break;
        }
        blockGeometry.getChildren().add(g);
        return blockGeometry;
    }
    
    
    public static Node createBlockGeometryDeformed_ColorMap(Block b, double size, double[] u, double[] colors, double min, double max, boolean nodes){
        double valueNode1 = colors[0];
        double valueNode2 = colors[1];
        double valueNode3 = colors[2];
        double valueNode4 = colors[3];

        Group blockGeometry = new Group();
        
        PolygonMesh4Node poly = new PolygonMesh4Node();
        int c = b.getColumn();
        int r = b.getRow();
        poly.setPoint(0,c*size+u[0],r*size+u[1],0,valueNode1);
        poly.setPoint(1,c*size+u[2],r*size+size+u[3],0,valueNode2);
        poly.setPoint(2,c*size+size+u[4],r*size+size+u[5],0,valueNode3);
        poly.setPoint(3,c*size+size+u[6],r*size+u[7],0,valueNode4);
        poly.updateMesh();
        poly.setId(b.getID());
        
        poly.setDiffuseColor(Color.TRANSPARENT);
        poly.setBlendMode(BlendMode.SCREEN);
       
        poly.setTextureModeVertices3D(255, p -> 1-NumericUtils.normalize(p.f,min,max),0, 1.5 ); 
       
        blockGeometry.getChildren().add(poly);
        
        Point2D[] thePoints = new Point2D[]{poly.getPoint2D(0),poly.getPoint2D(1),poly.getPoint2D(2),poly.getPoint2D(3)};
        
        if(nodes){
            for(Point2D p:thePoints){
                Circle node = new Circle();
                node.setCenterX(p.getX());
                node.setCenterY(p.getY());
                node.setRadius(size/2/7);
                node.setFill(Color.BLACK);
                blockGeometry.getChildren().add(node);
            }
        }
       
        return blockGeometry;
    }
    
    
    
}
