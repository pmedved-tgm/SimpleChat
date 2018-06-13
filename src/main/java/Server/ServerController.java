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
    //private PrintWriter out;
    //private BufferedReader in;
    private Socket clientSocket;
    private Thread accepter;
    private ArrayList<Thread> clients = new ArrayList<>();
    private ArrayList<Socket> clientSockets = new ArrayList<>();
    private ArrayList<PrintWriter> writers = new ArrayList<>();
    private ArrayList<BufferedReader> readers = new ArrayList<>();

    @FXML
    private TextArea mainTextArea;
    @FXML
    private TextArea onlineUsers;

    private boolean keepGoing = true;

    public  ServerController(){
        try {
            serverSocket = new ServerSocket(5050);
        } catch (IOException e) {
            e.printStackTrace();
        }



        //Man braucht den Thread damit .accept nicht alles Blockiert
        accepter = new Thread() {
            @Override
            public void run() {
                while(keepGoing){
                    try {
                        clientSocket = serverSocket.accept();

                        //Client wird in die Clientliste gespiechert
                        clientSockets.add(clientSocket);

                        //Thread für die einzelnen CLients
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                BufferedReader in = null;

                                try {
                                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                                    //Leser wird in die Leserliste hinzugefügt
                                    readers.add(in);

                                    //added den Client in eine Liste um die Nachricht an alle senden zu können
                                    writers.add(new PrintWriter(clientSocket.getOutputStream(), true));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Socket myClient = clientSocket;
                                while(keepGoing){
                                    try {
                                        String msg = in.readLine();

                                        //Sieht nach ob die vom Server erhaltene nachricht !! enthält --> !! = Nutzer offline
                                        if(msg.substring(0,2).equals("!!")){
                                            // Splitten die onlineUser nach den neuen Zeilen
                                            ArrayList<String> namen = new ArrayList<>(Arrays.asList(onlineUsers.getText().split("\n")));
                                            clientSockets.remove(myClient);
                                            //Geht die Liste durch und löscht den offline User aus der Liste
                                            for(String name : namen){
                                                if(name.equals(msg.substring(2,msg.length()))){
                                                    namen.remove(name);
                                                }
                                            }

                                            //Setzt die onlineUser anzeige neu, damit der offline User nicht mehr angezeigt wird
                                            onlineUsers.setText(String.join("\n", (String[]) namen.toArray(new String[0])));

                                        }

                                        //Check ob der User zum ersten mal schreibt
                                        String name = msg.split(":")[0];
                                        if (!onlineUsers.getText().contains(name) && !name.contains("!!")) {
                                            onlineUsers.setText(onlineUsers.getText() + "\n" + name);
                                        }


                                        if(mainTextArea.getText().equals("")) {
                                            mainTextArea.setText(msg);
                                        }else{
                                            mainTextArea.setText(mainTextArea.getText() + "\n" + msg);
                                        }

                                        //Schickt die Nachrichten an alle Clients
                                        for(PrintWriter writer: writers){
                                            writer.println(msg);
                                        }

                                        System.out.println(msg);
                                    } catch (IOException e) {
                                        //e.printStackTrace();
                                    }
                                }
                            }
                        };
                        t.start();
                        clients.add(t);

                        System.out.println("Client accepted");
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }
        };
        accepter.start();
    }

    public void shutdown(){
        this.keepGoing = false;

        try {
            accepter.interrupt();
            serverSocket.close();
            accepter.join();

            for(int i = 0; i < writers.size(); i++){
                //Thread
                clients.get(i).interrupt();

                //Server out, in
                writers.get(i).close();
                readers.get(i).close();

                //Schließen der client Sockets
                try{
                    clientSockets.get(i).close();
                }catch(Exception e){

                }

                //Thread schließen
                clients.get(i).join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
