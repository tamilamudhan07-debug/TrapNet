package ui;

import detector.DetectionEngine;
import model.AttackAttempt;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TrapLoginFrame extends JFrame {
    private final DetectionEngine engine = new DetectionEngine();
    private final JTextField userField = new JTextField(18);
    private final JPasswordField passField = new JPasswordField(18);
    private final JTextField ipField = new JTextField("192.168.1.45", 18);
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);

    public TrapLoginFrame() {
        setTitle("Company Admin Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 340);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin Portal", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; panel.add(new JLabel("Username"), c);
        c.gridx = 1; panel.add(userField, c);
        c.gridy = 2; c.gridx = 0; panel.add(new JLabel("Password"), c);
        c.gridx = 1; panel.add(passField, c);

        JButton loginBtn = new JButton("Log in");
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        panel.add(loginBtn, c);

        messageLabel.setForeground(Color.RED);
        c.gridy = 4;
        panel.add(messageLabel, c);

        // Demo-only control: lets you pretend to be different attackers
        JLabel ipLabel = new JLabel("Demo IP");
        ipLabel.setForeground(Color.GRAY);
        c.gridy = 5; c.gridwidth = 1; c.gridx = 0;
        panel.add(ipLabel, c);
        c.gridx = 1;
        panel.add(ipField, c);

        loginBtn.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(loginBtn);
        add(panel);
    }

    private void handleLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        String ip = ipField.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || ip.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        try {
            DetectionEngine.Result result = engine.process(new AttackAttempt(ip, user, pass));
            if (result == DetectionEngine.Result.BLOCKED) {
                messageLabel.setText("Access denied.");
            } else {
                // Same message for normal and detected attacks, so the attacker learns nothing
                messageLabel.setText("Invalid credentials");
            }
        } catch (SQLException ex) {
            messageLabel.setText("System error");
            ex.printStackTrace();
        }
        passField.setText("");
    }
}