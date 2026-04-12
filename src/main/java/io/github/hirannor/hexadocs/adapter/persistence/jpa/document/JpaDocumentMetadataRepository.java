package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentMetadataRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
class JpaDocumentMetadataRepository implements DocumentMetadataRepository {

    private final Function<DocumentEntity, Document> mapToDomain;
    private final Function<Document, DocumentEntity> mapToEntity;

    private final DocumentMetadataSpringDataJpaRepository documentMetadata;

    JpaDocumentMetadataRepository(final DocumentMetadataSpringDataJpaRepository documentMetadata) {
        this.documentMetadata = documentMetadata;
        this.mapToDomain = new DocumentEntityToDomainMapper();
        this.mapToEntity = new DocumentToEntityMapper();
    }

    @Override
    public void save(final Document document) {
        final DocumentEntity toPersist = mapToEntity.apply(document);
        documentMetadata.save(toPersist);
    }

    @Override
    public Optional<Document> findById(final DocumentId id) {
        return documentMetadata.findByDocumentId(id.asText())
                .map(mapToDomain);
    }

    @Override
    public List<Document> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
        return documentMetadata.findByKnowledgeBaseId(knowledgeBaseId.asText())
                .stream()
                .map(mapToDomain)
                .toList();
    }

    @Override
    public boolean existsById(final DocumentId id) {
        return documentMetadata.existsByDocumentId(id.asText());
    }

    @Override
    public void deleteById(final DocumentId id) {
        documentMetadata.deleteByDocumentId(id.asText());
    }
}
