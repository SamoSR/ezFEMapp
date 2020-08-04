/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.documentation;

import ezfemapp.gui.mdcomponents.SeparatorInvisible;
import ezfemapp.gui.mdcomponents.utilsGUI;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class DocumentationItem {
    
    String itemName;
    List<DocumentationItem> childs = new ArrayList<>();
    List<Node> items = new ArrayList<>();
    
    public DocumentationItem(String name){
        this.itemName = name;
    }
    
    public void addChild(DocumentationItem item){
        childs.add(item);
    }
    public void addChild(DocumentationItem... items){
        for(DocumentationItem i:items){
             childs.add(i);
        }
    }
    
    public void addItems(Node... addItems){
        for(Node item:addItems){
             items.add(item);
        }
    }
    
    @Override
    public String toString(){
        return itemName;
    }
    
    public TreeItem<DocumentationItem> createTree(){
        TreeItem<DocumentationItem> root = new TreeItem<>(this);
        
        for(DocumentationItem item:childs){
            root.getChildren().add(item.createTree());
        }
        return root;    
    }
    
    public Node getDisplay(){ 
        
        if(items.isEmpty()){
            System.out.println("is empty");
            return new Group();
        }
    
        System.out.println(""+itemName);
        
        VBox g = new VBox();

        for(Node node:items){
            g.getChildren().add(node);
        }
        
        AnchorPane.setBottomAnchor(g, 0.0);
        AnchorPane.setTopAnchor(g, 0.0);
        AnchorPane.setLeftAnchor(g, 0.0);
        AnchorPane.setRightAnchor(g, 0.0);

      
        return g;
    }
    
    
            
}
