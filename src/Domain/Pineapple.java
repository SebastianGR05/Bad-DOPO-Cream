package Domain;

import java.util.Random;

/**
 * Represents a pineapple fruit in the game.
 * Pineapples move randomly around the board every game update and are worth 200 points.
 */
public class Pineapple extends Fruit {
    private Random random;
    private int moveCounter; // Counter to control movement frequency
    
    /**
     * Creates a new pineapple at the specified position.
     * The pineapple is worth 200 points and will move randomly every few updates.
     * 
     * @param x the horizontal position on the board
     * @param y the vertical position on the board
     */
    public Pineapple(int x, int y) {
        super(x, y, "PINEAPPLE", 200);
        this.random = new Random();
        this.moveCounter = 0;
    }
    
    /**
     * Moves the pineapple to a random adjacent position if possible.
     * @param board the game board used to check for valid positions to move
     */
    public void move(Board board) {
        if (collected) {
            return;
        }
        
        moveCounter++;
        
        // Move every update
        if (moveCounter < 1) {
            return;
        }
        
        moveCounter = 0;
        
        int currentX = position.getX();
        int currentY = position.getY();
        
        // Try to move in a random direction
        int direction = random.nextInt(4);
        int newX = currentX;
        int newY = currentY;
        
        switch(direction) {
            case 0: 
            	newY--; 
            	break; // Up
            case 1: 
            	newX++; 
            	break; // Right
            case 2: 
            	newY++; 
            	break; // Down
            case 3: 
            	newX--; 
            	break; // Left
        }
        
        // Check that the new position is valid
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            position.setX(newX);
            position.setY(newY);
        }
    }
}