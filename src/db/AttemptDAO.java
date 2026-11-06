package db;

import model.AttackAttempt;
import java.sql.*;

public class AttemptDAO {
    private final AttackerDAO attackerDAO = new AttackerDAO();

    // Saves one login attempt. The database sets the time itself.
    public void saveAttempt(AttackAttempt attempt) throws SQLException {
        int attackerId = attackerDAO.getOrCreateAttackerId(attempt.getIpAddress());
        String sql = "INSERT INTO attempts (attacker_id, username_tried, password_tried) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attackerId);
            ps.setString(2, limit(attempt.getUsernameTried(), 100));
            ps.setString(3, limit(attempt.getPasswordTried(), 100));
            ps.executeUpdate();
        }
    }

    // How many attempts did this IP make in the last N seconds?
    public int countRecentAttempts(String ip, int seconds) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attempts a "
                + "JOIN attackers k ON a.attacker_id = k.attacker_id "
                + "WHERE k.ip_address = ? AND a.attempt_time >= NOW() - INTERVAL ? SECOND";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ip);
            ps.setInt(2, seconds);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Cuts long text so it fits the column (attackers may paste very long input)
    private String limit(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max) : text;
    }
}