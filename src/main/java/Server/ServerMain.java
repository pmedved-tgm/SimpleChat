package Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            //Layout File wird geladen
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("ServerLayout.fxml"));

            //Scene wird erstellt
            Scene scene = new Scene(root);

            //Fenster wird erstellt
            primaryStage.setTitle("Simple Chat Server");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
