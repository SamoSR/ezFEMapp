/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author GermanSR
 */
public class InterfaceUtils {
    
    
    public static final void setAnchors(Node node,double top, double bot, double right, double left){
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bot);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setLeftAnchor(node, left);
    }
    
}
