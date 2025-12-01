package Domain;

/**
 * Representa una posición en el tablero con coordenadas x, y
 */
public class Position {
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Verifica si esta posición es igual a otra
     */
    public boolean equals(Position other) {
        return this.x == other.x && this.y == other.y;
    }
}
