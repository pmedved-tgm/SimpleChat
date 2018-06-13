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
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("ServerController.fxml"));
            Parent root = loader.load();

            //Controller wird vom loader geholt
            ServerController controller = loader.getController();

            //Scene wird erstellt
            Scene scene = new Scene(root);

            //Fenster wird erstellt
            primaryStage.setTitle("Simple Chat Server");
            primaryStage.setScene(scene);

            //Close Event wird definiert
            primaryStage.setOnCloseRequest(e -> controller.shutdown());

            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
