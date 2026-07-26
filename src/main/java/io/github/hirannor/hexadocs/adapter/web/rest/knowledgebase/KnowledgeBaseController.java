package io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase;

import io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase.mapping.KnowledgeBaseToResponseMapper;
import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseCreation;
import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseDisplaying;
import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/knowledge-bases")
class KnowledgeBaseController {

    private final Function<KnowledgeBase, KnowledgeBaseResponse> mapToResponse;

    private final KnowledgeBaseCreation knowledgeBaseCreation;
    private final KnowledgeBaseDisplaying knowledgeBaseDisplaying;

    KnowledgeBaseController(final KnowledgeBaseCreation knowledgeBaseCreation,
            final KnowledgeBaseDisplaying knowledgeBaseDisplaying) {

        this.knowledgeBaseCreation = knowledgeBaseCreation;
        this.knowledgeBaseDisplaying = knowledgeBaseDisplaying;

        this.mapToResponse = new KnowledgeBaseToResponseMapper();
    }

    @PostMapping
    public ResponseEntity<CreateKnowledgeBaseResponse> create(@RequestBody final CreateKnowledgeBaseRequest request) {

        final KnowledgeBaseId knowledgeBaseId = knowledgeBaseCreation.create(
                CreateKnowledgeBase.issue(request.getName()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateKnowledgeBaseResponse(knowledgeBaseId.asText()));
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeBaseResponse>> displayAll() {

        final List<KnowledgeBaseResponse> knowledgeBases = knowledgeBaseDisplaying.displayAll()
                .stream()
                .map(mapToResponse)
                .toList();

        return ResponseEntity.ok(knowledgeBases);
    }
}