import ui.AdminLoginFrame;
import javax.swing.SwingUtilities;

public class AdminMain {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> new AdminLoginFrame().setVisible(true));
    }
}