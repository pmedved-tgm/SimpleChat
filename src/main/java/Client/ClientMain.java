package Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.naming.ldap.Control;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            //Layout File wird geladen
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("ClientLayout.fxml"));
            Parent root = loader.load();

            //Controller wird vom loader geholt
            ClientController controller = loader.getController();

            //Scene wird erstellt
            Scene scene = new Scene(root);

            //Fenster wird erstellt
            primaryStage.setTitle("Simple Chat Client");
            primaryStage.setScene(scene);


            //Close Event wird definiert
            primaryStage.setOnCloseRequest(e -> controller.shutdown());

            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
