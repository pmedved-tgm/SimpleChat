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

public class ServerController {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @FXML
    private TextArea mainTextArea;
    @FXML
    private TextArea onlineUsers;

    public  ServerController(){
        try {
            serverSocket = new ServerSocket(2231);
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
                                try {
                                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                    writers.add(new PrintWriter(clientSocket.getOutputStream(), true));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                while(true){
                                    try {
                                        String msg = in.readLine();

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
