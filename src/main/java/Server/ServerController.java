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
        try {
            serverSocket = new ServerSocket(2231);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                                while(true){
                                    try {

                                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                        String msg = in.readLine();
                                        //out = new PrintWriter(clientSocket.getOutputStream(), true);
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
