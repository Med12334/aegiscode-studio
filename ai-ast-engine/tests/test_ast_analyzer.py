from app.ast_analyzer import PolyglotAstAnalyzer

def test_detects_sql_injection_and_pandas_iterrows():
    analyzer = PolyglotAstAnalyzer()
    sample_py = """
def load_user_data(conn, user_id, df):
    query = f"SELECT * FROM accounts WHERE user_id = '{user_id}'"
    for idx, row in df.iterrows():
        print(row)
"""
    result = analyzer.analyze_code("python", sample_py, "analytics_service.py")
    assert result["diagnostics_count"] == 2
    rule_ids = {d["rule_id"] for d in result["diagnostics"]}
    assert "SEC_SQL_INJECTION_CONCAT" in rule_ids
    assert "DATA_PANDAS_ITERROWS_SLOW" in rule_ids
    assert any("fn:load_user_data" in sym for sym in result["ast_symbols"])

def test_detects_java_string_reference_equality():
    analyzer = PolyglotAstAnalyzer()
    sample_java = '''
public class AuthValidator {
    public boolean isAdmin(String role) {
        if (role == "ADMIN") {
            return true;
        }
        return false;
    }
}
'''
    result = analyzer.analyze_code("java", sample_java, "AuthValidator.java")
    assert result["diagnostics_count"] == 1
    diag = result["diagnostics"][0]
    assert diag["rule_id"] == "JAVA_STRING_REF_EQUALITY"
    assert '"ADMIN".equals(role)' in diag["corrected_code_snippet"]
