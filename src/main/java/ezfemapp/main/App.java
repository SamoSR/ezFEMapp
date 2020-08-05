/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ezfemapp.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ezfemapp.services.BasicAppFactory;

public class App extends Application{
    @Override
    public void start(Stage primaryStage) {
    	
    	BasicAppFactory app = BasicAppFactory.getInstance();
    	
    	app.initGUI(primaryStage);
    	
        //App_Desktop app = new App_Desktop(primaryStage);   
    }

    public static void main(String[] args) {

        // Since this method is a member of the HelloWorld class the first
        // parameter is not required
        launch(args);
    }
}
