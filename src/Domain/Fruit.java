package Domain;

/**
 * Clase base para todas las frutas del juego
 */
public abstract class Fruit {
    protected Position position;
    protected boolean collected;
    protected String type;
    
    public Fruit(int x, int y, String type) {
        this.position = new Position(x, y);
        this.collected = false;
        this.type = type;
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
}