/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import com.jfoenix.controls.JFXRippler;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class MatDesignKeyboard extends AnchorPane{
    

    public MatDesignKeyboard(double width){
        
        int s = 5;
        KeyBoardLayout layoutNormal = new KeyBoardLayout("");
        int bw = (int)((width-(12*s)) / 11);
        int height = (int) (bw*0.6);
        int defaultSize = bw;

        KeyBoardRow keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.Q, "q"));
        keyRow.row.add(new KeyBoardKey(KeyCode.W, "w"));
        keyRow.row.add(new KeyBoardKey(KeyCode.E, "e"));
        keyRow.row.add(new KeyBoardKey(KeyCode.R, "r"));
        keyRow.row.add(new KeyBoardKey(KeyCode.T, "t"));
        keyRow.row.add(new KeyBoardKey(KeyCode.Y, "y"));
        keyRow.row.add(new KeyBoardKey(KeyCode.U, "u"));
        keyRow.row.add(new KeyBoardKey(KeyCode.I, "i"));
        keyRow.row.add(new KeyBoardKey(KeyCode.O, "o"));
        keyRow.row.add(new KeyBoardKey(KeyCode.P, "p"));
        keyRow.row.add(new KeyBoardKey(KeyCode.DELETE, "del"));
        keyRow.createAllWidths(bw);
        keyRow.height = height;
        layoutNormal.rows.add(keyRow);
 
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.A, "a"));
        keyRow.row.add(new KeyBoardKey(KeyCode.S, "s"));
        keyRow.row.add(new KeyBoardKey(KeyCode.D, "d"));
        keyRow.row.add(new KeyBoardKey(KeyCode.F, "f"));
        keyRow.row.add(new KeyBoardKey(KeyCode.G, "g"));
        keyRow.row.add(new KeyBoardKey(KeyCode.H, "h"));
        keyRow.row.add(new KeyBoardKey(KeyCode.J, "j"));
        keyRow.row.add(new KeyBoardKey(KeyCode.K, "k"));
        keyRow.row.add(new KeyBoardKey(KeyCode.L, "l"));
        keyRow.row.add(new KeyBoardKey(KeyCode.BACK_SPACE, "bckspc"));
        keyRow.createWidths(bw,bw,bw,bw,bw,bw,bw,bw,bw,bw*1.5);
        keyRow.offset = bw*0.5;
        keyRow.height = height;
        layoutNormal.rows.add(keyRow);
        
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.SHIFT, "shift"));
        keyRow.row.add(new KeyBoardKey(KeyCode.Z, "z"));
        keyRow.row.add(new KeyBoardKey(KeyCode.X, "x"));
        keyRow.row.add(new KeyBoardKey(KeyCode.C, "c"));
        keyRow.row.add(new KeyBoardKey(KeyCode.V, "v"));
        keyRow.row.add(new KeyBoardKey(KeyCode.B, "b"));
        keyRow.row.add(new KeyBoardKey(KeyCode.N, "n"));
        keyRow.row.add(new KeyBoardKey(KeyCode.M, "m"));
        keyRow.row.add(new KeyBoardKey(KeyCode.COMMA, ","));
        keyRow.row.add(new KeyBoardKey(KeyCode.PERIOD, "."));
        keyRow.row.add(new KeyBoardKey(KeyCode.SHIFT, "shift"));
        keyRow.createAllWidths(defaultSize);
        keyRow.height = height;
        layoutNormal.rows.add(keyRow);
        
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "1"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "2"));
        keyRow.row.add(new KeyBoardKey(KeyCode.SPACE, "space"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "3"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "4"));
        keyRow.createAllWidths(defaultSize);
        keyRow.createWidths(bw,bw,(bw*7)+(s*6),bw,bw);
        keyRow.height = height;
        layoutNormal.rows.add(keyRow);

        this.getChildren().add(layoutNormal.contructGUI(null));
    }
    
    

    

    

    

    
 


    
    
    
    
}
