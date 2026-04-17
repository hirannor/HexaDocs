package io.github.hirannor.hexadocs;

import io.github.hirannor.hexadocs.adapter.ai.SpringAiConfiguration;
import io.github.hirannor.hexadocs.adapter.chunking.npl.NplTextChunkerConfiguration;
import io.github.hirannor.hexadocs.adapter.extraction.pdf.HybridPdfTextExtractionConfiguration;
import io.github.hirannor.hexadocs.adapter.file.localfilesystem.LocalFileSystemDocumentStorageConfiguration;
import io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit.RabbitMqMessagingConfiguration;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.JpaPersistenceConfiguration;
import io.github.hirannor.hexadocs.adapter.web.gui.GuiConfiguration;
import io.github.hirannor.hexadocs.adapter.web.rest.RestConfiguration;
import io.github.hirannor.hexadocs.adapter.web.websocket.WebSocketConfiguration;
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
        HybridPdfTextExtractionConfiguration.class,
        NplTextChunkerConfiguration.class,
        LocalFileSystemDocumentStorageConfiguration.class,
        WebSocketConfiguration.class,
        GuiConfiguration.class,
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
