/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.screen;

import ezfemapp.gui.mdcomponents.SeparatorInvisible;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class AppTopBar extends HBox{
    
    AppScreen parentScreen;
    String text;
    Node leftBox;
    Node rightBox;
    Text textObj;
    Color txtColor;
    private final List<AppBarState> barStates = new ArrayList<>();
    
    public AppTopBar(AppScreen screen){
        text = screen.screenID;
        txtColor =  GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_NAVIGATION_BAR_ICON);
        leftBox = new HBox(5);
        rightBox = new HBox(5);
        textObj  = new Text();
        parentScreen = screen;
        this.setPrefHeight(GUImanager.topBarHeight);
        this.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_NAVIGATION_BAR)+";");
        
        
    }
    
    public void addBarState(AppBarState state){
        barStates.add(state);
    }
    
    public void setState(String stateName){
        for(AppBarState barState:barStates){
            if(barState.stateName.equals(stateName)){
                if(barState.leftBox!=null){
                    leftBox = barState.leftBox;
                }
                if(barState.rightBox!=null){
                    rightBox = barState.rightBox;
                }
               // text = barState.text;
                construct();
                return;
            }
        }
    }
    
    
    public void setLeftBox(Node... btns){
        HBox btnBox = new HBox(5);
        leftBox = btnBox;
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        for(Node btn:btns){
            btnBox.getChildren().add(btn);
        }
    }
    
    public void setRightBox(Node... btns){
        HBox btnBox = new HBox(5);
        rightBox = btnBox;
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        for(Node btn:btns){
            btnBox.getChildren().add(btn);
        }
    }
    
    public void setText(String txt){
        text=txt;
        //textObj.setText(txt);
    }
    
    public void setColorText(Color color){
        txtColor = color;
    }
    
    public void construct(){
        this.getChildren().clear();
        textObj = new Text(text);
        textObj.setFont(Font.font (GUImanager.defaultFont, GUImanager.topBarTextSize));
       // textObj.setStyle("-fx-font-weight: bold");
        textObj.setFill(txtColor);
        this.setAlignment(Pos.CENTER_LEFT);
        

        
        this.getChildren().addAll(leftBox,
                                new SeparatorInvisible(Orientation.HORIZONTAL, 5),
                                textObj,
                                new SeparatorInvisible(Orientation.HORIZONTAL, 50000),
                                rightBox,
                                new SeparatorInvisible(Orientation.HORIZONTAL, 0));
    }

    
    
   
}
