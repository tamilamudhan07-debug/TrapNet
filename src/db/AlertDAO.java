package db;

import java.sql.*;

public class AlertDAO {
    private final AttackerDAO attackerDAO = new AttackerDAO();

    public void saveAlert(String ip, String attackType, String severity, String description) throws SQLException {
        int attackerId = attackerDAO.getOrCreateAttackerId(ip);
        String sql = "INSERT INTO alerts (attacker_id, attack_type, severity, description) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attackerId);
            ps.setString(2, attackType);
            ps.setString(3, severity);
            ps.setString(4, description);
            ps.executeUpdate();
        }
    }
}
