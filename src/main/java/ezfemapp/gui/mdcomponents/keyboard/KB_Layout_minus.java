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
public class KB_Layout_minus extends KeyBoardLayout{
    
    public static final String ID = "LAYOUT_MINUS";
    
    public KB_Layout_minus(double screenWidth){
        super(ID);
                
        int s = 5;
        int bw = (int)((screenWidth-(12*s)) / 10);
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
        //keyRow.row.add(new KeyBoardKey(KeyCode.DELETE, "del"));
        keyRow.createAllWidths(bw);
        keyRow.height = height;
        rows.add(keyRow);
 
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
       // keyRow.row.add(new KeyBoardKey(KeyCode.BACK_SPACE, "bckspc"));
        keyRow.createWidths(bw,bw,bw,bw,bw,bw,bw,bw,bw,bw*1.5);
        
        keyRow.offset = bw*0.5;
        keyRow.height = height;
        rows.add(keyRow);
        
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
        //keyRow.row.add(new KeyBoardKey(KeyCode.SHIFT, "shift"));
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
        keyRow.createWidths(bw,bw,(bw*5)+(s*4),bw,bw);
        keyRow.height = height;
        rows.add(keyRow);
    }
    
}
