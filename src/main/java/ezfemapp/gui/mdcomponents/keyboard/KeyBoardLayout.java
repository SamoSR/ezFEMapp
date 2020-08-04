/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;


import ezfemapp.gui.mdcomponents.SeparatorInvisible;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author GermanSR
 */
public class KeyBoardLayout {
      
    String id;    
    List<KeyBoardRow> rows = new ArrayList<>();
    double spacing = 5;
    
    public KeyBoardLayout(String name){
        this.id=name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    public VBox contructGUI(ScreenKeyBoard kbRef){

        VBox container = new VBox(spacing);
        container.setStyle("-fx-background-color: whitesmoke;");
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0); 

        int rownum=0;
        for(KeyBoardRow keyBoardRow:rows){

            HBox box = new HBox(spacing);
            if(keyBoardRow.offset>0){ // (for some reason, separator with 0 width still adds some spacing)
                SeparatorInvisible offset = new SeparatorInvisible(Orientation.HORIZONTAL, keyBoardRow.offset);
                box.getChildren().add(offset);
            }
            
            //marging first row
            if(rownum==0){
                box.setPadding(new Insets(spacing, 0, 0, spacing));
            //marging last row
            }else if(rownum==rows.size()-1){
                box.setPadding(new Insets(0, 0, spacing, spacing));
            //marging middle rows
            }else{
                box.setPadding(new Insets(0, 0, 0, spacing));
            }

            int count=0;
            for(KeyBoardKey key:keyBoardRow.row){

                double w = keyBoardRow.widths.get(count++);
                double h = keyBoardRow.height;
                KeyBoardButton btn = new KeyBoardButton(key,w,h);
                btn.setListener(kbRef);
                box.getChildren().add(btn.getRippler());

            }
            container.getChildren().add(box);
            rownum++;
        }
        return container;
    }
        
    
}
