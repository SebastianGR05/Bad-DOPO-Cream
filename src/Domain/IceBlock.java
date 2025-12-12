package Domain;

/**
 * Bloque de hielo
 * Impide el movimiento pero puede ser destruido
 */
public class IceBlock extends Obstacle {
    
    public IceBlock(int x, int y) {
        super(x, y, "ICE_BLOCK", true, false);
    }
}