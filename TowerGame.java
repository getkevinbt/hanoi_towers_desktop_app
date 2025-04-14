import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tower Game extends JFrame {

    public Tower Game() {
        setTitle("Tower of Hanoi");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Prevent default exit

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit(); // Ask if sure
            }
        });

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> confirmBackToMenu());

        add(backButton, BorderLayout.SOUTH);
        
        // You can add your tower drawing panel here
        add(new JLabel("Game is running..."), BorderLayout.CENTER);

        setVisible(true);
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            dispose(); // Close the frame
            System.exit(0);
        }
    }

    private void confirmBackToMenu() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return to the main menu?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Here you would open the main menu view
            JOptionPane.showMessageDialog(this, "Returning to Main Menu...");
            dispose(); // Or hide current game window
            new MainMenu(); // Assuming you have this class
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HanoiGame());
    }
}


class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Tower of Hanoi - Main Menu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            dispose(); // Close main menu
            new TowerGame(); // Open game
        });

        add(startButton, BorderLayout.CENTER);
        setVisible(true);
    }
}
