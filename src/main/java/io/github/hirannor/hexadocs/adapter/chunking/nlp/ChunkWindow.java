package io.github.hirannor.hexadocs.adapter.chunking.nlp;

import java.util.List;

record ChunkWindow(List<String> sentences, int end) {
}