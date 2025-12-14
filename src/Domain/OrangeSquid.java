package Domain;

/**
 * Represents an Orange Squid enemy in the game.
 * They chase the player and can destroy ice blocks in their path.
 * They break one ice block at a time before they move.
 */
public class OrangeSquid extends Enemy {
    private IceCream target;
    private Board board;
    
    /**
     * Creates a new Orange Squid at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public OrangeSquid(int x, int y) {
        super(x, y, "SQUID");
    }
    
    /**
     * Sets the target(player) that the squid will chase.
     * @param target the ice cream to pursue
     */
    public void setTarget(IceCream target) {
        this.target = target;
    }
    
    /**
     * Sets the board reference needed to break ice blocks.
     * @param board the game board
     */
    public void setBoard(Board board) {
        this.board = board;
    }
    
    /**
     * Updates the squid's position to chase the target player.
     * @param board the game board used to check for obstacles and break ice blocks
     */
    @Override
    public void updatePosition(Board board) {
        if (target == null || !target.isAlive()) {
            return;
        }
        
        int playerX = target.getPosition().getX();
        int playerY = target.getPosition().getY();
        int currentX = position.getX();
        int currentY = position.getY();
        
        
        int deltaX = playerX - currentX;
        int deltaY = playerY - currentY;
        
        int newX = currentX;
        int newY = currentY;
        
        // Determine movement direction
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Move horizontally
            if (deltaX > 0) {
                newX++;
            } else if (deltaX < 0) {
                newX--;
            }
        } else {
            // Move vertically
            if (deltaY > 0) {
                newY++;
            } else if (deltaY < 0) {
                newY--;
            }
        }
        
        // If there's an ice block, break it and move
        if (board.hasIceBlock(newX, newY)) {
            destroyIceBlockAt(newX, newY);
        }
        
        // Try to move if position is valid and obstacle-free
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // If can't move in main direction, try alternative
            newX = currentX;
            newY = currentY;
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Try moving vertically
                if (deltaY > 0) {
                    newY++;
                } else if (deltaY < 0) {
                    newY--;
                }
            } else {
                // Try moving horizontally
                if (deltaX > 0) {
                    newX++;
                } else if (deltaX < 0) {
                    newX--;
                }
            }
            
            // If there's an ice block in alternative direction, break it
            if (board.hasIceBlock(newX, newY)) {
                destroyIceBlockAt(newX, newY);
            }
            
            // Try the alternative movement
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                move(newX, newY);
            }
        }
    }
    
    /**
     * Destroy an ice block at the given position.
     * @param x the horizontal position of the ice block
     * @param y the vertical position of the ice block
     */
    private void destroyIceBlockAt(int x, int y) {
        if (board != null) {
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
}