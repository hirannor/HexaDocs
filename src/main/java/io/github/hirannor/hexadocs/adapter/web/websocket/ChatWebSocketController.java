package io.github.hirannor.hexadocs.adapter.web.websocket;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
class ChatWebSocketController {

    private final QuestionAsking question;
    private final SimpMessagingTemplate messagingTemplate;

    ChatWebSocketController(
            QuestionAsking question,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.question = question;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.ask")
    public void ask(final ChatQuestionMessage message) {
        final Answer answer = question.ask(
                 AskQuestion.issue(
                        KnowledgeBaseId.from(message.getKnowledgeBaseId()),
                        message.getQuestion()
                ));

        messagingTemplate.convertAndSend(
                "/topic/chat",
                new ChatAnswerMessage(answer.content())
        );
    }
}