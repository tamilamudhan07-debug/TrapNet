import db.AttemptDAO;
import model.AttackAttempt;

public class TestDAO {
    public static void main(String[] args) throws Exception {
        AttemptDAO dao = new AttemptDAO();
        dao.saveAttempt(new AttackAttempt("192.168.1.45", "admin", "admin123"));
        dao.saveAttempt(new AttackAttempt("192.168.1.45", "admin", "password"));
        System.out.println("Attempts in last 60s: " + dao.countRecentAttempts("192.168.1.45", 60));
    }
}