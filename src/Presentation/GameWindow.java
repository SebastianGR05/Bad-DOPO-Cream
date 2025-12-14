package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main game window that contains the gameplay interface.
 * This window manages:
 * - The game level display
 * - HUD information (score, time, level)
 * - Game controls and keyboard input
 * - Game timers for updates and rendering
 * - Win/loss dialogs and level progression
 */
public class GameWindow extends JFrame {
    
    private Game game;
    private int levelNumber;
    private LevelPanel levelPanel;
    
    private JLabel lblScore;
    private JLabel lblTime;
    private JLabel lblLevel;
    private JButton btnPause;
    
    private Timer gameTimer;
    private Timer updateTimer;
    
    /**
     * Creates a new game window for the specified game and level.
     * @param game the Game instance containing all game logic
     * @param levelNumber the current level number
     */
    public GameWindow(Game game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        prepareElements();
        prepareActions();
        startGameTimers();
    }
    
    /**
     * Sets up all visual elements of the game window.
     */
    private void prepareElements() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create the appropriate level panel based on level number
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
        
        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Creates the information panel (HUD) displayed at the top of the window.
     * @return the configured information panel
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.setBackground(new Color(50, 50, 100));
        panel.setPreferredSize(new Dimension(800, 60));
        
        lblLevel = new JLabel("Level " + levelNumber, SwingConstants.CENTER);
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblScore = new JLabel("Score: " + game.getTotalScore(), SwingConstants.CENTER);
        lblScore.setForeground(Color.YELLOW);
        lblScore.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblTime = new JLabel("Time: 3:00", SwingConstants.CENTER);
        lblTime.setForeground(Color.WHITE);
        lblTime.setFont(new Font("Arial", Font.BOLD, 20));
        
        btnPause = new JButton("Pause (P)");
        btnPause.setFont(new Font("Arial", Font.BOLD, 16));
        btnPause.setBackground(new Color(255, 165, 0));
        btnPause.setForeground(Color.WHITE);
        btnPause.setFocusPainted(false);
        
        panel.add(lblLevel);
        panel.add(lblScore);
        panel.add(lblTime);
        panel.add(btnPause);
        
        return panel;
    }
    
    /**
     * Creates the controls panel displayed at the bottom of the window.
     * @return the configured controls panel
     */
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(800, 80));
        
        JLabel controls = new JLabel(
            "<html><center>CONTROLS: WASD = Move  |  SPACE = Ice Block  |  P/ESC = Pause  |  R = Restart</center></html>"
        );
        controls.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(controls);
        
        return panel;
    }
    
    /**
     * Configures the behavior of all interactive elements and keyboard controls.
     */
    private void prepareActions() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(GameWindow.this,
                    "Exit the game? Current progress will be lost.",
                    "",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    stopTimers();
                    MainMenu menu = new MainMenu();
                    menu.setVisible(true);
                    dispose();
                }
            }
        });
        
        btnPause.addActionListener(e -> {
            game.togglePause();
            if (game.isPaused()) {
                btnPause.setText("Resume");
                btnPause.setBackground(new Color(50, 200, 50));
            } else {
                btnPause.setText("Pause (P)");
                btnPause.setBackground(new Color(255, 165, 0));
            }
            levelPanel.repaint();
            levelPanel.requestFocusInWindow();
        });
        
        levelPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                // Movement controls  WASD
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    game.movePlayer("UP");
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    game.movePlayer("DOWN");
                } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    game.movePlayer("LEFT");
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    game.movePlayer("RIGHT");
                } 
                // Ice block creation/destruction
                else if (key == KeyEvent.VK_SPACE) {
                    game.handleIceBlock();
                } 
                // Pause toggle
                else if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    game.togglePause();
                    if (game.isPaused()) {
                        btnPause.setText("Resume");
                        btnPause.setBackground(new Color(50, 200, 50));
                    } else {
                        btnPause.setText("Pause (P)");
                        btnPause.setBackground(new Color(255, 165, 0));
                    }
                } 
                // Restart level
                else if (key == KeyEvent.VK_R) {
                    int confirm = JOptionPane.showConfirmDialog(GameWindow.this,
                        "Restart the level? Current progress will be lost.",
                        "",
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        restartLevel();
                    }
                }
                
                levelPanel.repaint();
            }
        });
        
        levelPanel.requestFocusInWindow();
    }
    
    /**
     * Starts the game timers for rendering and logic updates.
     */
    private void startGameTimers() {
        gameTimer = new Timer(16, e -> {
            levelPanel.repaint();
            updateLabels();
            checkGameStatus();
        });
        gameTimer.start();
        
        updateTimer = new Timer(500, e -> game.update());
        updateTimer.start();
    }
    
    /**
     * Updates the HUD labels with current game information.
     */
    private void updateLabels() {
        lblScore.setText("Score: " + game.getPlayer().getScore());
        
        long timeRemaining = game.getTimeRemaining();
        int minutes = (int) (timeRemaining / 60000);
        int seconds = (int) ((timeRemaining % 60000) / 1000);
        lblTime.setText(String.format("Time: %d:%02d", minutes, seconds));
        
        if (timeRemaining < 30000) {
            lblTime.setForeground(Color.RED);
        } else {
            lblTime.setForeground(Color.WHITE);
        }
    }
    
    /**
     * Checks if the game has ended and shows appropriate dialogs.
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
     * Shows the victory dialog when the player completes a level.
     */
    private void showVictoryDialog() {
        int option = JOptionPane.showOptionDialog(this,
            "Congratulations! You completed level " + levelNumber + "!\n" +
            "Score: " + game.getPlayer().getScore() + "\n" +
            "What's next?",
            "Victory!",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Next level", "Restart", "Main Menu"},
            "Next level");
        
        if (option == 0) {
            if (levelNumber < 3) {
                Game newGame = new Game(levelNumber + 1, game.getPlayer().getFlavor());
                GameWindow newWindow = new GameWindow(newGame, levelNumber + 1);
                newWindow.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "You've completed all levels!\n" +
                    "Final score: " + game.getPlayer().getScore(),
                    "Game completed!",
                    JOptionPane.INFORMATION_MESSAGE);
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
                dispose();
            }
        } else if (option == 1) {
            restartLevel();
        } else {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
            dispose();
        }
    }
    
    /**
     * Shows the defeat dialog when the player loses.
     */
    private void showDefeatDialog() {
        int option = JOptionPane.showConfirmDialog(this,
            "You've been defeated\n" +
            "Score: " + game.getPlayer().getScore() + "\n" +
            "Try again?",
            "Loss",
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
     * Restarts the current level from the beginning.
     */
    private void restartLevel() {
        stopTimers();
        game.restart();
        btnPause.setText("Pause (P)");
        btnPause.setBackground(new Color(255, 165, 0));
        lblTime.setForeground(Color.WHITE);
        startGameTimers();
        levelPanel.repaint();
        levelPanel.requestFocusInWindow();
    }
    
    /**
     * Stops both game timers.
     */
    private void stopTimers() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
        }
    }
}