package io.github.hirannor.hexadocs.application.chat.usecase;

public record Answer(
        String content
) {
    public static Answer of(final String content) {
        return new Answer(content);
    }
}