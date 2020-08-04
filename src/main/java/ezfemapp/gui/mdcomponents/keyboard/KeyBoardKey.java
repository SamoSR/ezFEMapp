/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezfemapp.gui.mdcomponents.keyboard;

import javafx.scene.input.KeyCode;

    public class KeyBoardKey{
        
        String character;
        KeyCode code;
        
        public KeyBoardKey(String value){
            this.character = value;
        }
        public KeyBoardKey(KeyCode k, String character){
            this.code = k;
            this.character = character;
        }
  
    }
