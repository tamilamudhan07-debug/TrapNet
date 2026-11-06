package detector;

import db.AttemptDAO;
import model.AttackAttempt;
import java.sql.SQLException;

public class BruteForceDetector extends AttackDetector {
    private final AttemptDAO attemptDAO;
    private final int maxAttempts;
    private final int windowSeconds;

    public BruteForceDetector(AttemptDAO attemptDAO, int maxAttempts, int windowSeconds) {
        super("BRUTE_FORCE", "MEDIUM");
        this.attemptDAO = attemptDAO;
        this.maxAttempts = maxAttempts;
        this.windowSeconds = windowSeconds;
    }

    @Override
    public boolean isAttack(AttackAttempt attempt) throws SQLException {
        int count = attemptDAO.countRecentAttempts(attempt.getIpAddress(), windowSeconds);
        return count >= maxAttempts;
    }

    @Override
    public String describe(AttackAttempt attempt) {
        return maxAttempts + " or more attempts within " + windowSeconds + " seconds";
    }
}