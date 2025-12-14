package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Display panel specific to Level 1:
 * - Enemies: 2 Trolls
 * - Fruits: 8 Grapes, 8 Bananas
 */
public class Level1Panel extends LevelPanel {
    
    /**
     * Creates a new Level 1 display panel.
     * @param game the Game instance to display
     */
    public Level1Panel(Game game) {
        super(game);
    }
    
    /**
     * Draws all enemies for Level 1.
     * @param g the graphics context to draw on
     */
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;

            g.drawImage(images.get("troll"), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
}