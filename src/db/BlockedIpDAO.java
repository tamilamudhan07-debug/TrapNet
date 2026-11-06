package db;

import java.sql.*;

public class BlockedIpDAO {
    private final AttackerDAO attackerDAO = new AttackerDAO();

    // INSERT IGNORE means blocking the same IP twice causes no error
    public void blockIp(String ip, String reason) throws SQLException {
        int attackerId = attackerDAO.getOrCreateAttackerId(ip);
        String sql = "INSERT IGNORE INTO blocked_ips (attacker_id, reason) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attackerId);
            ps.setString(2, reason);
            ps.executeUpdate();
        }
    }
}