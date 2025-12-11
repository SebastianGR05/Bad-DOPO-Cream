package Domain;

/**
 * Clase base para todas las frutas del juego
 */
public abstract class Fruit {
    protected Position position;
    protected boolean collected;
    protected String type;
    protected int points;
    
    public Fruit(int x, int y, String type, int points) {
        this.position = new Position(x, y);
        this.collected = false;
        this.type = type;
        this.points = points;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void collect() {
        collected = true;
    }
    
    public String getType() {
        return type;
    }
    
    public int getPoints() {
    	return points;
    }
}