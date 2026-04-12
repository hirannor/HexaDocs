# 📚 HexaDocs

A **domain-driven, event-driven document intelligence system** built with **Hexagonal Architecture**, designed to
support semantic document ingestion, chunking, embedding, and AI-powered retrieval (RAG).

---

## 🚀 Overview

HexaDocs is a backend system that allows users to:

- Upload documents into a **Knowledge Base**
- Automatically process and split documents into semantic chunks
- Generate embeddings for semantic search
- Store vectors in a dedicated vector store
- Enable AI-powered question answering over documents

The system is designed around **clean architecture principles**, strong domain modeling, and asynchronous event-driven
workflows.

---

## 🧠 Core Idea

Instead of treating documents as static files, HexaDocs transforms them into:

> **Structured, searchable knowledge units powered by embeddings**

Each document becomes:

- A set of semantic chunks
- Indexed

---

## 🧩 Core Capabilities

### 📄 Document Ingestion

- Upload documents (PDF)
- Extract and normalize content
- Split into chunks

### 🧠 Embeddings

- Generate embeddings using:
    - `nomic-embed-text` (Ollama)
- Store vectors in `pgvector`

### 🔎 Semantic Search (RAG)

- Query embedding generation
- Vector similarity search
- Context retrieval for LLM

### 💬 AI Chat

- Uses `mistral` via Ollama
- Context-aware answers (RAG-based)

### ⚡ Event-Driven Pipeline

- RabbitMQ-based async processing
- Decoupled ingestion and embedding pipeline

---

## 🚀 Running the system

Start all services:

```bash
docker-compose up -d