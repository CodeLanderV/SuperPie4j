package com.superpie.superpie.back;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import static org.mapdb.Serializer.STRING;
import java.util.*;
import dev.langchain4j.data.message.*;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import static dev.langchain4j.model.chat.request.ResponseFormat.JSON;




public class Ollama4JwCLI33 {
    public static Scanner scn = new Scanner(System.in);

// =======================================================================================================

    private ChatLanguageModel model;
    public String ModelName = "gemma3:1b";
    private boolean WantInJSON = false;
    public double Temp = 0.7;
    private double TempX;
    private ChatMemory Memory;
    private ChatMemoryStore Store;

    ResponseFormat ResponseFormatJSON(boolean x) { // MODEL_PARAMETER
        // what im tryna do here is if the user wants the response in JSON format, then he has to select yes and then this fucntion will return "JSON" to the format
        if (x) {
            return JSON;
        } else {
            return null;
        }
    }

    void SetTemperature() { // MODEL_PARAMETER
        // what im trying to do here is, if the temperature is same as before, then it wont return a different value but the same
        System.out.println("Set Temperature");
        double x = scn.nextInt();
        this.TempX = x;
        if (TempX != Temp) {
            this.Temp = TempX;
        }
    }
//    public void runOllamaCommand(boolean Stop, String N) {
//        String stop_run = "run";
//        if(Stop){
//            stop_run = "stop";
//        }
//        try {
//            Process process = Runtime.getRuntime().exec(new String[]{"ollama",stop_run, N});
//
//            // Read the output
//            try (BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    void SelectModelName() {
//        runOllamaCommand(true,ModelName);
        System.out.println("1. Gemma 3 1B \n 2. Gemma 3 4B (MultiModal) \n 3. DeepSeek-R1 1.5B \n 4. DeepSeek-R1 7B");
        int z = scn.nextInt();
        switch (z) {
            case 1:
                this.ModelName = "gemma3:1b";
            case 2:
                this.ModelName = "gemma3:4b";
            case 3:
                this.ModelName = "deepseek-r1:1.5b";
            case 4:
                this.ModelName = "deepseek-r1:7b";
            default:
                this.ModelName = "gemma3:1b";
        }

    }

    public ChatLanguageModel You_A_VogueModel(String name) {

        this.ModelName = name;
//        runOllamaCommand(false,ModelName);
        this.model = OllamaChatModel.builder().baseUrl("http://localhost:11434")
                // OLLAMA ALWAYS HAS BASE URL AS 11434
                .modelName("gemma3:1b")
                .responseFormat(ResponseFormatJSON(WantInJSON))
                .temperature(Temp)
                // .maxTokens()
                //.listeners()
                .logRequests(true)
                .build();
        // model.chat("System: Your name is SuperPie. A bot that is designed to help users with anything they want.");
        // this line is of no use as Ollama API doesnt retain memory like how it does in cli by degault.
        return model;
    }
// =======================================================================================================

    ChatResponse ReturnChatResponse(UserMessage X) {
        Memory.add(X);
        ChatRequest request = ChatRequest.builder().messages(Memory.messages()).build();
        return model.chat(request);
    }

    String ReturnAiMessageString(ChatResponse X) {
        AiMessage aiMessage = X.aiMessage();
        if (Memory != null) {
            Memory.add(aiMessage);
        }
        return aiMessage.text();
    }

    UserMessage ReturnUserMessage(String S) {
        UserMessage X = UserMessage.from(
                TextContent.from(S)
        );
        if (Memory != null) {
            Memory.add(X);
        }
        return X;
    }

    UserMessage ReturnUserMessage(String S, String URL) {
        UserMessage X = UserMessage.from(
                TextContent.from(S),
                ImageContent.from(URL)
        );
        if (Memory != null) {
            Memory.add(X);
        }
        return X;
    }


    private ImageContent IGC;
    void returnImageUserMessage(String imageUrl, boolean jpeg) {
        String type = "png";
        if(jpeg){
            type = "jpg";
        }
        try {
            // Read image bytes from URL
            InputStream in = new URL(imageUrl).openStream();
            byte[] imageBytes = in.readAllBytes();
            in.close();

            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
            this.IGC = ImageContent.from(base64Data, "image/"+type); // or "image/png" depending on image type
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch image: " + e.getMessage(), e);
        }
    }

    UserMessage ReturnUserMessage(String S, String Address, boolean jpeg) {
        returnImageUserMessage(Address,jpeg);
        UserMessage X = UserMessage.from(
                TextContent.from(S),
                IGC
        );
        if (Memory != null) {
            Memory.add(X);
        }
        return X;
    }

    public String ModelTalking(String X) {
        String S = ReturnAiMessageString(ReturnChatResponse(ReturnUserMessage(X)));
        return S;
    }

    public String ModelTalking(String X, String URL) {
        return ReturnAiMessageString(ReturnChatResponse(ReturnUserMessage(X, URL)));
    }

    // =======================================================================================================


    public void InitMemory(String memoryId) {

             Store = new SuperPiePersistentMemoryStore();
//            Store = new SuperPieChatBunkerr();


        Memory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(Store)
                .id(memoryId)
                .build();
        Memory.add(SystemMessage.from("Your name is SuperPie. A chatbot designed by satya paladugu, using Ollama inferencing, via langchain4j on javafx. You are supposed to do what the user asked completely."));

    }

    public void Quit(){
        SuperPiePersistentMemoryStore.exit();
    }

    public static void main(String[] args) throws IOException {
        boolean WhileLoop = true;
        Ollama4JwCLI33 O = new Ollama4JwCLI33();
        O.InitMemory("chat2");
        O.You_A_VogueModel(O.ModelName);
        System.out.println(" <<<<<<<<<<------------------------------- SUPERPIE ------------------------------->>>>>>>>>>");
        System.out.println("%help to get help.");
        while (WhileLoop) {
            System.out.print("You:> ");
            String X = scn.nextLine();
            if (X.equals("%exit")) {
                WhileLoop = false;
            } else if (X.equals("%model")) {
                O.SelectModelName();
                O.You_A_VogueModel(O.ModelName);
                System.out.println("Changed the Model.");
            } else if (X.equals("%temp")) {
                O.SetTemperature();
                O.You_A_VogueModel(O.ModelName);
                System.out.println("Changed the Temperature.");
            } else if (X.equals("%help")) {
                System.out.println("%exit: exit the chat \n %model: Change model \n %temp: Change temperature");
            } else if (X.equals("%image")) {
                System.out.println("Please enter Image URL");
                String Url = scn.nextLine();
                System.out.print("Text:> ");
                X = scn.nextLine();
                System.out.println("SuperPie:> " + O.ModelTalking(X,Url));
            }
            else {
                System.out.println("SuperPie:> " + O.ModelTalking(X));
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        }
            O.Quit();


    }
}

class SuperPiePersistentMemoryStore implements ChatMemoryStore {
    private static final DB db = DBMaker.fileDB("chat-memory.db").transactionEnable().make();
    private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = map.get((String) memoryId);
        return messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = messagesToJson(messages);
        map.put((String) memoryId, json);
        db.commit();
    }

    @Override
    public void deleteMessages(Object memoryId) {
        map.remove((String) memoryId);
        db.commit();
    }
    public static void exit(){
        db.close();
    }
}