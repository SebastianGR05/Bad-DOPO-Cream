package Domain;

/**
 * Enemigo Calamar Naranja - Persigue al jugador Y puede romper bloques de hielo
 */
public class OrangeSquid extends Enemy { //e
    private IceCream target;
    private Board board;
    
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
        
        // Determinar dirección de movimiento
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Moverse horizontalmente
            if (deltaX > 0) {
                newX++;
            } else if (deltaX < 0) {
                newX--;
            }
        } else {
            // Moverse verticalmente
            if (deltaY > 0) {
                newY++;
            } else if (deltaY < 0) {
                newY--;
            }
        }
        
        // Si hay un bloque de hielo, romperlo y moverse
        if (board.hasIceBlock(newX, newY)) {
            destroyIceBlockAt(newX, newY);
        }
        
        // Intentar moverse si la posición es válida y no hay obstáculos
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            move(newX, newY);
        } else {
            // Si no puede moverse en la dirección principal, intentar alternativa
            newX = currentX;
            newY = currentY;
            
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Intentar moverse verticalmente
                if (deltaY > 0) {
                    newY++;
                } else if (deltaY < 0) {
                    newY--;
                }
            } else {
                // Intentar moverse horizontalmente
                if (deltaX > 0) {
                    newX++;
                } else if (deltaX < 0) {
                    newX--;
                }
            }
            
            // Si hay bloque en la dirección alternativa, romperlo
            if (board.hasIceBlock(newX, newY)) {
                destroyIceBlockAt(newX, newY);
            }
            
            // Intentar el movimiento alternativo
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                move(newX, newY);
            }
        }
    }
    
    /**
     * Destruye un bloque de hielo específico en la posición indicada
     */
    private void destroyIceBlockAt(int x, int y) {
        if (board != null) {
            for (IceBlock block : board.getIceBlocks()) {
                if (block.exists() && 
                    block.getPosition().getX() == x && 
                    block.getPosition().getY() == y) {
                    block.destroy();
                    break;
                }
            }
        }
    }
}