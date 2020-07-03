/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.commands.CommandResult;
import serializableApp.utils.RoundDouble;

/**
 *
 * @author GermanSR
 */
public class PropertyDouble extends Property{
    
   private double value;
    
    public PropertyDouble(String name){
        super(name,Property.TYPE_DOUBLE);
        this.value=0;
    }
    
    public PropertyDouble(String name, double value){
        super(name, Property.TYPE_DOUBLE);
        this.value=value;
    }
    public PropertyDouble(String name, String shortName,double value){
        super(name, Property.TYPE_DOUBLE);
        this.value=value;
        this.setShortName(shortName);
    }
    
    public void setValue(double val){
        this.value = val;
    }
    public double getValue(){
        return value;
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
        String Value = e.getAttribute("Value");
        value = Double.parseDouble(Value);     
    }
    
    @Override
    public CommandResult editWithString(String val){
        try {
            double numVal = Double.parseDouble(val);
            String oldValue = RoundDouble.Round2(value);
            String newValue = RoundDouble.Round2(numVal);
            setValue(numVal);
            return new CommandResult(true,"Edited object="+getParent().getTypeAndID()+ ", property="+getPropertyName()+", old value="+oldValue+ ", new value="+newValue);
        } catch (Exception e) {
            return new CommandResult(false,"New value for property <"+getPropertyName()+"> is not numeric");
        }   
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
        
        TextField tb = new TextField(""+RoundDouble.Round2(value));
        tb.setPrefWidth(75);

        //FOCUS LOST
        tb.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
                //FOCUS ON
                if (newPropertyValue){
                    
                }//FOCUS LOST
                else{
                    attempToChangeProp(tb,mng);
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
 
        //PANEL
        pane.add(getLabelName(), 0, 0);
        pane.add(tb, 1, 0);
        group.getChildren().add(pane);
       
        return group;
    }
    */
    /*
    private void attempToChangeProp(TextField tb, GUImanager mng){
        double newVal = castToNumber(tb.getText());
        if(Double.isNaN(newVal)){
            tb.setText(""+value);
        }else{
            setValue(newVal);
            mng.propertyChanged(this.getPropertyName());
        }
    }*/
    
    
    
}
