import ui.TrapLoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> new TrapLoginFrame().setVisible(true));
    }
}


