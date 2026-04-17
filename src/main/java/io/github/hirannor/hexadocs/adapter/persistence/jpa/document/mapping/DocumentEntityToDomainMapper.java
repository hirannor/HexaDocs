package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentEntity;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentLanguageEntity;
import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import io.github.hirannor.hexadocs.domain.document.FileReference;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.function.Function;

public class DocumentEntityToDomainMapper implements Function<DocumentEntity, Document> {

    private final Function<DocumentLanguageEntity, DocumentLanguage> mapLanguage;

    public DocumentEntityToDomainMapper() {
        this.mapLanguage = new DocumentLanguageEntityToDomainMapper();
    }


    @Override
    public Document apply(final DocumentEntity entity) {
        if (entity == null) return null;

        return Document.empty()
                .id(DocumentId.from(entity.getDocumentId()))
                .kbId(KnowledgeBaseId.from(entity.getKnowledgeBaseId()))
                .name(entity.getName())
                .fileReference(FileReference.of(entity.getFileReference()))
                .language(mapLanguage.apply(entity.getLanguage()))
                .createDocument();
    }
}
