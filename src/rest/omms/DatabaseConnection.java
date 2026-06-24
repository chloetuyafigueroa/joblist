package rest.omms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Connection connection2;

    private DatabaseConnection() {
        // Private constructor to prevent direct instantiation
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                // Register the PostgreSQL driver
                Class.forName("org.postgresql.Driver");

                // Establish the connection
              //String url = "jdbc:postgresql://172.17.100.6:5432/joblist";
                String url = "jdbc:postgresql://localhost:5432/joblist";
                String username = "postgres";
                String password = "03_0431A";//ileco1_amfm
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public Connection getConnection2() {
        if (connection2 == null) {
            try {
                // Register the PostgreSQL driver
                Class.forName("org.postgresql.Driver");

                // Establish the connection
              //String url = "jdbc:postgresql://172.17.100.6:5432/joblist";
                String url = "jdbc:postgresql://localhost:5432/joblist";
                String username = "postgres";
                String password = "03_0431A";//ileco1_amfm
                connection2 = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection2;
    }
}
