-- Enable pgvector for AST Semantic Codebase Embeddings
CREATE EXTENSION IF NOT EXISTS vector;

-- Developer Mistake Memory Ledger (Spaced-Repetition Habit Tracker)
CREATE TABLE IF NOT EXISTS developer_mistake_notes (
    note_id UUID PRIMARY KEY,
    developer_handle VARCHAR(64) NOT NULL,
    language VARCHAR(32) NOT NULL,
    domain_category VARCHAR(32) NOT NULL, -- FULLSTACK, CYBERSEC, CLOUD_IAC, DATA_SCIENCE
    pattern_fingerprint VARCHAR(128) UNIQUE NOT NULL,
    mistake_title VARCHAR(160) NOT NULL,
    bad_code_snippet TEXT NOT NULL,
    corrected_code_snippet TEXT NOT NULL,
    mentor_side_note TEXT NOT NULL,
    occurrence_count INTEGER NOT NULL DEFAULT 1,
    severity VARCHAR(20) NOT NULL DEFAULT 'WARNING',
    last_triggered_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Polyglot Sandbox Execution & Debugger History
CREATE TABLE IF NOT EXISTS sandbox_execution_runs (
    run_id UUID PRIMARY KEY,
    language VARCHAR(32) NOT NULL,
    exit_code INTEGER NOT NULL,
    execution_time_ms BIGINT NOT NULL,
    stdout_log TEXT,
    stderr_trace TEXT,
    root_cause_summary TEXT,
    auto_patched_code TEXT,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_mistake_fingerprint ON developer_mistake_notes(pattern_fingerprint);
CREATE INDEX idx_mistake_language ON developer_mistake_notes(language);
