package Domain;

/**
 * Abstract class for all the obstacles in the game.
 * Obstacles can block movement, eliminate the player or have special behaviors.
 */
public abstract class Obstacle {
    protected Position position;
    protected String type;
    protected boolean exists;    
    protected boolean solid;
    protected boolean lethal;
    
    /**
     * Creates a new obstacle with the specified properties.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     * @param type the type of obstacle: "ICE_BLOCK", "CAMPFIRE" or "HOT_TILE"
     * @param solid true if this obstacle blocks movement, false otherwise
     * @param lethal true if this obstacle eliminates the player on contact, false otherwise
     */
    public Obstacle(int x, int y, String type, boolean solid, boolean lethal) {
        this.position = new Position(x, y);
        this.type = type;
        this.exists = true; 
        this.solid = solid;
        this.lethal = lethal;
    }
    
    /**
     * Gets the current position of this obstacle on the board.
     * @return the position of the obstacle
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Gets the type identifier of this obstacle.
     * @return the obstacle type: "ICE_BLOCK", "CAMPFIRE" or "HOT_TILE"
     */
    public String getType() {
        return type;
    }
    
    /**
     * Checks if this obstacle currently exists in the game.
     * @return true if the obstacle exists, false if it has been destroyed
     */
    public boolean exists() {
        return exists;
    }
    
    /**
     * Destroys the obstacle, removing it from the board.
     */
    public void destroy() {
        exists = false;
    }
    
    /**
     * Recreates the obstacle.
     */
    public void create() {
        exists = true;
    }
    
    /**
     * Checks if the obstacle blocks movement.
     * @return true if the obstacle is solid, false otherwise
     */
    public boolean isSolid() {
        return solid;
    }
    
    /**
     * Checks if the obstacle is dangerous to the player.
     * @return true if the obstacle is lethal, false otherwise
     */
    public boolean isLethal() {
        return lethal;
    }
    
    /**
     * Updates the obstacle state each half second.
     */
    public void update() {
    }
}