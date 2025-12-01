package Domain;

/**
 * Enemigo Calamar Naranja - Persigue al jugador Y puede romper bloques de hielo
 * Este es el enemigo más difícil del juego porque combina la persecución
 * activa con la habilidad de destruir las defensas del jugador.
 */
public class OrangeSquid extends Enemy {
    private IceCream target;
    private Board board; // Necesita referencia al tablero para romper bloques
    
    public OrangeSquid(int x, int y) {
        super(x, y, "SQUID");
    }
    
    public void setTarget(IceCream target) {
        this.target = target;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    @Override
    public void updatePosition(Board board) {
        if (target == null || !target.isAlive()) {
            return;
        }
        
        int playerX = target.getPosition().getX();
        int playerY = target.getPosition().getY();
        int currentX = position.getX();
        int currentY = position.getY();
        
        int deltaX = playerX - currentX;
        int deltaY = playerY - currentY;
        
        int newX = currentX;
        int newY = currentY;
        
        // Determinar dirección de movimiento (igual que la maceta)
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                newX++;
            } else if (deltaX < 0) {
                newX--;
            }
        } else {
            if (deltaY > 0) {
                newY++;
            } else if (deltaY < 0) {
                newY--;
            }
        }
        
        // Si hay un bloque de hielo en el camino, romperlo
        if (board.hasIceBlock(newX, newY)) {
            destroyIceBlockAt(newX, newY);
            // No moverse este turno, solo romper el bloque
            return;
        }
        
        // Moverse si la posición es válida
        if (board.isValidPosition(newX, newY) && !board.hasWall(newX, newY)) {
            move(newX, newY);
        } else {
            // Intentar movimiento alternativo
            newX = currentX;
            newY = currentY;
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaY > 0) {
                    newY++;
                } else if (deltaY < 0) {
                    newY--;
                }
            } else {
                if (deltaX > 0) {
                    newX++;
                } else if (deltaX < 0) {
                    newX--;
                }
            }
            
            // Si hay bloque en la dirección alternativa, romperlo
            if (board.hasIceBlock(newX, newY)) {
                destroyIceBlockAt(newX, newY);
                return;
            }
            
            if (board.isValidPosition(newX, newY) && !board.hasWall(newX, newY)) {
                move(newX, newY);
            }
        }
    }
    
    /**
     * Destruye un bloque de hielo específico en la posición indicada
     * El calamar rompe de a un bloque a la vez, no en dominó
     */
    private void destroyIceBlockAt(int x, int y) {
        if (board != null) {
            for (IceBlock block : board.getIceBlocks()) {
                if (block.exists() && 
                    block.getPosition().getX() == x && 
                    block.getPosition().getY() == y) {
                    block.destroy();
                    break; // Solo destruir un bloque
                }
            }
        }
    }
}