/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents;

/**
 *
 * @author GermanSR
 */
public class HtmlText {
    
    
    String text="<!DOCTYPE html><html><font size=\"2\" style = \"font-family:Roboto;\">";
 
    public void breakLine(){
        text+="<br />";
    }
    public void addLine(String txt){
        text+=txt+"<br/>";
    }
    public static String bold(String text){
        return "<b>"+text+"</b>";
    }
    public String getText(){
        return text;
    }
    public void addTittle(String title){
        text+= "<h1>"+title+"</h1>";
    }
    public void finish(){
        text+="</font></html>";
    }
    
    public static String color(String text, String color){
       String colored = "<font color=\""+color+"\"> "+text+" </font>";
       return colored;
    }
    
    public static String subs(String text, String subs){
        String fulltext = text+"<sub>"+subs+"</sub>";
        return fulltext;
    }
    
}
