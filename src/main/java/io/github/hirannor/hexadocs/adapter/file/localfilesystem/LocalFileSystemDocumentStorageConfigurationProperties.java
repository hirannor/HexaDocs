package io.github.hirannor.hexadocs.adapter.file.localfilesystem;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class LocalFileSystemDocumentStorageConfigurationProperties {
    private String LocalFileSystemPath;

    public LocalFileSystemDocumentStorageConfigurationProperties(final String localFileSystemPath) {
        LocalFileSystemPath = localFileSystemPath;
    }

    public String getLocalFileSystemPath() {
        return LocalFileSystemPath;
    }

    public void setLocalFileSystemPath(final String localFileSystemPath) {
        LocalFileSystemPath = localFileSystemPath;
    }
}
