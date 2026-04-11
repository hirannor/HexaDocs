package io.github.hirannor.hexadocs.domain.document;

public record FileReference(String value) {
    public static FileReference of(final String value) {
        return new FileReference(value);
    }
}