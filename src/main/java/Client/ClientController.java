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
            clientSocket = new Socket("127.0.0.1",2231);
            //Rausschicken
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            //Empfangen
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void send() throws IOException {
        if(username.equals("")){
            username = JOptionPane.showInputDialog("Bitte geben Sie ihren Usernamen ein");
            onlineUsers.setText(onlineUsers.getText() + "\n" + username);
        }

        out.println(username + ": " +inputTextArea.getText());

        String resp = "Error getting Response";

        try {
            resp = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mainTextArea.getText().equals("")) {
            mainTextArea.setText(username + ": " + resp);
        }else{
            mainTextArea.setText(mainTextArea.getText() + "\n" + username + ": " + resp);
        }

        inputTextArea.clear();

    }


}
