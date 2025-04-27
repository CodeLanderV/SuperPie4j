//package com.superpie.superpie;
//
//import com.superpie.superpie.back.Ollama4JwCLI33;
//import dev.langchain4j.data.message.ChatMessage;
//import dev.langchain4j.store.memory.chat.ChatMemoryStore;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;
//import com.google.common.reflect.TypeToken;
//import com.google.gson.Gson;
//import dev.langchain4j.data.message.SystemMessage;
//import dev.langchain4j.data.message.AiMessage;
//import dev.langchain4j.data.message.ChatMessage;
//import dev.langchain4j.data.message.UserMessage;
//import dev.langchain4j.service.MemoryId;
//import dev.langchain4j.store.memory.chat.ChatMemoryStore;
//import java.io.*;
//import java.net.URL;
//import java.nio.file.Files;
//import java.util.List;
//import java.util.Map;
//import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
//import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;
//
//import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
//import org.mapdb.DB;
//import org.mapdb.DBMaker;
//import static org.mapdb.Serializer.STRING;
//import java.util.*;
//import dev.langchain4j.data.message.*;
//import dev.langchain4j.memory.ChatMemory;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.memory.chat.MessageWindowChatMemory;
//import dev.langchain4j.model.chat.request.ChatRequest;
//import dev.langchain4j.model.chat.request.ResponseFormat;
//import dev.langchain4j.model.chat.response.ChatResponse;
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import static dev.langchain4j.model.chat.request.ResponseFormat.JSON;
//
//
//public class AiServicesBackend {
//
//    private String modelName = "gemma3:1b";
//    private double temperature = 0.7;
//    private ChatLanguageModel chatModel;
//    private MessageWindowChatMemory memory;
//    private SuperPiePersistentMemoryStore memoryStore;
//    private Scanner scanner;
//
//    public AiServicesBackend() throws IOException {
//        memoryStore = new SuperPiePersistentMemoryStore();
//        memory = MessageWindowChatMemory.builder()
//                .id("defaultSession")
//                .chatMemoryStore((ChatMemoryStore) memoryStore)
//                .maxMessages(10)
//                .build();
//        memory.add(SystemMessage.from("Your name is SuperPie. A chatbot designed by satya paladugu, using Ollama inferencing via langchain4j on javafx. The LLM being used is " + modelName + "."));
//        chatModel = OllamaChatModel.builder()
//                .modelName(modelName)
//                .temperature(temperature)
//                .baseUrl("http://localhost:11434")
//                .build();
//        scanner = new Scanner(System.in);
//    }
//
//    public String sendMessage(UserMessage userMessage) throws IOException {
//        memory.add(userMessage);
//        ChatRequest request = ChatRequest.builder()
//                .messages(memory.messages())
//                .build();
//        ChatResponse response = chatModel.chat(request);
//        AiMessage aiMessage = response.aiMessage();
//        memory.add(aiMessage);
//        return aiMessage.text();
//    }
//
//    public UserMessage createUserMessage(String text) {
//        return UserMessage.from(TextContent.from(text));
//    }
//
//    public void run() {
//        System.out.println("\\<\\<\\<\\<\\<\\<\\<\\<\\<\\<------------------------------- SUPERPIE ------------------------------->>>>>>>>>");
//        System.out.println("%help to get help.");
//
//        boolean running = true;
//        while (running) {
//            try {
//                System.out.print("You:> ");
//                String input = scanner.nextLine().trim();
//
//                if (input.equals("%exit")) {
//                    running = false;
//                    System.out.println("Exiting SuperPie. Goodbye!");
//                } else if (input.equals("%help")) {
//                    System.out.println("\\%exit: Exit the chat");
//                    System.out.println("\\%help: Display this help information");
//                } else {
//                    System.out.println("SuperPie: Please wait... Processing your request.");
//                    String response = sendMessage(createUserMessage(input));
//                    System.out.println("SuperPie:> " + response);
//                }
//            } catch (Exception e) {
//                System.err.println("An error occurred: " + e.getMessage());
//            }
//            System.out.println("---------------------------------------------------------------");
//        }
////        shutdown();
//    }
//
////    private void shutdown() {
////        memoryStore.close();
////    }
//
//    public static void main(String[] args) {
//        try {
//            AiServicesBackend backend = new AiServicesBackend();
//            backend.run();
//        } catch (IOException e) {
//            System.err.println("Failed to initialize SuperPie: " + e.getMessage());
//        }
//    }
//}
//    class SuperPiePersistentMemoryStore implements ChatMemoryStore {
//
//
//        private static final DB db = DBMaker.fileDB("User1.db").transactionEnable().make();
//        private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();
//
//        @Override
//        public List<ChatMessage> getMessages(Object memoryId) {
//            String json = map.get((String) memoryId);
//            return messagesFromJson(json);
//        }
//
//        @Override
//        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
//            String json = messagesToJson(messages);
//            map.put((String) memoryId, json);
//            db.commit();
//        }
//
//        @Override
//        public void deleteMessages(Object memoryId) {
//            map.remove((String) memoryId);
//            db.commit();
//        }
//
//        public static void exit() {
//            db.close();
//        }
//
//        public static void displayList() {
//            String names = db.getAllNames().toString();
//            System.out.println(names);
//        }
//
//    }