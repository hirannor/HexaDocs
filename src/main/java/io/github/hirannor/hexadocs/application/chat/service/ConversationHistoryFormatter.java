package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ConversationHistoryFormatter {

    public String format(final List<ConversationMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return "No previous conversation.";
        }


        final StringBuilder history = new StringBuilder();


        for (ConversationMessage message : messages) {

            history.append(switch (message.role()) {
                case USER -> "User";

                case ASSISTANT -> "Assistant";
            });

            history.append(":\n");
            history.append(message.content());
            history.append("\n\n");
        }


        return history.toString()
                .trim();
    }
}