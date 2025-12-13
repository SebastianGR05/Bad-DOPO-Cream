package Domain;

/**
 * Enemigo Troll, se mueve en patrón horizontal/vertical
 */
public class Troll extends Enemy {
    private int direction;
    private int stepCount;
    private int maxSteps;
    
    public Troll(int x, int y) {
        super(x, y, "TROLL");
        this.direction = 0;
        this.stepCount = 0;
        this.maxSteps = 14; // Cambia de dirección cada 14 pasos
    }
    
    @Override
    public void updatePosition(Board board) {
        stepCount++;
        
        if (stepCount >= maxSteps) {
            direction = (direction + 1) % 4;
            stepCount = 0;
        }
        
        int newX = position.getX();
        int newY = position.getY();
        
        // Calcular nueva posición según dirección
        switch(direction) {
            case 0: newX++; break; // Derecha
            case 1: newY++; break; // Abajo
            case 2: newX--; break; // Izquierda
            case 3: newY--; break; // Arriba
        }
        
        // Verificar que la posición sea válida
        if (board.isValidPosition(newX, newY) && !board.hasWall(newX, newY) 
            && !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // Si no puede moverse, cambiar de dirección inmediatamente
            direction = (direction + 1) % 4;
            stepCount = 0;
        }
    }
}
