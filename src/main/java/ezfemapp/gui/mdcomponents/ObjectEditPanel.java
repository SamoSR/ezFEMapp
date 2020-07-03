/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import ezfemapp.blockProject.ColorObject;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitUtils;
import serializableApp.commands.CommandResult;
import serializableApp.objects.EditPropertyGroup;
import serializableApp.objects.PropertyDouble;
import serializableApp.objects.SerializableObject;
import serializableApp.objects.Property;
import serializableApp.objects.PropertyDimension;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.PropertyString;
import serializableApp.objects.PropertyStringID;
import serializableApp.utils.RoundDouble;
import serializableApp.utils.StringResult;

/**
 *
 * @author GermanSR
 */
public class ObjectEditPanel extends StackPane{
    
    StackPane selfRef;
    JFXTabPane tabPane;
    int SelectedTab;
    double rowWidth=25;
    double fontSize = 12;
    GUImanager gui;
    SerializableObject obj;
    //List<String> avoidPropGroups;
    
    public ObjectEditPanel(SerializableObject obj, GUImanager gui){ 
        selfRef = this;
        this.gui=gui;
        this.obj=obj;
        construct();
    }
    /*
    public void avoidPropertyGroups(String... groups){
        avoidPropGroups.clear();
        for(String g:groups){
            avoidPropGroups.add(g);
        }
    }*/
    
    //avoid calling the listener when creating the panel so that the previously selected tab can be stored;
    boolean activeListener=false;
    
