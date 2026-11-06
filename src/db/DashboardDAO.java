package db;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DashboardDAO {

    public int getTotalAttempts()  throws SQLException { return scalar("SELECT COUNT(*) FROM attempts"); }
    public int getTotalAttackers() throws SQLException { return scalar("SELECT COUNT(*) FROM attackers"); }
    public int getTotalBlocked()   throws SQLException { return scalar("SELECT COUNT(*) FROM blocked_ips"); }
    public int getTotalAlerts()    throws SQLException { return scalar("SELECT COUNT(*) FROM alerts"); }

    public DefaultTableModel getTopAttackers() throws SQLException {
        return table("SELECT * FROM top_attackers");
    }

    public DefaultTableModel getCommonPasswords() throws SQLException {
        return table("SELECT * FROM common_passwords");
    }

    public DefaultTableModel getRecentAlerts() throws SQLException {
        return table("SELECT a.alert_time, k.ip_address, a.attack_type, a.severity, a.description "
                + "FROM alerts a JOIN attackers k ON a.attacker_id = k.attacker_id "
                + "ORDER BY a.alert_time DESC LIMIT 20");
    }
    public DefaultTableModel getRecentAttempts() throws SQLException {
        return table("SELECT a.attempt_time, k.ip_address, a.username_tried, a.password_tried "
                + "FROM attempts a JOIN attackers k ON a.attacker_id = k.attacker_id "
                + "ORDER BY a.attempt_time DESC LIMIT 100");
    }
    // The queries above are fixed text with no user input, so a plain Statement is safe here
    private int scalar(String sql) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private DefaultTableModel table(String sql) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            for (int i = 1; i <= cols; i++) {
                model.addColumn(md.getColumnLabel(i));
            }
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            return model;
        }
    }
}