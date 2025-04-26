//
//
////import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;
//
///*
//MAIN FOCUS: ENABLE CHAT MEMORY AND ENABLE RESPONSE STREAMING. ALSO ENABLE VARIOUS DIFFERENT THINGS FROM ChatResponse.
//1. ChatMemory
//2. ResponseStreaming
//3. ChatResponse Stats
//4. Model Paramters: Temperature, Format, ModelName
// */
//
///*
//   We Make a constructor that
//   1. Initialises the MemoryStore For the user and pulls up all his conversations.
//   2. Initialise the Model
//   3.
// */
//
//public class Ollama4JwCLI3 {
//
////=====================================================    MODEL PARAMETERS   ===================================================================
//
//
//
//    ================================================================================================================================================
//
//    //==========================================================    MODEL CHAT   ===================================================================
//
//
//    UserMessage Sir_You_Tell_me(String X) {//, boolean YesPic
//        UserMessage userMessage = UserMessage.from(
//                TextContent.from(X));
//        return userMessage;
//    }
//
//    UserMessage Sir_You_Tell_me(String X, boolean YesPic) throws IOException {
//        ImageContent C;
//        Scanner scn = new Scanner(System.in); // Assuming you haven't initialized a scanner yet
//        UserMessage userMessage = null;  // Declare userMessage at the start
//
//        System.out.println("Is the picture from online? 1. Yes || 0. No || Any other for No Image");
//
//        int choice = scn.nextInt();
//        scn.nextLine(); // Consume the newline left by nextInt()
//
//        if (choice == 1) {
//            // Image is from URL
//            System.out.println("Please enter the image URL:");
//            String imageUrl = scn.nextLine();
//            C = returnImageUserMessage(imageUrl);  // Assuming returnImageUserMessage() handles URL-based images
//            userMessage = UserMessage.from(
//                    TextContent.from(X),
//                    C
//            );
//        } else if (choice == 2) {
//            // Image is from local path
//            System.out.println("Please enter the image name (without extension):");
//            String imageName = scn.nextLine();
//            C = localReturnImageUserMessage("D:\\Bunker\\OneDrive - Amrita vishwa vidyapeetham\\BaseCamp\\SuperPie\\SuperPie\\OllamaLangChain4J\\SuperPie\\src\\main\\resources\\" + imageName + ".png");
//            userMessage = UserMessage.from(
//                    TextContent.from(X),
//                    C
//            );
//        } else {
//            // No image selected
//            System.out.println("No image selected.");
//            userMessage = UserMessage.from(
//                    TextContent.from(X)
//            );
//        }
//
//        return userMessage;
//    }
//
//
//
//    /*
//    the imagecontent class doesnt directly take the image from a source. it has to be converted from an image to a ByteStream
//        which is then encoded into a base64 image whcih is then converted to an image
//     */
//
//    ImageContent returnImageUserMessage(String imageUrl) {
//        try {
//            // Read image bytes from URL
//            InputStream in = new URL(imageUrl).openStream();
//            byte[] imageBytes = in.readAllBytes();
//            in.close();
//
//            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
//            return ImageContent.from(base64Data, "image/png"); // or "image/png" depending on image type
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to fetch image: " + e.getMessage(), e);
//        }
//    }
//
//    ImageContent localReturnImageUserMessage(String Address) throws IOException {
//        byte[] in = convertWithImageIO(Address);
//        String base64Data = Base64.getEncoder().encodeToString(in);
//        return ImageContent.from(base64Data, "image/png");
//    }
//
//    byte[] convertWithImageIO(String imagePath) throws IOException {
//        BufferedImage image = ImageIO.read(new File(imagePath));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
//
//
//    /*
//    ----------------------------------------------------

//--------------------------------------------     My Chat Storage and Context Strategy    ------------------------------------------------
//
//Our Goal: For the UI, we need to store all messages in file A.
//          For the Model, we need to store either "Summarized" data or like "N" important tokens.
//
//

//
//    So for the model, we need to maintain:
//    ChatMemory modelMemory and List<ChatMessage> UiMemory = new ArrayList<>();
//    Now there are ways to store the context and all of the chat.
//    We will make a method to store both the Context and the Chat
//
//
// */
//    /*
//    Lets understand the working of TokenWindowChatMemory:
//    iocuses on keeping the N most recent tokens, evicting older messages as needed.
//    Messages are indivisible. If a message doesn't fit, it is evicted completely.
//    TokenWindowChatMemory requires a Tokenizer to count the tokens in each ChatMessage.
//     */
//
//}
