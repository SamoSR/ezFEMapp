/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.theme;

/**
 *
 * @author GermanSR
 */
public class ThemeWhite extends ColorTheme{


    public ThemeWhite(){
        
        addColor(new ColorCode(COLOR_MAIN, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_BACKGROUND, 255, 255, 255, 1));
        addColor(new ColorCode(COLOR_BACKGROUND_TEXT, 50, 50, 50, 1));
        addColor(new ColorCode(COLOR_MAIN_TEXT, 255, 255, 255, 1));
        addColor(new ColorCode(COLOR_PANEL_BACKGROUND, 245, 245, 245, 1));
        //COLOR
        /*
        addColor(new ColorCode(COLOR_NAVIGATION_BAR, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_NAVIGATION_BAR_ICON, 255, 255, 255, 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_FILLED, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_FILLED_ICON, 255, 255, 255, 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_HOLLOW, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_HOLLOW_ICON, 60, 90, 255, 1));
       */
        
        //GREY
        addColor(new ColorCode(COLOR_NAVIGATION_BAR, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_NAVIGATION_BAR_ICON, 250, 250, 250, 1));
        
        addColor(new ColorCode(COLOR_BTN_CIRCLE_FILLED, 60, 90, 255, 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_FILLED_ICON, 255, 255, 255, 1));
        
        addColor(new ColorCode(COLOR_BTN_CIRCLE_HOLLOW, 50, 50, 50 , 1));
        addColor(new ColorCode(COLOR_BTN_CIRCLE_HOLLOW_ICON, 50, 50, 50, 1));
        

    }
    

    

    
}
