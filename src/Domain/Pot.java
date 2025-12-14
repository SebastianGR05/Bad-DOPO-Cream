package Domain;

/**
 * Represents a Pot enemy in the game.
 * They chase the player and cannot break ice blocks.
 */
public class Pot extends Enemy {
    private IceCream target;
    
    /**
     * Creates a new Pot at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Pot(int x, int y) {
        super(x, y, "POT");
    }
    
    /**
     * Sets the target(player) that the pot will chase.
     * @param target the ice cream that will chase
     */
    public void setTarget(IceCream target) {
        this.target = target;
    }
    
    /**
     * Updates the pot's position to chase the target player.
     * @param board the game board used to check for obstacles
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
        
        // Calculate distance in both axes
        int deltaX = playerX - currentX;
        int deltaY = playerY - currentY;
        
        int newX = currentX;
        int newY = currentY;
        
        // Move first along the axis with bigger distance
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Move horizontally
            if (deltaX > 0) {
                newX++; // Move right
            } else if (deltaX < 0) {
                newX--; // Move left
            }
        } else {
            // Move vertically
            if (deltaY > 0) {
                newY++; // Move down
            } else if (deltaY < 0) {
                newY--; // Move up
            }
        }
        
        // Check if the new position is valid
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // If can't move in the main direction, try the other axis
            newX = currentX;
            newY = currentY;
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Try moving vertically instead
                if (deltaY > 0) {
                    newY++;
                } else if (deltaY < 0) {
                    newY--;
                }
            } else {
                // Try moving horizontally instead
                if (deltaX > 0) {
                    newX++;
                } else if (deltaX < 0) {
                    newX--;
                }
            }
            
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                move(newX, newY);
            }
        }
    }
}