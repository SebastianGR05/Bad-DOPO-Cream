package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Ice cream flavor selection menu.
 * Allows the player to choose their character's appearance by selecting
 * one of three flavors: Vanilla, Strawberry, or Chocolate.
 */
public class IceCreamSelectionMenu extends JFrame {
    
    private String selectedFlavor = null;
    private JButton btnContinue;
    private JButton btnBack;
    private Image backgroundImage;
    
    // Ice cream selection panels
    private IceCreamPanel vanillaPanel;
    private IceCreamPanel strawberryPanel;
    private IceCreamPanel chocolatePanel;
    
    /**
     * Creates the ice cream selection menu window.
     */
    public IceCreamSelectionMenu() {
        prepareElements();
        prepareActions();
    }
    
    /**
     * Sets up all visual elements of the ice cream selection menu.
     */
    private void prepareElements() {
        setTitle("Selecciona tu Helado");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load common background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/menu/menusBackground.png"));
        } catch (Exception e) {
            System.out.println("Could not load menusBackground.png: " + e.getMessage());
        }
        
        // Main panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(200, 230, 255),
                        0, getHeight(), new Color(135, 206, 250)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Title
        JLabel title = new JLabel("Choose your flavor");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.BLACK);
        title.setBounds(250, 50, 400, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Ice cream panels
        vanillaPanel = new IceCreamPanel(
            "/images/characters/vanillaMenuSelector.png", 
            "Vainilla", 
            "VANILLA"
        );
        vanillaPanel.setBounds(45, 180, 240, 180);
        mainPanel.add(vanillaPanel);
        
        strawberryPanel = new IceCreamPanel(
            "/images/characters/strawberryMenuSelector.png", 
            "Fresa", 
            "STRAWBERRY"
        );
        strawberryPanel.setBounds(330, 180, 240, 180);
        mainPanel.add(strawberryPanel);
        
        chocolatePanel = new IceCreamPanel(
            "/images/characters/chocolateMenuSelector.png", 
            "Chocolate", 
            "CHOCOLATE"
        );
        chocolatePanel.setBounds(615, 180, 240, 180);
        mainPanel.add(chocolatePanel);
        
        // Continue button (disabled until a flavor is selected)
        btnContinue = new JButton("CONTINUE");
        btnContinue.setBounds(350, 480, 200, 60);
        btnContinue.setFont(new Font("Arial", Font.BOLD, 24));
        btnContinue.setBackground(new Color(50, 200, 50));
        btnContinue.setForeground(Color.WHITE);
        btnContinue.setFocusPainted(false);
        btnContinue.setBorderPainted(false);
        btnContinue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinue.setEnabled(false);
        mainPanel.add(btnContinue);
        
        // Back button
        btnBack = createBackButton();
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
    /**
     * Creates a back button with an image.
     * @return a configured back button
     */
    private JButton createBackButton() {
        Image backButtonImage = null;
        try {
            backButtonImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/buttons/backButton.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar backButton.png: " + e.getMessage());
        }
        
        JButton button;
        button = new JButton(new ImageIcon(backButtonImage));
        button.setBounds(20, 20, 160, 60);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Configures the behavior of all interactive elements.
     */
    private void prepareActions() {
        // Listeners for ice cream selection
        vanillaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("VANILLA", vanillaPanel);
            }
        });
        
        strawberryPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("STRAWBERRY", strawberryPanel);
            }
        });
        
        chocolatePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("CHOCOLATE", chocolatePanel);
            }
        });
        
        btnContinue.addActionListener(e -> {
            if (selectedFlavor != null) {
                LevelSelectionMenu levelMenu = new LevelSelectionMenu(selectedFlavor);
                levelMenu.setVisible(true);
                dispose();
            }
        });
        
        btnBack.addActionListener(e -> {
            ModalityMenu modalityMenu = new ModalityMenu();
            modalityMenu.setVisible(true);
            dispose();
        });
    }
    
    /**
     * Handles the selection of an ice cream flavor.
     * @param flavor the flavor code ("VANILLA", "STRAWBERRY", or "CHOCOLATE")
     * @param selectedPanel the panel that was clicked
     */
    private void selectIceCream(String flavor, IceCreamPanel selectedPanel) {
        selectedFlavor = flavor;
        
        // Deselect all
        vanillaPanel.setSelected(false);
        strawberryPanel.setSelected(false);
        chocolatePanel.setSelected(false);
        
        // Select the clicked one
        selectedPanel.setSelected(true);
        
        // Enable continue button
        btnContinue.setEnabled(true);
        btnContinue.setBackground(new Color(50, 200, 50));
    }
    
    /**
     * Inner class representing a selectable ice cream panel.
     */
    private class IceCreamPanel extends JPanel {
        private Image image;
        private String name;
        private String flavorCode;
        private boolean selected = false;
        
        /**
         * Creates a new ice cream selection panel.
         * @param imagePath path to the ice cream image resource
         * @param name display name of the flavor
         * @param flavorCode internal code for the flavor
         */
        public IceCreamPanel(String imagePath, String name, String flavorCode) {
            this.name = name;
            this.flavorCode = flavorCode;
            
            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            } catch (Exception e) {
                System.out.println("Could not load: " + imagePath + " - " + e.getMessage());
            }
            
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }
        
        /**
         * Sets the selection state of this panel.
         * @param selected true to select, false to deselect
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
            repaint();
        }
        
        /**
         * Paints the ice cream image on the panel.
         * @param g the graphics context to paint on
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillRect(0, 0, getWidth(), getHeight());
            // Draw ice cream image
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}