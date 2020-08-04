/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author GermanSR
 */
public class ScrollableVBox extends VBox{
    
    public ScrollPane getScroll(){
       // String css = this.getClass().getResource("/cssStyles/ScrollPaneTransparent.css").toExternalForm(); 
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(this);
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
      //  scroll.getStylesheets().add(css);
        return scroll;
    }
    
    public static ScrollPane getScroll(AnchorPane pane){
        String css = pane.getClass().getResource("/cssStyles/ScrollPaneTransparent.css").toExternalForm(); 
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(pane);
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        scroll.getStylesheets().add(css);
        return scroll;
    }
    
}
