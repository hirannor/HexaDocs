package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.extraction",
        havingValue = "pdf-text"
)
public class PdfTextExtractionConfiguration {
}
