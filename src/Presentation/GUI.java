package Presentation;

import Domain.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana principal del juego Bad DOPO-Cream
 */
public class GUI extends JFrame {
    
    private Game game;
    private GamePanel gamePanel;
    
    // Componentes del menú
    private JMenuBar menuBar;
    private JMenu menuGame;
    private JMenu menuHelp;
    private JMenuItem itemRestart;
    private JMenuItem itemExit;
    private JMenuItem itemControls;
    
    // Paneles
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel controlsPanel;
    
    // Etiquetas de información
    private JLabel lblFruits;
    private JLabel lblTime;
    private JLabel lblStatus;
    
    // Botones de control
    private JButton btnUp;
    private JButton btnDown;
    private JButton btnLeft;
    private JButton btnRight;
    private JButton btnIce;
    private JButton btnPause;
    
    // Timers
    private Timer gameTimer;
    private Timer updateTimer;
    
    public GUI() {
        super("Bad DOPO-Cream - Nivel 1");
        game = new Game();
        prepareElements();
        prepareActions();
    }
    
    /**
     * Prepara todos los elementos de la interfaz
     */
    private void prepareElements() {
        // Configuración de la ventana
        setResizable(false);
        
        prepareElementsMenu();
        
        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
        
        prepareElementsBoard();
        prepareElementsInfo();
        prepareElementsControls();
        
        pack();  // Ajustar tamaño automáticamente
        setLocationRelativeTo(null);  // Centrar en pantalla
    }
    
    /**
     * Prepara el menú de la ventana
     */
    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        
        // Menú Juego
        menuGame = new JMenu("Juego");
        itemRestart = new JMenuItem("Reiniciar");
        itemExit = new JMenuItem("Salir");
        
        menuGame.add(itemRestart);
        menuGame.add(new JSeparator());
        menuGame.add(itemExit);
        
        // Menú Ayuda
        menuHelp = new JMenu("Ayuda");
        itemControls = new JMenuItem("Controles");
        
        menuHelp.add(itemControls);
        
