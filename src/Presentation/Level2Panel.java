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
            }
        }
    }
}