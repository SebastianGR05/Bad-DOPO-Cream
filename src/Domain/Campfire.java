package Domain;

/**
 * Fogata - No sólida, mortal cuando está encendida
 */
public class Campfire extends Obstacle {
    
    private boolean on; // Si está encendida
    private int extinguishTimer; // Contador para reencenderse
    private final int TURN_ON_TIME = 20; // 20 actualizaciones
    
    public Campfire(int x, int y) {
        super(x, y, "CAMPFIRE", false, true);
        this.on = true;
        this.extinguishTimer = 0;
    }
    
    /**
     * Apaga la fogata temporalmente
     */
    public void extinguish() {
        on = false;
        lethal = false;
        extinguishTimer = TURN_ON_TIME;
    }
    
    /**
     * Verifica si está encendida
     */
    public boolean isOn() {
        return on;
    }
    
    /**
     * Actualiza el temporizador y reenciende si es necesario
     */
    @Override
    public void update() {
        if (!on && extinguishTimer > 0) {
            extinguishTimer--;
            if (extinguishTimer == 0) {
                on = true;
                lethal = true;
            }
        }
    }
}
