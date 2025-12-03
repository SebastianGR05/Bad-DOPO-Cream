package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Panel específico para el Nivel 1
 * Nivel 1: 2 Trolls, 8 Uvas, 8 Plátanos
 */
public class Level1Panel extends LevelPanel {
    
    public Level1Panel(Game game) {
        super(game);
    }
    
    
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (useImages && images.containsKey("troll") && images.get("troll") != null) {
                g.drawImage(images.get("troll"), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                drawSimpleTroll(g, x, y);
            }
        }
    }
    
    /**
     * Dibuja un troll simple sin imagen
     */
    private void drawSimpleTroll(Graphics g, int x, int y) {
        // Cara verde del troll
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 5, y + 5, 30, 30);
        
        // Ojos rojos enojados
        g.setColor(Color.RED);
        g.fillOval(x + 12, y + 13, 6, 6);
        g.fillOval(x + 22, y + 13, 6, 6);
        
        // Cejas
        g.setColor(new Color(20, 100, 20));
        g.drawLine(x + 11, y + 12, x + 17, y + 14);
        g.drawLine(x + 23, y + 14, x + 29, y + 12);
        
        // Boca fruncida
        g.setColor(Color.BLACK);
        g.drawArc(x + 12, y + 20, 16, 10, 0, -180);
        
        // Contorno
        g.setColor(new Color(20, 100, 20));
        g.drawOval(x + 5, y + 5, 30, 30);
    }
}