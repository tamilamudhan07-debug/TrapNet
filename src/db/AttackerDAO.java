package db;

import java.sql.*;

public class AttackerDAO {

    // Returns the attacker's ID for this IP. If the IP is new, creates a row first.
    public int getOrCreateAttackerId(String ip) throws SQLException {
        String find = "SELECT attacker_id FROM attackers WHERE ip_address = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(find)) {
            ps.setString(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("attacker_id");
                }
            }
        }

        String insert = "INSERT INTO attackers (ip_address) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ip);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Could not create attacker for IP " + ip);
    }

    public boolean isBlocked(String ip) throws SQLException {
        String sql = "SELECT is_blocked FROM attackers WHERE ip_address = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_blocked");
                }
            }
        }
        return false;
    }
}