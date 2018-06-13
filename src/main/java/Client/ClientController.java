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

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientController() {
        try {
            clientSocket = new Socket("127.0.0.1",5050);
            //Rausschicken
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            //Empfangen
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            Thread t = new Thread() {
                @Override
                public void run() {
                    String resp = "Error getting Response";
                    for(;;){
                        try {
                            resp = in.readLine();

                            String name = resp.split(":")[0];
                            if (!onlineUsers.getText().contains(name)) {
                                onlineUsers.setText(onlineUsers.getText() + "\n" + name);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(mainTextArea.getText().equals("")) {
                            mainTextArea.setText(resp);
                        }else{
                            mainTextArea.setText(mainTextArea.getText() + "\n"  + resp);
                        }
                    }
                }
            };

            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void send() throws IOException {
        if(username.equals("")){
            username = JOptionPane.showInputDialog("Bitte geben Sie ihren Usernamen ein");
            /*while(onlineUsers.getText().contains(username)){
                username = JOptionPane.showInputDialog("Dieser Username ist bereits vergeben bitte geben Sie einen anderen ein");
            }*/
            onlineUsers.setText(onlineUsers.getText() + "\n" + username);
        }

        out.println(username + ": " +inputTextArea.getText());


        inputTextArea.clear();

    }


}
