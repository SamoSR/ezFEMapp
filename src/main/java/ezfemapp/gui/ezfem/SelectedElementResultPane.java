/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.ezfem;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import ezfemapp.blockProject.Block;
import ezfemapp.fem.model.PlaneStress.FEMmodelPlaneStress;
import ezfemapp.fem.model.PlaneStress.StressFieldComputation;
import ezfemapp.fem.model.PlaneStress.ele2D4N_2DOF;
import ezfemapp.gui.mdcomponents.PulseIconButtonCustom;
import ezfemapp.gui.mdcomponents.utilsGUI;
import ezfemapp.gui.theme.ColorTheme;
import ezfemapp.main.GUImanager;
import ezfemapp.rendering.canvas.BlockRenderer;
import java.text.DecimalFormat;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import serializableApp.DimensionUnits.DimensionUnit;
import serializableApp.DimensionUnits.UnitUtils;
import serializableApp.DimensionUnits.UnitsManagerPro;
import serializableApp.utils.RoundDouble;

/**
 *
 * @author GermanSR
 */
public class SelectedElementResultPane extends AnchorPane {
    
    public SelectedElementResultPane(Block b, StressFieldComputation colorField, FEMmodelPlaneStress model, int w, int h, UnitsManagerPro units){
        
        Circle c = new Circle(10,Color.TRANSPARENT);
        c.setStrokeWidth(1);
        c.setStroke(Color.TRANSPARENT); 
        PulseIconButtonCustom btnClose = new PulseIconButtonCustom("cancelFirstToch");
        btnClose.setIconFontawesome(FontAwesomeIcon.REMOVE, 20+"px",GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_MAIN)); 
        btnClose.setBackGroundCustom(c);
        btnClose.setEventHandler((event)->{           
               this.setVisible(false);
        });
        
        btnClose.construct();    
        this.getChildren().add(btnClose);
        AnchorPane.setTopAnchor(btnClose, 3.0);
        AnchorPane.setRightAnchor(btnClose, 3.0);
        
        this.setStyle("-fx-background-color: "+GUImanager.colorTheme.getColor(ColorTheme.COLOR_BACKGROUND)+";");
        ele2D4N_2DOF e = model.getElementByIndex(b.getIndex());
        
        DropShadow shadow = new DropShadow();
        shadow.setWidth(w);
        shadow.setHeight(h);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(5);
        shadow.setColor(Color.GRAY);
        this.setEffect(shadow); 
        this.setPrefHeight(h);
        this.setPrefWidth(w);
        
        if(colorField==null){
            return;
        }
        
        DimensionUnit displayUnit; 
        if(colorField.getMode().equals(AnalysisScreen.MODE_DX)||
           colorField.getMode().equals(AnalysisScreen.MODE_DY)){
            displayUnit = units.getUnits(UnitsManagerPro.UNITS_DISPLACEMENTS);
        }else if(colorField.getMode().equals(AnalysisScreen.MODE_RX)||
           colorField.getMode().equals(AnalysisScreen.MODE_RY)){
            displayUnit = units.getUnits(UnitsManagerPro.UNITS_REACTION_FORCE);
        }else{
            displayUnit = units.getUnits(UnitsManagerPro.UNITS_FEM_STRESS);
        }
        
        double v1 = colorField.getValueAtNode(e.getNodes()[0].getIndex());
        double v2 = colorField.getValueAtNode(e.getNodes()[1].getIndex());
        double v3 = colorField.getValueAtNode(e.getNodes()[2].getIndex());
        double v4 = colorField.getValueAtNode(e.getNodes()[3].getIndex());
        
        double topoffset = 25;
        double size = h-topoffset;
        Node blockGeometry = BlockRenderer.createBlockGeometryDeformed_ColorMap(b,size,new double[]{0,0,0,0,0,0,0,0},
                new double[]{v1,v2,v3,v4},colorField.getMin(),colorField.getMax(),false);
        blockGeometry.setTranslateX(0);
        blockGeometry.setTranslateY(0);
        
        //VBox vbox = new VBox();
        double[] values = new double[]{v1,v2,v3,v4};
        Text[] lbls = new Text[4];
        //double zeroTressHold = 1e-10;
        int count=0;
        for(double v:values){ 
            
            double unitValue = UnitUtils.convertFromKgMToUnit(v, displayUnit.getRealUnits()); 
           // if(unitValue<zeroTressHold){
           //     unitValue = 0;
           // }
            String text = RoundDouble.Round2(unitValue);
            if(Math.abs(unitValue)<Math.abs(1)){
                double treashold = 1E-3;
                if(Math.abs(unitValue)<treashold){
                    DecimalFormat formatter = new DecimalFormat("0.###E0");
                    text = formatter.format(unitValue);
                } 
            }
            
          
            Text tx = utilsGUI.create(text, GUImanager.defaultFont, 10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT));
            lbls[count++] = tx;
        }
        
        //this.getChildren().add(vbox);
        //AnchorPane.setBottomAnchor(vbox, 0.0);
        //AnchorPane.setLeftAnchor(vbox, 0.0);
        
        //StackPane pane = new StackPane(blockGeometry);
        
        
        this.getChildren().add(lbls[0]);
        AnchorPane.setTopAnchor(lbls[0], topoffset+5);
        AnchorPane.setLeftAnchor(lbls[0], 2.0);
        
        this.getChildren().add(lbls[1]);
        AnchorPane.setTopAnchor(lbls[1], topoffset+5+size);
        AnchorPane.setLeftAnchor(lbls[1], 2.0);
        
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double size1 = fontLoader.computeStringWidth(lbls[0].getText(), lbls[0].getFont());
        double size2 = fontLoader.computeStringWidth(lbls[1].getText(), lbls[1].getFont());
        double sizeX = Math.max(size1,size2);
        
        this.getChildren().add(blockGeometry);
        AnchorPane.setBottomAnchor(blockGeometry, 4.0);
        AnchorPane.setLeftAnchor(blockGeometry, sizeX+5);
        
        this.getChildren().add(lbls[2]);
        AnchorPane.setTopAnchor(lbls[2], topoffset+5+size);
        AnchorPane.setLeftAnchor(lbls[2], 9.0+sizeX+size);
        
        this.getChildren().add(lbls[3]);
        AnchorPane.setTopAnchor(lbls[3], topoffset+5);
        AnchorPane.setLeftAnchor(lbls[3], 9.0+sizeX+size);
        
        
        //Label title = new Label("Element ("+b.getIndex()+") nodal values");
        Text title = utilsGUI.create("Element ("+b.getIndex()+") nodal values", GUImanager.defaultFont, 10, GUImanager.colorTheme.getColorFX(ColorTheme.COLOR_BACKGROUND_TEXT));
        title.setFont(new Font(11));
        AnchorPane.setTopAnchor(title, 4.0);
        AnchorPane.setLeftAnchor(title, 4.0);
        this.getChildren().add(title);
    }
    
   
    
}
