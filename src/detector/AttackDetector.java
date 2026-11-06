package detector;

import model.AttackAttempt;
import java.sql.SQLException;

public abstract class AttackDetector {
    private final String attackType;
    private final String severity;

    protected AttackDetector(String attackType, String severity) {
        this.attackType = attackType;
        this.severity = severity;
    }

    public String getAttackType() { return attackType; }
    public String getSeverity() { return severity; }

    // Every detector must answer: is this attempt an attack?
    public abstract boolean isAttack(AttackAttempt attempt) throws SQLException;

    // Every detector must explain why it flagged the attempt
    public abstract String describe(AttackAttempt attempt);
}