// Database.java
package main;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URLClassLoader;

public class Database {

    public static Connection connect() throws SQLException {
        // How to connect the database
        String url = "jdbc:mariadb://localhost:3306/cofeeshop";
        String username = "root";
        String password = "";

        String driverPath = "C:\\Program Files\\CData\\CData JDBC Driver for MariaDB 2023\\lib\\cdata.jdbc.mariadb.jar";

        // use the connector
        loadDriver(driverPath);

        // connect to database
        return DriverManager.getConnection(url, username, password);
    }

    // adding the driver path
    private static void loadDriver(String path) throws SQLException {
        try {
            URL url = new URL("jar:file:" + path + "!/");
            URLClassLoader loader = new URLClassLoader(new URL[]{url});
            Class.forName("cdata.jdbc.mariadb.MariaDBDriver", true, loader);
        } catch (Exception e) {
            throw new SQLException("Error loading MariaDB driver: " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection connection) {
        // Close the connection
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement statement) {
        // Close the statement
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
