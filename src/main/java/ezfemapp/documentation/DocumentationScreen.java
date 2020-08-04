/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.documentation;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import ezfemapp.gui.ezfem.ModelingScreen;
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author GermanSR
 */
public class DocumentationScreen extends AppScreen{
    
    public static String ID = "Documentation";
    

    AnchorPane leftPane = new AnchorPane();
    AnchorPane centerPane = new AnchorPane();
    
    DocumentationStrings currentLanguage = new DocumentationEnglish();
    double sizePanelWidth = 200;
    
    public DocumentationScreen(GUImanager gui){
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
        
        
        ContextMenu languageOptions = new ContextMenu();
        //BUTTON LANGUAGE
        PulseIconButtonCustom btnlanguage = new PulseIconButtonCustom("btnGoBack");
        btnlanguage.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnlanguage.setIconFontawesome(FontAwesomeIcon.LANGUAGE, GUImanager.topBarBurgerIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnlanguage.setEventHandler((event)->{
               Bounds b = btnlanguage.localToScreen(btnlanguage.getBoundsInLocal());
               languageOptions.show(btnlanguage,(int)b.getMinX()-5,(int)b.getMaxY()); 
           
        });
        btnlanguage.construct();
      
        
        Text currentLanguageTxt = utilsGUI.create(DocumentationStrings.LANGUAGE_ENGLISH, "Roboto", 14, Color.WHITE);
        String[] languages = new String[]{DocumentationStrings.LANGUAGE_ENGLISH
                                         ,DocumentationStrings.LANGUAGE_SPANISH};
        
        for(String language:languages){
            PulseIconButtonCustom btn = new PulseIconButtonCustom("btnChunk");
            btn.setBackGroundRectangle(GUImanager.toolBoxIconSize, GUImanager.toolBoxIconSize, Color.TRANSPARENT, false);
            btn.setIconCustom(new Rectangle(20,20,Color.TRANSPARENT));
            btn.createTextElement(language, GUImanager.defaultFont, 12, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BTN_CIRCLE_HOLLOW_ICON));
            btn.setEventHandler((event)->{
                currentLanguageTxt.setText(language);
                switch(language){
                    case DocumentationStrings.LANGUAGE_ENGLISH:
                        currentLanguage = new DocumentationEnglish();
                        break;
                    case DocumentationStrings.LANGUAGE_SPANISH:
                        currentLanguage = new DocumentationSpanish();
                        break;   
                    default: 
                        currentLanguage = new DocumentationEnglish();
                }
                createTreeTable();
            });
            btn.construct();
            CustomMenuItem item = new CustomMenuItem(btn);
            languageOptions.getItems().add(item);
        }
            

        getAppBar().setRightBox(currentLanguageTxt,btnlanguage);
        getAppBar().construct();
         
        createTreeTable();    
    }
    
    
    public void createTreeTable(){
       getCentralPane().getChildren().clear();
       centerPane.getChildren().clear();
       leftPane.getChildren().clear();
 
       getAppBar().setText(currentLanguage.get(DocumentationStrings.STRING_ITEM_DOCUMENTATION));
       getAppBar().construct();
       
       String css = this.getClass().getResource("/cssStyles/WhiteTheme/myTreeView.css").toExternalForm(); 
       leftPane.getStylesheets().add(css);

  
       DocumentationItem itemRoot = new DocumentationItem(currentLanguage.get(DocumentationStrings.STRING_ITEM_DOCUMENTATION));
       
       DocumentationItem Modeling = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_MODELING));
            DocumentationItem Elements = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_MODELING_ELEMENTS));
            DocumentationItem Materials = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_MODELING_MATERIALS));
            DocumentationItem Loads = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_MODELING_LOADS));
            DocumentationItem LoadCases = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_MODELING_LOADCASES));
            Modeling.addChild(Elements,Materials,Loads,LoadCases);
            
       DocumentationItem Analysis = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_ANLYSIS));
       
       DocumentationItem Results  = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_RESULTS));
            DocumentationItem ResultsGeneral = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_RESULTS_GENERAL));
                DocumentationItem DeformationScale = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_RESULTS_GENERAL_SCALE));
                DocumentationItem ActiveLoadCases = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_RESULTS_GENERAL_LOADCASES));
                ResultsGeneral.addChild(DeformationScale,ActiveLoadCases);
                
            DocumentationItem ResultsColorField = new DocumentationItem(currentLanguage.get(DocumentationStrings.ITEM_RESULTS_COLORFIELDS));
            Results.addChild(ResultsGeneral,ResultsColorField);
            
       
       itemRoot.addChild(Modeling);
       itemRoot.addChild(Analysis);
       itemRoot.addChild(Results);
       
       TreeItem<DocumentationItem> root = itemRoot.createTree();
       TreeView<DocumentationItem> tree = new TreeView<> (root) ; 
       root.setExpanded(true);

        
       // getAppBar().
       
       tree.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,Object newValue) {
                
                centerPane.getChildren().clear();
                TreeItem<DocumentationItem> selectedItem = (TreeItem<DocumentationItem>) newValue;
                DocumentationItem docItem = (DocumentationItem)selectedItem.getValue();
                System.out.println("Selected: "+docItem.itemName);
                centerPane.getChildren().add(docItem.getDisplay());
                
            }
        });

        centerPane.setStyle("-fx-background-color: white;");
        
        leftPane.getChildren().add(tree);
        AnchorPane.setBottomAnchor(tree, 0.0);
        AnchorPane.setTopAnchor(tree, 0.0);
        AnchorPane.setLeftAnchor(tree, 0.0);
        AnchorPane.setRightAnchor(tree, 0.0);
        
        leftPane.setPrefWidth(200);
        AnchorPane.setBottomAnchor(leftPane, 5.0);
        AnchorPane.setTopAnchor(leftPane, 5.0);
        AnchorPane.setLeftAnchor(leftPane, 5.0);
        
        AnchorPane.setBottomAnchor(centerPane, 5.0);
        AnchorPane.setTopAnchor(centerPane, 5.0);
        AnchorPane.setLeftAnchor(centerPane, sizePanelWidth+10.0);
        AnchorPane.setRightAnchor(centerPane, 5.0);
        
        DropShadow e = new DropShadow();
        e.setWidth(leftPane.getWidth());
        e.setHeight(leftPane.getHeight());
        e.setOffsetX(3);
        e.setOffsetY(3);
        e.setRadius(5);
        e.setColor(Color.GRAY);
        leftPane.setEffect(e); 

        e = new DropShadow();
        e.setWidth(centerPane.getWidth());
        e.setHeight(centerPane.getHeight());
        e.setOffsetX(3);
        e.setOffsetY(3);
        e.setRadius(5);
        e.setColor(Color.GRAY);
        centerPane.setEffect(e);    
           
        getCentralPane().getChildren().add(leftPane);
        getCentralPane().getChildren().add(centerPane);
        
      
        Elements.addItems(new TextRow(Elements.itemName));
        Materials.addItems(new TextRow(Materials.itemName));
        
    }
    
    

    @Override
    public void loadScreen() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String... args) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    class User extends RecursiveTreeObject<User>{
        StringProperty userName;
        StringProperty age;
        StringProperty department;

        public User(String department, String age, String userName) {
            this.department = new SimpleStringProperty(department) ;
            this.userName = new SimpleStringProperty(userName);
            this.age = new SimpleStringProperty(age);
        }
    }
    
}
