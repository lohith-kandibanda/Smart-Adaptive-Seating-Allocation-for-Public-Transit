package dsa_Metro;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MetroApp extends JFrame {
    private Graph_M metroMap;

    // Swing Components
    private JButton showMapButton;
    private JButton shortestPathButton;
    private JTextField sourceStationField;
    private JTextField destinationStationField;
    private JTextArea outputArea;

    public MetroApp() {
        metroMap = new Graph_M();

        // Initialize Swing Components
        showMapButton = new JButton("Show Metro Map");
        shortestPathButton = new JButton("Find Shortest Path");
        sourceStationField = new JTextField(20);
        destinationStationField = new JTextField(20);
        outputArea = new JTextArea(10, 30);

        // Set Layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Source Station:"));
        topPanel.add(sourceStationField);
        topPanel.add(new JLabel("Destination Station:"));
        topPanel.add(destinationStationField);
        topPanel.add(shortestPathButton);
        add(topPanel, BorderLayout.NORTH);
        add(outputArea, BorderLayout.CENTER);
        add(showMapButton, BorderLayout.SOUTH);

        // Event Listeners
        showMapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMetroMap();
            }
        });

        shortestPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findShortestPath();
            }
        });

        setTitle("Metro Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void displayMetroMap() {
        // Call the method from Graph_M to display the metro map
        metroMap.display_Map();
    }

    private void findShortestPath() {
        String source = sourceStationField.getText();
        String destination = destinationStationField.getText();

        // Call the method from Graph_M to find the shortest path
        String shortestPath = metroMap.Get_Minimum_Distance(source, destination);

        // Update the output area with the result
        outputArea.setText(shortestPath);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MetroApp();
            }
        });
    }
}
