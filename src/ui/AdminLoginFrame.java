package ui;

import db.AdminDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminLoginFrame extends JFrame {
    private final AdminDAO adminDAO = new AdminDAO();
    private final JTextField userField = new JTextField(16);
    private final JPasswordField passField = new JPasswordField(16);
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JButton loginBtn = new JButton("Secure log in");
    private int failedAttempts = 0;

    public AdminLoginFrame() {
        setTitle("TrapNet Admin Login (Secure)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 260);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("TrapNet Admin", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; panel.add(new JLabel("Username"), c);
        c.gridx = 1; panel.add(userField, c);
        c.gridy = 2; c.gridx = 0; panel.add(new JLabel("Password"), c);
        c.gridx = 1; panel.add(passField, c);

        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        panel.add(loginBtn, c);

        messageLabel.setForeground(Color.RED);
        c.gridy = 4;
        panel.add(messageLabel, c);

        loginBtn.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(loginBtn);
        add(panel);
    }

    private void handleLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        try {
            if (adminDAO.verifyLogin(user, pass)) {
                new DashboardFrame().setVisible(true);
                dispose();
                return;
            }
        } catch (SQLException ex) {
            messageLabel.setText("System error");
            ex.printStackTrace();
            return;
        }

        failedAttempts++;
        passField.setText("");
        if (failedAttempts >= 3) {
            loginBtn.setEnabled(false);
            messageLabel.setText("Too many failed attempts. Restart the app.");
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }
}