package io.github.hirannor.hexadocs.adapter.file.localfilesystem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.file",
        havingValue = "local-file-system-storage"
)
@EnableConfigurationProperties(value = LocalFileSystemDocumentStorageConfigurationProperties.class)
public class LocalFileSystemDocumentStorageConfiguration {
}
