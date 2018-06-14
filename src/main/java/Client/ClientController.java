package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * In dieser Klasse wird die Funtkionalität des Clients erstellt, einerseits werden hier die
 * Sockets erstellt aber auch Funktionalitäten wie senden und schließen
 *
 * @author Philipp Medved
 * @version 14.06.2018
 */
public class ClientController {

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private TextField inputTextArea = null;

    @FXML
    private TextArea onlineUsers;

    @FXML
    private TextField nameTextArea;

    private String username = "";

    private boolean keepGoing = true;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Thread listener;
    private Thread checkIfConnected;

    /**
     * Im Konstruktor des ClientControllers wird der Socket zur Verbindung erstellt
     * Ebenso wird ein Thread erstellt der es erlaubt, dass mehrere User
     * gleichzeitig chatten können
     *
     */
    public ClientController() {
        try {
            clientSocket = new Socket("127.0.0.1",5050);
            //Rausschicken
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            //Empfangen
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Thread der wartet ob nachrichten rein kommen
            listener = new Thread() {
                @Override
                public void run() {
                    String resp = "Error getting Response";
                    while(keepGoing){
                        try {
                            resp = in.readLine();
                            if(resp == null){
                                shutdown();
                            }
                            //Sieht nach ob die vom Server erhaltene nachricht !! enthält --> !! = Nutzer offline
                            if(resp.substring(0,2).equals("!!")){
                                // Splitten die onlineUser nach den neuen Zeilen
                                ArrayList<String> namen = new ArrayList<>(Arrays.asList(onlineUsers.getText().split("\n")));

                                //Geht die Liste durch und löscht den offline User aus der Liste
                                for(String name : namen){
                                    if(name.equals(resp.substring(2,resp.length()))){
                                        namen.remove(name);
                                    }
                                }

                                //Setzt die onlineUser anzeige neu, damit der offline User nicht mehr angezeigt wird
                                onlineUsers.setText(String.join("\n", (String[]) namen.toArray(new String[0])));
                                continue;
                            }

                            //Hier wird der name aus der Nachricht geholt um zu sehen welche User online sind
                            String name = resp.split(":")[0];
                            if (!onlineUsers.getText().contains(name) && !name.contains("!!")) {
                                onlineUsers.setText(onlineUsers.getText() + "\n" + name);
                            }
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }

                        if(mainTextArea.getText().equals("")) {
                            mainTextArea.setText(resp);
                        }else{
                            mainTextArea.setText(mainTextArea.getText() + "\n"  + resp);
                        }
                    }
                }
            };

            listener.start();

            checkIfConnected = new Thread() {
                @Override
                public void run() {
                    while(keepGoing){
                        if(!clientSocket.isConnected()){
                            shutdown();
                            break;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                    }
                }
            };
            checkIfConnected.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die shutdown Methode wird verwendet um den Client sauber zu schliesen
     * Shutdown sendet eine bestimmte Nachricht an den Server, dieser erkennt
     * den Befehl und löscht ihn aus der Liste
     */
    public void shutdown(){
        this.keepGoing = false;

        try {
            //checkIfConnected.interrupt();
            checkIfConnected.join();

            //Schickt den Server den Befehl die Resource des Useres überall zu entfernen
            out.println("!!"+nameTextArea.getText());

            //Thread
            listener.interrupt();

            //Out, in und Socket des Clients werden geschlossen
            out.close();
            in.close();
            clientSocket.close();

            //Thread wird geschlossen
            listener.join();


        } catch (Exception e) {
            System.out.println("Interrupted");
        }
        System.exit(0);
    }

    /**
     * Die send Methode wird verwendet um die Nachricht aus dem Textfield zu lesen und an
     * den Server zu schicken
     *
     */
    @FXML
    private void send(){
        if(username.equals("")){
            username = nameTextArea.getText();
            onlineUsers.setText(onlineUsers.getText() + "\n" + username);
        }

        out.println(username + ": " +inputTextArea.getText());

        nameTextArea.setEditable(false);
        inputTextArea.clear();

    }

    @FXML
    void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                send();
            default:
                break;
        }
    }
}
