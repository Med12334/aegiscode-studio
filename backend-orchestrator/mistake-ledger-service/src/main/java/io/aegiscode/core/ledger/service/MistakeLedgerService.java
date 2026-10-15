package io.aegiscode.core.ledger.service;

import io.aegiscode.core.contracts.CodeDiagnosticDto;
import io.aegiscode.core.ledger.entity.DeveloperMistakeNote;
import io.aegiscode.core.ledger.repository.DeveloperMistakeNoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MistakeLedgerService {

    private final DeveloperMistakeNoteRepository repository;

    public MistakeLedgerService(DeveloperMistakeNoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DeveloperMistakeNote recordOrUpdateMistake(String handle, String language, CodeDiagnosticDto diag) {
        Optional<DeveloperMistakeNote> existing = repository.findByPatternFingerprint(diag.patternFingerprint());
        if (existing.isPresent()) {
            DeveloperMistakeNote note = existing.get();
            note.incrementOccurrence();
            return repository.save(note);
        }

        DeveloperMistakeNote newNote = new DeveloperMistakeNote(
            handle,
            language,
            diag.domainCategory(),
            diag.patternFingerprint(),
            diag.title(),
            diag.badCodeSnippet(),
            diag.correctedCodeSnippet(),
            diag.mentorSideNote(),
            diag.severity()
        );
        return repository.save(newNote);
    }

    public List<DeveloperMistakeNote> getDeveloperNotes(String handle) {
        return repository.findByDeveloperHandleOrderByOccurrenceCountDesc(handle);
    }
}
