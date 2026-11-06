import db.AdminDAO;

public class SetupAdmin {
    public static void main(String[] args) throws Exception {
        new AdminDAO().createAdmin("admin","YOUR_PASSWORD");
        System.out.println("Admin created");
    }
}
