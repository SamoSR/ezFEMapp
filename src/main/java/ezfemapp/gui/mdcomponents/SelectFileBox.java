/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


/**
 *
 * @author GermanSR
 */
public class SelectFileBox {
    
    GUImanager gui;
    boolean singleSelection = true;
    String message;
    double w=250;
    double h=300;
    private boolean result = false;
    AnchorPane panel;
    JFXPopup popup;

    List<JFXCheckBox> checks;
    String[] options;
    
    int treashold = 250;
    Long time1;
    Long time2;
    boolean firstTap = false;
    
    PulseIconButtonCustom btnDel;
    
    
    double offset = 2;
    double btnWidth= w/2-offset*3;
    double btnHeight= 40;
    double iconSize = 20;
    String font = GUImanager.defaultFont;
    Color btnColor = Color.TRANSPARENT;
    Color textColor = Color.BLACK;
    Color iconColor1 = Color.DARKGRAY;
    Color iconColor2 = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN);
    
    
    public SelectFileBox(GUImanager gui, String inputMsg, String[] options, boolean singleSelection){
        this.singleSelection = singleSelection;
        
        this.options=options;
        this.gui = gui;
        panel = new AnchorPane();
        panel.setPrefSize(w,h);

        btnDel = new PulseIconButtonCustom("btnHamburger");
        btnDel.setBackGroundRectangle(32, 32, Color.TRANSPARENT, false);
        btnDel.setIconFontawesome(FontAwesomeIcon.TRASH, GUImanager.topBarBurgerIconSize-3+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_NAVIGATION_BAR_ICON));
        btnDel.setEventHandler((event)->{   
            
            if(firstTap){
                time2 = System.currentTimeMillis();
                Long elapsedTime = time2-time1;
                System.out.println("elapsed: "+elapsedTime);
                if(elapsedTime<treashold){
                    if(eraseFiles()){
                        createItemList();
                        gui.showNotification("Selected files deleted", GUImanager.NOTIFICATION_SUCCESS, 300, 25);
                    }else{
                        createItemList();
                    }
                    //gui.showNotification("Unable to delete files", GUImanager.NOTIFICATION_WARNING, 300, 25);
                }
                firstTap = false;
                return;
            }else{
               time1 = System.currentTimeMillis();
               firstTap = true;
            }

            new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        firstTap = false;
                    }
                }, 
                250 
            );
            gui.showNotification("Double tab the delete icon to delete file", GUImanager.NOTIFICATION_THEMED, 300, 25); 

        });
        btnDel.setVisible(false);
        btnDel.construct();
        
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_MAIN)+";");
        Text txt = utilsGUI.create(inputMsg, font, 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        pane.getChildren().addAll(txt);
        pane.setPrefHeight(40);
        pane.getChildren().add(btnDel);
        btnDel.setTranslateX(100);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        panel.getChildren().add(pane);
        
     
        createItemList();
        
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
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.REMOVE, iconSize+"px",iconColor2); 
        btn.adjustIconPosition(0, 0);
        btn.setEventHandler((event)->{
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
    
    File parentFolder = new File("");
    public void setWorkingFolder(File parent){
        parentFolder = parent;
    }
    
    public boolean eraseFiles(){
        
        List<String> selectedItems = getSelectedItems();
        List<String> eresed = getSelectedItems();
        
        for(String fileName:selectedItems){
            File f = new File(parentFolder, fileName);
            boolean b = f.delete();
            if(b){
               eresed.add(fileName);
            }
        }

        List<String> newList = new ArrayList<>();
        for(String opt:options){
            if(!eresed.contains(opt)){
                newList.add(opt);  
            }
        }
        
        String[] newOptions = newList.toArray(new String[newList.size()]);
        options = newOptions;
        checks.clear();
   
        return true;  
    }
    
    ScrollPane scroll;
    public void createItemList(){

        panel.getChildren().remove(scroll);
        
        VBox items = new VBox();
        checks = new ArrayList<>();
        
        for(String f:options){
            
            JFXCheckBox cb = new JFXCheckBox("");
            cb.setFocusTraversable(false);
            checks.add(cb);
            
            
            
                cb.setOnAction((actionEvent)->{
                    if(singleSelection){
                        if(cb.isSelected()){
                            for(JFXCheckBox p:checks){
                                if(p!=cb){
                                   p.setSelected(false);  
                                }
                            }                   
                        }
                    }
                    if(getSelectedItems().isEmpty()){
                        btnDel.setVisible(false);
                    }else{
                        btnDel.setVisible(true);
                    }
                });
            
            
            HBox itemBox = new HBox();
            TextIconButton btn = new TextIconButton(); 
            btn.setTextElement(f, font, 12, textColor);
            btn.setBackGroundCustom(new Rectangle(w-50,40,btnColor));
            btn.setIconFontawesome(FontAwesomeIcon.FILE, iconSize+"px",iconColor1); 
            btn.setBtnWidth(w-50);
            btn.setBtnHeight(40);
            btn.setEventHandler((event)->{
                cb.fire();
            });
            
            btn.construct();
            itemBox.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
            itemBox.getChildren().add(cb);
            itemBox.setAlignment(Pos.CENTER);
            items.getChildren().add(itemBox);   
        }
        
        scroll = new ScrollPane();
        scroll.setContent(items);
        scroll.fitToHeightProperty().set(true);
        scroll.fitToWidthProperty().set(true);
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/ScrollPaneTransparent.css").toExternalForm(); 
        scroll.getStylesheets().add(css);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
           
        AnchorPane.setRightAnchor(scroll, 5.0);
        AnchorPane.setBottomAnchor(scroll, btnHeight+5);
        AnchorPane.setLeftAnchor(scroll, 5.0);
        AnchorPane.setTopAnchor(scroll, 45.0);
        
        panel.getChildren().add(scroll);
    }
    
    public void show(){
        double offsetW = gui.getWidth().doubleValue()/2-w/2;
        double offsetH = 15;
        popup = new JFXPopup();
        popup.setPopupContent(panel);
        popup.show(gui.getRootPane(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,offsetW,offsetH);
    }

    
    public List<String> getSelectedItems(){
        List<String> selectedOptions = new ArrayList<>();
        int i=0;
        for(String opt:options){
            if(checks.get(i).isSelected()){
                selectedOptions.add(opt);
            }
            i++;
        }
        return selectedOptions;
    }
    
    public JFXPopup getPopUp(){
        return popup;
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
