package Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * In dieser Klasse wird das XML File f&uuml;r den Server geladen und die dazugeh&ouml;rige GUI erstellt
 *
 * @author Philipp Medved
 * @version 14.06.2018
 */
public class ServerMain extends Application {

    /**
     * Diese Methode wird verwendet um die GUI des Servers zu laden und erstellen
     *
     * @param primaryStage Standardparameter für eine FXMl GUI
     */
    @Override
    public void start(Stage primaryStage){
        try{
            //Layout File wird geladen
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("ServerLayout.fxml"));
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
