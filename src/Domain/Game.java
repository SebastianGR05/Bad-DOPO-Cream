package Domain;

import java.util.ArrayList;

/**
 * Clase principal que maneja toda la lógica del juego para los tres niveles
 * Esta clase coordina todos los elementos del juego y mantiene el estado general
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
    private int currentLevel; // 1, 2 o 3
    private String playerFlavor; // Sabor elegido por el jugador
    
    /**
     * Constructor por defecto que inicia en el nivel 1 con vainilla
     */
    public Game() {
        this(1, "VANILLA");
    }
    
    /**
     * Constructor que permite especificar nivel y sabor de helado
     */
    public Game(int level, String flavor) {
        this.currentLevel = level;
        this.playerFlavor = flavor;
        board = new Board(20, 20, level);
        initializeLevel(level);
        this.paused = false;
    }
    
    /**
     * Inicializa el nivel correspondiente según el número
     */
    private void initializeLevel(int level) {
        switch(level) {
            case 1:
                initializeLevel1();
                break;
            case 2:
                initializeLevel2();
                break;
            case 3:
                initializeLevel3();
                break;
            default:
                initializeLevel1();
        }
    }
    
    /**
     * Inicializa el nivel 1: 2 trolls, 8 uvas y 8 plátanos
     * Este es el nivel más fácil con enemigos que siguen un patrón fijo
     */
    private void initializeLevel1() {
        // Crear jugador en la esquina superior izquierda
        player = new IceCream(1, 1, playerFlavor);
        
        // Crear 2 trolls en posiciones opuestas
        enemies = new ArrayList<>();
        enemies.add(new Troll(18, 1));
        enemies.add(new Troll(1, 18));
        
        // Crear frutas: 8 uvas y 8 plátanos en posiciones estratégicas
        fruits = new ArrayList<>();
        
        // Distribuir uvas en el tablero
        fruits.add(new Grape(3, 3));
        fruits.add(new Grape(8, 3));
        fruits.add(new Grape(15, 3));
        fruits.add(new Grape(3, 10));
        fruits.add(new Grape(10, 10));
        fruits.add(new Grape(15, 10));
        fruits.add(new Grape(3, 16));
        fruits.add(new Grape(15, 16));
        
        // Distribuir plátanos
        fruits.add(new Banana(6, 6));
        fruits.add(new Banana(12, 6));
        fruits.add(new Banana(6, 13));
        fruits.add(new Banana(12, 13));
        fruits.add(new Banana(9, 9));
        fruits.add(new Banana(5, 16));
        fruits.add(new Banana(10, 16));
        fruits.add(new Banana(18, 10));
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Inicializa el nivel 2: 1 maceta, 8 piñas y 8 plátanos
     * La maceta persigue al jugador y las piñas se mueven constantemente
     */
    private void initializeLevel2() {
        player = new IceCream(1, 1, playerFlavor);
        
        // Crear una maceta que persigue al jugador
        enemies = new ArrayList<>();
        Pot pot = new Pot(18, 18);
        pot.setTarget(player); // Configurar al jugador como objetivo
        enemies.add(pot);
        
        fruits = new ArrayList<>();
        
        // 8 piñas que se mueven
        fruits.add(new Pineapple(5, 5));
        fruits.add(new Pineapple(10, 5));
        fruits.add(new Pineapple(15, 5));
        fruits.add(new Pineapple(5, 10));
        fruits.add(new Pineapple(15, 10));
        fruits.add(new Pineapple(5, 15));
        fruits.add(new Pineapple(10, 15));
        fruits.add(new Pineapple(15, 15));
        
        // 8 plátanos estáticos
        fruits.add(new Banana(3, 3));
        fruits.add(new Banana(8, 3));
        fruits.add(new Banana(13, 3));
        fruits.add(new Banana(3, 12));
        fruits.add(new Banana(8, 12));
        fruits.add(new Banana(13, 12));
        fruits.add(new Banana(3, 17));
        fruits.add(new Banana(13, 17));
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Inicializa el nivel 3: 1 calamar naranja, 8 piñas y 8 cerezas
     * El calamar rompe bloques y las cerezas se teletransportan
     */
    private void initializeLevel3() {
        player = new IceCream(1, 1, playerFlavor);
        
        // Crear calamar naranja que rompe bloques
        enemies = new ArrayList<>();
        OrangeSquid squid = new OrangeSquid(18, 18);
        squid.setTarget(player);
        squid.setBoard(board);
        enemies.add(squid);
        
        fruits = new ArrayList<>();
        
        // 8 piñas móviles
        fruits.add(new Pineapple(4, 4));
        fruits.add(new Pineapple(9, 4));
        fruits.add(new Pineapple(14, 4));
        fruits.add(new Pineapple(4, 9));
        fruits.add(new Pineapple(14, 9));
        fruits.add(new Pineapple(4, 14));
        fruits.add(new Pineapple(9, 14));
        fruits.add(new Pineapple(14, 14));
        
        // 8 cerezas que se teletransportan
        fruits.add(new Cherry(6, 6));
        fruits.add(new Cherry(11, 6));
        fruits.add(new Cherry(16, 6));
        fruits.add(new Cherry(6, 11));
        fruits.add(new Cherry(11, 11));
        fruits.add(new Cherry(16, 11));
        fruits.add(new Cherry(6, 16));
        fruits.add(new Cherry(16, 16));
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Mueve al jugador en la dirección especificada
     * También actualiza la dirección en la que mira el helado
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
        
        // Verificar si puede moverse a la nueva posición
        if (board.isValidPosition(newX, newY) && 
            !board.hasWall(newX, newY) && 
            !board.hasIceBlock(newX, newY)) {
            player.move(newX, newY);
            checkFruitCollection();
            checkEnemyCollision();
        }
    }
    
    /**
     * Maneja la creación o destrucción de bloques de hielo
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
        
        // Calcular posición objetivo según la dirección
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
        else if (board.isValidPosition(targetX, targetY) && 
                 !board.hasWall(targetX, targetY)) {
            board.createIceBlock(targetX, targetY);
        }
    }
    
    /**
     * Verifica si el jugador recogió alguna fruta en su posición actual
     */
    private void checkFruitCollection() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        for (Fruit fruit : fruits) {
            if (!fruit.isCollected() && 
                fruit.getPosition().getX() == x && 
                fruit.getPosition().getY() == y) {
                fruit.collect();
                player.collectFruit();
                
                // Verificar si ganó el nivel
                if (player.getFruitsCollected() >= totalFruits) {
                    gameWon = true;
                }
                break;
            }
        }
    }
    
    /**
     * Verifica si el jugador colisionó con algún enemigo
     */
    private void checkEnemyCollision() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().getX() == x && 
                enemy.getPosition().getY() == y) {
                player.die();
                gameLost = true;
                break;
            }
        }
    }
    
    /**
     * Actualiza el estado del juego: enemigos, frutas móviles, tiempo
     * Este método debe llamarse periódicamente (cada medio segundo aprox)
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
        
        // Mover frutas especiales según el nivel
        for (Fruit fruit : fruits) {
            if (fruit instanceof Pineapple) {
                ((Pineapple) fruit).move(board);
            } else if (fruit instanceof Cherry) {
                ((Cherry) fruit).teleport(board);
            }
        }
        
        // Verificar colisión después del movimiento
        checkEnemyCollision();
        
        // Limpiar bloques destruidos para optimizar memoria
        board.cleanDestroyedBlocks();
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
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Reinicia el nivel actual
     */
    public void restart() {
        board.clearAllIceBlocks();
        initializeLevel(currentLevel);
    }
}