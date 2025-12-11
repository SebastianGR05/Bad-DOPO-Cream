package Domain;

import java.util.Random;

/**
 * Representa una cereza que se teletransporta aleatoriamente
 * Esta es la fruta más difícil de recolectar porque desaparece y reaparece
 * en diferentes posiciones del tablero.
 */
public class Cherry extends Fruit {
    private Random random;
    private int teleportCounter;
    private final int TELEPORT_INTERVAL = 20; // Se teletransporta cada 5 actualizaciones
    
    public Cherry(int x, int y) {
        super(x, y, "CHERRY",150);
        this.random = new Random();
        this.teleportCounter = 0;
    }
    
    /**
     * Teletransporta la cereza a una posición aleatoria válida en el tablero
     * Este método debe llamarse periódicamente desde el Game
     */
    public void teleport(Board board) {
        if (collected) {
            return; // No teletransportarse si ya fue recolectada
        }
        
        teleportCounter++;
        
        if (teleportCounter < TELEPORT_INTERVAL) {
            return;
        }
        
        teleportCounter = 0;
        
        // Intentar encontrar una posición válida para teletransportarse
        int attempts = 0;
        int maxAttempts = 50; // Evitar bucles infinitos
        
        while (attempts < maxAttempts) {
            int newX = 1 + random.nextInt(board.getWidth() - 2);
            int newY = 1 + random.nextInt(board.getHeight() - 2);
            
            // Verificar que sea una posición válida y vacía
            if (board.isValidPosition(newX, newY) && 
                !board.hasWall(newX, newY) && 
                !board.hasIceBlock(newX, newY)) {
                position.setX(newX);
                position.setY(newY);
                break;
            }
            
            attempts++;
        }
    }
}