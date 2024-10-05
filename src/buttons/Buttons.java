package buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import menu.Beverages;
import menu.CoffeeMenu;
import menu.DessertsMenu;
import screens.Screens;

public class Buttons extends JPanel {
    private JButton buttonCoffee;
    private JButton buttonBeverages;
    private JButton buttonDesserts;
    private JButton returnButton;

    private double HEIGHT = 400;
    private double WIDTH = 200;
    private Screens screens;
    private Image backgroundImage;
    private JLabel currentImageLabel; // Added to keep track of the current displayed image

    public Buttons(Screens screens) {
        this.screens = screens;
        loadImage("/pictures/Main.jpg");  // Load the background image
        buttonCoffee = createBorderedButton("COFFEE", "/pictures/COFFEE.jpg", 10, 90);
        createBorderedLabel("COFFEE", 10, 100); // Added label for Coffee button

        buttonBeverages = createBorderedButton("BEVERAGES", "/pictures/Beverages.jpg", 350, 90);
        createBorderedLabel("BEVERAGES", 350, 100); // Added label for Beverages button

        buttonDesserts = createBorderedButton("DESSERTS", "/pictures/Desserts.jpg", 700, 90);
        createBorderedLabel("DESSERTS", 700, 100); // Added label for Desserts button


        buttonCoffee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfirmation("COFFEE");
            }
        });

        buttonBeverages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfirmation("BEVERAGES");
            }
        });

        buttonDesserts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfirmation("DESSERTS");
            }
        });

        this.setLayout(null);
    }

    private JLabel createLabel(String labelText, int x, int y) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.BLACK); // Set the label text color (you can adjust this)
        label.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text

        label.setPreferredSize(new Dimension((int) WIDTH, 50)); // Adjust the height as needed
        label.setBounds(x, y, (int) WIDTH, 50);

        label.setOpaque(true); // Make the label opaque
        label.setBackground(Color.WHITE); // Set the background color to white

        this.add(label);
        label.setVisible(true); // Ensure the label is visible

        return label;
    }
    // Method to create a JButton with a black border
    private JButton createBorderedButton(String button, String imageFileName, int x, int y) {
        JButton jbutton = createImageButton(button, imageFileName, x, y);
        setBlackBorder(jbutton);
        return jbutton;
    }

    // Method to create a black-bordered JLabel
    private JLabel createBorderedLabel(String labelText, int x, int y) {
        JLabel label = createLabel(labelText, x, y);
        setBlackBorder(label);
        return label;
    }

    private void setBlackBorder(JComponent component) {
        component.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 2 is the border thickness
    }

    private JButton createImageButton(String button, String imageFileName, int x, int y) {
        URL imageUrl = getClass().getResource(imageFileName);

        if (imageUrl == null) {
            System.err.println("Error loading image: " + imageFileName + " (not found)");
            return new JButton(button);
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        JButton jbutton = new JButton(icon);

        // Set a black border around the button
        jbutton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 2 is the border thickness

        jbutton.setContentAreaFilled(false);
        jbutton.setFocusPainted(false);
        jbutton.setOpaque(false);

        this.add(jbutton);
        jbutton.setPreferredSize(new Dimension((int) WIDTH, (int) HEIGHT));
        jbutton.setBounds(x, y, (int) WIDTH, (int) HEIGHT);

        return jbutton;
    }

    private void loadImage(String fileName) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(fileName)).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error loading image: " + fileName);
            e.printStackTrace();
            backgroundImage = new ImageIcon(getClass().getResource("/pictures/Main.jpg")).getImage();
        }
    }

    private void removeCurrentImage() {
        if (currentImageLabel != null) {
            this.remove(currentImageLabel);
            currentImageLabel = null;
        }
    }

    public JButton backbutton() {
        returnButton = new JButton("<-");
        returnButton.setPreferredSize(new Dimension(50, 50));
        returnButton.setBounds(10, 100, 50, 50);
        returnButton.setFocusable(false);
        return returnButton;
    }

    private void showConfirmation(String menuType) {
        int confirmOption = JOptionPane.showConfirmDialog(this, "Are you sure you want to choose " + menuType + "?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmOption == JOptionPane.YES_OPTION) {
            openMenu(menuType);
        }
        // No redirection for "No" or exit
    }

    private void openMenu(String menuType) {
        switch (menuType) {
            case "COFFEE":
                JOptionPane.showMessageDialog(null, "YOU HAVE CHOSEN COFFEE");
                new CoffeeMenu(screens);
                screens.dispose();
                removeCurrentImage(); // Remove the current image when switching frames
                break;
            case "BEVERAGES":
                JOptionPane.showMessageDialog(null, "YOU HAVE CHOSEN BEVERAGES");
                new Beverages(screens);
                screens.dispose();
                removeCurrentImage(); // Remove the current image when switching frames
                break;
            case "DESSERTS":
                JOptionPane.showMessageDialog(null, "YOU HAVE CHOSEN DESSERTS");
                new DessertsMenu(screens);
                screens.dispose();
                removeCurrentImage(); // Remove the current image when switching frames
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public double getButtonHeight() {
        return HEIGHT;
    }

    public double getButtonWIDTH() {
        return WIDTH;
    }
}
