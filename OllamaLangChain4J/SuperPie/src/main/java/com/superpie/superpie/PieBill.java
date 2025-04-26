package com.superpie.superpie;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.Tokenizer;

public class PieBill {
    Tokenizer PieBill = new Tokenizer() {
        @Override
        public int estimateTokenCountInText(String text) {
            return 0;
        }

        @Override
        public int estimateTokenCountInMessage(ChatMessage message) {
            return 0;
        }

        @Override
        public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
            return 0;
        }
    };
}
