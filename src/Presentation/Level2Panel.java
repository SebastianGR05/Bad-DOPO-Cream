package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Panel específico para el Nivel 2
 * Nivel 2: 1 Maceta, 8 Piñas, 8 Plátanos
 */
public class Level2Panel extends LevelPanel {
    
    public Level2Panel(Game game) {
        super(game);
    }
    
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (useImages && images.containsKey("pot") && images.get("pot") != null) {
                g.drawImage(images.get("pot"), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                drawSimplePot(g, x, y);
            }
        }
    }
    
    /**
     * Dibuja una maceta simple sin imagen
     */
    private void drawSimplePot(Graphics g, int x, int y) {
        // Maceta (trapecio marrón)
        g.setColor(new Color(139, 69, 19));
        int[] xPoints = {x + 10, x + 30, x + 28, x + 12};
        int[] yPoints = {y + 35, y + 35, y + 20, y + 20};
        g.fillPolygon(xPoints, yPoints, 4);
        
        // Borde de la maceta
        g.setColor(new Color(101, 51, 15));
        g.drawPolygon(xPoints, yPoints, 4);
        
        // Planta (círculo verde con hojas)
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 15, y + 10, 10, 10);
        
        // Hojas
        g.fillOval(x + 12, y + 8, 8, 8);
        g.fillOval(x + 20, y + 8, 8, 8);
        g.fillOval(x + 16, y + 5, 8, 8);
        
        // Ojos malvados en la planta
        g.setColor(Color.RED);
        g.fillOval(x + 17, y + 12, 3, 3);
        g.fillOval(x + 22, y + 12, 3, 3);
    }
}