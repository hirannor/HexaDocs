package io.github.hirannor.hexadocs.adapter.file.localfilesystem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.file",
        havingValue = "local-file-system-storage"
)
public class LocalFileSystemDocumentStorageConfiguration {
}
