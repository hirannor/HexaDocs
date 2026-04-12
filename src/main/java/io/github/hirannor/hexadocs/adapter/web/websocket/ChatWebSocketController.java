package io.github.hirannor.hexadocs.adapter.web.websocket;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final QuestionAsking chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(
            QuestionAsking chatService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.ask")
    public void ask(final ChatQuestionMessage message) {
        final Answer answer = chatService.ask(
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