package Presentation;

import Domain.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Menú de selección de nivel
 * Permite elegir entre los 3 niveles disponibles
 */
public class LevelSelectionMenu extends JFrame {
    
    private String iceCreamFlavor;
    private JButton btnLevel1;
    private JButton btnLevel2;
    private JButton btnLevel3;
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
        
        // Intentar cargar imagen de fondo
        try {
            backgroundImage = new ImageIcon(getClass().getResource(
                "/images/menu/level-selection-background.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo de selección de nivel");
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
        JLabel title = new JLabel("Selecciona el Nivel");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setBounds(200, 50, 500, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Paneles de niveles
        JPanel level1Panel = createLevelPanel(
            "Nivel 1", 
            "FÁCIL",
            "2 Trolls • 8 Uvas • 8 Plátanos",
            150, 150
        );
        mainPanel.add(level1Panel);
        
        JPanel level2Panel = createLevelPanel(
            "Nivel 2", 
            "MEDIO",
            "1 Maceta • 8 Piñas • 8 Plátanos",
            150, 300
        );
        mainPanel.add(level2Panel);
        
        JPanel level3Panel = createLevelPanel(
            "Nivel 3", 
            "DIFÍCIL",
            "1 Calamar • 8 Piñas • 8 Cerezas",
            150, 450
        );
        mainPanel.add(level3Panel);
        
        // Botón volver
        btnBack = createSmallButton("← Volver", 20, 20);
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
    private JPanel createLevelPanel(String levelName, String difficulty, 
                                    String description, int x, int y) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo del panel
                g.setColor(new Color(255, 255, 255, 230));
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
            case "FÁCIL": diffColor = new Color(50, 200, 50); break;
            case "MEDIO": diffColor = new Color(255, 165, 0); break;
            case "DIFÍCIL": diffColor = new Color(255, 50, 50); break;
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
        
        // Efecto hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(200, 230, 255));
                panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(null);
                panel.repaint();
            }
        });
        
        return panel;
    }
    
    private JButton createSmallButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 40);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(80, 150, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 170, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(80, 150, 220));
            }
        });
        
        return button;
    }
    
    private void prepareActions() {
        // Obtener los paneles de nivel
        JPanel level1Panel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
        JPanel level2Panel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(2);
        JPanel level3Panel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(3);
        
        level1Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(1);
            }
        });
        
        level2Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(2);
            }
        });
        
        level3Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startLevel(3);
            }
        });
        
        btnBack.addActionListener(e -> {
            IceCreamSelectionMenu iceCreamMenu = new IceCreamSelectionMenu();
            iceCreamMenu.setVisible(true);
            dispose();
        });
    }
    
    private void startLevel(int levelNumber) {
        // Crear el juego con el nivel y sabor seleccionados
        Game game = new Game(levelNumber, iceCreamFlavor);
        
        // Crear la ventana del juego con el panel correspondiente
        GameWindow gameWindow = new GameWindow(game, levelNumber);
        gameWindow.setVisible(true);
        dispose();
    }
}