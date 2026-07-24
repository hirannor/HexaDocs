package io.github.hirannor.hexadocs.application.chat.port;

public interface QueryClassifier {
  QueryType classify(final String question);
}
