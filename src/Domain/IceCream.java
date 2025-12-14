package Domain;

/**
 * Represents the ice cream character controlled by the player.
 * The ice cream can move in four directions(up, down, right, left), 
 * create and destroy ice blocks, collect fruits for points, 
 * and can be eliminated by colliding with enemies or certain obstacles.
 */
public class IceCream {
    private Position position;
    private String flavor; // "VANILLA", "STRAWBERRY", "CHOCOLATE"
    private String direction; // "UP", "DOWN", "LEFT", "RIGHT"
    private int fruitsCollected;
    private boolean alive;
    private int score;
    
    /**
     * Creates a new ice cream character at the specified position.
     * @param x the horizontal starting position on the board
     * @param y the vertical starting position on the board
     * @param flavor the flavor/color of the ice cream: "VANILLA", "STRAWBERRY" or "CHOCOLATE"
     */
    public IceCream(int x, int y, String flavor) {
        position = new Position(x, y);
        this.flavor = flavor;
        direction = "DOWN";
        fruitsCollected = 0;
        alive = true;
        score = 0;
    }
    
    /**
     * Gets the current position of the ice cream on the board.
     * @return the position of the ice cream
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Gets the flavor of this ice cream.
     * @return the flavor of the ice cream: "VANILLA", "STRAWBERRY" or "CHOCOLATE"
     */
    public String getFlavor() {
        return flavor;
    }
    
    /**
     * Gets the current direction the ice cream is facing.
     * @return the direction: "UP", "DOWN", "LEFT" or "RIGHT"
     */
    public String getDirection() {
        return direction;
    }
    
    /**
     * Gets the number of fruits the ice cream has collected.
     * @return the count of collected fruits
     */
    public int getFruitsCollected() {
        return fruitsCollected;
    }
    
    /**
     * Checks if the ice cream is still alive.
     * @return true if alive, false if eliminated
     */
    public boolean isAlive() {
        return alive;
    }
    
    /**
     * Updates the direction the ice cream is facing.
     * @param direction the new direction: "UP", "DOWN", "LEFT" or "RIGHT"
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    /**
     * Moves the ice cream to a new position on the board.
     * @param newX the new horizontal position
     * @param newY the new vertical position
     */
    public void move(int newX, int newY) {
        position.setX(newX);
        position.setY(newY);
    }
    
    /**
     * Saves that the ice cream collected a fruit.
     * @param points the point value of the collected fruit
     */
    public void collectFruit(int points) {
        fruitsCollected++;
        score += points;
    }
    
    /**
     * Marks the ice cream as dead.
     */
    public void die() {
        alive = false;
    }
    
    /**
     * Resets the ice cream to a starting state at a new position.
     * @param x the new horizontal starting position
     * @param y the new vertical starting position
     */
    public void reset(int x, int y) {
        position.setX(x);
        position.setY(y);
        fruitsCollected = 0;
        alive = true;
        direction = "DOWN";
        score = 0;
    }
    
    /**
     * Gets the current score of the collected fruits by the ice cream.
     * @return the total score
     */
    public int getScore() {
        return score;
    }
}