export interface CodeDiagnostic {
  line: number;
  rule_id: string;
  domain_category: 'FULLSTACK' | 'CYBERSEC' | 'CLOUD_IAC' | 'DATA_SCIENCE';
  severity: 'CRITICAL' | 'WARNING' | 'INFO';
  title: string;
  bad_code_snippet: string;
  corrected_code_snippet: string;
  mentor_side_note: string;
  pattern_fingerprint: string;
}

export interface AstAnalysisResponse {
  file_path: string;
  language: string;
  line_count: number;
  ast_symbols: string[];
  diagnostics_count: number;
  diagnostics: CodeDiagnostic[];
}

export interface DeveloperMistakeNote {
  noteId: string;
  developerHandle: string;
  language: string;
  domainCategory: string;
  patternFingerprint: string;
  mistakeTitle: string;
  badCodeSnippet: string;
  correctedCodeSnippet: string;
  mentorSideNote: string;
  occurrenceCount: number;
  severity: string;
}

export interface SandboxExecutionResult {
  runId: string;
  language: string;
  exitCode: number;
  executionTimeMs: number;
  stdout: string;
  stderr: string;
  timestamp: string;
}
