package io.github.hirannor.hexadocs.adapter.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentChunk;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentContentReader;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
class SpringAiPgVectorDocumentContentReader implements DocumentContentReader {

    private static final String KNOWLEDGE_BASE_ID_METADATA_KEY = "knowledgeBaseId";

    private static final String CHUNK_ORDER_METADATA_KEY = "chunkOrder";

    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    SpringAiPgVectorDocumentContentReader(final JdbcTemplate jdbcTemplate, final ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<DocumentChunk> readAll(final KnowledgeBaseId knowledgeBaseId) {
        return jdbcTemplate.query("""
                SELECT id, content, metadata
                FROM vector_store
                WHERE metadata ->> ? = ?
                ORDER BY (metadata ->> ?)::integer
                """, this::mapRow, KNOWLEDGE_BASE_ID_METADATA_KEY, knowledgeBaseId.asText(), CHUNK_ORDER_METADATA_KEY);
    }

    private DocumentChunk mapRow(final ResultSet resultSet, final int rowNumber) throws SQLException {
        return new DocumentChunk(resultSet.getString("id"), resultSet.getString("content"),
                parseMetadata(resultSet.getObject("metadata")));
    }

    private Map<String, Object> parseMetadata(final Object metadata) throws SQLException {
        final String json = metadata.toString();

        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new SQLException("Failed to parse vector document metadata", exception);
        }
    }
}
