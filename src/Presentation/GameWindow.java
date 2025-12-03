package Presentation;

import Domain.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana principal del juego que contiene el panel de nivel,
 * la información del juego y los controles
 */
public class GameWindow extends JFrame {
    
    private Game game;
    private int levelNumber;
    private LevelPanel levelPanel;
    
    // Componentes de UI
    private JLabel lblFruits;
    private JLabel lblTime;
    private JLabel lblLevel;
    private JButton btnPause;
    
    // Timers
    private Timer gameTimer;
    private Timer updateTimer;
    
    public GameWindow(Game game, int levelNumber) {
        super("Bad DOPO-Cream - Nivel " + levelNumber);
        this.game = game;
        this.levelNumber = levelNumber;
        prepareElements();
        prepareActions();
        startGameTimers();
    }
    
    private void prepareElements() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de información superior
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Crear el panel de nivel correspondiente
        switch(levelNumber) {
            case 1:
                levelPanel = new Level1Panel(game);
                break;
            case 2:
                levelPanel = new Level2Panel(game);
                break;
            case 3:
                levelPanel = new Level3Panel(game);
                break;
            default:
                levelPanel = new Level1Panel(game);
        }
        
        levelPanel.setFocusable(true);
        centerPanel.add(levelPanel, gbc);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel de controles inferior
        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Crea el panel de información superior con estadísticas del juego
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.setBackground(new Color(50, 50, 100));
        panel.setPreferredSize(new Dimension(800, 60));
        
        lblLevel = new JLabel("Nivel " + levelNumber, SwingConstants.CENTER);
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblFruits = new JLabel("Frutas: 0/" + game.getTotalFruits(), SwingConstants.CENTER);
        lblFruits.setForeground(Color.WHITE);
        lblFruits.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblTime = new JLabel("Tiempo: 3:00", SwingConstants.CENTER);
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Arial", Font.BOLD, 20));
        
        btnPause = new JButton("Pausar (P)");
        btnPause.setFont(new Font("Arial", Font.BOLD, 16));
        btnPause.setBackground(new Color(255, 165, 0));
        btnPause.setForeground(Color.WHITE);
        btnPause.setFocusPainted(false);
        
        panel.add(lblLevel);
        panel.add(lblFruits);
        panel.add(lblTime);
        panel.add(btnPause);
        
