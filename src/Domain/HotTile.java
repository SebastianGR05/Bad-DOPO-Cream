package Domain;

/**
 * Represents a hot tile obstacle in the game.
 * Hot tiles are special floor tiles that instantly melt any ice blocks
 * created on top of them. They are not solid and not lethal.
 */
public class HotTile extends Obstacle {
    
    /**
     * Creates a hot tile at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public HotTile(int x, int y) {
        super(x, y, "HOT_TILE", false, false);
    }
    
    /**
     * Melts any ice block that exists on this hot tile's position.
     * @param board the game board containing all ice blocks to check
     */
    public void meltIceBlockIfPresent(Board board) {
        int x = position.getX();
        int y = position.getY();
        
        for (IceBlock block : board.getIceBlocks()) {
            if (block.exists() && 
            	block.getPosition().getX() == x && 
            	block.getPosition().getY() == y) {
                block.destroy();
                break;
            }
        }
    }
}