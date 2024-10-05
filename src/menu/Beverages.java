package menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import buttons.Buttons;
import main.Database;
import screens.Screens;
import transaction.Transaction;
import transaction.Transaction;

// Represents the Coffee Menu frame
public class Beverages extends JFrame {
    private double HEIGHT = 600;
    private double WIDTH = 1000;
    int labelWidth = 100;
    int labelHeight = 100;
    private Screens screens;
    private int buttonHeight = 150;
    private int buttonWidth = 200;

    // Constructor for the Coffee Menu
    public Beverages(Screens screens) {
        this.screens = screens;

        this.setTitle("COFFEE MENU");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set background image
        setContentPane(new JLabel(new ImageIcon(resizeImage("/pictures/Main.jpg", (int) WIDTH, (int) HEIGHT))));
        // Set the frame to be undecorated (remove title bar)
        this.setUndecorated(true);
        
     // Create a JLabel with the text "COFFEE MENU"
    	JLabel label = new JLabel("BEVERAGES MENU");
    	label.setFont(new Font("Arial", Font.BOLD, 26));
    	label.setHorizontalAlignment(SwingConstants.CENTER);

    	// Create a JPanel as a container for the JLabel
    	JPanel labelPanel = new JPanel();
    	labelPanel.setBackground(Color.WHITE); // Set the background color to white
    	labelPanel.setLayout(new BorderLayout());

    	// Set the size of the labelPanel to reach from right to left and accommodate the text
    	int labelWidth = (int) WIDTH;
    	int labelHeight = label.getPreferredSize().height + 20; // Add some padding
    	labelPanel.setBounds(0, 10, labelWidth, labelHeight);

    	// Add the JLabel to the JPanel
    	labelPanel.add(label, BorderLayout.CENTER);

    	// Add the JPanel to the content pane at the top
    	this.getContentPane().add(labelPanel);

        this.setVisible(true);
        this.setSize((int) WIDTH, (int) HEIGHT);
        this.setLocationRelativeTo(null);

        Buttons buttons = new Buttons(screens);
        JButton returnButton = buttons.backbutton();
        this.getContentPane().add(returnButton);

        // Action listener for the return button
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Beverages.this.dispose();
                screens.setVisible(true);
            }
        });

        this.setLayout(null);
        ORDERBUTTONS();
    }

    private Image resizeImage(String imagePath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(imagePath));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage).getImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    

 // Method to create order buttons and a JLabel
    private void ORDERBUTTONS() {
        createOrderButtonWithImage("ORDER 6", 100, 150, 6, "greentea.jpg");
        createOrderButtonWithImage("ORDER 7", 270, 370, 7, "icedtea.jpg");
        createOrderButtonWithImage("ORDER 8", 440, 150, 8, "lemonade.jpg");
        createOrderButtonWithImage("ORDER 9", 610, 370, 9, "orangejuice.jpg");
        createOrderButtonWithImage("ORDER 10", 780, 150, 10, "mangosmoothie.jpg");
    }


    // Method to create an order button with image and labels
    private void createOrderButtonWithImage(String order, int x, int y, int productId, String imageName) {
        JButton button = buttonMethods(order, x, y, "Product " + productId, productId);

        loadImageForButton(button, imageName);
        addActionListenerForButton(button, productId);
        this.getContentPane().add(button);
    }

    private JButton buttonMethods(String order, int x, int y, String productName, int productId) {
        JButton button = new JButton(order);
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setBounds(x, y, buttonWidth, buttonHeight);
        button.setFocusable(false);
        button.setOpaque(true); // Add this line to make the button fully paint its background
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Set a black border with a thickness of 2 pixels
        this.getContentPane().add(button);
        return button;
    }


    // Method to load the image for the button
    private void loadImageForButton(JButton button, String imageName) {
        try {
            String imagePath = "/pictures/" + imageName;
            Image buttonImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            button.setIcon(new ImageIcon(buttonImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
        } catch (NullPointerException e) {
            System.err.println("Error loading image: " + imageName);
        }
    }

    // Method to add action listener for the button
    private void addActionListenerForButton(JButton button, int productId) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productDetails = getProductDetailsFromDatabase(productId);
                JOptionPane.showMessageDialog(Beverages.this, productDetails, "Product Details", JOptionPane.INFORMATION_MESSAGE);

                String quantityString = JOptionPane.showInputDialog(Beverages.this, "Enter quantity:", "Order Quantity", JOptionPane.QUESTION_MESSAGE);

                try {
                    int quantity = Integer.parseInt(quantityString);
                    showConfirmation(productId, quantity);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Beverages.this, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to show order confirmation
    private void showConfirmation(int productId, int quantity) {
        // Retrieve product details from the database
        String productDetails = getProductDetailsFromDatabase(productId);

        // Retrieve the current stock quantity from the database
        int stockQuantity = getStockQuantityFromDatabase(productId);

        if (stockQuantity < quantity) {
            JOptionPane.showMessageDialog(this, "Sorry, the product is out of stock. Available stock: " + stockQuantity, "Out of Stock", JOptionPane.ERROR_MESSAGE);
            return; // Stop the order process if stock is insufficient
        }

        // Calculate total price based on quantity
        double unitPrice = getPriceFromDatabase(productId);
        double totalPrice = unitPrice * quantity;

        // Customize confirmation message without the image and icon
        String confirmationMessage = "Are you sure you want to order " + quantity + " of the following product?\n\n" +
                productDetails +
                "\nUnit Price: $" + unitPrice +
                "\nTotal Price: $" + totalPrice;

        int confirmOption = JOptionPane.showConfirmDialog(this, confirmationMessage, "Order Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (confirmOption == JOptionPane.YES_OPTION) {
            // Proceed to TransactionCash frame
            new Transaction(productDetails, screens, productId, quantity, totalPrice, "Cash");

            // Update stock quantity in the database after a successful order
            updateStockQuantityInDatabase(productId, stockQuantity - quantity);
        }
    }

    // Method to update stock quantity in the database
    private void updateStockQuantityInDatabase(int productId, int newStockQuantity) {
        try (Connection connection = Database.connect()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the update query to set the new stock quantity
            String updateQuery = "UPDATE products SET StockQuantity = " + newStockQuantity + " WHERE ProductID = " + productId;
            int rowsAffected = statement.executeUpdate(updateQuery);

            // Show appropriate messages based on the success of the update
            if (rowsAffected > 0) {
                System.out.println("Stock quantity updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update stock quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close resources
            statement.close();
            Database.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating stock quantity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to retrieve product details from the database
    private String getProductDetailsFromDatabase(int productId) {
        try {
            // Connect to the database using your Database class
            Connection connection = Database.connect();

            if (connection == null) {
                return "Failed to connect to the database.";
            }

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query to retrieve product details based on the productId
            String query = "SELECT * FROM products WHERE ProductID = " + productId;
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set and build the product details string
            StringBuilder productDetails = new StringBuilder();
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String category = resultSet.getString("Category");
                String brand = resultSet.getString("Brand");
                String volume = resultSet.getString("Volume");
                double price = resultSet.getDouble("Price");
                int stockQuantity = resultSet.getInt("StockQuantity");

                // Append details to the string
                productDetails.append("Name: ").append(name).append("\n");
                productDetails.append("Category: ").append(category).append("\n");
                productDetails.append("Brand: ").append(brand).append("\n");
                productDetails.append("Volume: ").append(volume).append("\n");
                productDetails.append("Price: ").append(price).append("\n");
                productDetails.append("Stock Quantity: ").append(stockQuantity).append("\n");
            } else {
                return "Product with ID " + productId + " not found.";
            }

            // Close resources
            resultSet.close();
            statement.close();
            Database.closeConnection(connection);

            return productDetails.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving product details: " + e.getMessage();
        }
    }

    // Method to retrieve product price from the database
    private double getPriceFromDatabase(int productId) {
        try (Connection connection = Database.connect()) {
            if (connection == null) {
                return -1; // Error value
            }

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query to retrieve the product price based on the productId
            String query = "SELECT Price FROM products WHERE ProductID = " + productId;
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set and retrieve the price
            double price = -1; // Error value
            if (resultSet.next()) {
                price = resultSet.getDouble("Price");
            }

            // Close resources
            resultSet.close();
            statement.close();
            Database.closeConnection(connection);

            return price;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error value
        }
    }

    // Method to retrieve stock quantity from the database
    private int getStockQuantityFromDatabase(int productId) {
        try (Connection connection = Database.connect()) {
            if (connection == null) {
                return -1; // Error value
            }

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute the query to retrieve the product stock quantity based on the productId
            String query = "SELECT StockQuantity FROM products WHERE ProductID = " + productId;
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set and retrieve the stock quantity
            int stockQuantity = -1; // Error value
            if (resultSet.next()) {
                stockQuantity = resultSet.getInt("StockQuantity");
            }

            // Close resources
            resultSet.close();
            statement.close();
            Database.closeConnection(connection);

            return stockQuantity;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error value
        }
    }
}
