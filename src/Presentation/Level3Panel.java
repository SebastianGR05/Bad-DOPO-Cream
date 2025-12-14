package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Display panel specific to Level 3:
 * - Enemies: 1 Orange Squid
 * - Fruits: 8 Pineapples, 8 Cherries
 * - Obstacles: Campfires
 */
public class Level3Panel extends LevelPanel {
    
    /**
     * Creates a new Level 3 display panel.
     * @param game the Game instance to display
     */
    public Level3Panel(Game game) {
        super(game);
    }
    
    /**
     * Draws all enemies for Level 3.
     * @param g the graphics context to draw on
     */
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            g.drawImage(images.get("squid"), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
}