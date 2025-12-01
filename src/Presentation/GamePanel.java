package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel donde se dibuja el juego
 */
public class GamePanel extends JPanel {
    private Game game;
    private final int CELL_SIZE = 40;
    
    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawBoard(g);
        drawIceBlocks(g);
        drawFruits(g);
        drawPlayer(g);
        drawEnemies(g);
        
        if (game.isPaused()) {
            drawPausedMessage(g);
        }
    }
    
    /**
     * Dibuja el tablero (paredes y celdas vacías)
     */
    private void drawBoard(Graphics g) {
        Board board = game.getBoard();
        
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                int cellType = board.getCellType(x, y);
                
                if (cellType == 1) {
                    // Pared
                    g.setColor(new Color(139, 69, 19)); // Marrón
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    // Celda vacía
                    g.setColor(new Color(200, 230, 255)); // Azul claro
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(new Color(150, 200, 255));
                    g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
    
    /**
     * Dibuja los bloques de hielo
     */
    private void drawIceBlocks(Graphics g) {
        ArrayList<IceBlock> blocks = game.getBoard().getIceBlocks();
        
        g.setColor(new Color(173, 216, 230)); // Celeste
        for (IceBlock block : blocks) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                
                // Borde brillante
                g.setColor(Color.WHITE);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                g.drawLine(x, y, x + CELL_SIZE, y + CELL_SIZE);
                g.drawLine(x + CELL_SIZE, y, x, y + CELL_SIZE);
                g.setColor(new Color(173, 216, 230));
            }
        }
    }
    
    /**
     * Dibuja las frutas
     */
    private void drawFruits(Graphics g) {
        for (Fruit fruit : game.getFruits()) {
            if (!fruit.isCollected()) {
                int x = fruit.getPosition().getX() * CELL_SIZE;
                int y = fruit.getPosition().getY() * CELL_SIZE;
                
                if (fruit.getType().equals("GRAPE")) {
                    // Dibujar uva (círculo morado)
                    g.setColor(new Color(128, 0, 128));
                    g.fillOval(x + 10, y + 10, 20, 20);
                    g.setColor(new Color(75, 0, 130));
                    g.drawOval(x + 10, y + 10, 20, 20);
                } else if (fruit.getType().equals("BANANA")) {
                    // Dibujar plátano (forma curva amarilla)
                    g.setColor(Color.YELLOW);
                    g.fillArc(x + 5, y + 10, 30, 20, 0, 180);
                    g.setColor(new Color(200, 180, 0));
                    g.drawArc(x + 5, y + 10, 30, 20, 0, 180);
                }
            }
        }
    }
    
    /**
     * Dibuja al jugador (helado)
     */
    private void drawPlayer(Graphics g) {
        IceCream player = game.getPlayer();
        if (player.isAlive()) {
            int x = player.getPosition().getX() * CELL_SIZE;
            int y = player.getPosition().getY() * CELL_SIZE;
            
            // Color según sabor
            Color color;
            switch(player.getFlavor()) {
                case "VANILLA": color = Color.WHITE; break;
                case "STRAWBERRY": color = new Color(255, 182, 193); break;
                case "CHOCOLATE": color = new Color(139, 69, 19); break;
                default: color = Color.WHITE;
            }
            
            // Cuerpo del helado (rectángulo)
            g.setColor(color);
            g.fillRect(x + 8, y + 12, 24, 20);
            
            // Cono (triángulo)
            g.setColor(new Color(210, 180, 140));
            int[] xPoints = {x + 20, x + 10, x + 30};
            int[] yPoints = {y + 32, y + 38, y + 38};
            g.fillPolygon(xPoints, yPoints, 3);
            
            // Ojos
            g.setColor(Color.BLACK);
            g.fillOval(x + 13, y + 18, 4, 4);
            g.fillOval(x + 23, y + 18, 4, 4);
            
            // Borde
            g.setColor(Color.BLACK);
            g.drawRect(x + 8, y + 12, 24, 20);
        }
    }
    
    /**
     * Dibuja los enemigos
     */
    private void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (enemy.getType().equals("TROLL")) {
                // Dibujar troll (cara verde enojada)
                g.setColor(new Color(34, 139, 34));
                g.fillOval(x + 5, y + 5, 30, 30);
                
                // Ojos
                g.setColor(Color.RED);
                g.fillOval(x + 12, y + 13, 6, 6);
                g.fillOval(x + 22, y + 13, 6, 6);
                
                // Boca
                g.setColor(Color.BLACK);
                g.drawArc(x + 10, y + 20, 20, 10, 0, -180);
                
                // Borde
                g.setColor(Color.BLACK);
                g.drawOval(x + 5, y + 5, 30, 30);
            }
        }
    }
    
    /**
     * Dibuja mensaje de pausa
     */
    private void drawPausedMessage(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String msg = "PAUSA";
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        g.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
    }
}
