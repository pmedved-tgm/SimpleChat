package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            //Layout File wird geladen
            Parent root = FXMLLoader.load(getClass().getResource("../../resources/ClientLayout.fxml"));

            //Scene wird erstellt
            Scene scene = new Scene(root,900,600);

            //Fenster wird erstellt
            primaryStage.setTitle("Simple Chat Client");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
