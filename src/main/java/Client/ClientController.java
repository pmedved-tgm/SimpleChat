package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;

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


    @FXML
    private void send(){
        if(username.equals("")){
            username = JOptionPane.showInputDialog("Bitte geben Sie ihren Usernamen ein");
            onlineUsers.setText(onlineUsers.getText() + "\n" + username);
        }

        if(mainTextArea.getText().equals("")) {
            mainTextArea.setText(username + ": " +inputTextArea.getText());
        }else{
            mainTextArea.setText(mainTextArea.getText() + "\n" + username + ": " +inputTextArea.getText());
        }
        inputTextArea.clear();

    }


}
