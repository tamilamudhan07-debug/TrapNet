import db.AdminDAO;

public class SetupAdmin {
    public static void main(String[] args) throws Exception {
        new AdminDAO().createAdmin("admin", "Incorrect_007");
        System.out.println("Admin created");
    }
}