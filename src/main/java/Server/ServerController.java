package Server;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerController {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    //private PrintWriter out;
    //private BufferedReader in;

    @FXML
    private TextArea mainTextArea;
    @FXML
    private TextArea onlineUsers;

    public  ServerController(){
        try {
            serverSocket = new ServerSocket(5050);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<PrintWriter> writers = new ArrayList<>();

        //Man braucht den Thread damit .accept nicht alles Blockiert
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true){
                    try {

                        clientSocket = serverSocket.accept();


                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                BufferedReader in = null;
                                try {
                                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                                    //added den Client in eine Liste um die Nachricht an alle senden zu können
                                    writers.add(new PrintWriter(clientSocket.getOutputStream(), true));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                while(true){
                                    try {
                                        String msg = in.readLine();

                                        //Sieht nach ob die vom Server erhaltene nachricht !! enthält --> !! = Nutzer offline
                                        if(msg.substring(0,2).equals("!!")){
                                            // Splitten die onlineUser nach den neuen Zeilen
                                            ArrayList<String> namen = new ArrayList<>(Arrays.asList(onlineUsers.getText().split("\n")));

                                            //Geht die Liste durch und löscht den offline User aus der Liste
                                            for(String name : namen){
                                                if(name.equals(msg.substring(2,msg.length()))){
                                                    namen.remove(name);
                                                }
                                            }

                                            //Setzt die onlineUser anzeige neu, damit der offline User nicht mehr angezeigt wird
                                            onlineUsers.setText(String.join("\n", (String[]) namen.toArray(new String[0])));

                                        }

                                        String name = msg.split(":")[0];
                                        if (!onlineUsers.getText().contains(name) && !name.contains("!!")) {
                                            onlineUsers.setText(onlineUsers.getText() + "\n" + name);
                                        }

                                        if(mainTextArea.getText().equals("")) {
                                            mainTextArea.setText(msg);
                                        }else{
                                            mainTextArea.setText(mainTextArea.getText() + "\n" + msg);
                                        }

                                        for(PrintWriter writer: writers){
                                            writer.println(msg);
                                        }

                                        System.out.println(msg);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        t.start();

                        System.out.println("Client accepted");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }
}
