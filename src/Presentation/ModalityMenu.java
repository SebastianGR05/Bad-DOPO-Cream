package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Game modality selection menu.
 * Allows players to choose between different game modes:
 * Player vs Player (PvP), not implemented
 * Player (single player mode), fully functional
 * Machine vs Machine (MvM), not implemented
 */
public class ModalityMenu extends JFrame {
    
    private JButton btnPvP;
    private JButton btnP;
    private JButton btnMvM;
    private JButton btnBack;
    private Image backgroundImage;
    
    /**
     * Creates the modality selection menu window.
     */
    public ModalityMenu() {
        prepareElements();
        prepareActions();
    }
    
    /**
     * Sets up all visual elements of the modality menu.
     */
    private void prepareElements() {
        setTitle("Seleccionar Modalidad");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load common menu background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/menu/menusBackground.png"));
        } catch (Exception e) {
            System.out.println("Could not load menusBackground.png: " + e.getMessage());
        }
        
        // Main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(70, 130, 180)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Title
        JLabel title = new JLabel("Choose the Game Mode");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.BLACK);
        title.setBounds(150, 80, 600, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Game mode buttons
        btnPvP = createModalityButton("Player vs Player", 150, 200);
        btnP = createModalityButton("Player", 150, 290);
        btnMvM = createModalityButton("Machine vs Machine", 150, 380);
        
        mainPanel.add(btnPvP);
        mainPanel.add(btnP);
        mainPanel.add(btnMvM);
        
        // Back button
        btnBack = createBackButton();
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
    /**
     * Creates a styled button for selecting a game modality.
     * @param text the text to display on the button
     * @param x the horizontal position
     * @param y the vertical position
     * @return a configured modality button
     */
    private JButton createModalityButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 600, 70);
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setBackground(new Color(100, 180, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(120, 200, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 180, 255));
            }
        });
        
        return button;
    }
    
    /**
     * Creates a back button with an image.
     * @return a configured back button
     */
    private JButton createBackButton() {
        // Try to load back button image
        Image backButtonImage = null;
        try {
            backButtonImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/buttons/backButton.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar backButton.png: " + e.getMessage());
        }
        
        JButton button;
        // Use button image
        button = new JButton(new ImageIcon(backButtonImage));
        button.setBounds(370, 500, 160, 60);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Configures the behavior of all buttons in the modality menu.
     */
    private void prepareActions() {
        btnPvP.addActionListener(e -> {
            // PvP not implemented
            JOptionPane.showMessageDialog(this,
                "The mode Player vs Player it's not available.\n",
                "Warning",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnP.addActionListener(e -> {
            // Open ice cream selection menu
            IceCreamSelectionMenu iceCreamMenu = new IceCreamSelectionMenu();
            iceCreamMenu.setVisible(true);
            dispose();
        });
        
        btnMvM.addActionListener(e -> {
            // MvM not implemented
            JOptionPane.showMessageDialog(this,
                "The mode Machine vs Machine it's not available.\n",
                "Warning",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnBack.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose();
        });
    }
}