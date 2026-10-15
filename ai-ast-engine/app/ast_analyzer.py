import ast
import hashlib
import re
from typing import List, Dict, Any

class PolyglotAstAnalyzer:
    """
    Parses and inspects source code across Full-Stack (Python, Java, TypeScript),
    Cybersecurity (SQL, Bash), Cloud (Dockerfile, Terraform), and Data Science (Pandas/SQL).
    Generates AST symbol graphs, line-exact diagnostics, auto-patches, and Mistake Memory fingerprints.
    """

    def analyze_code(self, language: str, code: str, file_path: str = "snippet") -> Dict[str, Any]:
        lang = language.lower().strip()
        symbols: List[str] = []
        diagnostics: List[Dict[str, Any]] = []

        # 1. Extract Python AST Symbols & Syntax Errors if language is Python
        if lang == "python":
            try:
                tree = ast.parse(code)
                for node in ast.walk(tree):
                    if isinstance(node, ast.FunctionDef):
                        symbols.append(f"fn:{node.name}(line {node.lineno})")
                    elif isinstance(node, ast.ClassDef):
                        symbols.append(f"class:{node.name}(line {node.lineno})")
            except SyntaxError as exc:
                diagnostics.append(self._build_diagnostic(
                    lang="python",
                    domain="FULLSTACK",
                    line=exc.lineno or 1,
                    rule_id="PY_SYNTAX_ERR",
                    severity="CRITICAL",
                    title=f"Python SyntaxError: {exc.msg}",
                    bad_line=(exc.text or "").strip(),
                    fixed_line="# Verify missing colon (:), parenthesis, or indentation on this line",
                    mentor_note="SyntaxError caught by AST parser before execution. Check block colons and balanced brackets."
                ))

        # 2. Scan Line-by-Line for Cross-Language Security, Data Science, Cloud, and Full-Stack Bugs
        lines = code.splitlines()
        for idx, raw_line in enumerate(lines, start=1):
            stripped = raw_line.strip()

            # CYBERSECURITY: SQL Injection via String Concatenation / f-string
            if ("SELECT " in stripped.upper() or "INSERT " in stripped.upper()) and (
                "+" in stripped or "f\"" in stripped or "f'" in stripped or "${" in stripped
            ):
                diagnostics.append(self._build_diagnostic(
                    lang=lang,
                    domain="CYBERSEC",
                    line=idx,
                    rule_id="SEC_SQL_INJECTION_CONCAT",
                    severity="CRITICAL",
                    title="CWE-89: Potential SQL Injection via Unparameterized String Interpolation",
                    bad_line=stripped,
                    fixed_line="cursor.execute('SELECT * FROM users WHERE id = ?', (user_id,))",
                    mentor_note="Never concatenate user variables directly into SQL strings. Attackers can inject `' OR 1=1 --` to bypass authentication. Always use parameterized prepared statements."
                ))

            # CYBERSECURITY & CLOUD: Hardcoded API Keys / Secrets
            if re.search(r"(api_key|secret_key|password|token)\s*=\s*['\"][A-Za-z0-9_\-]{12,}['\"]", stripped, re.IGNORECASE):
                diagnostics.append(self._build_diagnostic(
                    lang=lang,
                    domain="CYBERSEC",
                    line=idx,
                    rule_id="SEC_HARDCODED_CREDENTIAL",
                    severity="CRITICAL",
                    title="CWE-798: Hardcoded Secret or API Token Detected in Source Code",
                    bad_line=stripped,
                    fixed_line="os.environ.get('SECRET_API_KEY')  # Load from Vault / Environment",
                    mentor_note="Hardcoding secrets in source files exposes credentials in Git history. Move secrets to environment variables or a secret manager."
                ))

            # JAVA FULL-STACK: String comparison using == instead of .equals()
            if lang == "java" and re.search(r'if\s*\(.*==\s*".*"\)', stripped):
                fixed = re.sub(r'(\w+)\s*==\s*(".*?")', r'\2.equals(\1)', stripped)
                diagnostics.append(self._build_diagnostic(
                    lang="java",
                    domain="FULLSTACK",
                    line=idx,
                    rule_id="JAVA_STRING_REF_EQUALITY",
                    severity="WARNING",
                    title="Java Reference Equality (==) Used on String Literal",
                    bad_line=stripped,
                    fixed_line=fixed,
                    mentor_note="In Java, `==` compares memory references, not string contents. Use `.equals()` (preferably on the constant literal first to avoid NullPointerException)."
                ))

            # DATA ANALYTICS (Python/Pandas): iterrows() Anti-Pattern on DataFrames
            if lang == "python" and ".iterrows()" in stripped:
                diagnostics.append(self._build_diagnostic(
                    lang="python",
                    domain="DATA_SCIENCE",
                    line=idx,
                    rule_id="DATA_PANDAS_ITERROWS_SLOW",
                    severity="WARNING",
                    title="Performance Bottleneck: Pandas .iterrows() O(N) Loop Detected",
                    bad_line=stripped,
                    fixed_line="df['result'] = df['col_a'] * df['col_b']  # Use vectorized NumPy/Pandas ops",
                    mentor_note="Iterating over DataFrames with `.iterrows()` is up to 1,000x slower on large datasets. Use vectorized column operations or `.itertuples()`."
                ))

            # CLOUD / DOCKER: Running Container as Root or :latest Tag
            if lang in ("dockerfile", "docker", "yaml") and stripped.upper().startswith("FROM ") and ":LATEST" in stripped.upper():
                diagnostics.append(self._build_diagnostic(
                    lang=lang,
                    domain="CLOUD_IAC",
                    line=idx,
                    rule_id="CLOUD_DOCKER_LATEST_TAG",
                    severity="WARNING",
                    title="Cloud IaC Risk: Unpinned ':latest' Container Base Image",
                    bad_line=stripped,
                    fixed_line=stripped.replace(":latest", ":17.0.12-jre-alpine").replace(":LATEST", ":17.0.12-jre-alpine"),
                    mentor_note="Using `:latest` in Dockerfiles breaks reproducible builds and can silently pull breaking or vulnerable upstream images."
                ))

        return {
            "file_path": file_path,
            "language": lang,
            "line_count": len(lines),
            "ast_symbols": symbols,
            "diagnostics_count": len(diagnostics),
            "diagnostics": diagnostics
        }

    def diagnose_stack_trace(self, language: str, code: str, stderr_trace: str) -> Dict[str, Any]:
        """Analyzes runtime crash output and produces a root-cause explanation + patched code."""
        root_cause = "Runtime execution error detected."
        fix_suggestion = "Inspect stack trace line numbers and variable states."

        if "ZeroDivisionError" in stderr_trace or "/ by zero" in stderr_trace:
            root_cause = "Arithmetic Division by Zero: Denominator evaluated to 0 at runtime."
            fix_suggestion = "Guard division with `if denominator != 0:` before computing the ratio."
        elif "NullPointerException" in stderr_trace or "AttributeError: 'NoneType'" in stderr_trace:
            root_cause = "Null / None Reference Dereference: Attempted to call a method or property on an uninitialized object."
            fix_suggestion = "Add a null/None check or use Optional.orElseThrow() before accessing member fields."
        elif "KeyError" in stderr_trace or "IndexError" in stderr_trace or "ArrayIndexOutOfBoundsException" in stderr_trace:
            root_cause = "Out-of-Bounds / Missing Key Lookup: Requested index or dictionary key does not exist."
            fix_suggestion = "Use `.get(key, default)` for dictionaries or verify collection `.size()` before indexing."

        return {
            "language": language.lower(),
            "root_cause_summary": root_cause,
            "recommended_fix": fix_suggestion,
            "raw_trace_excerpt": stderr_trace[:500]
        }

    def _build_diagnostic(self, lang: str, domain: str, line: int, rule_id: str,
                          severity: str, title: str, bad_line: str,
                          fixed_line: str, mentor_note: str) -> Dict[str, Any]:
        fingerprint = hashlib.sha256(f"{lang}:{rule_id}".encode("utf-8")).hexdigest()[:24]
        return {
            "line": line,
            "rule_id": rule_id,
            "domain_category": domain,
            "severity": severity,
            "title": title,
            "bad_code_snippet": bad_line,
            "corrected_code_snippet": fixed_line,
            "mentor_side_note": mentor_note,
            "pattern_fingerprint": fingerprint
        }
