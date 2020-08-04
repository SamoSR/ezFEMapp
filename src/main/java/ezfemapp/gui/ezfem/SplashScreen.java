/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.SeparatorInvisible;
import ezfemapp.gui.mdcomponents.keyboard.ScreenKeyBoard;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author GermanSR
 */
public class SplashScreen extends AppScreen{
    
    public static String ID = "Splash";

    public SplashScreen(GUImanager gui){
        super(ID,gui);

        double logoWidth = 512;
        
        getCentralPane().setStyle("-fx-background-color: white;");
        getAppBar().setStyle("-fx-background-color: white;");
        getAppBar().construct();
         
        BorderPane bp = new BorderPane();
        getCentralPane().getChildren().add(bp);
        AnchorPane.setBottomAnchor(bp, 0.0);
        AnchorPane.setTopAnchor(bp, 0.0);
        AnchorPane.setLeftAnchor(bp, 0.0);
        AnchorPane.setRightAnchor(bp, 0.0);
        
        //String cssSlider = this.getClass().getResource("/cssStyles/WhiteTheme/MySlider.css").toExternalForm(); 
        String iconPath = this.getClass().getResource("/icons/ezFEM1024x500.png").toExternalForm(); 
        try {
            //FileInputStream input = new FileInputStream("src/logo300x100.png");
            Image image = new Image(iconPath);
            ImageView imageView = new ImageView(image); 
            imageView.setFitHeight(250);
            imageView.setFitWidth(logoWidth);
            //getCentralPane().getChildren().add(imageView);
            bp.setCenter(imageView);
        } catch (Exception e) {
            System.out.println("FAILLED TO LOAD IAMGE");
        }
     
        PulseIconButtonCustom btnStart = new PulseIconButtonCustom("btnClearBlocks"); 
        btnStart.setBackGroundCustom(new Rectangle(logoWidth,35,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnStart.setIconCustom(utilsGUI.create("Start Modeling!", GUImanager.defaultFont, 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
        btnStart.setEventHandler((event)->{
             gui.loadScreen(ModelingScreen.ID);
        });
        btnStart.construct();
   
        PulseIconButtonCustom btnTutorial = new PulseIconButtonCustom("btnClearBlocks"); 
        btnTutorial.setBackGroundCustom(new Rectangle(logoWidth,35,GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)));
        btnTutorial.setIconCustom(utilsGUI.create("Tutorial", GUImanager.defaultFont, 14, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT))); 
        btnTutorial.setEventHandler((event)->{
           // getGUI().showNotification("This is an error message",GUImanager.NOTIFICATION_WARNING,300,25);
           //ConfirmationPanelText popup = new ConfirmationPanelText(gui, "File name: ");
           //popup.show();
           //SelectOptionsBox popup = new SelectOptionsBox(gui, "File name ",new String[]{"op1","op2","op2","op2","op2","op2","op2","op2","op2"},false);
           //popup.show();
           //InputBoxWithOptions popup = new InputBoxWithOptions(gui, "Save File ","Name: ",new String[]{"op1","op2","op2","op2","op2","op2","op2","op2","op2"});
           //popup.show();
           //SelectFileBox popup2 = new SelectFileBox(gui, "Open File ",new String[]{"op1","op2","op3","op4","op5","op6","op7","op8","op9"},false);
           //popup2.show();
           //ListPickerPopup pane = new ListPickerPopup(gui, new String[]{"file1","file2","file3"});
           //pane.show();      
           
           
           gui.showNotification("Section under construction", GUImanager.NOTIFICATION_YELLOWALERT, 250, 30);
        });
        btnTutorial.construct();

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(Double.MAX_VALUE);
        box.getChildren().addAll(btnStart);
        box.getChildren().add(new SeparatorInvisible(Orientation.VERTICAL, 10));
        bp.setBottom(box);
    }
    
    @Override
    public void loadScreen() {
        
    }

    @Override
    public void update(String... args) {
       
    }
    
    
}
