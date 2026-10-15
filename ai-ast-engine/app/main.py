from fastapi import FastAPI
from pydantic import BaseModel
from typing import Optional
from app.ast_analyzer import PolyglotAstAnalyzer

app = FastAPI(
    title="AegisCode Studio — Polyglot AST & Root-Cause Intelligence Engine",
    version="1.0.0"
)

analyzer = PolyglotAstAnalyzer()

class CodeAnalysisRequest(BaseModel):
    language: str
    code: str
    file_path: Optional[str] = "workspace_buffer"

class CrashTraceRequest(BaseModel):
    language: str
    code: str
    stderr_trace: str

@app.get("/health")
def health_check():
    return {"status": "UP", "engine": "AegisCode-Polyglot-AST-v1"}

@app.post("/api/v1/ast/analyze")
def analyze_source_code(req: CodeAnalysisRequest):
    return analyzer.analyze_code(req.language, req.code, req.file_path or "workspace_buffer")

@app.post("/api/v1/ast/debug-trace")
def analyze_crash_trace(req: CrashTraceRequest):
    return analyzer.diagnose_stack_trace(req.language, req.code, req.stderr_trace)
