package Domain;

/**
 * Class that represents the position on the game board using x and y coordinates
 * of all the game elements such as players, enemies, fruits, and obstacles.
 */
public class Position {
    private int x;
    private int y;
    
    /**
     * Creates a new position with the given coordinates.
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the horizontal coordinate of this position.
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the vertical coordinate of this position.
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Updates the horizontal coordinate of this position.
     * @param x the new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Updates the vertical coordinate of this position.
     * @param y the new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Checks if this position has the same coordinates as another position.
     * @param other the position to compare with
     * @return true if both positions have the same x and y coordinates, false otherwise
     */
    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }
}