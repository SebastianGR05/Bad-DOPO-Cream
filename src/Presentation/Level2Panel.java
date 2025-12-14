package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Display panel specific to Level 2:
 * - Enemies: 1 Pot 
 * - Fruits: 8 Pineapples, 8 Bananas
 * - Obstacles: Hot Tiles
 */
public class Level2Panel extends LevelPanel {
    
    /**
     * Creates a new Level 2 display panel.
     * @param game the Game instance to display
     */
    public Level2Panel(Game game) {
        super(game);
    }
    
    /**
     * Draws all enemies for Level 2.
     * @param g the graphics context to draw on
     */
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            g.drawImage(images.get("pot"), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
}