/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import javafx.scene.input.KeyCode;

/**
 *
 * @author GermanSR
 */
public class KB_Layout_mayus extends KeyBoardLayout{
    
    public static final String ID = "LAYOUT_MINUS";
    
    public KB_Layout_mayus(double screenWidth){
        super(ID);
                
        int s = 5;
        int bw = (int)((screenWidth-(12*s)) / 11);
        int height = (int) (bw*0.6);
        int defaultSize = bw;

        KeyBoardRow keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.Q, "Q"));
        keyRow.row.add(new KeyBoardKey(KeyCode.W, "W"));
        keyRow.row.add(new KeyBoardKey(KeyCode.E, "E"));
        keyRow.row.add(new KeyBoardKey(KeyCode.R, "R"));
        keyRow.row.add(new KeyBoardKey(KeyCode.T, "T"));
        keyRow.row.add(new KeyBoardKey(KeyCode.Y, "Y"));
        keyRow.row.add(new KeyBoardKey(KeyCode.U, "U"));
        keyRow.row.add(new KeyBoardKey(KeyCode.I, "I"));
        keyRow.row.add(new KeyBoardKey(KeyCode.O, "O"));
        keyRow.row.add(new KeyBoardKey(KeyCode.P, "P"));
        //keyRow.row.add(new KeyBoardKey(KeyCode.DELETE, "del"));
        keyRow.createAllWidths(bw);
        keyRow.height = height;
        rows.add(keyRow);
 
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.A, "A"));
        keyRow.row.add(new KeyBoardKey(KeyCode.S, "S"));
        keyRow.row.add(new KeyBoardKey(KeyCode.D, "D"));
        keyRow.row.add(new KeyBoardKey(KeyCode.F, "F"));
        keyRow.row.add(new KeyBoardKey(KeyCode.G, "G"));
        keyRow.row.add(new KeyBoardKey(KeyCode.H, "H"));
        keyRow.row.add(new KeyBoardKey(KeyCode.J, "J"));
        keyRow.row.add(new KeyBoardKey(KeyCode.K, "K"));
        keyRow.row.add(new KeyBoardKey(KeyCode.L, "L"));
       // keyRow.row.add(new KeyBoardKey(KeyCode.BACK_SPACE, "bckspc"));
        keyRow.createWidths(bw,bw,bw,bw,bw,bw,bw,bw,bw,bw*1.5);
        
        keyRow.offset = bw*0.5;
        keyRow.height = height;
        rows.add(keyRow);
        
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.SHIFT, "shift"));
        keyRow.row.add(new KeyBoardKey(KeyCode.Z, "Z"));
        keyRow.row.add(new KeyBoardKey(KeyCode.X, "X"));
        keyRow.row.add(new KeyBoardKey(KeyCode.C, "C"));
        keyRow.row.add(new KeyBoardKey(KeyCode.V, "V"));
        keyRow.row.add(new KeyBoardKey(KeyCode.B, "B"));
        keyRow.row.add(new KeyBoardKey(KeyCode.N, "N"));
        keyRow.row.add(new KeyBoardKey(KeyCode.M, "M"));
        keyRow.row.add(new KeyBoardKey(KeyCode.COMMA, ","));
        keyRow.row.add(new KeyBoardKey(KeyCode.PERIOD, "."));
        keyRow.row.add(new KeyBoardKey(KeyCode.SHIFT, "shift"));
        keyRow.createAllWidths(defaultSize);
        keyRow.height = height;
        rows.add(keyRow);
        
        keyRow = new KeyBoardRow();
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "CLS"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "2"));
        keyRow.row.add(new KeyBoardKey(KeyCode.SPACE, "space"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "3"));
        keyRow.row.add(new KeyBoardKey(KeyCode.UNDEFINED, "4"));
        keyRow.createAllWidths(defaultSize);
        keyRow.createWidths(bw,bw,(bw*7)+(s*6),bw,bw);
        keyRow.height = height;
        rows.add(keyRow);
    }
    
}
