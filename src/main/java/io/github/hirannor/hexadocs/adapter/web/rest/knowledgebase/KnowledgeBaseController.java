package io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase;

import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseCreation;
import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge-bases")
class KnowledgeBaseController {

    private final KnowledgeBaseCreation knowledgeBase;

    KnowledgeBaseController(final KnowledgeBaseCreation knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KnowledgeBaseId> create(@RequestBody CreateKnowledgeBaseRequest request) {
        final KnowledgeBaseId knowledgeBaseId = knowledgeBase.create(
                CreateKnowledgeBase.issue(request.name())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(knowledgeBaseId);
    }

}