package com.superpie.superpie.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginWindow extends Application {


//    public void AnyKey(Scene scene, Stage Stage){
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                Switch_Control_to_main(Stage);
//            }
//        });
//    }
// retrun stage

    public void start(Stage stage) {
        try {
            // Load the SuperPieLoginPage FXML file

            Parent root = FXMLLoader.load(getClass().getResource("SuperPieLoginPage1.fxml"));
            Scene scene = new Scene(root);
            Image icon = new Image("pie.png");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setTitle("Super Pie Login Page");
            stage.show();
//            AnyKey(scene, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        launch(args);
    }
}