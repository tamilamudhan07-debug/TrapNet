package detector;

import db.AlertDAO;
import db.AttackerDAO;
import db.AttemptDAO;
import db.BlockedIpDAO;
import model.AttackAttempt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetectionEngine {
    public enum Result { NORMAL, ATTACK_DETECTED, BLOCKED }

    private final AttemptDAO attemptDAO = new AttemptDAO();
    private final AttackerDAO attackerDAO = new AttackerDAO();
    private final AlertDAO alertDAO = new AlertDAO();
    private final BlockedIpDAO blockedIpDAO = new BlockedIpDAO();
    private final List<AttackDetector> detectors = new ArrayList<>();

    public DetectionEngine() {
        detectors.add(new SQLInjectionDetector());
        detectors.add(new BruteForceDetector(attemptDAO, 5, 60));
    }

    public Result process(AttackAttempt attempt) throws SQLException {
        String ip = attempt.getIpAddress();

        attemptDAO.saveAttempt(attempt);          // always log the attempt first

        if (attackerDAO.isBlocked(ip)) {
            return Result.BLOCKED;                // already blocked, retry is logged
        }

        for (AttackDetector detector : detectors) {
            if (detector.isAttack(attempt)) {
                alertDAO.saveAlert(ip, detector.getAttackType(),
                        detector.getSeverity(), detector.describe(attempt));
                blockedIpDAO.blockIp(ip, detector.getAttackType());
                return Result.ATTACK_DETECTED;
            }
        }
        return Result.NORMAL;
    }
}
