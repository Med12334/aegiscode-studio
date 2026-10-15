package io.aegiscode.core.cybersec;

import io.aegiscode.core.contracts.CodeDiagnosticDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/cybersec")
@CrossOrigin(origins = "*")
public class CybersecEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CybersecEngineApplication.class, args);
    }

    public record AuditRequest(String language, String sourceCode) {}

    @PostMapping("/audit")
    public List<CodeDiagnosticDto> auditCodeSecurity(@RequestBody AuditRequest req) {
        List<CodeDiagnosticDto> findings = new ArrayList<>();
        String[] lines = req.sourceCode().split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (Pattern.compile("password\\s*=\\s*['\"].+['\"]", Pattern.CASE_INSENSITIVE).matcher(line).find() ||
                Pattern.compile("aws_secret_access_key", Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                findings.add(new CodeDiagnosticDto(
                    i + 1,
                    "CWE-798",
                    "CYBERSEC",
                    "CRITICAL",
                    "Hardcoded Cloud Credential or Password",
                    line,
                    "System.getenv(\"SECURE_TOKEN\")",
                    "Hardcoding credentials allows anyone with repository read access to compromise cloud infrastructure.",
                    "cwe798_hardcoded_secret"
                ));
            }

            if (line.toUpperCase().contains("SELECT ") && (line.contains("+") || line.contains("format("))) {
                findings.add(new CodeDiagnosticDto(
                    i + 1,
                    "CWE-89",
                    "CYBERSEC",
                    "CRITICAL",
                    "SQL Injection Vulnerability",
                    line,
                    "PreparedStatement pstmt = conn.prepareStatement(\"SELECT * FROM users WHERE id = ?\");",
                    "Direct string concatenation in SQL queries enables SQL Injection attacks.",
                    "cwe89_sql_injection"
                ));
            }
        }
        return findings;
    }
}
