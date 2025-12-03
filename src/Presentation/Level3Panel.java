package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Panel específico para el Nivel 3
 * Nivel 3: 1 Calamar Naranja, 8 Piñas, 8 Cerezas
 */
public class Level3Panel extends LevelPanel {
    
    public Level3Panel(Game game) {
        super(game);
    }
    
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (useImages && images.containsKey("squid") && images.get("squid") != null) {
                g.drawImage(images.get("squid"), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
}