/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.gui.mdcomponents.HtmlText;
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.BasicApp;
import ezfemapp.main.GUImanager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


/**
 * @author GermanSR
 */
public class AboutScreen extends AppScreen{
    
    public static String ID = "About ezFEM";
    
    public AboutScreen(GUImanager gui){
        super(ID,gui);
        
         //BUTTON GO BACK
        PulseIconButtonCustom btnBack = new PulseIconButtonCustom("btnGoBack");
        btnBack.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnBack.setIconFontawesome(FontAwesomeIcon.CHEVRON_LEFT, GUImanager.topBarBurgerIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnBack.setEventHandler((event)->{
            gui.loadScreen(ModelingScreen.ID);
            
        });
        btnBack.construct();
        getAppBar().setLeftBox(btnBack);
        getAppBar().construct();
        
        getAppBar().construct();
        
        String iconPath = this.getClass().getResource("/icons/zf250x250.png").toExternalForm(); 
        HtmlText htmlText = new HtmlText();
        htmlText.addLine(HtmlText.bold(BasicApp.APP_NAME));
        htmlText.addLine(HtmlText.bold(BasicApp.APP_VERSION));
        htmlText.breakLine();
        htmlText.addLine(HtmlText.bold("Version notes: ")+"First beta version released for testing in multiple devices. Some errors, bugs and instabilities are expected, be patient :)");
        htmlText.breakLine();
        htmlText.addLine(HtmlText.bold("Description: ")+"ezFEM is a simple Finite Element Method application where you can create 2D structural models with a few clicks/touches. It has a powerful solver that analyzes the structure in a few seconds and a modern user-friendly interface to visualize the results with high-quality graphics and color fields.");
        htmlText.breakLine();
        htmlText.addLine(HtmlText.bold("Goal: ")+"The goal of this application is to assist the user in the process of learning and understanding the basic principles of structural analysis and Finite Element Modeling using a highly interactive approach within an user-friendly environment.");
        htmlText.breakLine();
        htmlText.addLine(HtmlText.bold("About Developer: ")+"M.Sc. Germán Solórzano Ramírez is a passionate Civil Engineering focused in the application of modern computational techniques such as "
                + "Optimization Methods and Artificial Intelligence into the Structural Engineering field. He is currently a Ph.D. candidate at the Oslo Metropolitan University in the Department of Civil Engineering and Energy Technology.");      
        htmlText.breakLine();
        htmlText.addLine(HtmlText.bold("Contact: ")+"germanso@oslomet.no / ezfemapp@gmail.com");
        
        
        //htmlText.addLine("<img src=\""+iconPath+"\" width=\"100\" height=\"100\">");
        
      
        htmlText.finish();
   

        
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(htmlText.getText());
        getCentralPane().getChildren().add(webView);
        
        
       
        
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);
        
    }

   
   
   
  
    @Override
    public void loadScreen() {
     
    }

    @Override
    public void update(String... args) {
      
    }

    
    
}