        return panel;
    }
    
    /**
     * Crea el panel de controles inferior con las instrucciones
     */
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(800, 80));
        
        JLabel controls = new JLabel(
            "<html><center>CONTROLES: Flechas/WASD = Mover  |  ESPACIO = Hielo  |  P/ESC = Pausa  |  R = Reiniciar</center></html>"
        );
        controls.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(controls);
        
        return panel;
    }
    
    /**
     * Configura todas las acciones de la ventana
     */
    private void prepareActions() {
        // Cerrar ventana con confirmación
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(GameWindow.this,
                    "¿Salir del juego? Se perderá el progreso.",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    stopTimers();
                    MainMenu menu = new MainMenu();
                    menu.setVisible(true);
                    dispose();
                }
            }
        });
        
        // Botón de pausa
        btnPause.addActionListener(e -> {
            game.togglePause();
            if (game.isPaused()) {
                btnPause.setText("Reanudar");
                btnPause.setBackground(new Color(50, 200, 50));
            } else {
                btnPause.setText("Pausar (P)");
                btnPause.setBackground(new Color(255, 165, 0));
            }
            levelPanel.repaint();
            levelPanel.requestFocusInWindow();
        });
        
        // Controles del teclado
        levelPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                // Movimiento
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    game.movePlayer("UP");
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    game.movePlayer("DOWN");
                } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    game.movePlayer("LEFT");
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    game.movePlayer("RIGHT");
                } 
                // Crear/romper bloques de hielo
                else if (key == KeyEvent.VK_SPACE) {
                    game.handleIceBlock();
                } 
                // Pausa
                else if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    game.togglePause();
                    if (game.isPaused()) {
                        btnPause.setText("Reanudar");
                        btnPause.setBackground(new Color(50, 200, 50));
                    } else {
                        btnPause.setText("Pausar (P)");
                        btnPause.setBackground(new Color(255, 165, 0));
                    }
                } 
                // Reiniciar
                else if (key == KeyEvent.VK_R) {
                    int confirm = JOptionPane.showConfirmDialog(GameWindow.this,
                        "¿Reiniciar el nivel? Se perderá el progreso actual.",
                        "Confirmar Reinicio",
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        restartLevel();
                    }
                }
                
                levelPanel.repaint();
            }
        });
        
        // Asegurar que el panel tenga el foco para recibir eventos del teclado
        levelPanel.requestFocusInWindow();
    }
    
    /**
     * Inicia los timers del juego para actualización y renderizado
     */
    private void startGameTimers() {
        // Timer para redibujar el juego (60 FPS = aproximadamente cada 16ms)
        gameTimer = new Timer(16, e -> {
            levelPanel.repaint();
            updateLabels();
            checkGameStatus();
        });
        gameTimer.start();
        
        // Timer para actualizar la lógica del juego (cada 500ms)
        // Aquí se mueven los enemigos, frutas especiales, etc.
        updateTimer = new Timer(500, e -> game.update());
        updateTimer.start();
    }
    
    /**
     * Actualiza las etiquetas de información en la interfaz
     */
    private void updateLabels() {
        // Actualizar contador de frutas
        lblFruits.setText("Frutas: " + game.getPlayer().getFruitsCollected() + 
                         "/" + game.getTotalFruits());
        
        // Actualizar tiempo restante
        long timeRemaining = game.getTimeRemaining();
        int minutes = (int) (timeRemaining / 60000);
        int seconds = (int) ((timeRemaining % 60000) / 1000);
        lblTime.setText(String.format("Tiempo: %d:%02d", minutes, seconds));
        
        // Cambiar color del tiempo si queda poco
        if (timeRemaining < 30000) { // Menos de 30 segundos
            lblTime.setForeground(Color.RED);
        } else {
            lblTime.setForeground(Color.WHITE);
        }
    }
    
    /**
     * Verifica si el juego ha terminado (ganado o perdido)
     */
    private void checkGameStatus() {
        if (game.isGameWon()) {
            stopTimers();
            showVictoryDialog();
        } else if (game.isGameLost()) {
            stopTimers();
            showDefeatDialog();
        }
    }
    
    /**
     * Muestra el diálogo de victoria y las opciones disponibles
     */
    private void showVictoryDialog() {
        int option = JOptionPane.showOptionDialog(this,
            "¡Felicidades! ¡Has completado el nivel " + levelNumber + "!\n" +
            "Frutas recolectadas: " + game.getPlayer().getFruitsCollected() + "/" + game.getTotalFruits() + "\n" +
            "¿Qué deseas hacer?",
            "¡Victoria!",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Siguiente Nivel", "Reintentar", "Menú Principal"},
            "Siguiente Nivel");
        
        if (option == 0) { // Siguiente nivel
            if (levelNumber < 3) {
                Game newGame = new Game(levelNumber + 1, game.getPlayer().getFlavor());
                GameWindow newWindow = new GameWindow(newGame, levelNumber + 1);
                newWindow.setVisible(true);
                dispose();
            } else {
                // Completó todos los niveles
                JOptionPane.showMessageDialog(this, 
                    "¡Has completado todos los niveles!\n" +
                    "¡Eres un maestro del hielo!\n" +
                    "Gracias por jugar Bad DOPO-Cream.",
                    "¡Juego Completado!",
                    JOptionPane.INFORMATION_MESSAGE);
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
                dispose();
            }
        } else if (option == 1) { // Reintentar
            restartLevel();
        } else { // Menú principal o cerró el diálogo
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
            dispose();
        }
    }
    
    /**
     * Muestra el diálogo de derrota
     */
    private void showDefeatDialog() {
        String reason;
        if (game.getTimeRemaining() == 0) {
            reason = "Se acabó el tiempo.";
        } else {
            reason = "Fuiste atrapado por un enemigo.";
        }
        
        int option = JOptionPane.showConfirmDialog(this,
            "¡Oh no! Has sido derrotado.\n" + reason + "\n" +
            "Frutas recolectadas: " + game.getPlayer().getFruitsCollected() + "/" + game.getTotalFruits() + "\n" +
            "¿Deseas intentarlo de nuevo?",
            "Derrota",
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            restartLevel();
        } else {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
            dispose();
        }
    }
    
    /**
     * Reinicia el nivel actual
     */
    private void restartLevel() {
        stopTimers();
        game.restart();
        btnPause.setText("Pausar (P)");
        btnPause.setBackground(new Color(255, 165, 0));
        lblTime.setForeground(Color.WHITE);
        startGameTimers();
        levelPanel.repaint();
        levelPanel.requestFocusInWindow();
    }
    
    /**
     * Detiene todos los timers del juego
     */
    private void stopTimers() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
        }
    }
    
    /**
     * Obtiene el panel principal del juego para acceso externo si es necesario
     */
    public JPanel getMainPanel() {
        return (JPanel) getContentPane();
    }
}