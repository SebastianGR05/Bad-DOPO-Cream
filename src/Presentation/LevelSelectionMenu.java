package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Menú de selección de nivel
 * Permite elegir entre los 3 niveles disponibles
 */
public class LevelSelectionMenu extends JFrame {
    
    private String iceCreamFlavor;
    private JButton btnBack;
    private Image backgroundImage;
    
    public LevelSelectionMenu(String iceCreamFlavor) {
        this.iceCreamFlavor = iceCreamFlavor;
        prepareElements();
        prepareActions();
    }
    
    private void prepareElements() {
        setTitle("Selecciona el Nivel");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar imagen de fondo común
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/menu/menusBackground.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar menusBackground.png: " + e.getMessage());
        }
        
        // Panel principal
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(25, 25, 112)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Título
        JLabel title = new JLabel("Choose the level");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 50, 500, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Paneles de niveles
        JPanel level1Panel = createLevelPanel(
            "Level 1", 
            "EASY",
            "2 Trolls • 8 Grapes • 8 Bananas",
            150, 150
        );
        mainPanel.add(level1Panel);
        
        JPanel level2Panel = createLevelPanel(
            "Level 2", 
            "INTERMEDIATE",
            "1 Pot • 8 Pineapples • 8 Bananas",
            150, 300
        );
        mainPanel.add(level2Panel);
        
        JPanel level3Panel = createLevelPanel(
            "Level 3", 
            "HARD",
            "1 Squid • 8 Pineapples • 8 Cherries",
            150, 450
        );
        mainPanel.add(level3Panel);
        
        // Botón volver
        btnBack = createBackButton();
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
    private JPanel createLevelPanel(String levelName, String difficulty, 
                                    String description, int x, int y) {
        JPanel panel = new JPanel() {
            private boolean hover = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo del panel
                if (hover) {
                    g.setColor(new Color(255, 255, 255, 250));
                } else {
                    g.setColor(new Color(255, 255, 255, 230));
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Borde
                g.setColor(new Color(100, 180, 255));
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        panel.setLayout(null);
        panel.setBounds(x, y, 600, 100);
        panel.setOpaque(false);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Nombre del nivel
        JLabel nameLabel = new JLabel(levelName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        nameLabel.setForeground(new Color(50, 50, 150));
        nameLabel.setBounds(20, 10, 200, 35);
        panel.add(nameLabel);
        
        // Dificultad
        JLabel diffLabel = new JLabel(difficulty);
        diffLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        Color diffColor;
        switch(difficulty) {
            case "EASY": diffColor = new Color(50, 200, 50); break;
            case "INTERMEDIATE": diffColor = new Color(255, 165, 0); break;
            case "HARD": diffColor = new Color(255, 50, 50); break;
            default: diffColor = Color.BLACK;
        }
        diffLabel.setForeground(diffColor);
        diffLabel.setBounds(20, 45, 150, 25);
        panel.add(diffLabel);
        
        // Descripción
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setForeground(Color.BLACK);
        descLabel.setBounds(220, 30, 360, 25);
        panel.add(descLabel);
        
        return panel;
    }
    
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
    
    private void prepareActions() {
        // Obtener los paneles de nivel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        JPanel level1Panel = (JPanel) mainPanel.getComponent(1);
        JPanel level2Panel = (JPanel) mainPanel.getComponent(2);
        JPanel level3Panel = (JPanel) mainPanel.getComponent(3);
        
        level1Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(1);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                level1Panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                level1Panel.repaint();
            }
        });
        
        level2Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(2);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                level2Panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                level2Panel.repaint();
            }
        });
        
        level3Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(3);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                level3Panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                level3Panel.repaint();
            }
        });
        
        btnBack.addActionListener(e -> {
            IceCreamSelectionMenu iceCreamMenu = new IceCreamSelectionMenu();
            iceCreamMenu.setVisible(true);
            dispose();
        });
    }
    
    /**
     * Inicia el nivel seleccionado
     */
    private void startLevel(int levelNumber) {
        try {
            // Crear el juego con el nivel y sabor seleccionados
            Game game = new Game(levelNumber, iceCreamFlavor);
            
            // Crear la ventana del juego
            GameWindow gameWindow = new GameWindow(game, levelNumber);
            gameWindow.setVisible(true);
            dispose();
        } catch (BadDopoCreamException e) {
            JOptionPane.showMessageDialog(this,
                "Error al iniciar el nivel " + levelNumber + ":\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("Error creando nivel: " + e.getMessage());
        }
    }
}