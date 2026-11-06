package detector;

import model.AttackAttempt;
import java.util.regex.Pattern;

public class SQLInjectionDetector extends AttackDetector {
    private static final Pattern[] PATTERNS = {
            Pattern.compile("'"),
            Pattern.compile("--"),
            Pattern.compile(";"),
            Pattern.compile("\\bor\\b\\s+\\S+\\s*=\\s*\\S+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bunion\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bselect\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bdrop\\b", Pattern.CASE_INSENSITIVE)
    };

    public SQLInjectionDetector() {
        super("SQL_INJECTION", "HIGH");
    }

    @Override
    public boolean isAttack(AttackAttempt attempt) {
        return matches(attempt.getUsernameTried()) || matches(attempt.getPasswordTried());
    }

    private boolean matches(String text) {
        if (text == null) return false;
        for (Pattern p : PATTERNS) {
            if (p.matcher(text).find()) return true;
        }
        return false;
    }

    @Override
    public String describe(AttackAttempt attempt) {
        return "SQL injection pattern found in login input";
    }
}