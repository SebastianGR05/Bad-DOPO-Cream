package Domain;

/**
 * Baldosa caliente que derrete bloques de hielo
 */
public class HotTile extends Obstacle {
    
    public HotTile(int x, int y) {
        super(x, y, "HOT_TILE", false, false);
    }
    
    /**
     * Derrite bloques de hielo sobre esta baldosa
     */
    public void meltIceBlockIfPresent(Board board) {
        int x = position.getX();
        int y = position.getY();
        
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
