package io.github.hirannor.hexadocs.adapter.web.rest.conversation;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.application.chat.usecase.ConversationCreation;
import io.github.hirannor.hexadocs.application.chat.usecase.ConversationDisplaying;
import io.github.hirannor.hexadocs.application.chat.usecase.ConversationHistoryDisplaying;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import io.github.hirannor.hexadocs.domain.conversation.CreateConversation;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/conversations")
class ConversationController {

    private final Function<Conversation, ConversationResponse> mapToConversationResponse;
    private final Function<ConversationMessage, ConversationMessageResponse> mapToMessageResponse;

    private final ConversationCreation conversationCreation;
    private final ConversationDisplaying conversationDisplaying;
    private final ConversationHistoryDisplaying conversationHistoryDisplaying;

    ConversationController(final ConversationCreation conversationCreation,
            final ConversationDisplaying conversationDisplaying,
            final ConversationHistoryDisplaying conversationHistoryDisplaying) {

        this.conversationCreation = conversationCreation;
        this.conversationDisplaying = conversationDisplaying;
        this.conversationHistoryDisplaying = conversationHistoryDisplaying;
        this.mapToConversationResponse = new ConversationToResponseMapper();
        this.mapToMessageResponse = new ConversationMessageToResponseMapper();
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> create(@RequestParam final String knowledgeBaseId,
            @RequestBody final CreateConversationRequest request) {
        final Conversation conversation = conversationCreation.create(
                CreateConversation.issue(KnowledgeBaseId.from(knowledgeBaseId), request.getTitle()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToConversationResponse.apply(conversation));
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> displayAll(@RequestParam final String knowledgeBaseId) {
        final List<ConversationResponse> conversations = conversationDisplaying.displayAllByKnowledgeBaseId(
                        KnowledgeBaseId.from(knowledgeBaseId))
                .stream()
                .map(mapToConversationResponse)
                .toList();

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ConversationMessageResponse>> displayMessages(
            @PathVariable final String conversationId) {

        final List<ConversationMessageResponse> messages = conversationHistoryDisplaying.displayAllById(
                        ConversationId.from(conversationId))
                .stream()
                .map(mapToMessageResponse)
                .toList();

        return ResponseEntity.ok(messages);
    }
}