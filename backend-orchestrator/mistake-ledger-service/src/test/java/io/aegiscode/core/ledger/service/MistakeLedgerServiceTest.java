package io.aegiscode.core.ledger.service;

import io.aegiscode.core.contracts.CodeDiagnosticDto;
import io.aegiscode.core.ledger.entity.DeveloperMistakeNote;
import io.aegiscode.core.ledger.repository.DeveloperMistakeNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MistakeLedgerServiceTest {

    @Mock
    private DeveloperMistakeNoteRepository repository;

    private MistakeLedgerService ledgerService;

    @BeforeEach
    void setUp() {
        ledgerService = new MistakeLedgerService(repository);
    }

    @Test
    void shouldIncrementOccurrenceWhenMistakeFingerprintAlreadyExists() {
        CodeDiagnosticDto diag = new CodeDiagnosticDto(
            12, "SEC_SQL_INJECTION", "CYBERSEC", "CRITICAL",
            "SQL Injection Risk", "SELECT * FROM users", "SELECT * FROM users WHERE id = ?",
            "Always use parameterized queries.", "fingerprint123"
        );

        DeveloperMistakeNote existing = new DeveloperMistakeNote(
            "Med12334", "python", "CYBERSEC", "fingerprint123",
            "SQL Injection Risk", "SELECT * FROM users", "SELECT * FROM users WHERE id = ?",
            "Always use parameterized queries.", "CRITICAL"
        );

        when(repository.findByPatternFingerprint("fingerprint123")).thenReturn(Optional.of(existing));
        when(repository.save(any(DeveloperMistakeNote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeveloperMistakeNote result = ledgerService.recordOrUpdateMistake("Med12334", "python", diag);

        assertEquals(2, result.getOccurrenceCount());
    }
}
