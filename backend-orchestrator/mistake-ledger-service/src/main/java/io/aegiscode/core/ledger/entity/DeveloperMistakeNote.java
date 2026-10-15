package io.aegiscode.core.ledger.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "developer_mistake_notes")
public class DeveloperMistakeNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "developer_handle", length = 64, nullable = false)
    private String developerHandle;

    @Column(name = "language", length = 32, nullable = false)
    private String language;

    @Column(name = "domain_category", length = 32, nullable = false)
    private String domainCategory;

    @Column(name = "pattern_fingerprint", length = 128, nullable = false, unique = true)
    private String patternFingerprint;

    @Column(name = "mistake_title", length = 160, nullable = false)
    private String mistakeTitle;

    @Column(name = "bad_code_snippet", columnDefinition = "TEXT", nullable = false)
    private String badCodeSnippet;

    @Column(name = "corrected_code_snippet", columnDefinition = "TEXT", nullable = false)
    private String correctedCodeSnippet;

    @Column(name = "mentor_side_note", columnDefinition = "TEXT", nullable = false)
    private String mentorSideNote;

    @Column(name = "occurrence_count", nullable = false)
    private int occurrenceCount = 1;

    @Column(name = "severity", length = 20, nullable = false)
    private String severity;

    @Column(name = "last_triggered_at", nullable = false)
    private Instant lastTriggeredAt = Instant.now();

    public DeveloperMistakeNote() {}

    public DeveloperMistakeNote(String developerHandle, String language, String domainCategory,
                                String patternFingerprint, String mistakeTitle, String badCodeSnippet,
                                String correctedCodeSnippet, String mentorSideNote, String severity) {
        this.developerHandle = developerHandle;
        this.language = language;
        this.domainCategory = domainCategory;
        this.patternFingerprint = patternFingerprint;
        this.mistakeTitle = mistakeTitle;
        this.badCodeSnippet = badCodeSnippet;
        this.correctedCodeSnippet = correctedCodeSnippet;
        this.mentorSideNote = mentorSideNote;
        this.occurrenceCount = 1;
        this.severity = severity;
        this.lastTriggeredAt = Instant.now();
    }

    public void incrementOccurrence() {
        this.occurrenceCount++;
        this.lastTriggeredAt = Instant.now();
    }

    public UUID getNoteId() { return noteId; }
    public String getDeveloperHandle() { return developerHandle; }
    public String getLanguage() { return language; }
    public String getDomainCategory() { return domainCategory; }
    public String getPatternFingerprint() { return patternFingerprint; }
    public String getMistakeTitle() { return mistakeTitle; }
    public String getBadCodeSnippet() { return badCodeSnippet; }
    public String getCorrectedCodeSnippet() { return correctedCodeSnippet; }
    public String getMentorSideNote() { return mentorSideNote; }
    public int getOccurrenceCount() { return occurrenceCount; }
    public String getSeverity() { return severity; }
}
