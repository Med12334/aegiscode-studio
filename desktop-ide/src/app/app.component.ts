import { Component, ChangeDetectionStrategy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AegisWorkstationService } from './services/aegis-workstation.service';
import { CodeDiagnostic, DeveloperMistakeNote, SandboxExecutionResult } from './models/aegiscode.models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="ide-workstation">
      <header class="ide-topbar">
        <h1>AEGISCODE STUDIO // Polyglot AST Debugger, Cybersec SAST & Mistake Memory Ledger</h1>
        <div class="toolbar">
          <select [(ngModel)]="selectedLanguage" name="lang">
            <option value="python">Python (Data Science / AI / Backend)</option>
            <option value="java">Java 17 (Enterprise Full-Stack)</option>
            <option value="typescript">TypeScript (Angular / Node)</option>
            <option value="sql">SQL (PostgreSQL / DB2 Analytics)</option>
            <option value="dockerfile">Dockerfile / Cloud IaC</option>
          </select>
          <button (click)="inspectAndHighlight()">AST Inspect & Pin Mentor Notes</button>
          <button (click)="executeInSandbox()">Run & Debug in Sandbox</button>
        </div>
      </header>

      <main class="ide-split-pane">
        <!-- Left Pane: Polyglot Code Editor -->
        <section class="editor-pane">
          <h2>Active Code Buffer ({{ selectedLanguage | uppercase }})</h2>
          <textarea [(ngModel)]="codeBuffer" rows="18" class="code-textarea"></textarea>

          <div class="terminal-console" *ngIf="sandboxResult() as run">
            <h3>Sandbox Execution Output (Exit Code: {{ run.exitCode }} | {{ run.executionTimeMs }}ms)</h3>
            <pre class="stdout">{{ run.stdout }}</pre>
            <pre class="stderr" *ngIf="run.stderr">{{ run.stderr }}</pre>
          </div>
        </section>

        <!-- Right Pane: Side-by-Side Ghost Mentor & Mistake Memory Notes -->
        <aside class="mentor-gutter-pane">
          <h2>Pinned Mistake Memory Notes (Don't Repeat History)</h2>
          <div class="note-card" *ngFor="let diag of activeDiagnostics()">
            <div class="note-header">
              <span class="badge">[{{ diag.domain_category }} // {{ diag.severity }}]</span>
              <strong>Line {{ diag.line }}: {{ diag.title }}</strong>
            </div>
            <p class="bad-code">❌ Mistake: <code>{{ diag.bad_code_snippet }}</code></p>
            <p class="fixed-code">✅ Auto-Fix: <code>{{ diag.corrected_code_snippet }}</code></p>
            <blockquote class="mentor-note">💡 Why & How to Avoid: {{ diag.mentor_side_note }}</blockquote>
            <button (click)="applyInlineFix(diag)">Apply 1-Click Fix to Editor</button>
          </div>
        </aside>
      </main>
    </div>
  `
})
export class AppComponent {
  private readonly workstation = inject(AegisWorkstationService);

  selectedLanguage = 'python';
  codeBuffer = `def load_user_records(conn, user_id, df):\n    query = f"SELECT * FROM accounts WHERE id = '{user_id}'"\n    for idx, row in df.iterrows():\n        print(row)`;

  readonly activeDiagnostics = signal<CodeDiagnostic[]>([]);
  readonly mistakeLedger = signal<DeveloperMistakeNote[]>([]);
  readonly sandboxResult = signal<SandboxExecutionResult | null>(null);

  inspectAndHighlight(): void {
    this.workstation.analyzeCodeAst(this.selectedLanguage, this.codeBuffer).subscribe(res => {
      this.activeDiagnostics.set(res.diagnostics);
    });
  }

  executeInSandbox(): void {
    this.workstation.runInSandbox(this.selectedLanguage, this.codeBuffer).subscribe(res => {
      this.sandboxResult.set(res);
    });
  }

  applyInlineFix(diag: CodeDiagnostic): void {
    this.codeBuffer = this.codeBuffer.replace(diag.bad_code_snippet, diag.corrected_code_snippet);
  }
}
