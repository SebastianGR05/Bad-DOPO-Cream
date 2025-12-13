package Domain;

/**
 * Enemigo Maceta, persigue al jugador pero no puede romper bloques
 */
public class Pot extends Enemy {
    private IceCream target;
    
    /**
     * Constructor que inicializa la maceta en una posición específica
     */
    public Pot(int x, int y) {
        super(x, y, "POT");
    }
    
    /**
     * Establece el objetivo que la maceta debe perseguir
     */
    public void setTarget(IceCream target) {
        this.target = target;
    }
    
    /**
     * Actualiza la posición de la maceta moviéndose hacia el jugador
     */
    @Override
    public void updatePosition(Board board) {
        if (target == null || !target.isAlive()) {
            return;
        }
        
        int playerX = target.getPosition().getX();
        int playerY = target.getPosition().getY();
        int currentX = position.getX();
        int currentY = position.getY();
        
        // Calcular diferencias en ambos ejes
        int deltaX = playerX - currentX;
        int deltaY = playerY - currentY;
        
        int newX = currentX;
        int newY = currentY;
        
        // Moverse primero en el eje donde hay mayor distancia
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Moverse horizontalmente
            if (deltaX > 0) {
                newX++; // Moverse a la derecha
            } else if (deltaX < 0) {
                newX--; // Moverse a la izquierda
            }
        } else {
            // Moverse verticalmente
            if (deltaY > 0) {
                newY++; // Moverse hacia abajo
            } else if (deltaY < 0) {
                newY--; // Moverse hacia arriba
            }
        }
        
        // Verificar que la nueva posición sea válida
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // Si no puede moverse en la dirección principal, intentar la otra
            newX = currentX;
            newY = currentY;
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Intentar moverse verticalmente en su lugar
                if (deltaY > 0) {
                    newY++;
                } else if (deltaY < 0) {
                    newY--;
                }
            } else {
                // Intentar moverse horizontalmente en su lugar
                if (deltaX > 0) {
                    newX++;
                } else if (deltaX < 0) {
                    newX--;
                }
            }
            
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                move(newX, newY);
            }
        }
    }
}