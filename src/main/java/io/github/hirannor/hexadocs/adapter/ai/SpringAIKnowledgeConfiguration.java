package io.github.hirannor.hexadocs.adapter.ai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.ai",
        havingValue = "spring-ai"
)
public class SpringAIKnowledgeConfiguration {

}
