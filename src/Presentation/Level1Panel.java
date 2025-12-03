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
            }
        }
    }
}