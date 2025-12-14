package Domain;

/**
 * Represents a banana in the game.
 * Bananas are fruits that don't move and are worth 100 points.
 */
public class Banana extends Fruit {
    
    /**
     * Creates a new banana at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Banana(int x, int y) {
        super(x, y, "BANANA", 100);
    }
}