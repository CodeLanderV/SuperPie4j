package com.superpie.superpie;

import com.superpie.superpie.back.Ollama4JwCLI33;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SuperPieView extends Application {

    // UI components
    private Button newChat, shareChat, addChatReference, saveExit, userSend, uploadImage, uploadPDF;
    private MenuButton selectModel;
    private Slider responseTemperature;
    private ListView<String> chatHistory;
    private VBox completeChat;
    private TextArea userInput;

    // Application state and helper
    private Ollama4JwCLI33 OO;
    private String currentImageUrl = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Initialize the model interface
        OO = new Ollama4JwCLI33();
        OO.InitMemory("ChatX");

        // Title text
        Text title = new Text("SuperPie");
        title.setFont(Font.font("Century Gothic Bold", 50));
        title.setLayoutX(14);
        title.setLayoutY(50);

        // Left control pane
        newChat = new Button("NEW CHAT");
        newChat.setFont(Font.font("SansSerif Bold", 18));
        newChat.setPrefWidth(250);

        shareChat = new Button("Share Chat");
        shareChat.setFont(Font.font("SansSerif Regular", 19));
        shareChat.setPrefWidth(250);

        chatHistory = new ListView<>();
        chatHistory.setPrefSize(250, 200);

        addChatReference = new Button("Add Chat Reference");
        addChatReference.setFont(Font.font("SansSerif Bold", 17));
        addChatReference.setPrefWidth(250);

        saveExit = new Button("Save Chat and Exit");
        saveExit.setFont(Font.font("SansSerif Bold", 19));
        saveExit.setPrefWidth(250);

        VBox leftPane = new VBox(10);
        leftPane.getChildren().addAll(newChat, shareChat, chatHistory, addChatReference, saveExit);
        leftPane.setLayoutX(4);
        leftPane.setLayoutY(100);

        // Chat area (messages container)
        completeChat = new VBox(5);
        // Wrap chat messages in an AnchorPane to allow proper layout
        AnchorPane chatPane = new AnchorPane();
        chatPane.getChildren().add(completeChat);
        AnchorPane.setTopAnchor(completeChat, 10.0);
        AnchorPane.setBottomAnchor(completeChat, 10.0);
        AnchorPane.setLeftAnchor(completeChat, 10.0);
        AnchorPane.setRightAnchor(completeChat, 10.0);

        // User input area (text area and send button)
        AnchorPane inputPane = new AnchorPane();
        userInput = new TextArea();
        userInput.setPrefSize(910, 80);
        userSend = new Button("Send");
        userSend.setPrefSize(50, 80);
        inputPane.getChildren().addAll(userInput, userSend);
        AnchorPane.setTopAnchor(userInput, 10.0);
        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setLeftAnchor(userSend, 930.0);
        AnchorPane.setTopAnchor(userSend, 10.0);
        AnchorPane.setBottomAnchor(userSend, 10.0);

        // SplitPane to arrange chat area and user input vertically
        SplitPane centerPane = new SplitPane();
        centerPane.setOrientation(Orientation.VERTICAL);
        centerPane.getItems().addAll(chatPane, inputPane);
        centerPane.setLayoutX(270);
        centerPane.setLayoutY(100);
        centerPane.setPrefSize(990, 480);

        // Top-right controls: select model menu and temperature slider
        selectModel = new MenuButton("Select Model");
        selectModel.setFont(Font.font("SansSerif Bold", 21));
        selectModel.setPrefSize(180, 68);
        selectModel.setLayoutX(1080);
        selectModel.setLayoutY(100);
        // Populate model menu items
        List<String> models = Arrays.asList("gemma3:1b", "gemma3:4b", "deepseek-r1:1.5b", "deepseek-r1:7b");
        selectModel.getItems().clear();
        for (String model : models) {
            MenuItem mi = new MenuItem(model);
            mi.setOnAction(e -> {
                OO.You_A_VogueModel(model);
                selectModel.setText("MODEL: " + model);
            });
            selectModel.getItems().add(mi);
        }

        responseTemperature = new Slider(0, 2.0, OO.Temp);
        responseTemperature.setBlockIncrement(1.0);
        responseTemperature.setPrefSize(180, 14);
        responseTemperature.setLayoutX(1080);
        responseTemperature.setLayoutY(180);
        responseTemperature.valueProperty().addListener((obs, oldVal, newVal) -> {
            OO.Temp = newVal.doubleValue();
            OO.You_A_VogueModel(OO.ModelName);
        });

        // Bottom-right file upload buttons
        uploadImage = new Button("Upload Image");
        uploadImage.setPrefSize(180, 40);
        uploadImage.setLayoutX(1080);
        uploadImage.setLayoutY(220);

        uploadPDF = new Button("Upload PDF");
        uploadPDF.setPrefSize(180, 40);
        uploadPDF.setLayoutX(1080);
        uploadPDF.setLayoutY(270);

        // Root layout
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(title, leftPane, centerPane, selectModel, responseTemperature, uploadImage, uploadPDF);

        // Setup event handlers
        userSend.setOnAction(e -> sendMessage());
        newChat.setOnAction(e -> {
            completeChat.getChildren().clear();
            OO.InitMemory("Chat" + System.currentTimeMillis());
            currentImageUrl = null;
        });
        uploadImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                currentImageUrl = selectedFile.toURI().toString();
                uploadImage.setText("Image Selected");
            }
        });
        uploadPDF.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select PDF");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                uploadPDF.setText("PDF Selected");
            }
        });
        saveExit.setOnAction(e -> {
//            OO.Quit();
            stage.close();
        });
        // (Optional) Setup shareChat and addChatReference event handlers as needed

        Scene scene = new Scene(root, 1280, 600);
        stage.setScene(scene);
        stage.setTitle("SuperPie");
        stage.getIcons().add(new Image("pie.png"));
        stage.show();
    }

    private void sendMessage() {
        String userMessage = userInput.getText().trim();
        if (userMessage.isEmpty()) return;

        // Add user message to chat area
        addMessageToChat("You: " + userMessage, true);

        String aiResponse;
        if (currentImageUrl != null) {
            aiResponse = OO.ModelTalking(userMessage, currentImageUrl);
            currentImageUrl = null;
        } else {
            aiResponse = OO.ModelTalking(userMessage);
        }
        addMessageToChat("SuperPie: " + aiResponse, false);
        userInput.clear();
    }

    private void addMessageToChat(String message, boolean isUser) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(780);
        if (isUser) {
            messageLabel.setStyle("-fx-background-color: #c3c87b; -fx-padding: 10; -fx-background-radius: 5;");
        } else {
            messageLabel.setStyle("-fx-background-color: #d0802a; -fx-padding: 10; -fx-background-radius: 5;");
        }
        completeChat.getChildren().add(messageLabel);
    }
}