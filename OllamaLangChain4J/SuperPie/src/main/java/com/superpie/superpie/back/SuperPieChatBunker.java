package com.superpie.superpie.back;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
/*
For our memory Store, we will use Hashmaps as it is easy to store things and manage user-specific contents and all.

THE IDEA OF THIS ENTIRE SUPERPIECHATBUNKER:

so the idea is that, per user, every chat is stored in one instance of this SuperPieChatBunker
Each chat has a unique ID that is mapped to the Chat's


 */

public class SuperPieChatBunker implements ChatMemoryStore {

private final HashMap<Object, List<ChatMessage>> BunkerData = new HashMap<>();
      @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return BunkerData.get(memoryId);
    }
    public void loadFromFile() throws IOException {
        File file = new File("SampleOutput.json");
        if (!file.exists()) return;

        ObjectMapper mapper = new ObjectMapper();
        try (FileReader reader = new FileReader(file)) {
            TypeReference<HashMap<Object, List<ChatMessage>>> typeRef =
                    new TypeReference<>() {};
            this.BunkerData.putAll(mapper.readValue(reader, typeRef));
        }
    }
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        BunkerData.put(memoryId,messages);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        BunkerData.remove(memoryId);
    }
    public  void exitChat() throws IOException {
       SaveJsonToFile(ConvertToJson());

    }
    public String ConvertToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(BunkerData);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void SaveJsonToFile(String S) throws IOException {
        FileOutputStream out = new FileOutputStream("SampleOutput"+".json");
        byte[] data = S.getBytes(StandardCharsets.UTF_8);
        out.write(data);
        System.out.println("Saved to json");
        }

}
