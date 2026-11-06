package ui;
import detector.DetectionEngine;
import model.AttackAttempt;
import db.DashboardDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardFrame extends JFrame {
    private final DashboardDAO dao = new DashboardDAO();
    private final JLabel attemptsLbl = new JLabel("0");
    private final JLabel attackersLbl = new JLabel("0");
    private final JLabel blockedLbl = new JLabel("0");
    private final JLabel alertsLbl = new JLabel("0");
    private final JTable topTable = new JTable();
    private final JTable passTable = new JTable();
    private final JTable alertTable = new JTable();
    private final JTable attemptTable = new JTable();          // NEW

    public DashboardFrame() {
        setTitle("TrapNet Security Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel summary = new JPanel(new GridLayout(1, 4, 10, 10));
        summary.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        summary.add(card("Total attempts", attemptsLbl));
        summary.add(card("Attackers seen", attackersLbl));
        summary.add(card("IPs blocked", blockedLbl));
        summary.add(card("Alerts raised", alertsLbl));
        add(summary, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Top attackers", new JScrollPane(topTable));
        tabs.addTab("Most-tried passwords", new JScrollPane(passTable));
        tabs.addTab("Recent alerts", new JScrollPane(alertTable));
        tabs.addTab("Recent attempts", new JScrollPane(attemptTable));   // NEW
        add(tabs, BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh now");
        refresh.addActionListener(e -> loadData());
        JButton simulate = new JButton("Simulate attack");
        simulate.addActionListener(e -> runSimulation());
        JPanel bottom = new JPanel();
        bottom.add(refresh);
        bottom.add(simulate);
        add(bottom, BorderLayout.SOUTH);
        loadData();
        new javax.swing.Timer(5000, e -> loadData()).start();   // auto-refresh every 5 seconds
    }

    private JPanel card(String title, JLabel value) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(new Font("SansSerif", Font.BOLD, 26));
        p.add(new JLabel(title, SwingConstants.CENTER));
        p.add(value);
        return p;
    }

    // Fires a fake attack from a random IP on a background thread
    private void runSimulation() {
        new Thread(() -> {
            try {
                DetectionEngine engine = new DetectionEngine();
                java.util.Random rnd = new java.util.Random();
                String ip = "10.0." + rnd.nextInt(250) + "." + (1 + rnd.nextInt(250));
                String[] pw = {"123456", "password", "admin123", "qwerty", "letmein", "welcome1", "admin@123"};

                if (rnd.nextBoolean()) {
                    for (int i = 0; i < 7; i++) {          // brute force: 7 quick guesses
                        engine.process(new AttackAttempt(ip, "admin", pw[i % pw.length]));
                        Thread.sleep(400);
                    }
                } else {
                    engine.process(new AttackAttempt(ip, "' OR 1=1 --", "x"));   // SQL injection
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void loadData() {
        try {
            attemptsLbl.setText(String.valueOf(dao.getTotalAttempts()));
            attackersLbl.setText(String.valueOf(dao.getTotalAttackers()));
            blockedLbl.setText(String.valueOf(dao.getTotalBlocked()));
            alertsLbl.setText(String.valueOf(dao.getTotalAlerts()));
            topTable.setModel(dao.getTopAttackers());
            passTable.setModel(dao.getCommonPasswords());
            alertTable.setModel(dao.getRecentAlerts());
            attemptTable.setModel(dao.getRecentAttempts());      // NEW
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load data: " + ex.getMessage());
        }
    }
}