/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.blockProject.ColorObject;
import ezfemapp.gui.mdcomponents.ObjectEditPanel;
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.screen.AppBarState;
import ezfemapp.gui.screen.AppScreen;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import serializableApp.commands.CmdDeleteObject;
import serializableApp.commands.CommandResult;
import serializableApp.objects.Property;
import serializableApp.objects.PropertyObject;
import serializableApp.objects.PropertyObjectList;
import serializableApp.objects.SerializableObject;


/**
 *
 * @author GermanSR
 */
public class VariousObjectEditScreen extends AppScreen{
    
    public static final String STATE_UNSELECT = "unselected";
    public static final String STATE_SELECT = "selected";
    
    //PropertyObjectList listObj;
    List<SerializableObject> objList;
    double listWidth = 180;
    JFXListView<Label> list;
    String selectedObject;
    
    public VariousObjectEditScreen(List<SerializableObject> objList,String screenName, GUImanager gui){
        super(screenName,gui); 
        String appBarText = screenName;
        
        this.objList = objList;
        //BUTTON GO BACK
        PulseIconButtonCustom btnBack = new PulseIconButtonCustom("btnGoBack");
        btnBack.setBackGroundRectangle(42, 42, Color.TRANSPARENT, false);
        btnBack.setIconFontawesome(FontAwesomeIcon.CHEVRON_LEFT, GUImanager.topBarBurgerIconSize+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnBack.setEventHandler((event)->{
            gui.loadScreen(ModelingScreen.ID);
            //ModelingScreen modScreen = (ModelingScreen)gui.getCurrentScreen();
           // modScreen.updateDynamicGUI();
           // modScreen.showBlockTools();
            unselect();
        });
        btnBack.construct();
        getAppBar().setLeftBox(btnBack);
        
        //AppBarState unselectState =  new AppBarState(STATE_UNSELECT);
      //  AppBarState selectionState =  new AppBarState(STATE_SELECT);
       // getAppBar().addBarState(unselectState);
       // getAppBar().addBarState(selectionState);
        
        /*
        //BTN DELETE
        PulseIconButtonCustom btnDelete = new PulseIconButtonCustom("btnLoads");
        btnDelete.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnDelete.setIconFontawesome(FontAwesomeIcon.TRASH, "22px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnDelete.setEventHandler((event)->{
            //getAppBar().setText(ID+" - "+"Loads");
            //setTopRightCornerTools(loadTools,loadToolsScroll);
            deleteObject(selectedObject);
        });
        btnDelete.construct();
        */
        /*
        //BTN COPY
        PulseIconButtonCustom btnCopy = new PulseIconButtonCustom("btnLoads");
        btnCopy.setBackGroundRectangle(35, 35, Color.TRANSPARENT, false);
        btnCopy.setIconFontawesome(FontAwesomeIcon.COPY, "22px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnCopy.setEventHandler((event)->{
            //getAppBar().setText(ID+" - "+"Loads");
            //setTopRightCornerTools(loadTools,loadToolsScroll);
        });
        btnCopy.construct();
        selectionState.setRightBox(btnCopy,btnDelete);
        unselectState.setRightBox(new Group());
        */
        /*
        //BUTTON ADD NEW MATERIAL
        PulseIconButtonCustom btnPlay = new PulseIconButtonCustom("btnAnalysis");
        btnPlay.setBackGroundCircle(50, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN), true);
        btnPlay.setIconFontawesome(FontAwesomeIcon.PLUS, "20px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN_TEXT));
        btnPlay.setEventHandler((event)->{
            //gui.loadScreen(AnalysisScreen.ID);
            createNewObject();
        });
        btnPlay.construct();
        getCentralPane().getChildren().add(btnPlay);
        AnchorPane.setBottomAnchor(btnPlay, 10.0);
        AnchorPane.setRightAnchor(btnPlay, 10.0); 
        getAppBar().setState(STATE_UNSELECT);
        */
        getAppBar().setText(appBarText);
        getAppBar().construct();
    }
    

    

    @Override
    public void loadScreen(){
       createList();
    }
    
    private SerializableObject getObjectByID(String id){
        for(SerializableObject obj:objList){
            if(obj.getID().equals(id)){
                return obj;
            }
        }
        return null;
    }
    
    
    boolean ignoreListener=false;
    private void createList(){

        VBox boxList = new VBox();
        boxList.setPrefWidth(listWidth);
        
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/MyListView.css").toExternalForm(); 
        
        list = new JFXListView<>();
        list.getStylesheets().add(css);
        
        for(SerializableObject obj:objList){
            Label lbl = new Label(obj.getID());
            Property colorObjProp = obj.getProperty("Color");
            if(colorObjProp!=null){
                PropertyObject colorObj = (PropertyObject)colorObjProp;
                if(colorObj.getObject() instanceof ColorObject){
                    ColorObject color = (ColorObject)colorObj.getObject();
                    Circle c = new Circle(10,color.getColorFX());
                    lbl.setGraphic(c);
                }
            }
            list.getItems().add(lbl);
        }
    
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable,Label oldValue, Label newValue) {
                if(ignoreListener){
                    return;
                }
                selectedObject = newValue.getText();
              
                openEditObjectEditPane(getObjectByID(selectedObject));
                //getAppBar().setText(ID+": "+selectedObject);  
                getAppBar().setState(STATE_SELECT);
            }
        });
        
        VBox.setVgrow(list, Priority.ALWAYS);
        list.setPrefHeight(1);
        list.setVerticalGap(10.0);
        list.setExpanded(true);
        list.setDepth(1);

        boxList.getChildren().add(list);
        boxList.toBack();
        
        AnchorPane.setTopAnchor(boxList, 0.0);
        AnchorPane.setLeftAnchor(boxList, 5.0);
        AnchorPane.setBottomAnchor(boxList, 0.0);
        
        getCentralPane().getChildren().add(boxList);
    }
    
    
    private void unselect(){
        ignoreListener = true;
        list.getSelectionModel().clearSelection();
        getCentralPane().getChildren().remove(editPane);
        getAppBar().setState(STATE_UNSELECT);
        ignoreListener = false;
    }
    
    Node editPane;
    public void openEditObjectEditPane(SerializableObject obj){
        getCentralPane().getChildren().remove(editPane);
        if(obj==null){
            System.out.println("null object");
            return;
        }
        editPane = new ObjectEditPanel(obj,getGUI());
        AnchorPane.setTopAnchor(editPane, 5.0);
        AnchorPane.setRightAnchor(editPane, 5.0);
        AnchorPane.setLeftAnchor(editPane, listWidth+10);
        AnchorPane.setBottomAnchor(editPane, 5.0);
        getCentralPane().getChildren().add(editPane);
        editPane.toBack();
    }
    
    @Override
    public void update(String... args){
        updateListLabels();
        updateListColors();
    }
    
    private void updateListLabels(){
        if(objList.size()!=list.getItems().size()){
            return;
        }
        int count=0;
        for(SerializableObject obj:objList){
            list.getItems().get(count++).setText(""+obj.getID());
        }
    }
    
    private void updateListColors(){
        int count=0;
        for(SerializableObject obj:objList){
           Label lbl = list.getItems().get(count++);
           lbl.setText(""+obj.getID()); 
            Property colorObjProp = obj.getProperty("Color");
            if(colorObjProp!=null){
                PropertyObject colorObj = (PropertyObject)colorObjProp;
                if(colorObj.getObject() instanceof ColorObject){
                    ColorObject color = (ColorObject)colorObj.getObject();
                    Circle c = new Circle(10,color.getColorFX());
                    lbl.setGraphic(c);
                }
            }
        }
    }
    
    
}
