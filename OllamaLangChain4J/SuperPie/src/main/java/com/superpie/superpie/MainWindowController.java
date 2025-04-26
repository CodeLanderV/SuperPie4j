// File: src/main/java/com/superpie/superpie/MainWindowController.java

        package com.superpie.superpie;

        import com.superpie.superpie.back.Ollama4JwCLI33;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.layout.VBox;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import java.io.File;
        import java.util.Arrays;
        import java.util.List;
        import com.superpie.superpie.back.*;

        public class MainWindowController {
            @FXML
            private Button NewChat;
            @FXML
            private Button ShareChat;
            @FXML
            private Button AddChatReference;
            @FXML
            private Button SaveExit;
            @FXML
            private Button UserSend;
            @FXML
            private Button UploadImage;
            @FXML
            private Button UploadPDF;
            @FXML
            private MenuButton SelectModel;
            @FXML
            private Slider ResponseTemperature;
            @FXML
            private ListView<String> ChatHistory;
            @FXML
            private VBox CompleteChat;
            @FXML
            private TextArea UserInput;

            private Ollama4JwCLI33 OO;
            private String currentImageUrl = null;
            private Stage stage;

            public void initialize() {
                OO = new Ollama4JwCLI33();
                OO.InitMemory("ChatX");
                initializeModelMenu();
                initializeTemperatureSlider();
                setupButtonHandlers();
            }

            private void initializeModelMenu() {
                List<String> models = Arrays.asList(
                    "gemma3:1b", "gemma3:4b",
                    "deepseek-r1:1.5b", "deepseek-r1:7b"
                );
                SelectModel.getItems().clear();
                for (String model : models) {
                    MenuItem mi = new MenuItem(model);
                    mi.setOnAction(e -> {
                        OO.You_A_VogueModel(model);
                        SelectModel.setText("MODEL: " + model);
                    });
                    SelectModel.getItems().add(mi);
                }
            }

            private void initializeTemperatureSlider() {
                // Initialize the slider to current temperature value
                ResponseTemperature.setValue(OO.Temp);
                ResponseTemperature.valueProperty().addListener((obs, oldVal, newVal) -> {
                    OO.Temp = newVal.doubleValue();
                    OO.You_A_VogueModel(OO.ModelName);
                });
            }

            private void setupButtonHandlers() {
                UserSend.setOnAction(e -> sendMessage());
                NewChat.setOnAction(e -> {
                    CompleteChat.getChildren().clear();
                    OO.InitMemory("Chat" + System.currentTimeMillis());
                    currentImageUrl = null;
                });
                UploadImage.setOnAction(e -> uploadImage());
                UploadPDF.setOnAction(e -> uploadPdf());
                SaveExit.setOnAction(e -> {
                    OO.Quit();
                    stage.close();
                });
                // Further handlers (ShareChat, AddChatReference) can be added similarly
            }

            private void sendMessage() {
                String userMessage = UserInput.getText().trim();
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
                UserInput.clear();
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
                CompleteChat.getChildren().add(messageLabel);
            }

            private void uploadImage() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
                );
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    currentImageUrl = selectedFile.toURI().toString();
                    UploadImage.setText("Image Selected");
                }
            }

            private void uploadPdf() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select PDF");
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    UploadPDF.setText("PDF Selected");
                    // PDF handling logic can be added here
                }
            }

            // This method is used to inject the current stage from the Application class after loading FXML
            public void setStage(Stage stage) {
                this.stage = stage;
            }
        }