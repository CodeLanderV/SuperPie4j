package com.superpie.superpie.back;
import java.util.*;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
/*
MAIN FOCUS: IMPLEMENTATION OF EXTREMELY EXTREMELY LOW LEVEL BASIC CHAT APP WITH NO MEMORY.
 */

class SuperPie_CLI{
private ChatLanguageModel model;
private String ModelName;

/*
The Builder Pattern is a powerful tool for creating complex objects with flexible configurations.
By implementing the Builder Pattern in your application,
you can simplify the process of creating objects with numerous optional parameters,
leading to cleaner and more maintainable code
 */

    ChatLanguageModel You_A_VogueModel(String name){
        this.ModelName = name;
        this.model = OllamaChatModel.builder().baseUrl("http://localhost:11434")
                // OLLAMA ALWAYS HAS BASE URL AS 11434
                .modelName(ModelName)
                .logRequests(true)
                .build();
            model.chat("System: Your name is SuperPie. A bot that is designed to help users with anything they want.");
            // this line is of no use as Ollama API doesnt retain memory like how it does in cli by degault.


        return model;
    }

    String Arrey_Response_Re_baba(String Prompt){
        return (model.chat(Prompt));
    }


}

public class Ollama4JwCLI {
    public static void main(String args[]){
        SuperPie_CLI s = new SuperPie_CLI();
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter /exit to exit. \n Please select a model.");
        s.You_A_VogueModel("gemma3:1b");
        System.out.println("Model selected is gemma3 1b");
        boolean Z =true;
        while(Z){
            System.out.print(">>");
            String prompt = scn.nextLine();
            if(prompt.toLowerCase().equals("/exit")){
                System.out.println("ok bye!");
              Z = false;
            }else{
                System.out.println("-----------------------------------------------------------------");
                System.out.println(s.Arrey_Response_Re_baba(prompt));
                System.out.println("-----------------------------------------------------------------");
                Z =true;
            }
        }

    }

}
