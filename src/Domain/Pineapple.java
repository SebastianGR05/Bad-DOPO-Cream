package Domain;

import java.util.Random;

/**
 * Representa una piña que se mueve constantemente por el tablero
 * Esta fruta aparece en los niveles 2 y 3, agregando un desafío adicional
 * porque el jugador debe atraparla mientras está en movimiento.
 */
public class Pineapple extends Fruit {
    private Random random;
    private int moveCounter; // Contador para controlar la frecuencia de movimiento
    
    public Pineapple(int x, int y) {
        super(x, y, "PINEAPPLE",200);
        this.random = new Random();
        this.moveCounter = 0;
    }
    
    /**
     * Mueve la piña a una posición adyacente aleatoria si es posible
     */
    public void move(Board board) {
        if (collected) {
            return; // No moverse si ya fue recolectada
        }
        
        moveCounter++;
        
        // Moverse cada 3 actualizaciones para que no sea demasiado rápida
        if (moveCounter < 3) {
            return;
        }
        
        moveCounter = 0;
        
        int currentX = position.getX();
        int currentY = position.getY();
        
        // Intentar moverse en una dirección aleatoria
        int direction = random.nextInt(4); // 0=arriba, 1=derecha, 2=abajo, 3=izquierda
        int newX = currentX;
        int newY = currentY;
        
        switch(direction) {
            case 0: 
            	newY--; 
            	break; // Arriba
            case 1: 
            	newX++; 
            	break; // Derecha
            case 2: 
            	newY++; 
            	break; // Abajo
            case 3: 
            	newX--; 
            	break; // Izquierda
        }
        
        // Verificar que la nueva posición sea válida
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            position.setX(newX);
            position.setY(newY);
        }
    }
}