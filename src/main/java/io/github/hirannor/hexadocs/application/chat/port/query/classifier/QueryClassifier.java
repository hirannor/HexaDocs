package io.github.hirannor.hexadocs.application.chat.port.query.classifier;

/**
 * Classifies user questions into supported query types.
 */
public interface QueryClassifier {

    /**
     * Classifies the specified question.
     *
     * @param question the user's question
     * @return the type of the query
     */
    QueryType classify(final String question);
}
