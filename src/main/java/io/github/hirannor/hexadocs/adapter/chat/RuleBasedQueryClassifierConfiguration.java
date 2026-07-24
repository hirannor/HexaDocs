package io.github.hirannor.hexadocs.adapter.chat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(prefix = "adapter.chat",
        name = "query-classifier",
        havingValue = "rule-based",
        matchIfMissing = true)
public class RuleBasedQueryClassifierConfiguration {
}
