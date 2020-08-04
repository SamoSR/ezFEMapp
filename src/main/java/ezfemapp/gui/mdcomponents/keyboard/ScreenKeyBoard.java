/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;


import ezfemapp.gui.mdcomponents.MyTextField;
import ezfemapp.main.GUImanager;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class ScreenKeyBoard extends VBox{
    
    Pane currentContainer;
    String currentLayout = KB_Layout_minus.ID;
    Text keyBoardCurrentInput;
    double w;
    double h;
  
    
    public ScreenKeyBoard(double screenW, double screenH){
        this.w = screenW;
        this.h = screenH;
    }
    
    
    private void create(){
        
        keyBoardCurrentInput = new Text();
        keyBoardCurrentInput.setFont(new Font(GUImanager.defaultFont,12));
        keyBoardCurrentInput.setText("");
        
        this.getChildren().clear();
        KB_Layout_minus layOutMinus = new KB_Layout_minus(w);
    

        VBox box = new VBox(2);
        TextField tf = new TextField();
        tf.setEditable(false);
        
        tf.textProperty().bind(keyBoardCurrentInput.textProperty());
        box.getChildren().addAll(tf,layOutMinus.contructGUI(this));
        
        this.getChildren().addAll(box);
        
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        
        keyBoardCurrentInput.setText("");
    }
    
    
    public void show(Pane container){
       this.currentContainer = container;
       if(container.getChildren().contains(this)){
           return;
       }
       create();
       this.setVisible(true);
       currentContainer.getChildren().add(this);
     
    }
    
    public void createOnContainer(Pane container){
       this.currentContainer = container;
       create();
       //this.setVisible(false);
       //currentContainer.getChildren().add(this);       
    }

    MyTextField currentTextField;
    public void show(MyTextField tf){
        keyBoardCurrentInput.setText(tf.getText());
        currentTextField=tf;
        tf.textProperty().bind(keyBoardCurrentInput.textProperty());
        if(!currentContainer.getChildren().contains(this)){
            currentContainer.getChildren().add(this); 
        }
        this.setVisible(true);
    }
 
    public MyTextField getCurrentTextField(){
        return currentTextField;
    }
    
    public void hide(){
        currentTextField.textProperty().unbind();
        /*FadeTransition ft = new FadeTransition(Duration.millis(200), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        ft.setOnFinished((e)->{
           this.setVisible(false);
           currentContainer.getChildren().remove(this);
        });*/
        this.setVisible(false);
        currentContainer.getChildren().remove(this);
    }

    
    
}
