package io.github.hirannor.hexadocs.adapter.chunking.simple;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.chunking",
        havingValue = "simple-text"
)
public class SimpleTextChunkerConfiguration {
}
