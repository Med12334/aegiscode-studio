package io.aegiscode.core.contracts;

public record CodeDiagnosticDto(
    int line,
    String ruleId,
    String domainCategory,
    String severity,
    String title,
    String badCodeSnippet,
    String correctedCodeSnippet,
    String mentorSideNote,
    String patternFingerprint
) {}
