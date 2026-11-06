package db;

import util.HashUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class AdminDAO {

    public void createAdmin(String username, String password) throws SQLException {
        String salt = HashUtil.generateSalt();
        String hash = HashUtil.hash(password, salt);
        String sql = "INSERT INTO admins (username, password_hash, salt) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setString(3, salt);
            ps.executeUpdate();
        }
    }

    public boolean verifyLogin(String username, String password) throws SQLException {
        String sql = "SELECT password_hash, salt FROM admins WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String salt = rs.getString("salt");
                    String attemptHash = HashUtil.hash(password, salt);
                    return MessageDigest.isEqual(
                            storedHash.getBytes(StandardCharsets.UTF_8),
                            attemptHash.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return false;
    }
}