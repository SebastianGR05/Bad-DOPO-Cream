package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Menú principal del juego Bad DOPO-Cream
 * Primera pantalla que ve el jugador al iniciar el juego
 */
public class MainMenu extends JFrame {
    
    private JButton btnPlay;
    private JButton btnExit;
    private Image backgroundImage;
    
    public MainMenu() {
        prepareElements();
        prepareActions();
    }
    
    private void prepareElements() {
        setTitle("Bad DOPO-Cream");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar imagen de fondo del menú principal
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(
                "/images/menu/principalMenu.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar principalMenu.png: " + e.getMessage());
        }
        
        // Panel principal con imagen de fondo
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fondo degradado si no hay imagen
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(25, 25, 112)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Dibujar título si no hay imagen
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 72));
                    String title = "Bad DOPO-Cream";
                    FontMetrics fm = g2d.getFontMetrics();
                    int titleWidth = fm.stringWidth(title);
                    g2d.drawString(title, (getWidth() - titleWidth) / 2, 200);
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Botón JUGAR (invisible sobre la imagen)
        // Ajusta estas coordenadas según donde esté el botón en tu imagen
        btnPlay = createInvisibleButton(275, 480, 340, 50);
        mainPanel.add(btnPlay);
        
        // Botón SALIR (invisible, debajo del botón jugar)
        btnExit = createInvisibleButton(275, 590, 340, 50);
        mainPanel.add(btnExit);
        
        add(mainPanel);
    }
    
    /**
     * Crea un botón invisible que se coloca sobre la imagen
     */
    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover sutil
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Opcional: podrías agregar un efecto visual aquí
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
     * Configura las acciones de los botones
     */
    private void prepareActions() {
        btnPlay.addActionListener(e -> {
            // Abrir menú de selección de modalidad
            ModalityMenu modalityMenu = new ModalityMenu();
            modalityMenu.setVisible(true);
            dispose();
        });
        
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Método principal para iniciar el juego
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}