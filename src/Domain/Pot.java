package Domain;

/**
 * Enemigo Maceta - Persigue al jugador pero no puede romper bloques
 * Este enemigo aparece en el nivel 2 y representa un desafío mayor
 * porque sigue al helado activamente en lugar de moverse en patrón.
 */
public class Pot extends Enemy {
    private IceCream target; // Referencia al jugador para perseguirlo
    
    /**
     * Constructor que inicializa la maceta en una posición específica
     */
    public Pot(int x, int y) {
        super(x, y, "POT");
    }
    
    /**
     * Establece el objetivo que la maceta debe perseguir
     * Esto normalmente será el helado del jugador
     */
    public void setTarget(IceCream target) {
        this.target = target;
    }
    
    /**
     * Actualiza la posición de la maceta moviéndose hacia el jugador
     * Usa un algoritmo simple de persecución: se mueve hacia el jugador
     * primero en el eje donde hay mayor distancia
     */
    @Override
    public void updatePosition(Board board) {
        if (target == null || !target.isAlive()) {
            return; // No hacer nada si no hay objetivo
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
        // Esto hace que el movimiento sea más natural y predecible
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
        // La maceta no puede atravesar paredes ni bloques de hielo
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