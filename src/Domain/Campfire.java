package Domain;

/**
 * Represents a campfire obstacle in the game.
 * They are obstacles that don't block movement but 
 * can eliminate the player on contact when they are on.
 * They can be temporarily extinguished by creating an ice block on them
 * but they will melt it and then reignite after 10 seconds.
 */
public class Campfire extends Obstacle {
    
    private boolean on; // Whether the fire is on
    private int extinguishTimer; // Counter for reigniting
    private final int TURN_ON_TIME = 20; // 20 updates to reignite
    
    /**
     * Creates a new campfire at the specified position.
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Campfire(int x, int y) {
        super(x, y, "CAMPFIRE", false, true);
        this.on = true;
        this.extinguishTimer = 0;
    }
    
    /**
     * Temporarily extinguishes the campfire.
     */
    public void extinguish() {
        on = false;
        lethal = false;
        extinguishTimer = TURN_ON_TIME;
    }
    
    /**
     * Checks if the campfire is currently on.
     * @return true if the fire is on, false if it's extinguished
     */
    public boolean isOn() {
        return on;
    }
    
    /**
     * Updates the campfire's state each update(half second).
     */
    @Override
    public void update() {
        if (!on && extinguishTimer > 0) {
            extinguishTimer--;
            if (extinguishTimer == 0) {
                on = true;
                lethal = true;
            }
        }
    }
}