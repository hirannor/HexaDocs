package io.github.hirannor.hexadocs.adapter.chat;

import io.github.hirannor.hexadocs.application.chat.port.query.classifier.QueryClassifier;
import io.github.hirannor.hexadocs.application.chat.port.query.classifier.QueryType;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
class RuleBasedQueryClassifier implements QueryClassifier {

    private static final Pattern DOCUMENT_WIDE_PATTERN = Pattern.compile("""
            \\b(
                summarize|
                summarise|
                summary|
                overview|
                describe\\s+(this|the)\\s+document|
                what\\s+is\\s+(this|the)\\s+document\\s+about|
                give\\s+me\\s+(a\\s+)?(summary|overview)|
                explain\\s+the\\s+(main|key|important)\\s+(points|ideas|内容)
            )\\b
            """, Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);

    @Override
    public QueryType classify(final String question) {

        if (DOCUMENT_WIDE_PATTERN.matcher(question).find()) {
            return QueryType.DOCUMENT_WIDE;
        }

        return QueryType.SPECIFIC;
    }
}
