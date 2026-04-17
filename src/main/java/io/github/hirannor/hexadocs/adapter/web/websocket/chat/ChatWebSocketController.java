package io.github.hirannor.hexadocs.adapter.web.websocket.chat;

import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
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
        question.ask(
                AskQuestion.issue(
                        KnowledgeBaseId.from(message.getKnowledgeBaseId()),
                        message.getQuestion()
                )
        );
    }
}