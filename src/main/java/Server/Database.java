package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "java";
    private static final String PASSWORD = "java";
    private static Connection connection = null;

    private Database() { }

    public static Connection createConnection() {

        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);

        //creating connection to Oracle database using JDBC
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, props);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static Connection getConnection() {

        if (connection == null) {
            connection = createConnection();
        }

        return connection;
    }

    public static void commit() {

        try {

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {

        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}