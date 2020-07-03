/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.blockProject;

import javafx.scene.paint.Color;
import serializableApp.objects.PropertyDouble;
import serializableApp.objects.PropertyInteger;
import serializableApp.objects.SerializableObject;

/**
 *
 * @author GermanSR
 */
public class ColorObject extends SerializableObject{
    
    public static final String OBJECT_TYPE="Color";
    
    public static final String PROPNAME_RED="Red";
    public static final String PROPNAME_GREEN="Green";
    public static final String PROPNAME_BLUE="Blue";
    public static final String PROPNAME_OPACITY="Opacity";
    
    public ColorObject(String id, int r, int g, int b){
        super(OBJECT_TYPE,id);
        addProperty(new PropertyDouble(PROPNAME_RED,r));
        addProperty(new PropertyDouble(PROPNAME_GREEN,g));
        addProperty(new PropertyDouble(PROPNAME_BLUE,b));
        addProperty(new PropertyDouble(PROPNAME_OPACITY,1));
    }
    
    public Color getColorFX(){
        return new Color(getProperty(PROPNAME_RED).getValueDouble()/255,
                        getProperty(PROPNAME_GREEN).getValueDouble()/255,
                        getProperty(PROPNAME_BLUE).getValueDouble()/255,
                        getProperty(PROPNAME_OPACITY).getValueDouble());
    }
    
    public void setColor(double r, double g, double b, double opacity){
        getProperty(PROPNAME_RED).castToPropertyDouble().setValue(r);
        getProperty(PROPNAME_GREEN).castToPropertyDouble().setValue(g);
        getProperty(PROPNAME_BLUE).castToPropertyDouble().setValue(b);
        getProperty(PROPNAME_OPACITY).castToPropertyDouble().setValue(opacity);
    }
    
   
}
