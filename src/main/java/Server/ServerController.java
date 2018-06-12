package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public  ServerController(){
        System.out.println("seas");
        //Man braucht den Thread damit .accept nicht alles Blockiert
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true){
                    try {
                        serverSocket = new ServerSocket(2231);
                        clientSocket = serverSocket.accept();
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }
}
