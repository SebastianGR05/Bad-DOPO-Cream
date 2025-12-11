package Domain;

/**
 * Representa al helado (jugador)
 */
public class IceCream {
    private Position position;
    private String flavor; // "VANILLA", "STRAWBERRY", "CHOCOLATE"
    private String direction; // "UP", "DOWN", "LEFT", "RIGHT"
    private int fruitsCollected;
    private boolean alive;
    private int score;
    
    public IceCream(int x, int y, String flavor) {
        position = new Position(x, y);
        this.flavor = flavor;
        direction = "DOWN";
        fruitsCollected = 0;
        alive = true;
        score = 0;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public String getFlavor() {
        return flavor;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public int getFruitsCollected() {
        return fruitsCollected;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public void move(int newX, int newY) {
        position.setX(newX);
        position.setY(newY);
    }
    
    public void collectFruit(int points) {
        fruitsCollected++;
        score += points;
    }
    
    public void die() {
        alive = false;
    }
    
    public void reset(int x, int y) {
        position.setX(x);
        position.setY(y);
        fruitsCollected = 0;
        alive = true;
        direction = "DOWN";
        score = 0;
    }
    
    public int getScore() {
        return score;
    }
}
