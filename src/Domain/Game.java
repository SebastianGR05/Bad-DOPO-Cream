package Domain;

import java.util.ArrayList;

/**
 * Clase principal que maneja toda la lógica del juego para los tres niveles
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
    private final long TIME_LIMIT = 180000; // 3 minutos
    private boolean paused;
    private int currentLevel;
    private String playerFlavor;
    private long pausedTime;      // Tiempo total pausado
    private long lastPauseStart;  // Momento en que se pausó
    
    public Game(int level, String flavor) throws BadDopoCreamException {
        // Validar nivel
        if (level < 1 || level > 3) {
            throw new BadDopoCreamException(BadDopoCreamException.INVALID_LEVEL + ": " + level);
        }
        
        // Validar sabor
        if (flavor == null || (!flavor.equals("VANILLA") && !flavor.equals("STRAWBERRY") && !flavor.equals("CHOCOLATE"))) {
            throw new BadDopoCreamException(BadDopoCreamException.INVALID_FLAVOR + ": " + flavor);
        }
        
        this.currentLevel = level;
        this.playerFlavor = flavor;
        this.pausedTime = 0;
        this.lastPauseStart = 0;
        board = new Board(16, 16, level);
        initializeLevel(level);
        this.paused = false;
    }
    
    /**
     * Inicializa el nivel usando la matriz correspondiente
     * @throws BadDopoCreamException si hay error al inicializar
     */
    private void initializeLevel(int level) throws BadDopoCreamException {
        enemies = new ArrayList<>();
        fruits = new ArrayList<>();
        
        // Obtener la matriz del nivel
        int[][] levelMatrix = getLevelMatrix(level);
        
        // Paso 1: Buscar y crear al jugador primero
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int value = levelMatrix[y][x];
                if (value == 3) { // Jugador
                    player = new IceCream(x, y, playerFlavor);
                    break;
                }
            }
            if (player != null) { 
            	break;
            }
        }
        
        // Paso 2: Crear enemigos y frutas (ahora el jugador ya existe)
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int value = levelMatrix[y][x];
                switch(value) {
                    case 3: // Jugador ya creado, saltar
                        break;
                    case 4: // Enemigo
                        createEnemy(x, y, level);
                        break;
                    case 5: // Banana
                        fruits.add(new Banana(x, y));
                        break;
                    case 6: // Uvas
                        fruits.add(new Grape(x, y));
                        break;
                    case 7: // Piña
                        fruits.add(new Pineapple(x, y));
                        break;
                    case 8: // Cereza
                        fruits.add(new Cherry(x, y));
                        break;
                }
            }
        }
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
        pausedTime = 0;  // INICIALIZAR tiempo pausado
    }
    
    /**
     * Crea el enemigo correspondiente al nivel
     * @throws BadDopoCreamException si hay error al crear enemigo
     */
    private void createEnemy(int x, int y, int level) throws BadDopoCreamException {
        switch(level) {
            case 1:
                enemies.add(new Troll(x, y));
                break;
            case 2:
                Pot pot = new Pot(x, y);
                pot.setTarget(player);
                enemies.add(pot);
                break;
            case 3:
                OrangeSquid squid = new OrangeSquid(x, y);
                squid.setTarget(player);
                squid.setBoard(board);
                enemies.add(squid);
                break;
        }
    }
    
    /**
     * Obtiene la matriz del nivel
     * @throws BadDopoCreamException si el nivel es inválido
     */
    private int[][] getLevelMatrix(int level) throws BadDopoCreamException {
        switch(level) {
            case 1:
                return new int[][] {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,6,0,0,0,0,0,0,0,0,0,0,4,0,6,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,5,0,0,0,0,0,0,0,0,0,0,5,0,1},
                    {1,0,0,2,2,0,0,0,0,0,0,2,2,0,0,1},
                    {1,0,0,2,6,0,0,0,0,0,4,6,2,0,0,1},
                    {1,5,0,2,0,0,1,1,1,1,0,0,2,0,5,1},
                    {1,0,0,2,0,0,1,1,1,1,0,0,2,0,0,1},
                    {1,0,0,2,0,0,1,1,1,1,0,0,2,0,0,1},
                    {1,5,0,2,0,0,1,1,1,1,0,0,2,0,5,1},
                    {1,0,0,2,6,0,0,0,0,0,0,6,2,0,0,1},
                    {1,0,0,2,2,0,0,0,0,0,0,2,2,0,0,1},
                    {1,0,5,0,0,0,0,0,0,0,0,0,0,5,0,1},
                    {1,0,0,0,0,0,0,3,0,0,0,0,0,0,0,1},
                    {1,6,0,0,0,0,0,0,0,0,0,0,0,0,6,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
                };
            case 2:
                return new int[][] {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,0,0,7,0,0,0,0,0,0,0,0,7,0,0,1},
                    {1,0,2,0,2,0,2,0,0,2,0,2,0,2,0,1},
                    {1,7,0,0,0,0,0,0,0,0,0,0,0,0,7,1},
                    {1,0,2,0,0,0,5,2,2,5,0,0,0,2,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,2,0,2,0,1,1,1,1,0,2,0,2,0,1},
                    {1,0,2,0,5,0,1,1,1,1,0,5,0,2,0,1},
                    {1,0,2,0,5,0,1,1,1,1,0,5,0,2,0,1},
                    {1,0,2,0,2,0,1,1,1,1,0,2,0,2,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,2,0,0,0,5,2,2,5,0,0,0,2,0,1},
                    {1,7,0,3,0,0,0,0,0,0,0,0,4,0,7,1},
                    {1,0,2,0,2,0,2,0,0,2,0,2,0,2,0,1},
                    {1,0,0,7,0,0,0,0,0,0,0,0,7,0,0,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
                };
            case 3:
                return new int[][] {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,8,0,0,7,0,0,0,0,0,0,7,0,0,8,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,7,0,0,0,0,0,0,0,0,0,0,0,0,7,1},
                    {1,0,2,2,0,8,0,4,0,0,8,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,8,0,0,3,0,8,0,2,2,0,1},
                    {1,7,0,0,0,0,0,0,0,0,0,0,0,0,7,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,8,0,0,7,0,0,0,0,0,0,7,0,0,8,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
                };
            default:
                throw new BadDopoCreamException(BadDopoCreamException.INVALID_LEVEL + ": " + level);
        }
    }
    
    /**
     * Mueve al jugador en una dirección
     * @throws BadDopoCreamException si el juego ya terminó
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
        
        switch(direction) {
            case "UP": targetY--; break;
            case "DOWN": targetY++; break;
            case "LEFT": targetX--; break;
            case "RIGHT": targetX++; break;
        }
        
        try {
            if (board.hasIceBlock(targetX, targetY)) {
                board.destroyIceBlocks(x, y, direction);
            } else if (board.isValidPosition(targetX, targetY) && 
                       !board.hasWall(targetX, targetY)) {
                board.createIceBlock(targetX, targetY);
            }
        } catch (BadDopoCreamException e) {
            // Silenciosamente ignorar errores de bloques de hielo en el juego
            System.err.println("Error con bloque de hielo: " + e.getMessage());
        }
    }
    
    private void checkFruitCollection() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        for (Fruit fruit : fruits) {
            if (!fruit.isCollected() && 
                fruit.getPosition().getX() == x && 
                fruit.getPosition().getY() == y) {
                fruit.collect();
                player.collectFruit();
                
                if (player.getFruitsCollected() >= totalFruits) {
                    gameWon = true;
                }
                break;
            }
        }
    }
    
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
    
    public void update() {
        if (paused || gameLost || gameWon) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime - pausedTime >= TIME_LIMIT) {
            gameLost = true;
            return;
        }
        
        for (Enemy enemy : enemies) {
            enemy.updatePosition(board);
        }
        
        for (Fruit fruit : fruits) {
            if (fruit instanceof Pineapple) {
                ((Pineapple) fruit).move(board);
            } else if (fruit instanceof Cherry) {
                ((Cherry) fruit).teleport(board);
            }
        }
        
        checkEnemyCollision();
        board.cleanDestroyedBlocks();
    }
    
    public void togglePause() {
        if (paused) {
            // Reanudar: calcular cuánto tiempo estuvo pausado
            long pauseDuration = System.currentTimeMillis() - lastPauseStart;
            pausedTime += pauseDuration;
            paused = false;
        } else {
            // Pausar: guardar el momento actual
            lastPauseStart = System.currentTimeMillis();
            paused = true;
        }
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
        long elapsed = System.currentTimeMillis() - startTime - pausedTime;
        // Si está pausado actualmente, no contar el tiempo desde que se pausó
        if (paused) {
            long currentPauseDuration = System.currentTimeMillis() - lastPauseStart;
            elapsed -= currentPauseDuration;
        }
        long remaining = TIME_LIMIT - elapsed;
        return Math.max(0, remaining);
    }
    
    public int getTotalFruits() {
        return totalFruits;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void restart() {
        try {
        	//Eliminamos jugador, frutas y enemigos
        	enemies.clear();
        	fruits.clear();
        	player = null;
            // Recreamos el tablero
            board = new Board(16, 16, currentLevel);
            // Reinicio de nivel
            initializeLevel(currentLevel);
        } catch (BadDopoCreamException e) {
            System.err.println("Error al reiniciar: " + e.getMessage());
        }
    }
}