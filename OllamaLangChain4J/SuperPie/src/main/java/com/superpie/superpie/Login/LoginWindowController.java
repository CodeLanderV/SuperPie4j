package com.superpie.superpie.Login;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginWindowController {
    @FXML
    private Label FoundNot;
    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    @FXML
    private Text YesNo;

    public void CheckPassword(ActionEvent E){ // dummy function for now!
        if(Password.getText().equals(Username.getText())){
            YesNo.setText("Welcome "+Username.getText()+" ! ");
            PauseTransition pause = new PauseTransition(Duration.seconds(10));




            // now the control goes back to login window java file
            // Switch Stage/Java program to the MainWindow


        }
        else {
            YesNo.setText("Not Found");
        }
    }
}
