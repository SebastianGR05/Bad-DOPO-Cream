package Domain;

/**
 * Clase base para todos los obstáculos del juego
 */
public abstract class Obstacle {
    protected Position position;
    protected String type;
    protected boolean exists;    
    protected boolean solid;
    protected boolean lethal;
    
    /**
     * Constructor que inicializa las propiedades del obstáculo
     * @param x posición X
     * @param y posición Y
     * @param type tipo de obstáculo
     * @param solid si bloquea movimiento
     * @param lethal si elimina al jugador
     */
    public Obstacle(int x, int y, String type, boolean solid, boolean lethal) {//e
        this.position = new Position(x, y);
        this.type = type;
        this.exists = true; 
        this.solid = solid;
        this.lethal = lethal;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public String getType() {
        return type;
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
    
    public boolean isSolid() {
        return solid;
    }
    
    public boolean isLethal() {
        return lethal;
    }
    
    public void update() {
    }
}