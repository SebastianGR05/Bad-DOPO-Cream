package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Menú de selección de modalidad de juego
 * Permite elegir entre Player vs Player, Player vs Machine, y Machine vs Machine
 */
public class ModalityMenu extends JFrame {
    
    private JButton btnPvP;
    private JButton btnPvM;
    private JButton btnMvM;
    private JButton btnBack;
    private Image backgroundImage;
    
    public ModalityMenu() {
        prepareElements();
        prepareActions();
    }
    
    private void prepareElements() {
        setTitle("Seleccionar Modalidad");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar imagen de fondo común para menús
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
                        0, getHeight(), new Color(70, 130, 180)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Título
        JLabel title = new JLabel("Selecciona la Modalidad");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.BLACK);
        title.setBounds(150, 80, 600, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Botones de modalidades
        btnPvP = createModalityButton("Player vs Player", 150, 200);
        btnPvM = createModalityButton("Player vs Machine", 150, 290);
        btnMvM = createModalityButton("Machine vs Machine", 150, 380);
        
        mainPanel.add(btnPvP);
        mainPanel.add(btnPvM);
        mainPanel.add(btnMvM);
        
        
        // Botón de regresar
        btnBack = createBackButton();
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
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
    
    private JButton createBackButton() {
        // Intentar cargar imagen del botón de regresar
        Image backButtonImage = null;
        try {
            backButtonImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/buttons/backButton.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar backButton.png: " + e.getMessage());
        }
        
        JButton button;
        if (backButtonImage != null) {
            // Usar imagen del botón
            button = new JButton(new ImageIcon(backButtonImage));
            button.setBounds(370, 500, 160, 60);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
        } else {
            // Botón de texto simple
            button = new JButton("← Volver");
            button.setBounds(20, 20, 130, 40);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setBackground(new Color(80, 150, 220));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            
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
        }
        
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void prepareActions() {
        btnPvP.addActionListener(e -> {
            // Por ahora, PvP no está implementado
            JOptionPane.showMessageDialog(this,
                "La modalidad Player vs Player estará disponible en una versión futura.\n",
                "Próximamente",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnPvM.addActionListener(e -> {
            // Abrir menú de selección de helado
            IceCreamSelectionMenu iceCreamMenu = new IceCreamSelectionMenu();
            iceCreamMenu.setVisible(true);
            dispose();
        });
        
        btnMvM.addActionListener(e -> {
            // Por ahora, MvM no está implementado
            JOptionPane.showMessageDialog(this,
                "La modalidad Machine vs Machine estará disponible en una versión futura.\n",
                "Próximamente",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnBack.addActionListener(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            dispose();
        });
    }
}