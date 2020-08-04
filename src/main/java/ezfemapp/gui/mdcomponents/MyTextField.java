/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXTextField;
import ezfemapp.main.GUImanager;
import javafx.beans.value.ObservableValue;


/**
 *
 * @author GermanSR
 */
public class MyTextField extends JFXTextField{
     
    public MyTextField(String text){
        this.setText(text);
        initialize();
    }
    
    private void initialize(){
        this.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm()); 
        this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            if(newValue){
                GUImanager.keyboard.show(this);
            } else{
                GUImanager.keyboard.hide();
            }    
        });
    }
}
