package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        
        // Intentar cargar imagen de fondo
        try {
            backgroundImage = new ImageIcon(getClass().getResource(
                "/images/menu/modality-menu-background.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo del menú de modalidades");
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
        title.setForeground(Color.WHITE);
        title.setBounds(200, 80, 600, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Descripción de cada modalidad
        JPanel descriptionsPanel = new JPanel();
        descriptionsPanel.setLayout(new BoxLayout(descriptionsPanel, BoxLayout.Y_AXIS));
        descriptionsPanel.setOpaque(false);
        descriptionsPanel.setBounds(150, 450, 600, 150);
        
        JLabel descriptionLabel = new JLabel("<html><center>Elige cómo quieres jugar:<br>" +
            "• PvP: Tú controlas el helado, otro jugador controla al enemigo<br>" +
            "• PvM: Tú contra enemigos automáticos<br>" +
            "• MvM: Observa cómo la máquina juega contra sí misma</center></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionsPanel.add(descriptionLabel);
        
        mainPanel.add(descriptionsPanel);
        
        // Botones de modalidades
        btnPvP = createModalityButton("Player vs Player", 150, 200);
        btnPvM = createModalityButton("Player vs Machine", 150, 290);
        btnMvM = createModalityButton("Machine vs Machine", 150, 380);
        
        mainPanel.add(btnPvP);
        mainPanel.add(btnPvM);
        mainPanel.add(btnMvM);
        
        // Botón de regresar
        btnBack = createSmallButton("← Volver", 20, 20);
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
        btnPvP.addActionListener(e -> {
            // Por ahora, PvP no está implementado
            JOptionPane.showMessageDialog(this,
                "La modalidad Player vs Player estará disponible en una versión futura.\n" +
                "Por ahora, prueba Player vs Machine.",
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
                "La modalidad Machine vs Machine estará disponible en una versión futura.\n" +
                "Por ahora, prueba Player vs Machine.",
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