package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentEntity;
import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.FileReference;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.function.Function;

public class DocumentEntityToDomainMapper implements Function<DocumentEntity, Document> {
    @Override
    public Document apply(final DocumentEntity entity) {
        if (entity == null) return null;

        return Document.empty()
                .id(DocumentId.from(entity.getDocumentId()))
                .kbId(KnowledgeBaseId.from(entity.getKnowledgeBaseId()))
                .name(entity.getName())
                .fileReference(FileReference.of(entity.getFileReference()))
                .createDocument();
    }
}
