package Domain;

/**
 * Clase base para todos los enemigos
 */
public abstract class Enemy {
    protected Position position;
    protected String type;
    
    public Enemy(int x, int y, String type) {
        this.position = new Position(x, y);
        this.type = type;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public String getType() {
        return type;
    }
    
    public void move(int newX, int newY) {
        position.setX(newX);
        position.setY(newY);
    }
    
    /**
     * Método abstracto para el movimiento específico de cada enemigo
     */
    public abstract void updatePosition(Board board);
}
