package com.superpie.superpie.back;
//import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.model.chat.request.ChatRequest;
//import dev.langchain4j.model.chat.request.ChatRequestParameters;
//import dev.langchain4j.model.chat.request.ResponseFormat;
//import dev.langchain4j.model.chat.request.ResponseFormatType;
//import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
//import dev.langchain4j.model.chat.request.json.JsonSchema;
//import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class OllamaExample {
    static void UseMe(){
        ChatLanguageModel model = OllamaChatModel.builder().baseUrl("http://localhost:11434").modelName("gemma3:1b").logRequests(true).build();

        String answer  = model.chat("System: Your name is SuperPie. A bot that is designed to help users with anything they want. Now Introduce yourself to me.");
        System.out.println(answer);

    }
    public static void main(String args[]){
       UseMe();
    }
}
