package Domain;

/**
 * Represents a grape in the game.
 * Grapes are fruits that don't move and are worth 50 points.
 */
public class Grape extends Fruit {
    
    /**
     * Creates a new grape at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Grape(int x, int y) {
        super(x, y, "GRAPE", 50);
    }
}