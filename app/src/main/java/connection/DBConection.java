package connection;

import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConection {
    private static final String URL = "https://jlhdbpiaetbpzvwmmbrx.supabase.co";
    private static final String USER = "root";
    private static final String PASSWORD = "GfHjKm123456";

    private static Connection connection;

    private EditText message;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Good");
            } catch (ClassNotFoundException e) {
                System.out.println("Bad");
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }

}
