import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AstAnalysisResponse, DeveloperMistakeNote, SandboxExecutionResult } from '../models/aegiscode.models';

@Injectable({ providedIn: 'root' })
export class AegisWorkstationService {
  private readonly http = inject(HttpClient);
  private readonly astEngineUrl = 'http://localhost:8085/api/v1/ast';
  private readonly ledgerUrl = 'http://localhost:8081/api/v1/ledger';
  private readonly sandboxUrl = 'http://localhost:8083/api/v1/sandbox';

  analyzeCodeAst(language: string, code: string, filePath = 'workspace_buffer'): Observable<AstAnalysisResponse> {
    return this.http.post<AstAnalysisResponse>(`${this.astEngineUrl}/analyze`, {
      language,
      code,
      file_path: filePath
    });
  }

  runInSandbox(language: string, code: string): Observable<SandboxExecutionResult> {
    return this.http.post<SandboxExecutionResult>(`${this.sandboxUrl}/run`, { language, code });
  }

  getDeveloperMistakeLedger(handle: string): Observable<DeveloperMistakeNote[]> {
    return this.http.get<DeveloperMistakeNote[]>(`${this.ledgerUrl}/notes/${handle}`);
  }
}
