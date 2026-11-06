import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/trapnet";
        String user = "root";
        String password = "YOUR_PASSWORD";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to MySQL successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}