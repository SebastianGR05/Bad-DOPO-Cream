package Domain;

import java.util.ArrayList;

/**
 * Clase principal que maneja toda la lógica del juego
 */
public class Game {
    private Board board;
    private IceCream player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Fruit> fruits;
    private int totalFruits;
    private boolean gameWon;
    private boolean gameLost;
    private long startTime;
    private final long TIME_LIMIT = 180000; // 3 minutos en milisegundos
    private boolean paused;
    
    public Game() {
        board = new Board(15, 12);
        initializeLevel1();
        this.paused = false;
    }
    
    /**
     * Inicializa el nivel 1: helado, 2 trolls, 8 uvas y 8 plátanos
     */
    private void initializeLevel1() {
        // Crear jugador (helado)
        player = new IceCream(1, 1, "VANILLA");
        
        // Crear enemigos (2 trolls)
        enemies = new ArrayList<>();
        enemies.add(new Troll(13, 1));
        enemies.add(new Troll(1, 10));
        
        // Crear frutas (8 uvas y 8 plátanos)
        fruits = new ArrayList<>();
        
        // Uvas en posiciones fijas
        fruits.add(new Grape(3, 2));
        fruits.add(new Grape(5, 2));
        fruits.add(new Grape(11, 2));
        fruits.add(new Grape(3, 5));
        fruits.add(new Grape(9, 5));
        fruits.add(new Grape(3, 8));
        fruits.add(new Grape(11, 8));
        fruits.add(new Grape(9, 10));
        
        // Plátanos en posiciones fijas
        fruits.add(new Banana(7, 2));
        fruits.add(new Banana(13, 5));
        fruits.add(new Banana(5, 6));
        fruits.add(new Banana(7, 7));
        fruits.add(new Banana(11, 6));
        fruits.add(new Banana(2, 9));
        fruits.add(new Banana(7, 9));
        fruits.add(new Banana(13, 10));
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Mueve al jugador en una dirección
     */
    public void movePlayer(String direction) {
        if (paused || gameLost || gameWon) {
            return;
        }
        
        player.setDirection(direction);
        
        int currentX = player.getPosition().getX();
        int currentY = player.getPosition().getY();
        int newX = currentX;
        int newY = currentY;
        
        switch(direction) {
            case "UP": newY--; break;
            case "DOWN": newY++; break;
            case "LEFT": newX--; break;
            case "RIGHT": newX++; break;
        }
        
        // Verificar si puede moverse
        if (board.isValidPosition(newX, newY) && !board.hasWall(newX, newY) 
            && !board.hasIceBlock(newX, newY)) {
            player.move(newX, newY);
            checkFruitCollection();
            checkEnemyCollision();
        }
    }
    
    /**
     * Crea o destruye bloques de hielo
     */
    public void handleIceBlock() {
        if (paused || gameLost || gameWon) {
            return;
        }
        
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        String direction = player.getDirection();
        
        int targetX = x;
        int targetY = y;
        
        switch(direction) {
            case "UP": targetY--; break;
            case "DOWN": targetY++; break;
            case "LEFT": targetX--; break;
            case "RIGHT": targetX++; break;
        }
        
        // Si hay bloque, destruir en dominó
        if (board.hasIceBlock(targetX, targetY)) {
            board.destroyIceBlocks(x, y, direction);
        } 
        // Si no hay bloque, crear uno
        else if (board.isValidPosition(targetX, targetY) && !board.hasWall(targetX, targetY)) {
            board.createIceBlock(targetX, targetY);
        }
    }
    
    /**
     * Verifica si el jugador recogió alguna fruta
     */
    private void checkFruitCollection() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        for (Fruit fruit : fruits) {
            if (!fruit.isCollected() && fruit.getPosition().getX() == x 
                && fruit.getPosition().getY() == y) {
                fruit.collect();
                player.collectFruit();
                
                // Verificar si ganó
                if (player.getFruitsCollected() >= totalFruits) {
                    gameWon = true;
                }
                break;
            }
        }
    }
    
    /**
     * Verifica colisión con enemigos
     */
    private void checkEnemyCollision() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().getX() == x && enemy.getPosition().getY() == y) {
                player.die();
                gameLost = true;
                break;
            }
        }
    }
    
    /**
     * Actualiza el estado del juego (enemigos, tiempo, etc.)
     */
    public void update() {
        if (paused || gameLost || gameWon) {
            return;
        }
        
        // Verificar tiempo
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime >= TIME_LIMIT) {
            gameLost = true;
            return;
        }
        
        // Mover enemigos
        for (Enemy enemy : enemies) {
            enemy.updatePosition(board);
        }
        
        // Verificar colisión después del movimiento
        checkEnemyCollision();
    }
    
    public void togglePause() {
        paused = !paused;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public IceCream getPlayer() {
        return player;
    }
    
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    
    public ArrayList<Fruit> getFruits() {
        return fruits;
    }
    
    public boolean isGameWon() {
        return gameWon;
    }
    
    public boolean isGameLost() {
        return gameLost;
    }
    
    public long getTimeRemaining() {
        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = TIME_LIMIT - elapsed;
        return Math.max(0, remaining);
    }
    
    public int getTotalFruits() {
        return totalFruits;
    }
    
    /**
     * Reinicia el nivel
     */
    public void restart() {
        initializeLevel1();
    }
}