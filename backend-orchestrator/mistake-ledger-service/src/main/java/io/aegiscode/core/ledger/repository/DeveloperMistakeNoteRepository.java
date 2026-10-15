package io.aegiscode.core.ledger.repository;

import io.aegiscode.core.ledger.entity.DeveloperMistakeNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeveloperMistakeNoteRepository extends JpaRepository<DeveloperMistakeNote, UUID> {
    Optional<DeveloperMistakeNote> findByPatternFingerprint(String patternFingerprint);
    List<DeveloperMistakeNote> findByDeveloperHandleOrderByOccurrenceCountDesc(String developerHandle);
}
