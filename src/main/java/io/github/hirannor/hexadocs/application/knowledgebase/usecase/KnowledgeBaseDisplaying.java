package io.github.hirannor.hexadocs.application.knowledgebase.usecase;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;

import java.util.List;

public interface KnowledgeBaseDisplaying {
    List<KnowledgeBase> displayAll();
}
