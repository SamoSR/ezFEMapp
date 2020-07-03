/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author GermanSR
 */
public class ScrollableAnchorPane extends AnchorPane{
    public ScrollPane getScroll(){
        String css = this.getClass().getResource("/cssStyles/ScrollPaneTransparent.css").toExternalForm(); 
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(this);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStylesheets().add(css);
        return scroll;
    }
}