        menuBar.add(menuGame);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }
    
    /**
     * Prepara el tablero de juego
     */
    private void prepareElementsBoard() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.DARK_GRAY);
        
        gamePanel = new GamePanel(game);
        gamePanel.setFocusable(true);
        
        centerPanel.add(gamePanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Prepara el panel de información
     */
    private void prepareElementsInfo() {
        infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.setBackground(Color.DARK_GRAY);
        infoPanel.setPreferredSize(new Dimension(600, 50));
        
        lblFruits = new JLabel("Frutas: 0/" + game.getTotalFruits(), SwingConstants.CENTER);
        lblFruits.setForeground(Color.WHITE);
        lblFruits.setFont(new Font("Arial", Font.BOLD, 18));
        
        lblTime = new JLabel("Tiempo: 3:00", SwingConstants.CENTER);
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Arial", Font.BOLD, 18));
        
        lblStatus = new JLabel("¡Recolecta todas las frutas!", SwingConstants.CENTER);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 16));
        
        infoPanel.add(lblFruits);
        infoPanel.add(lblTime);
        infoPanel.add(lblStatus);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
    }
    
    /**
     * Prepara el panel de controles
     */
    private void prepareElementsControls() {
        controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(Color.LIGHT_GRAY);
        controlsPanel.setPreferredSize(new Dimension(600, 120));
        
        // Panel de movimiento
        JPanel movementPanel = new JPanel(new GridBagLayout());
        movementPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        btnUp = new JButton("↑");
        btnDown = new JButton("↓");
        btnLeft = new JButton("←");
        btnRight = new JButton("→");
        
        // Configurar botones de movimiento
        Dimension btnSize = new Dimension(60, 40);
        btnUp.setPreferredSize(btnSize);
        btnDown.setPreferredSize(btnSize);
        btnLeft.setPreferredSize(btnSize);
        btnRight.setPreferredSize(btnSize);
        
        // Arriba
        gbc.gridx = 1;
        gbc.gridy = 0;
        movementPanel.add(btnUp, gbc);
        
        // Izquierda, Abajo, Derecha
        gbc.gridx = 0;
        gbc.gridy = 1;
        movementPanel.add(btnLeft, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        movementPanel.add(btnDown, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        movementPanel.add(btnRight, gbc);
        
        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout());
        actionsPanel.setBackground(Color.LIGHT_GRAY);
        
        btnIce = new JButton("Hielo (Espacio)");
        btnPause = new JButton("Pausar (P)");
        
        btnIce.setPreferredSize(new Dimension(150, 40));
        btnPause.setPreferredSize(new Dimension(150, 40));
        
        actionsPanel.add(btnIce);
        actionsPanel.add(btnPause);
        
        controlsPanel.add(movementPanel, BorderLayout.CENTER);
        controlsPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Prepara todas las acciones de la interfaz
     */
    private void prepareActions() {
        // Terminar de manera adecuada
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        
        prepareActionsMenu();
        prepareActionsButtons();
        prepareActionsKeyboard();
        startGameTimers();
    }
    
    /**
     * Prepara las acciones del menú
     */
    private void prepareActionsMenu() {
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        
        itemRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(GUI.this,
                    "¿Reiniciar el juego? Se perderá el progreso actual.",
                    "Reiniciar",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    restart();
                }
            }
        });
        
        itemControls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showControls();
            }
        });
    }
    
    /**
     * Prepara las acciones de los botones
     */
    private void prepareActionsButtons() {
        btnUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.movePlayer("UP");
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
        
        btnDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.movePlayer("DOWN");
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
        
        btnLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.movePlayer("LEFT");
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
        
        btnRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.movePlayer("RIGHT");
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
        
        btnIce.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.handleIceBlock();
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
        
        btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.togglePause();
                if (game.isPaused()) {
                    btnPause.setText("Reanudar");
                } else {
                    btnPause.setText("Pausar (P)");
                }
                gamePanel.repaint();
                gamePanel.requestFocusInWindow();
            }
        });
    }
    
    /**
     * Prepara las acciones del teclado
     */
    private void prepareActionsKeyboard() {
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                // Movimiento con flechas
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    game.movePlayer("UP");
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    game.movePlayer("DOWN");
                } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    game.movePlayer("LEFT");
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    game.movePlayer("RIGHT");
                }
                // Crear/destruir bloques de hielo
                else if (key == KeyEvent.VK_SPACE) {
                    game.handleIceBlock();
                }
                // Pausa
                else if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    game.togglePause();
                    if (game.isPaused()) {
                        btnPause.setText("Reanudar");
                    } else {
                        btnPause.setText("Pausar (P)");
                    }
                }
                // Reiniciar
                else if (key == KeyEvent.VK_R) {
                    restart();
                }
                
                gamePanel.repaint();
            }
        });
        
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Inicia los timers del juego
     */
    private void startGameTimers() {
        // Timer para redibujar (60 FPS)
        gameTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.repaint();
                updateLabels();
                checkGameStatus();
            }
        });
        gameTimer.start();
        
        // Timer para actualizar lógica del juego (enemigos cada 500ms)
        updateTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.update();
            }
        });
        updateTimer.start();
    }
    
    /**
     * Actualiza las etiquetas de información
     */
    private void updateLabels() {
        lblFruits.setText("Frutas: " + game.getPlayer().getFruitsCollected() 
                         + "/" + game.getTotalFruits());
        
        long timeRemaining = game.getTimeRemaining();
        int minutes = (int) (timeRemaining / 60000);
        int seconds = (int) ((timeRemaining % 60000) / 1000);
        lblTime.setText(String.format("Tiempo: %d:%02d", minutes, seconds));
    }
    
    /**
     * Verifica el estado del juego (ganado/perdido)
     */
    private void checkGameStatus() {
        if (game.isGameWon()) {
            gameTimer.stop();
            updateTimer.stop();
            lblStatus.setText("¡GANASTE!");
            lblStatus.setForeground(Color.GREEN);
            showEndDialog("¡Felicidades! ¡Has completado el nivel!", "Victoria");
        } else if (game.isGameLost()) {
            gameTimer.stop();
            updateTimer.stop();
            lblStatus.setText("PERDISTE");
            lblStatus.setForeground(Color.RED);
            showEndDialog("¡Oh no! Fuiste atrapado o se acabó el tiempo.", "Derrota");
        }
    }
    
    /**
     * Muestra diálogo al terminar el juego
     */
    private void showEndDialog(String message, String title) {
        int option = JOptionPane.showConfirmDialog(
            this,
            message + "\n¿Deseas jugar de nuevo?",
            title,
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            restart();
        }
    }
    
    /**
     * Muestra los controles del juego
     */
    private void showControls() {
        String controlsText = "=== CONTROLES DEL JUEGO ===\n\n" +
                             "Movimiento:\n" +
                             "  • Flechas o WASD - Mover el helado\n\n" +
                             "Acciones:\n" +
                             "  • ESPACIO - Crear/Romper bloques de hielo\n" +
                             "  • P o ESC - Pausar el juego\n" +
                             "  • R - Reiniciar nivel\n\n" +
                             "Objetivo:\n" +
                             "  • Recolecta 8 uvas (moradas) y 8 plátanos (amarillos)\n" +
                             "  • Evita a los 2 trolls enemigos\n" +
                             "  • Completa el nivel en 3 minutos\n\n" +
                             "Estrategia:\n" +
                             "  • Usa bloques de hielo para bloquear enemigos\n" +
                             "  • Los bloques se destruyen en efecto dominó";
        
        JTextArea textArea = new JTextArea(controlsText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(this, textArea, 
            "Controles del Juego", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Reinicia el juego
     */
    private void restart() {
        if (gameTimer != null) gameTimer.stop();
        if (updateTimer != null) updateTimer.stop();
        
        game.restart();
        lblStatus.setText("¡Recolecta todas las frutas!");
        lblStatus.setForeground(Color.WHITE);
        btnPause.setText("Pausar (P)");
        
        gameTimer.start();
        updateTimer.start();
        gamePanel.repaint();
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Termina de manera adecuada
     */
    private void exit() {
        int confirmation = JOptionPane.showConfirmDialog(this, 
            "¿Desea salir del juego?", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            if (gameTimer != null) gameTimer.stop();
            if (updateTimer != null) updateTimer.stop();
            setVisible(false);
            dispose();
            System.exit(0);
        }
    }
    
    /**
     * Método principal para ejecutar el juego desde consola
     */
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   Bad DOPO-Cream - Nivel 1");
        System.out.println("=================================");
        System.out.println("Controles:");
        System.out.println("  Flechas/WASD - Mover");
        System.out.println("  ESPACIO - Crear/Romper hielo");
        System.out.println("  P/ESC - Pausar");
        System.out.println("  R - Reiniciar");
        System.out.println("=================================");
        System.out.println("Objetivo: Recolectar 8 uvas y 8 plátanos");
        System.out.println("Evita a los 2 trolls enemigos!");
        System.out.println("=================================\n");
        System.out.println("Iniciando juego...\n");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}
