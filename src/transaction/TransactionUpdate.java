// TransactionUpdate.java
package transaction;

import javax.swing.*;
import java.awt.*;

public class TransactionUpdate extends JFrame {
    public TransactionUpdate(String receiptContent, double totalAmount) {
        setTitle("Receipt");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea receiptTextArea = new JTextArea(receiptContent);
        receiptTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JLabel totalAmountLabel = new JLabel("Total Amount: $" + totalAmount);
        add(totalAmountLabel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
