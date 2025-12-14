package Domain;

/**
 * Abstract class for all the different fruits in the game.
 * Fruits are collectible items that give points to the player when he picks them up.
 * Each fruit type has different behaviors and point values.
 */
public abstract class Fruit {
    protected Position position;
    protected boolean collected;
    protected String type;
    protected int points;
    
    /**
     * Creates a new type fruit with a certain points value.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     * @param type the type of fruit: "BANANA", "GRAPE", "PINEAPPLE" or "CHERRY"
     * @param points the number of points this fruit is worth
     */
    public Fruit(int x, int y, String type, int points) {
        this.position = new Position(x, y);
        this.collected = false;
        this.type = type;
        this.points = points;
    }
    
    /**
     * Gets the current position of this fruit on the board.
     * @return the position of the fruit
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Checks if this fruit has been collected by the player.
     * @return true if the fruit was collected, false if it isn't collected
     */
    public boolean isCollected() {
        return collected;
    }
    
    /**
     * Marks this fruit as collected.
     */
    public void collect() {
        collected = true;
    }
    
    /**
     * Gets the type of this fruit.
     * @return the fruit type: "BANANA", "GRAPE", "PINEAPPLE" or "CHERRY"
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the point value of this fruit.
     * @return the the number of points this fruit is worth
     */
    public int getPoints() {
    	return points;
    }
}