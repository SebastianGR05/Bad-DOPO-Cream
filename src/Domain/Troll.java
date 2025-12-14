package Domain;

/**
 * Represents a Troll enemy in the game.
 * They move in one direction until they hit a wall or an obstacle or have move for 14 steps, 
 * then changes to the next direction in the pattern(right, down, left, up).
 * They don't chase the player or break ice blocks.
 */
public class Troll extends Enemy {
    private int direction;
    private int stepCount;
    private int maxSteps;
    
    /**
     * Creates a new Troll at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Troll(int x, int y) {
        super(x, y, "TROLL");
        this.direction = 0;
        this.stepCount = 0;
        this.maxSteps = 14; // Changes direction every 14 steps
    }
    
    /**
     * Updates the troll's position.
     * @param board the game board used to check for walls and obstacles
     */
    @Override
    public void updatePosition(Board board) {
        stepCount++;
        
        if (stepCount >= maxSteps) {
            direction = (direction + 1) % 4;
            stepCount = 0;
        }
        
        int newX = position.getX();
        int newY = position.getY();
        
        // Calculate new position based on current direction
        switch(direction) {
            case 0: 
            	newX++;
            	break; // Right
            case 1: 
            	newY++; 
            	break; // Down
            case 2: 
            	newX--; 
            	break; // Left
            case 3: 
            	newY--; 
            	break; // Up
        }
        
        // Check if the position is valid
        if (board.isValidPosition(newX, newY) && !board.hasWall(newX, newY) 
            && !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // If can't move, change direction immediately
            direction = (direction + 1) % 4;
            stepCount = 0;
        }
    }
}