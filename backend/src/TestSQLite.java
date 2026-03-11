import java.sql.Connection;
import java.sql.DriverManager;

public class TestSQLite {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:workout.db");
        System.out.println("Connected to SQLite!");
    }
}
