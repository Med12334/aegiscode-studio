# AegisCode Studio — Polyglot AI Workstation, SAST Security Engine & Mistake Memory Ledger

AegisCode Studio is a multi-layered desktop developer workstation and static/dynamic code intelligence platform. Designed for software engineers across **Full-Stack, Cybersecurity, Cloud/DevOps, and Data Science**, it combines multi-language AST analysis, isolated sandbox execution, OWASP security auditing, and a persistent **Mistake Memory Ledger** that pins contextual side-notes beside recurring coding errors so developers never repeat the same mistake twice.

## Tech Stack
* **Desktop IDE Shell (`desktop-ide/`):** Electron (Windows Native Shell) + Angular 18 (Standalone Components, Signals, OnPush)
* **Polyglot AST & Root-Cause Engine (`ai-ast-engine/`):** Python 3.11, FastAPI, Abstract Syntax Tree (AST) Parser, Stack-Trace Debugger
* **Backend Orchestrator (`backend-orchestrator/`):** Java 17, Spring Boot 3.3, Spring Data JPA, Flyway, JUnit 5 & Mockito
* **Storage & Event Bus (`infra/`):** PostgreSQL 16 + `pgvector` (Semantic Embeddings & Mistake Memory Ledger), Apache Kafka (KRaft mode)

---

## 7-Layer System Architecture

    +-----------------------------------------------------------------------------------+
    |            WINDOWS DESKTOP IDE SHELL (Electron + Angular 18 Workstation)          |
    |  [Polyglot Code Editor + AST Highlights] | [Side-by-Side Mistake Memory Notes]    |
    |  [1-Click Inline Auto-Patch Diffs]       | [Interactive Sandbox Debug Console]    |
    +---------------------+-----------------------------------------+-------------------+
                          |                                         |
                          v (Port 8081-8083)                        v (Port 8085)
    +-----------------------------------------+   +-------------------------------------+
    |   JAVA 17 SPRING BOOT ORCHESTRATOR      |   |   PYTHON 3.11 AST & RAG ENGINE      |
    | - mistake-ledger-service (PostgreSQL)   |   | - Multi-Language AST Symbol Graph   |
    | - cybersec-sast-engine (CWE / OWASP)    |   | - Data Science & Cloud Anti-Patterns|
    | - sandbox-execution-service (Runner)    |   | - Runtime Crash Root-Cause Debugger |
    +---------------------+-------------------+   +-----------------+-------------------+
                          |                                         |
                          +--------------------+--------------------+
                                               |
                                               v
                  +---------------------------------------------------------+
                  |    POSTGRESQL 16 (pgvector) + APACHE KAFKA EVENT BUS    |
                  +---------------------------------------------------------+

---

## Core Capabilities Across Engineering Domains

1. **Persistent Mistake Memory Ledger (`mistake-ledger-service`):**
   * Computes a deterministic SHA-256 fingerprint for every syntax, logic, performance, or security bug detected in the editor.
   * Tracks `occurrence_count` in PostgreSQL and pins a persistent **"Ghost Mentor Note"** in the right-hand IDE gutter explaining *why* the bug happened and *how* to fix it permanently.
2. **Real-Time Cybersecurity & Cloud IaC Auditing (`cybersec-sast-engine`):**
   * Scans source buffers in real time for **CWE-89 (SQL Injection)**, **CWE-798 (Hardcoded Cloud Credentials/Secrets)**, and unpinned Docker/IaC configurations.
3. **Data Science & Full-Stack Performance Profiler (`ai-ast-engine`):**
   * Detects expensive DataFrame anti-patterns (such as `.iterrows()` loops in Python/Pandas) and reference-equality traps (`==` on Strings in Java) and offers 1-click vectorized/idiomatic replacements.
4. **Isolated Polyglot Sandbox & Crash Debugger (`sandbox-execution-service`):**
   * Executes code snippets, captures `stdout`/`stderr` and execution timing, and translates raw stack traces into human-readable root-cause diagnostics.

---

## Getting Started

### 1. Start PostgreSQL (pgvector) & Apache Kafka
    cd infra
    docker-compose up -d

### 2. Run the Python AST & Root-Cause Engine
    cd ai-ast-engine
    pip install -r requirements.txt
    uvicorn app.main:app --port 8085

### 3. Build & Test the Java 17 Spring Boot Orchestrator
    cd backend-orchestrator
    mvn clean test package

### 4. Launch the Windows Desktop IDE (Electron + Angular 18)
    cd desktop-ide
    npm install
    npm start
