package io.github.hirannor.hexadocs.adapter.chunking.npl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.chunking",
        havingValue = "npl"
)
public class NplTextChunkerConfiguration {
}
