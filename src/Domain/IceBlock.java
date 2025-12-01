package Domain;

/**
 * Representa un bloque de hielo que el helado puede crear o destruir
 */
public class IceBlock {
    private Position position;
    private boolean exists;
    
    public IceBlock(int x, int y) {
        this.position = new Position(x, y);
        this.exists = true;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public boolean exists() {
        return exists;
    }
    
    public void destroy() {
        exists = false;
    }
    
    public void create() {
        exists = true;
    }
}
