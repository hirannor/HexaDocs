package io.github.hirannor.hexadocs.adapter.web.rest.chat;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
class ChatController {

    private final QuestionAsking question;

    ChatController(final QuestionAsking question) {
        this.question = question;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> ask(@RequestBody final ChatRequest request) {
        final AskQuestion command = AskQuestion.issue(
                KnowledgeBaseId.from(request.getKnowledgeBaseId()), request.getQuestion()
        );

        final Answer answer = question.ask(command);

        return ResponseEntity.ok(
                new ChatResponse(answer.content())
        );
    }

}
