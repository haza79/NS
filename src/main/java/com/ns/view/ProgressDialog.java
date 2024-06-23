package com.ns.view;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {

    public ProgressDialog(JFrame parent, String message) {
        super(parent, true); // Set modal dialog
        initComponents(message);
    }

    private void initComponents(String message) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(messageLabel, BorderLayout.CENTER);

        setSize(300, 100);
        setResizable(false);
    }
}
