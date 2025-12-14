package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Main menu screen of the Bad DOPO-Cream game.
 * This is the first screen the player sees when starting the game.
 */
public class MainMenu extends JFrame {
    
    private JButton btnPlay;
    private JButton btnExit;
    private Image backgroundImage;
    
    /**
     * Creates the main menu window.
     */
    public MainMenu() {
        prepareElements();
        prepareActions();
    }
    
    /**
     * Sets up all visual elements of the main menu.
     */
    private void prepareElements() {
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load main menu background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/menu/principalMenu.png"));
        } catch (Exception e) {
            System.out.println("Could not load principalMenu.png: " + e.getMessage());
        }
        
        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);
        
        // PLAY button 
        btnPlay = createInvisibleButton(275, 480, 340, 50);
        mainPanel.add(btnPlay);
        
        // EXIT button
        btnExit = createInvisibleButton(275, 590, 340, 50);
        mainPanel.add(btnExit);
        
        add(mainPanel);
    }
    
    /**
     * Creates an invisible button that overlays the background image.
     * @param x the horizontal position
     * @param y the vertical position
     * @param width the button width
     * @param height the button height
     * @return a configured invisible button with hover effects
     */
    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Subtle hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(false);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });
        
        return button;
    }
    
    /**
     * Configures the behavior of all buttons in the main menu.
     */
    private void prepareActions() {
        btnPlay.addActionListener(e -> {
            // Open modality selection menu
            ModalityMenu modalityMenu = new ModalityMenu();
            modalityMenu.setVisible(true);
            dispose();
        });
        
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Are you sure you want to exit?",
                "",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Main method to start the game application.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
    }
}