    public void construct(){
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/TabPane.css").toExternalForm();
        
        activeListener = false;
        this.getChildren().clear();
        tabPane = new JFXTabPane();
        tabPane.getSelectionModel().selectedItemProperty().addListener((event)->{
            if(!activeListener){
                return;
            }
            SelectedTab = tabPane.getSelectionModel().getSelectedIndex();
        });  
        
        tabPane.getStylesheets().add(css);
        this.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_BACKGROUND)+";");
        
        for(EditPropertyGroup propGroup:obj.getEditPropertyGroups()){
            /*
            boolean avoid = false;
            for(String avoidGroup:avoidPropGroups){
                if(propGroup.getName().equals(avoidGroup)){
                    avoid=true;
                }
            }
            if(avoid){
                continue;
            }*/
            Tab tab = new Tab();
            tab.setText(propGroup.getName());
            
            HBox propList = new HBox(10);
            VBox propNamesBox = new VBox(10);
            VBox propValuesBox = new VBox(10);

            propList.getChildren().addAll(propNamesBox,propValuesBox);
            propNamesBox.setPadding(new Insets(0, 0, 0, 10));
            propList.setPadding(new Insets(5, 0, 0, 0));
            
            for(String propName:propGroup.getProperties()){
            
                Property prop = obj.getProperty(propName);
                switch(prop.getPropertyType()){
                    case Property.TYPE_DOUBLE:
                        propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                        propValuesBox.getChildren().add(getEditComponenet(prop.castToPropertyDouble()));
                    break;
                    case Property.TYPE_INTEGER:
                        propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                        propValuesBox.getChildren().add(getEditComponenet(prop.castToPropertyInteger()));
                    break;
                    case Property.TYPE_DIMENSION:
                        propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                        propValuesBox.getChildren().add(getEditComponenet(prop.castoToPropertyDimension()));
                    break;
                    case Property.TYPE_STRINGID:
                        propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                        propValuesBox.getChildren().add(getEditComponenet(prop.castoToPropertyStringID()));
                    break;
                    case Property.TYPE_STRING:
                        propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                        propValuesBox.getChildren().add(getEditComponenet(prop.castoToPropertyString()));
                    break;
                    case Property.TYPE_OBJECT:
                        SerializableObject object = prop.castoToPropertyObject().getObject();
                        if(object instanceof ColorObject){
                            propNamesBox.getChildren().add(createText(prop.getPropertyName()));
                            propValuesBox.getChildren().add(getEditComponenet((ColorObject)object));
                        }
                        
                    break;
                }

            }
            
            tab.setContent(propList);
            tabPane.getTabs().add(tab);
        }
        activeListener=true;
        this.getChildren().add(tabPane); 
    }

    
    public HBox createText(String text){
       
        HBox box = new HBox();
        Text t = new Text(text);
        t.setFont(new Font("Arial", fontSize));
        t.setFill(GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(t);
        box.setPrefHeight(rowWidth);
        return box;
    }
    
    public HBox getEditComponenet(ColorObject obj){
        
        double rw = 80;
        double rh = 25;
        HBox box = new HBox();
        String css = this.getClass().getResource("/cssStyles/WhiteTheme/MyCustomColorPicker.css").toExternalForm(); 
        box.getStylesheets().add(css);
        
        PulseIconButtonCustom btnColor = new PulseIconButtonCustom("btnLoadList");
        btnColor.setBackGroundCustom(new Rectangle(rw,rh,obj.getColorFX()));
        btnColor.setIconCustom(new Rectangle(rw,rh,obj.getColorFX()));
        btnColor.setEventHandler((event)->{
            
            Bounds b = btnColor.localToScreen(btnColor.getBoundsInLocal());
            
            Button btnOK = new Button("OK");
            ContextMenu contextMenu = new ContextMenu();
            
            MyCustomColorPicker myCustomColorPicker = new MyCustomColorPicker(btnOK);
            myCustomColorPicker.setCustomColor(obj.getColorFX());
            myCustomColorPicker.setOpacity(1);
            
            CustomMenuItem itemColor = new CustomMenuItem(myCustomColorPicker);
            itemColor.setHideOnClick(false);
            contextMenu.getItems().add(itemColor);
            
            contextMenu.setOnHidden(event2 -> {
                
            });

            btnOK.setOnAction((event3) -> {
                btnColor.setIconCustom(new Rectangle(rw,rh,myCustomColorPicker.getCustomColor()));
                obj.setColor(myCustomColorPicker.getCustomColor().getRed()*255, 
                             myCustomColorPicker.getCustomColor().getGreen()*255, 
                             myCustomColorPicker.getCustomColor().getBlue()*255, 1);
                btnColor.construct();
                contextMenu.hide();
                gui.getCurrentScreen().update();
            });
            contextMenu.show(btnColor,b.getMaxX(),b.getMinY());
            
        });
        btnColor.construct();
        box.getChildren().add(btnColor);
        return box;
    }
    
    public HBox getEditComponenet(PropertyDouble prop){
        HBox box = new HBox();
        JFXTextField tf=new JFXTextField(""+prop.getValueString());
        tf.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm());      
        addFocusRemovalOnEnterListener(tf);
        box.getChildren().add(tf);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(rowWidth);
        tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            if(!newValue){
                CommandResult r = prop.editWithString(tf.getText());
                System.out.println("command: "+r.getFirstLine());
                String newText = RoundDouble.Round5(prop.getValue());
                tf.setText(newText);
                if(r.getResult()){
                    //if the command was executed succesfully, call the methjod update of the current screen
                    gui.getCurrentScreen().update();
                }
            }       
        });
        tf.setDisable(!prop.isEditable());
        return box;
    }
    
    public HBox getEditComponenet(PropertyInteger prop){
        HBox box = new HBox();
        JFXTextField tf=new JFXTextField(""+prop.getValueString());
        tf.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm());      
        addFocusRemovalOnEnterListener(tf);
        box.getChildren().add(tf);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(rowWidth);
        tf.setText(""+prop.getValue());
        tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            if(!newValue){
                CommandResult r = prop.editWithString(tf.getText());
                System.out.println("command: "+r.getFirstLine());
                String newText = RoundDouble.Round5(prop.getValue());
                tf.setText(newText);
                if(r.getResult()){
                    //if the command was executed succesfully, call the methjod update of the current screen
                    gui.getCurrentScreen().update();
                }
            }       
        });
        tf.setDisable(!prop.isEditable());
        return box;
    }
    
    public HBox getEditComponenet(PropertyStringID prop){
        HBox box = new HBox();
        JFXTextField tf=new JFXTextField(""+prop.getValueString());
        tf.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm());
        addFocusRemovalOnEnterListener(tf);
        box.getChildren().add(tf);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(rowWidth);
        tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            
            if(!newValue){
                System.out.println("focus lost");
                //checkl if the the name already exists
                StringResult result2 = prop.checkDuplicatedName(tf.getText());
                System.out.println("CHANGING : "+result2.stringValue);
                if(result2.getResult()){
                    System.out.println("CHANGING ID2");
                    //check if the name input is correct
                    CommandResult r = prop.editWithString(tf.getText());
                    System.out.println("command: "+r.getFirstLine());
                    if(r.getResult()){
                        //if the command was executed succesfully, call the methjod update of the current screen
                        gui.getCurrentScreen().update();
                    }
                }
                String newText = prop.getValue();
                tf.setText(newText);
            }       
        });
        tf.setDisable(!prop.isEditable());
        return box;
    }
    
    
    public HBox getEditComponenet(PropertyString prop){
        HBox box = new HBox();
        JFXTextField tf=new JFXTextField(""+prop.getValueString());
        tf.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm());
        addFocusRemovalOnEnterListener(tf);
        box.getChildren().add(tf);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(rowWidth);
        tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            if(!newValue){
                
                //check if the name input is correct
                CommandResult r = prop.editWithString(tf.getText());
                System.out.println("command: "+r.getFirstLine());
                if(r.getResult()){
                    //if the command was executed succesfully, call the methjod update of the current screen
                    gui.getCurrentScreen().update();
                }

                String newText = prop.getValue();
                tf.setText(newText);
            }       
        });
        tf.setDisable(!prop.isEditable());
        return box;
    }
    
    public void selectTab(int index){
        tabPane.getSelectionModel().select(index);
    }
    
    public HBox getEditComponenet(PropertyDimension prop){
        String outputUnitCode = prop.getOutputUnitCode();
        DimensionUnit propUnits = gui.getApp().getBlocks().getUnitsManager().getUnits(outputUnitCode);
        prop.setUnitManagerRef(gui.getApp().getBlocks().getUnitsManager());
        String outputUnits = propUnits.getRealUnits();
        String fotmatedText = RoundDouble.Round2(UnitUtils.convertFromKgMToUnit(prop.getValue(), outputUnits));
        HBox box = new HBox(5);
        JFXTextField tf=new JFXTextField(""+fotmatedText);
        tf.getStylesheets().add(this.getClass().getResource("/cssStyles/WhiteTheme/MyTextField.css").toExternalForm());
        tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)->{
            if(!newValue){
                CommandResult r = prop.editWithString(tf.getText());
                System.out.println("command: "+r.getFirstLine());
                String newText = RoundDouble.Round2(UnitUtils.convertFromKgMToUnit(prop.getValue(), outputUnits));
                tf.setText(newText);
                if(r.getResult()){
                    //if the command was executed succesfully, call the methjod update of the current screen
                    gui.getCurrentScreen().update();
                }
            }       
        });
        addFocusRemovalOnEnterListener(tf);
        box.getChildren().add(tf);
        box.getChildren().add(createText(UnitUtils.convertToPrettyFormat(outputUnits)));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefHeight(rowWidth);
        tf.setDisable(!prop.isEditable());
        return box;
    }
    
    private void addFocusRemovalOnEnterListener(Node n){
        n.setOnKeyPressed((event)->{
            if(event.getCode()==KeyCode.ENTER){
                this.requestFocus();
            }else if(event.getCode()==KeyCode.ESCAPE){
                this.requestFocus();
            }
        });
    }
    
}
