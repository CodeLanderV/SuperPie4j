package com.superpie.superpie.back;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
/*
MAIN FOCUS: IMPLEMENTATION OF EXTREMELY EXTREMELY LOW LEVEL BASIC MULTIMODAL(images only) CHAT APP WITH NO MEMORY.
 */


class SuperPie_CLI2{
    private ChatLanguageModel model;
    private String ModelName;


    ChatLanguageModel You_A_VogueModel(String name){
        this.ModelName = name;
        this.model = OllamaChatModel.builder().baseUrl("http://localhost:11434")
                // OLLAMA ALWAYS HAS BASE URL AS 11434
                .modelName(ModelName)
                .logRequests(true)
                .build();
//        model.chat("System: Your name is SuperPie. A bot that is designed to help users with anything they want.");
        // this line is of no use as Ollama API doesnt retain memory like how it does in cli by degault.
        return model;
    }

    ChatResponse GetResponseFromUserMessage(UserMessage u){
        return model.chat(u);
    }
//    ChatResponse GetResponseFromChatRequest(ChatRequest x){
//        return model.generate(x);
//    }

}
public class Ollama4JwCLI2 {
    static ImageContent returnImageBytes(String Address, String ext) {
        try {
            Path path = Paths.get(Address);
            byte[] imageBytes = Files.readAllBytes(path);
            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
            return ImageContent.from(base64Data, "image/" + ext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    static ImageContent localReturnImageUserMessage(String Address) throws IOException {
//        byte[] in = convertWithImageIO(Address);
//        String base64Data = Base64.getEncoder().encodeToString(in);
//        return ImageContent.from(base64Data, "image/png");
//   }
//
//    public static byte[] convertWithImageIO(String imagePath) throws IOException {
//        BufferedImage image = ImageIO.read(new File(imagePath));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
//
//    static ImageContent returnImageUserMessage(String imageUrl) {
//        try {
//            // Read image bytes from URL
//            InputStream in = new URL(imageUrl).openStream();
//            byte[] imageBytes = in.readAllBytes();
//            in.close();
//            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
//            return ImageContent.from(base64Data, "image/jpg"); // or "image/png" depending on image type
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to fetch image: " + e.getMessage(), e);
//        }
//    }

    /*
    what is the difference between UserMessage AiMessage and ChatResponse ChatRequest is that
    ChatResponse and ChatRequest not only transfer and exchange messages but also MetaData information
     */

    /*
    overall workflow in using the model
    1. Load up the model into an object.
    2. Create a UserMessage class that can also be loaded with Text, Images, Videos, Pdfs etc
    3. Convert it to ChatResponse.
    4. From ChatRespnce make it to Aimessage object
    5. Print the Ai Message

     */

    public static void main(String[] args) throws IOException {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("Can you analyse this image for me?"),
                returnImageBytes("D:\\Bunker\\OneDrive - Amrita vishwa vidyapeetham\\BaseCamp\\SuperPie4j\\SuperPie4j\\OllamaLangChain4J\\SuperPie\\src\\main\\resources\\pie.png","png")
        );
//        ChatRequest request = ChatRequest.builder()
//                .messages(List.of(userMessage))  // you can also add system/user messages here for context
//                .build();

        SuperPie_CLI2 SS = new SuperPie_CLI2();
        SS.You_A_VogueModel("gemma3:4b");
        ChatResponse x = SS.GetResponseFromUserMessage(userMessage);
        AiMessage aiMessage =x.aiMessage();
        System.out.println("SuperPie: "+ aiMessage.text());

    }


}
