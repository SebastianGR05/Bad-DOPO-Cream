package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Menú principal del juego Bad DOPO-Cream
 * Primera pantalla que ve el jugador al iniciar el juego
 */
public class MainMenu extends JFrame {
    
    private JButton btnPlay;
    private JButton btnControls;
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
        
        // Intentar cargar imagen de fondo
        try {
            backgroundImage = new ImageIcon(getClass().getResource(
                "/images/menu/main-menu-background.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo del menú principal");
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
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Título del juego
        JLabel title = new JLabel("Bad DOPO-Cream");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.WHITE);
        title.setBounds(180, 100, 600, 100);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Botones del menú
        btnPlay = createMenuButton("JUGAR", 350, 300);
        btnControls = createMenuButton("CONTROLES", 350, 390);
        btnExit = createMenuButton("SALIR", 350, 480);
        
        mainPanel.add(btnPlay);
        mainPanel.add(btnControls);
        mainPanel.add(btnExit);
        
        add(mainPanel);
    }
    
    /**
     * Crea un botón con el estilo del menú
     */
    private JButton createMenuButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 60);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(100, 180, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
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
     * Configura las acciones de los botones
     */
    private void prepareActions() {
        btnPlay.addActionListener(e -> {
            // Abrir menú de selección de modalidad
            ModalityMenu modalityMenu = new ModalityMenu();
            modalityMenu.setVisible(true);
            dispose();
        });
        
        btnControls.addActionListener(e -> {
            showControls();
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
     * Muestra una ventana con los controles del juego
     */
    private void showControls() {
        String controlsText = "=== CONTROLES DEL JUEGO ===\n\n" +
                             "MOVIMIENTO:\n" +
                             "  • Flechas o WASD - Mover el helado\n\n" +
                             "ACCIONES:\n" +
                             "  • ESPACIO - Crear/Romper bloques de hielo\n" +
                             "  • P o ESC - Pausar el juego\n" +
                             "  • R - Reiniciar nivel\n\n" +
                             "OBJETIVO:\n" +
                             "  • Recolecta todas las frutas del nivel\n" +
                             "  • Evita a los enemigos\n" +
                             "  • Completa el nivel en 3 minutos\n\n" +
                             "ESTRATEGIA:\n" +
                             "  • Usa bloques de hielo para bloquear enemigos\n" +
                             "  • Los bloques se destruyen en efecto dominó\n" +
                             "  • Cada nivel tiene enemigos y frutas diferentes";
        
        JTextArea textArea = new JTextArea(controlsText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(240, 240, 240));
        
        JOptionPane.showMessageDialog(this, textArea, 
            "Controles del Juego", JOptionPane.INFORMATION_MESSAGE);
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