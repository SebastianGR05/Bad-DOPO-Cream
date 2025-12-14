package Domain;

import java.util.Random;

/**
 * Represents a cherry fruit in the game.
 * Cherries are partially static fruits that teleport to random positions
 * every 20 seconds and they are worth 150 points.
 */
public class Cherry extends Fruit {
    private Random random;
    private int teleportCounter;
    private final int TELEPORT_INTERVAL = 40; // Teleports every 40 updates
    
    /**
     * Creates a new cherry at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Cherry(int x, int y) {
        super(x, y, "CHERRY", 150);
        this.random = new Random();
        this.teleportCounter = 0;
    }
    
    /**
     * Teleport the cherry to a random valid position on the board.
     * @param board the game board used to check for valid positions to move
     */
    public void teleport(Board board) {
        if (collected) {
            return; // Don't teleport if already collected
        }
        
        teleportCounter++;
        
        if (teleportCounter < TELEPORT_INTERVAL) {
            return;
        }
        
        teleportCounter = 0;
        
        // Try to find a valid position to teleport to
        int attempts = 0;
        int maxAttempts = 50; // Avoid infinite loops
        
        while (attempts < maxAttempts) {
            int newX = 1 + random.nextInt(board.getWidth() - 2);
            int newY = 1 + random.nextInt(board.getHeight() - 2);
            
            // Check that it's a valid and empty position
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                position.setX(newX);
                position.setY(newY);
                break;
            }
            
            attempts++;
        }
    }
}