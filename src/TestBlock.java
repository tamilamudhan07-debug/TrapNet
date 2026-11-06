import db.AlertDAO;
import db.AttackerDAO;
import db.BlockedIpDAO;

public class TestBlock {
    public static void main(String[] args) throws Exception {
        new AlertDAO().saveAlert("192.168.1.45", "BRUTE_FORCE", "MEDIUM", "Test alert");
        new BlockedIpDAO().blockIp("192.168.1.45", "BRUTE_FORCE");
        new BlockedIpDAO().blockIp("192.168.1.45", "BRUTE_FORCE");  // second call is safely ignored
        System.out.println("Is blocked? " + new AttackerDAO().isBlocked("192.168.1.45"));
    }
}