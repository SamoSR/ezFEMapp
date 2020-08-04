/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandResult;

/**
 *
 * @author GermanSR
 */
public class PropertyString extends Property{
    
    private String value;
    List<String> allowableValues = null;
 
    
    public PropertyString(String name, String value){
        super(name, Property.TYPE_STRING);
        this.value=value;
    }
    public PropertyString(String name, String value,boolean editable){
        super(name, ((editable)?Property.TYPE_STRING:Property.TYPE_STRINGUNEDITABLE) );
        this.value=value;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public void setAllowableValues(String... values){
        allowableValues = new ArrayList<>();
        for(String v:values){
            allowableValues.add(v);
        }
    }
    
    public List<String>  getAllowableValues(){
        return allowableValues;
    }
    
    @Override
    public CommandResult editWithString(String val){
        if(allowableValues!=null){
            for(String allowableValue:allowableValues){
                if(value.equals(allowableValue)){
                    String oldVal = value;
                    setValue(val);
                    return new CommandResult(true,"Value changed from: "+oldVal+" to: "+val); 
                }
            }  
        }else{

            
        }
        return new CommandResult(false, "unknown course of action changing string property");
    }
    
    @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.setAttribute("Value", ""+value);
        return rootEle;
    }
    
    @Override
    public void deserializeXML(Project project, Element e){
        value = e.getAttribute("Value"); 
    }
    
    /*
    @Override
    public Group getEditComponent(GUImanager mng) {
        Group group = new Group();
        
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(2,2,2,2));
        
        TextField tb = new TextField(value);
        tb.setPrefWidth(75);

        //FOCUS LOST
        tb.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
                //FOCUS ON
                if (newPropertyValue){
                    
                }//FOCUS LOST
                else{
                    
                    value=tb.getText();
                }
            }
        });
        
        tb.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    pane.requestFocus(); //REMOVE FOCUS FROM TEXTFIELD
                }
                if(event.getCode() == KeyCode.ENTER){
                    pane.requestFocus(); //REMOVE FOCUS FROM TEXTFIELD
                }
            }
        });

        tb.setEditable(isEditable());
        tb.setDisable(!isEditable());
        
        //PANEL
        pane.add(getLabelName(), 0, 0);
        pane.add(tb, 1, 0);
        group.getChildren().add(pane);
       
        return group;
    }
    */
   
}
