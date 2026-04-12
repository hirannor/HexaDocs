package io.github.hirannor.hexadocs;

import io.github.hirannor.hexadocs.adapter.ai.SpringAiConfiguration;
import io.github.hirannor.hexadocs.adapter.chunking.simple.SimpleTextChunkerConfiguration;
import io.github.hirannor.hexadocs.adapter.extraction.pdf.PdfTextExtractionConfiguration;
import io.github.hirannor.hexadocs.adapter.file.localfilesystem.LocalFileSystemDocumentStorageConfiguration;
import io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit.RabbitMqMessagingConfiguration;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.JpaPersistenceConfiguration;
import io.github.hirannor.hexadocs.adapter.web.rest.RestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Import({
        RestConfiguration.class,
        JpaPersistenceConfiguration.class,
        SpringAiConfiguration.class,
        RabbitMqMessagingConfiguration.class,
        PdfTextExtractionConfiguration.class,
        SimpleTextChunkerConfiguration.class,
        LocalFileSystemDocumentStorageConfiguration.class
})
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration.class,
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        }
)
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "io.github.hirannor.hexadocs.adapter.*"
                ),
        }
)
public class HexaDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexaDocsApplication.class, args);
    }

}
