package io.aegiscode.core.ledger.controller;

import io.aegiscode.core.contracts.CodeDiagnosticDto;
import io.aegiscode.core.ledger.entity.DeveloperMistakeNote;
import io.aegiscode.core.ledger.service.MistakeLedgerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ledger")
@CrossOrigin(origins = "*")
public class MistakeLedgerController {

    private final MistakeLedgerService ledgerService;

    public MistakeLedgerController(MistakeLedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    public record RecordMistakeRequest(String developerHandle, String language, CodeDiagnosticDto diagnostic) {}

    @PostMapping("/record")
    public DeveloperMistakeNote recordMistake(@RequestBody RecordMistakeRequest req) {
        return ledgerService.recordOrUpdateMistake(req.developerHandle(), req.language(), req.diagnostic());
    }

    @GetMapping("/notes/{handle}")
    public List<DeveloperMistakeNote> getNotes(@PathVariable String handle) {
        return ledgerService.getDeveloperNotes(handle);
    }
}
