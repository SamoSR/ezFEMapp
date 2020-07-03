/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitUtils;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.commands.CommandResult;

import serializableApp.utils.RoundDouble;


/**
 * @author GermanSR
 */
public class PropertyDimension extends Property{
    
    private String outputUnitCode;
    private double value;
    
    //REFERENCE TO UNITS
    private UnitsManagerPro unitManagerRef;

    public PropertyDimension(String name){
        super(name,Property.TYPE_DIMENSION);   
    }
    
    public String getOutputUnitCode(){
        return outputUnitCode;
    }
    
   @Override
    public Element serializeXML(Document dom){
        Element rootEle = dom.createElement("Property");
        rootEle.setAttribute("Name", ""+getPropertyName());
        rootEle.setAttribute("Type", ""+getPropertyType());
        rootEle.setAttribute("Value", ""+value);
        rootEle.setAttribute("UnitType", ""+outputUnitCode);
        return rootEle;
    }
    
    @Override
    public void deserializeXML(Project project, Element e){
        String Value = e.getAttribute("Value");
        outputUnitCode = e.getAttribute("UnitType");
        value = Double.parseDouble(Value);     
    }
    
    public PropertyDimension(String name, double val, DimensionUnit units){
        super(name,Property.TYPE_DIMENSION);   
        this.value = UnitUtils.convertToKgM(val,units.getRealUnits());
        this.outputUnitCode = units.getID();
    }
    
    public PropertyDimension(String name, double val, String inputUnits, String unitCode){
        super(name,Property.TYPE_DIMENSION);   
        this.value = UnitUtils.convertToKgM(val,inputUnits);
        this.outputUnitCode = unitCode;
    } 

    public double getValue(){
        return value;
    }
    
    public void setValue(double val, String units){
        value = UnitUtils.convertToKgM(val,units);
    }

    
    public void setUnitManagerRef(UnitsManagerPro unitMng){
        this.unitManagerRef=unitMng;
    }
    
    @Override
    public CommandResult editWithString(String v){
        
        CommandResult cmdResult = new CommandResult();
        String[] split = v.split("\\s+");
        
        String newValue;
        String newUnitString;
     
        if(unitManagerRef==null){
            cmdResult.setResult(false);
            cmdResult.addLine("Project UnitsManager is null");
            return cmdResult;
        }
        DimensionUnit unit = unitManagerRef.getUnits(outputUnitCode);
        if(unit==null){
            cmdResult.setResult(false);
            cmdResult.addLine("Could not find unit named <"+outputUnitCode+"> in the UnitsManager");
            return cmdResult;
        }  
     
        
        if(split.length==2){
            newValue = split[0];
            newUnitString = split[1]; 
        }else{
            newValue = v;
            newUnitString = unit.getRealUnits();
        }
        
        if(!UnitUtils.validateUnit(newUnitString)){
            cmdResult.setResult(false);
            cmdResult.addLine("Invalid dimensional unit <"+newUnitString+">");
            return cmdResult;
        }
        
        if(!UnitUtils.compatibleUnits(newUnitString, unit.getRealUnits())){
            cmdResult.setResult(false);
            cmdResult.addLine("Incompatible specified units <"+newUnitString+">, required units (or similar) <"+unit.getRealUnits()+">");
            return cmdResult;
        }
        
        double realValue;
        try {
            realValue = Double.parseDouble(newValue); 
        } catch (Exception e) {
            cmdResult.setResult(false);
            cmdResult.addLine("New value for property <"+getPropertyName()+"> is not numeric");
            return cmdResult;
        }

        String valueUnit = RoundDouble.Round2(UnitUtils.convertFromKgMToUnit(value, unit.getRealUnits()));
        cmdResult.setResult(true);
        cmdResult.addLine("Edited object="+getParent().getTypeAndID()+ ", property="+getPropertyName()+", old value="+valueUnit+" "+unit.getRealUnits()+ ", new value="+realValue+" "+newUnitString);
        setValue(realValue, newUnitString);
        
        return cmdResult;
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
        
        //ERROR WITH UNITS
        if(!mng.getApp().getUnitsManager().getCustomUnits().containsKey(outputUnitCode)){
            System.out.println("ERROR EDDITING PROPERTY: READING UNITS");
            return new Group();
        }
        
        //TEXT BOX
        String outputUnits = mng.getApp().getUnitsManager().getNamedUnits(outputUnitCode);
        
        TextField tb = new TextField(RoundDouble.Round2(UnitUtils.convertFromKgMToUnit(value, outputUnits)));
        tb.setPrefWidth(75);

        //FOCUS LOST
        tb.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
                //FOCUS ON
                if (newPropertyValue){
                    
                }//FOCUS LOST
                else{
                    
                    SerializableObject parent = getParent();
                    String type = parent.getObjectType();
                    String id = parent.getID();
                    CommandResult result= mng.getApp().getCmdMng().processCommand(CmdEditProp.NAME+","
                                                            +type+","
                                                            +id+","
                                                            +getPropertyName()+","
                                                            +tb.getText());
                    
                  
                    tb.setText(RoundDouble.Round2(UnitUtils.convertFromKgMToUnit(value, outputUnits))); 
                    if(result.getResult()){
                          mng.propertyChanged(getPropertyName());
                    }
                  
                    
                }
            }
        });
        
        tb.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    pane.requestFocus(); //REMOVE FOCUS FROM TEXTFIELD
                    //attempToChangeProp(tb,mng,outputUnits);
                }
                if(event.getCode() == KeyCode.ENTER){
                    pane.requestFocus(); //REMOVE FOCUS FROM TEXTFIELD
                    //attempToChangeProp(tb,mng,outputUnits);
                }
            }
        });

        //LABEL WITH UNIT
        Label lblUnit = new Label( outputUnits );

        //PANEL
        pane.add(getLabelName(), 0, 0);
        pane.add(tb, 1, 0);
        pane.add(lblUnit, 2, 0);
        group.getChildren().add(pane);
       
        return group;
    }
    */
    /*
    private void attempToChangeProp(TextField tb, GUImanager mng, String units){
        double newVal = castToNumber(tb.getText());
        if(Double.isNaN(newVal)){
            tb.setText(""+value);
        }else{
            setValue(newVal, units);
            mng.propertyChanged(this.getPropertyName());
        }
    }
    */

    
    private double castToNumber(String num){
        try {  
          double cast = Double.parseDouble(num);  
          return cast;
        } catch(NumberFormatException e){  
          return Double.NaN;  
        } 
    }
    
    
    
}
