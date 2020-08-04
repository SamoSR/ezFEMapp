/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class InputBoxWithOptions {
    
    GUImanager gui;
    
    String message;
    double w=250;
    double h=300;
    private boolean result = false;
    AnchorPane panel;
    JFXPopup popup;
   

    JFXTextField inputBox;
    
    public InputBoxWithOptions(GUImanager gui, String title, String inputMsg, String[] options){
               this.gui = gui;
        panel = new AnchorPane();
        panel.setPrefSize(w,h);
        
        double offset = 2;
        double btnWidth= w/2-offset*3;
        double btnHeight= 40;
        double iconSize = 20;
        
        int textSize = 12;
        String font = GUImanager.defaultFont;
        Color btnColor = Color.TRANSPARENT;
        Color iconColor1 = Color.DARKGRAY;
        Color iconColor2 = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN);
        Color textColor = Color.BLACK;
        
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_MAIN)+";");
        Text txt = utilsGUI.create(title, font, 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        pane.getChildren().addAll(txt);
        pane.setPrefHeight(40);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        panel.getChildren().add(pane);
        
       
        inputBox=new JFXTextField();
        inputBox.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm()); 
        inputBox.setOnKeyPressed((e)->{
            if(e.getCode()==KeyCode.ENTER){
                panel.requestFocus();
            }
        });
        
        HBox hbox1 = new HBox(5);
        hbox1.setAlignment(Pos.CENTER_LEFT);
        
        Text inputText = utilsGUI.create(inputMsg, font, textSize, textColor);
        
        hbox1.getChildren().addAll(inputText,inputBox);
        HBox.setHgrow(inputBox, Priority.ALWAYS);
        
        AnchorPane.setTopAnchor(hbox1, 45.0);
        AnchorPane.setRightAnchor(hbox1, 5.0);
        AnchorPane.setLeftAnchor(hbox1, 5.0);
        panel.getChildren().add(hbox1);
        
        VBox items = new VBox();
        for(String f:options){
            TextIconButton btn = new TextIconButton(); 
            btn.setTextElement(f, font, textSize, textColor);
            btn.setBackGroundCustom(new Rectangle(w-30,40,btnColor));
            btn.setIconFontawesome(FontAwesomeIcon.FILE, iconSize+"px",iconColor1); 
            btn.setBtnWidth(w-30);
            btn.setBtnHeight(40);
            btn.setEventHandler((event)->{
                inputBox.setText(f);     
            });
            btn.construct();
            items.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        }
        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(items);
        scroll.fitToHeightProperty().set(true);
        scroll.fitToWidthProperty().set(true);
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/ScrollPaneTransparent.css").toExternalForm(); 
        scroll.getStylesheets().add(css);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        panel.getChildren().add(scroll);
        AnchorPane.setRightAnchor(scroll, 5.0);
        AnchorPane.setBottomAnchor(scroll, btnHeight+5);
        AnchorPane.setLeftAnchor(scroll, 5.0);
        AnchorPane.setTopAnchor(scroll, 80.0+5);
        
        HBox boxBtns = new HBox();
        PulseIconButtonCustom btn = new PulseIconButtonCustom(""); 
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.CHECK, iconSize+"px",iconColor2); 
        btn.adjustIconPosition(0, 0);
        btn.setEventHandler((event)->{
            result = true;
            popup.hide();
        });
        btn.construct();
        
        JFXRippler rip = new  MyRippler(btn,Color.LIGHTGRAY);
        boxBtns.getChildren().addAll(rip);
        
        btn = new PulseIconButtonCustom(""); 
       // btn.setTextElement("", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.REMOVE, iconSize+"px",iconColor2); 
        btn.adjustIconPosition(0, 0);
        btn.setEventHandler((event)->{
            result = false;
            popup.hide();
        });
        btn.construct();
        
        rip = new  MyRippler(btn,Color.LIGHTGRAY);
        boxBtns.getChildren().addAll(rip);
        
        panel.getChildren().add(boxBtns);
        AnchorPane.setRightAnchor(boxBtns, 5.0);
        AnchorPane.setLeftAnchor(boxBtns, 5.0);
        AnchorPane.setBottomAnchor(boxBtns, 5.0);

        
        
    }
    
    public void show(){
        double offsetW = gui.getWidth().doubleValue()/2-w/2;
        double offsetH = 15;
        popup = new JFXPopup();
        popup.setPopupContent(panel);
        popup.show(gui.getRootPane(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,offsetW,offsetH);
    }

    
    public JFXPopup getPopUp(){
        return popup;
    }
    
    public String getTextResult(){
        return inputBox.getText();
    }
    
    public double getBtnWidth(){
        return w;
    }
    public double getBtnHeight(){
        return h;
    }
    public boolean getResult(){
        return result;
    }

    
    
    
}
