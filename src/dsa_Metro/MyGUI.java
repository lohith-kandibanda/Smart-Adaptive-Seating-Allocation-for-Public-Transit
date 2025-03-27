package dsa_Metro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyGUI extends JFrame implements ActionListener {
    private JButton button;
    private JLabel label;

    public MyGUI() {
        super("My GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create components
        button = new JButton("Click me");
        label = new JLabel("Nothing clicked yet");
        
        // Add components to JFrame
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(button);
        getContentPane().add(label);
        
        // Add ActionListener to button
        button.addActionListener(this);
        
        // Set JFrame size and visibility
        setSize(300, 200);
        setVisible(true);
    }

    // ActionListener implementation
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            // Call your Java code methods here
            label.setText("Button clicked!");
        }
    }

    // Main method to run the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyGUI();
        });
    }
}
