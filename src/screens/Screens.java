package screens;

import buttons.Buttons;

import javax.swing.*;
import java.awt.*;

public class Screens extends JFrame {
    private double HEIGHT = 600;
    private double WIDTH = 1000;

    public Screens() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("HEAVENLY SANCTUARY");
        this.setSize((int) WIDTH, (int) HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setUndecorated(true);

        // Create a JLabel with the text "MENU"
        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 24)); // You can adjust the font and size
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text

        // Add the label to the content pane at the top
        this.getContentPane().add(menuLabel, BorderLayout.NORTH);

        Buttons buttons = new Buttons(this);
        this.getContentPane().add(buttons);

        // Make the frame visible
        this.setVisible(true);
    }
}
