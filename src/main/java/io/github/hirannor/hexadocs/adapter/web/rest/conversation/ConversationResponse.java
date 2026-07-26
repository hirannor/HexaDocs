package io.github.hirannor.hexadocs.adapter.web.rest.conversation;


import java.time.Instant;

public record ConversationResponse(String id, String title, Instant createdAt) {
}