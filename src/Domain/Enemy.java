package Domain;

/**
 * Abstract class for all enemies in the game.
 * Enemies eliminate the player on contact and move according to their specific behaviors.
 */
public abstract class Enemy {
    protected Position position;
    protected String type;
    
    /**
     * Creates a new enemy at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     * @param type the type of enemy: "TROLL", "POT" or "SQUID"
     */
    public Enemy(int x, int y, String type) {
        this.position = new Position(x, y);
        this.type = type;
    }
    
    /**
     * Gets the current position of this enemy on the board.
     * @return the position of the enemy
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Gets the type of this enemy.
     * @return the enemy type: "TROLL", "POT" or "SQUID"
     */
    public String getType() {
        return type;
    }
    
    /**
     * Moves the enemy to a new position on the board.
     * @param newX the new horizontal position
     * @param newY the new vertical position
     */
    public void move(int newX, int newY) {
        position.setX(newX);
        position.setY(newY);
    }
    
    /**
     * Updates the enemy's position based on its specific behavior.
     * @param board the game board, used to check for obstacles and calculate movement
     */
    public abstract void updatePosition(Board board);
}