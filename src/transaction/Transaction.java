// TransactionCash.java

// Import necessary libraries
package transaction;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat; // Import SimpleDateFormat

import main.Database;
import screens.Screens;

// Class definition for handling cash transactions
public class Transaction extends JFrame {

    // Instance variables
    private Screens screens;
    private String productDetails;
    private int productId;
    private int quantity;
    private double totalPrice;

    // Constructor
    public Transaction(String productDetails, Screens screens, int productId, int quantity, double totalPrice, String paymentMethod) {
        // Initialization and setup of the transaction window
        this.screens = screens;
        this.productDetails = productDetails;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;

        setTitle("TRANSACTION"); // Set window title
        setSize(600, 400); // Set window size
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set close operation

        // Create and display the transaction details table
        JTable transactionDetailsTable = createTransactionDetailsTable();
        JScrollPane scrollPane = new JScrollPane(transactionDetailsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create and configure buttons for order and return
        JButton orderButton = new JButton("Order");
        JButton returnButton = new JButton("Return");
        configureButtons(orderButton, returnButton);

        // Add buttons to the window
        add(returnButton, BorderLayout.NORTH);
        add(orderButton, BorderLayout.SOUTH);

        setVisible(true); // Make the window visible
    }

    // Method to create the transaction details table
    private JTable createTransactionDetailsTable() {
        // Define column names
        String[] columnNames = {"Transaction ID", "Date", "Total Amount", "Payment Method"};

        // Use DefaultTableModel with overridden isCellEditable method to make cells non-editable
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        // Populate the table model with transaction details from the database
        populateTableModel(model);

        return new JTable(model); // Return the created table
    }

    // Method to populate the table model with transaction details from the database
    private void populateTableModel(DefaultTableModel model) {
        try (Connection connection = Database.connect()) {
            // Database connection check
            if (connection == null) {
                System.out.println("Failed to connect to the database.");
                return;
            }

            // Execute SQL queries to retrieve transaction details
            executeTransactionQuery(connection, model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Transaction.this, "Error retrieving transaction details: " + e.getMessage());
        }
    }

    // Method to execute SQL queries to retrieve transaction details
    private void executeTransactionQuery(Connection connection, DefaultTableModel model) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM transactions WHERE PaymentMethod IN ('Cash', 'Credit')";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                // Process the result set and populate the table model
                while (resultSet.next()) {
                    int transactionID = resultSet.getInt("TransactionID");
                    Timestamp timestamp = resultSet.getTimestamp("Time");
                    double totalAmount = resultSet.getDouble("TotalAmount");
                    String paymentMethodFromDB = resultSet.getString("PaymentMethod");

                    // Format timestamp to display date in the table
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String formattedDate = dateFormat.format(timestamp);

                    Object[] row = {transactionID, formattedDate, totalAmount, paymentMethodFromDB};
                    model.addRow(row);
                }
            }
        }
    }

    // Method to configure buttons for order and return
    private void configureButtons(JButton orderButton, JButton returnButton) {
        // ActionListener for the order button
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ((JTable) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getSelectedRow();
                if (selectedRow != -1) {
                    int transactionID = (int) ((JTable) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getValueAt(selectedRow, 0);
                    handleOrder(transactionID, productDetails);
                } else {
                    JOptionPane.showMessageDialog(Transaction.this, "Please select a transaction to order.");
                }
            }
        });

        // ActionListener for the return button
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Transaction.this.dispose(); // Close the transaction window
            }
        });

        // Set tooltips to null to turn them off
        orderButton.setToolTipText(null);
        returnButton.setToolTipText(null);
    }

    // Method to handle the order process
    private void handleOrder(int transactionID, String productDetails) {
        // Show product details confirmation
        boolean confirmed = showProductDetailsConfirmation(productDetails);

        if (confirmed) {
            // Display receipt and update the transaction details table
            displayReceipt(transactionID, productDetails, totalPrice);
            updateTransactionDetailsTable(transactionID);
        }
    }

    // Method to update the transaction details table in the database
    private void updateTransactionDetailsTable(int transactionID) {
        // Update transaction total amount in the database
        try (Connection connection = Database.connect()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(Transaction.this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE transactions SET TotalAmount = TotalAmount - ? WHERE TransactionID = ?")) {
                // Set values for placeholders
                updateStatement.setDouble(1, totalPrice); // Deduct totalPrice from TotalAmount
                updateStatement.setInt(2, transactionID);

                // Execute the update statement
                int rowsAffected = updateStatement.executeUpdate();

                // Show appropriate messages based on the success of the update
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(Transaction.this, "Transaction total amount updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Transaction.this, "Failed to update transaction total amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Transaction.this, "Error updating transaction details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 // Method to display the receipt window
    private void displayReceipt(int transactionID, String productDetails, double totalPrice) {
        // Retrieve updated transaction details from the database
        String transactionDetails = getTransactionDetails(transactionID);

        // Format date in 12-hour format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(new java.util.Date());

        // Customize receipt content based on your requirements
        String receiptContent = "Transaction ID: " + transactionID + "\n\n" +
                "Product Details:\n" + productDetails + "\n\n" +
                "Transaction Details:\n" + transactionDetails + "\n" +
                "Date: " + formattedDate + "\n" +
                "Total Price: $" + totalPrice;

        new TransactionUpdate(receiptContent, totalPrice); // Display the receipt window
    }


    // Method to retrieve transaction details from the database
    private String getTransactionDetails(int transactionID) {
        try (Connection connection = Database.connect()) {
            // Database connection check
            if (connection == null) {
                return "Failed to connect to the database.";
            }

            // Execute SQL queries to retrieve transaction details
            return executeTransactionDetailsQuery(connection, transactionID);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving transaction details: " + e.getMessage();
        }
    }

    // Method to execute SQL queries to retrieve transaction details
    private String executeTransactionDetailsQuery(Connection connection, int transactionID) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM transactions WHERE TransactionID = " + transactionID;
            try (ResultSet resultSet = statement.executeQuery(query)) {
                // Process the result set and retrieve transaction details
                if (resultSet.next()) {
                    String time = resultSet.getString("Time");
                    double totalAmount = resultSet.getDouble("TotalAmount");
                    String paymentMethod = resultSet.getString("PaymentMethod");

                    return "Time: " + time + "\n" +
                            "Total Amount: " + totalAmount + "\n" +
                            "Payment Method: " + paymentMethod + "\n";
                }
            }
        }

        return "Transaction details not found.";
    }

    // Method to show a confirmation dialog for product details
    private boolean showProductDetailsConfirmation(String productDetails) {
        int confirmOption = JOptionPane.showConfirmDialog(
                null,
                productDetails,
                "Product Details",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE  // Use JOptionPane.PLAIN_MESSAGE to remove the question mark icon
        );

        // If the user clicks "Yes" (or OK), return true; otherwise, return false
        return confirmOption == JOptionPane.YES_OPTION;
    }
}
