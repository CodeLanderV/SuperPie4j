package com.superpie.superpie.back;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class OpenAIExample {
    static void UseMe(){
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .build();

        String answer  = model.chat("System: Your name is SuperPie. A bot that is designed to help users with anything they want. Now Introduce yourself to me.");
        System.out.println(answer);

    }
    public static void main(String args[]){
        UseMe();
    }
}

