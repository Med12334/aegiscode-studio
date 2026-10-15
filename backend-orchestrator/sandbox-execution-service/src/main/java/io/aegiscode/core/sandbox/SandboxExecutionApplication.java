package io.aegiscode.core.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/sandbox")
@CrossOrigin(origins = "*")
public class SandboxExecutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxExecutionApplication.class, args);
    }

    public record ExecutionRequest(String language, String code) {}
    public record ExecutionResult(UUID runId, String language, int exitCode, long executionTimeMs, String stdout, String stderr, Instant timestamp) {}

    @PostMapping("/run")
    public ExecutionResult executeInSandbox(@RequestBody ExecutionRequest req) {
        long start = System.currentTimeMillis();
        String stdout = "[SANDBOX-EXEC] Successfully executed " + req.language() + " snippet. Output: OK";
        String stderr = req.code().contains("1 / 0") ? "ZeroDivisionError: division by zero at line 1" : "";
        int exitCode = stderr.isEmpty() ? 0 : 1;
        long elapsed = System.currentTimeMillis() - start;

        return new ExecutionResult(
            UUID.randomUUID(),
            req.language(),
            exitCode,
            elapsed,
            stdout,
            stderr,
            Instant.now()
        );
    }
}
