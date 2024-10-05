// Main.java
package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SwingUtilities;

import screens.Screens;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Screens screens = new Screens();
            screens.setVisible(true);
        });

        try {
            // Establish the connection
            Connection connection = Database.connect();

            // Create a statement
            Statement statement = connection.createStatement();

           

            // Close resources
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}	
