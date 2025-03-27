package dsa_Metro;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Fra extends JFrame implements ActionListener {
	String destination;
	String source;
    public Fra(String source, String destination) {
        this.source = source;
        this.destination = destination;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("bg1.jpeg"));
        Image image = imageIcon.getImage().getScaledInstance(1530,830 , Image.SCALE_DEFAULT);
        ImageIcon imageIcon2 = new ImageIcon(image);
        JLabel imageLable = new JLabel(imageIcon2);
        add(imageLable);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu menu = new JMenu("Menu");
        menu.setFont(new Font("serif",Font.PLAIN,15));
        
        JMenu ShortestPD = new JMenu("Shortest Path (Distance)");
        ShortestPD.setFont(new Font("serif",Font.PLAIN,15));
        ShortestPD.addActionListener(this);
        
        JMenu ShortestPT = new JMenu("Shortest Path (Time)");
        ShortestPT.setFont(new Font("serif",Font.PLAIN,15));
        ShortestPT.addActionListener(this);
        
        JMenu exit = new JMenu("Exit");
        ShortestPD.setFont(new Font("serif",Font.PLAIN,15));
        
        JMenuItem eexit =new JMenuItem("Exit");
        eexit.setFont(new Font("monospaced",Font.PLAIN,14));
        ImageIcon eexitImg = new ImageIcon(ClassLoader.getSystemResource("exit.png"));
        Image eexitImage = eexitImg.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT);
        eexit.setIcon(new ImageIcon(eexitImage));
        eexit.addActionListener(this);
        exit.add(eexit);
        
        JMenu Routemap = new JMenu("Route Map");
        Routemap.setFont(new Font("serif",Font.PLAIN,15));
        
        
        menuBar.add(ShortestPD);
        menuBar.add(ShortestPT);
        menuBar.add(exit);
        menuBar.add(Routemap);
        
        setLayout(new FlowLayout());
        setVisible(true);

        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set frame size to match screen size
        setSize(screenSize.width, screenSize.height);

        // Make frame visible
        setVisible(true);

        // Add any GUI components and event listeners here
    }

    // Implement actionPerformed method for ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action events here
    }

    public static void main(String[] args) {
        new Fra("","");
    }
}

