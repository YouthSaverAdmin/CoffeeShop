package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Checker extends JFrame {

    private Image backgroundImage;

    public Checker() {
        // Set up the frame
        setTitle("Image Display with Button");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JButton
        JButton imageButton = new JButton();

        // Set the button's icon (initially null)
        imageButton.setIcon(new ImageIcon());

        // Add an ActionListener to the button
        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load and display the image when the button is clicked
                loadImage("/menu/espresso.jpg");
                updateButtonIcon(imageButton);
            }
        });

        // Add components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(imageButton, BorderLayout.CENTER);

        // Add a window listener to handle closing the frame
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void loadImage(String fileName) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(fileName)).getImage();
        } catch (NullPointerException e) {
            // Handle the exception (print a message, use a default image, etc.)
            System.err.println("Error loading image: " + fileName);
            backgroundImage = new ImageIcon(getClass().getResource("/menu/Main.jpg")).getImage();
        }
    }

    private void updateButtonIcon(JButton button) {
        // Set the button's icon to the loaded image
        button.setIcon(new ImageIcon(backgroundImage.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH)));
    }

    public static void main(String[] args) {
        // Create an instance of the ImageDisplayWithButton class
        Checker imageDisplay = new Checker();

        // Make the frame visible
        imageDisplay.setVisible(true);
    }
}
