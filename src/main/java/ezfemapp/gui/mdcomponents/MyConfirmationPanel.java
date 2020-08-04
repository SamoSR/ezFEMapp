/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class MyConfirmationPanel {
    
    String message;
    double w=200;
    double h=100;
    private boolean result = false;
    AnchorPane panel;
    JFXPopup popup;
    
    public MyConfirmationPanel(){

        panel = new AnchorPane();
        panel.setPrefSize(w,h);
        
        double offset = 2;
        double btnWidth= w/2-offset*3;
        double btnHeight= 40;
        double iconSize = 20;
        int textSize = 12;
        String font = GUImanager.defaultFont;
        Color btnColor = Color.TRANSPARENT;
        Color iconColor = GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN);
        Color textColor = Color.BLACK;
        
        //SAVE ICON
        PulseIconButtonCustom btn = new PulseIconButtonCustom(""); 
        //btn.setTextElement("", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.CHECK, iconSize+"px",iconColor); 
        btn.adjustIconPosition(0, 0);
        btn.setEventHandler((event)->{
            result = true;
            popup.hide();
        });
        btn.construct();
        btn.setTranslateX(offset);
        
        
        JFXRippler rip = new  MyRippler(btn,Color.LIGHTGRAY);
        panel.getChildren().add(rip);
        AnchorPane.setLeftAnchor(rip, offset);
        AnchorPane.setBottomAnchor(rip, offset);
        
        
        //SAVE ICON
        btn = new PulseIconButtonCustom(""); 
       // btn.setTextElement("", font, textSize, textColor);
        btn.setBackGroundCustom(new Rectangle(btnWidth,btnHeight,btnColor));
        btn.setIconFontawesome(FontAwesomeIcon.REMOVE, iconSize+"px",iconColor); 
        btn.adjustIconPosition(0, 0);
        btn.setEventHandler((event)->{
            popup.hide();
        });
        btn.construct();
        btn.setTranslateX(offset);
        //box.getChildren().add(new MyRippler(btn,Color.LIGHTGRAY));
        
        rip = new  MyRippler(btn,Color.LIGHTGRAY);
        panel.getChildren().add(rip);
        AnchorPane.setRightAnchor(rip, offset);
        AnchorPane.setBottomAnchor(rip, offset);
      
    }
    
    public void show(GUImanager gui){
        double offsetW = gui.getWidth().doubleValue()/2-w/2;
        double offsetH = gui.getHeight().doubleValue()/2-h/2;
        popup = new JFXPopup();
        popup.setPopupContent(panel);
        popup.show(gui.getRootPane(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,offsetW,offsetH);
    }
    
    public void setMessage(String text){
        double offset = 2;
        double btnHeight= 40;
        Text txt = utilsGUI.create(text, GUImanager.defaultFont, 13, Color.BLACK);
        HBox box = new HBox(txt);
        box.setAlignment(Pos.CENTER);
        AnchorPane.setRightAnchor(box, offset);
        AnchorPane.setTopAnchor(box, offset);
        AnchorPane.setLeftAnchor(box, offset);
        AnchorPane.setBottomAnchor(box, btnHeight+offset);
        panel.getChildren().add(box);
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
