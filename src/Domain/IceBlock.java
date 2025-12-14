package Domain;

/**
 * Represents an ice block obstacle in the game.
 * They are solid but not lethal obstacles that block movement for players and enemies.
 * They can be created by the player and destroyed also by the player or the Orange Squid.
 */
public class IceBlock extends Obstacle {
    
    /**
     * Creates a new ice block at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public IceBlock(int x, int y) {
        super(x, y, "ICE_BLOCK", true, false);
    }
}