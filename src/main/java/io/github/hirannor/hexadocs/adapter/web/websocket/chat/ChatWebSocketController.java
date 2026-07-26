package io.github.hirannor.hexadocs.adapter.web.websocket.chat;

import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
class ChatWebSocketController {
    private final QuestionAsking question;

    ChatWebSocketController(final QuestionAsking question) {
        this.question = question;
    }

    @MessageMapping("/chat.ask")
    public void ask(final ChatQuestionMessage message) {
        question.ask(AskQuestion.issue(ConversationId.from(message.getConversationId()), message.getQuestion()));
    }
}