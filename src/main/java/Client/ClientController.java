package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientController {

    @FXML
    private TextArea mainTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private TextField inputTextArea;

    @FXML
    private TextArea onlineUsers;

    private String username = "";

    private boolean keepGoing = true;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Thread listener;

    public ClientController() {
        try {
            clientSocket = new Socket("127.0.0.1",5050);
            //Rausschicken
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            //Empfangen
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            listener = new Thread() {
                @Override
                public void run() {
                    String resp = "Error getting Response";
                    while(keepGoing){
                        try {
                            resp = in.readLine();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.keepGoing = false;

        try {
            //Schickt den Server den Befehl die Resource des Useres überall zu entfernen
            out.println("!!"+username);

            listener.interrupt();
            out.close();
            in.close();
            clientSocket.close();
            listener.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @FXML
    private void send() throws IOException {
        if(username.equals("")){
            username = JOptionPane.showInputDialog("Bitte geben Sie ihren Usernamen ein");
            while(onlineUsers.getText().contains(username) || username.contains("!!")){
                username = JOptionPane.showInputDialog("Dieser Username ist ungültig");
            }
            onlineUsers.setText(onlineUsers.getText() + "\n" + username);
        }

        out.println(username + ": " +inputTextArea.getText());


        inputTextArea.clear();

    }


}
