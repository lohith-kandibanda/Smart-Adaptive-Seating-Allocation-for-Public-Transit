package dsa_Metro;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
public class MyFrame extends Frame{
	private JPanel contentPane;
	public static void main(String[] args) {
		JFrame frame = new JFrame("My first Jframe");
		JLabel label = new JLabel("First one");
		frame.add(label);
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
