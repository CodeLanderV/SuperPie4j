package com.superpie.superpie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.io.IOException;

public class MainWindow extends Application {
//    Stage run(){
//        Stage stage = new Stage();
//        return stage;
//    }
    public void start(Stage stage){
        try {
            // Load the SuperPieLoginPage FXML file
            Parent root = FXMLLoader.load(getClass().getResource("SuperPieMainChat.fxml"));
            Scene scene = new Scene(root);
            Image icon = new Image("pie.png");
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setTitle("SuperPie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        launch(args);

    }

}
