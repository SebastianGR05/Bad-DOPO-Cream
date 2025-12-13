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
    private int totalScore;
    private boolean gameWon;
    private boolean gameLost;
    private long startTime;
    private final long TIME_LIMIT = 180000; // 3 minutos
    private boolean paused;
    private int currentLevel;
    private String playerFlavor;
    private long pausedTime;
    private long lastPauseStart;
    
    public Game(int level, String flavor) {
        this.currentLevel = level;
        this.playerFlavor = flavor;
        this.pausedTime = 0;
        this.lastPauseStart = 0;
        board = new Board(16, 16, level);
        initializeLevel(level);
        this.paused = false;
    }
    
    private void initializeLevel(int level) {
        enemies = new ArrayList<>();
        fruits = new ArrayList<>();
        totalScore = 0;
        
        int[][] levelMatrix = getLevelMatrix(level);
        
        // Crear jugador primero
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int value = levelMatrix[y][x];
                if (value == 3) {
                    player = new IceCream(x, y, playerFlavor);
                    break;
                }
            }
            if (player != null) { 
                break;
            }
        }
        
        // Crear enemigos y frutas
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int value = levelMatrix[y][x];
                Fruit newFruit = null;
                
                switch(value) {
                    case 3: // Jugador ya creado
                        break;
                    case 4: // Enemigo
                        createEnemy(x, y, level);
                        break;
                    case 5: // Banana
                        newFruit = new Banana(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 6: // Uvas
                        newFruit = new Grape(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 7: // Piña
                        newFruit = new Pineapple(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 8: // Cereza //e
                        newFruit = new Cherry(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                }
            }
        }
        
        totalFruits = fruits.size();
        gameWon = false;
        gameLost = false;
        startTime = System.currentTimeMillis();
        pausedTime = 0;
    }
    
    private void createEnemy(int x, int y, int level) { //e
        switch(level) {
            case 1:
                enemies.add(new Troll(x, y));
                break;
            case 2:
                Pot pot = new Pot(x, y);
                pot.setTarget(player);
                enemies.add(pot);
                break;
            case 3: //e
                OrangeSquid squid = new OrangeSquid(x, y);
                squid.setTarget(player);
                squid.setBoard(board);
                enemies.add(squid);
                break;
        }
    }
    
    private int[][] getLevelMatrix(int level) {
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
                    {1,0,2,0,9,0,5,2,2,5,0,9,0,2,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,2,0,2,0,1,1,1,1,0,2,0,2,0,1},
                    {1,0,2,0,5,0,1,1,1,1,0,5,0,2,0,1},
                    {1,0,2,0,5,0,1,1,1,1,0,5,0,2,0,1},
                    {1,0,2,0,2,0,1,1,1,1,0,2,0,2,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,2,0,9,0,5,2,2,5,0,9,0,2,0,1},
                    {1,7,0,3,0,0,0,0,0,0,0,0,4,0,7,1},
                    {1,0,2,0,2,0,2,0,0,2,0,2,0,2,0,1},
                    {1,0,0,7,0,0,0,0,0,0,0,0,7,0,0,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
                };
            case 3:
                return new int[][] { //e
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,8,0,0,7,0,0,0,0,0,0,7,0,0,8,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,7,0,0,10,0,0,0,0,0,0,10,0,0,7,1},
                    {1,0,2,2,0,8,0,4,0,0,8,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,0,1,1,1,1,0,0,2,2,0,1},
                    {1,0,2,2,0,8,0,0,3,0,8,0,2,2,0,1},
                    {1,7,0,0,10,0,0,0,0,0,0,10,0,0,7,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,0,2,2,0,2,2,2,2,2,2,0,2,2,0,1},
                    {1,8,0,0,7,0,0,0,0,0,0,7,0,0,8,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
                };
            default:
                return getLevelMatrix(1);
        }
    }
    
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
            checkObstacleCollision();
        }
    }
    
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
            case "UP": 
            	targetY--; 
            	break;
            case "DOWN": 
            	targetY++; 
            	break;
            case "LEFT": 
            	targetX--; 
            	break;
            case "RIGHT": 
            	targetX++; 
            	break;
        }
        
        if (board.hasIceBlock(targetX, targetY)) {
            board.destroyIceBlocks(x, y, direction);
        } else if (board.isValidPosition(targetX, targetY) && 
                   !board.hasWall(targetX, targetY)) {
            board.createIceBlocks(targetX, targetY,direction);
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
                player.collectFruit(fruit.getPoints());
                
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
    
    private void checkObstacleCollision() {//e
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        if (board.hasCampfireOn(x, y)) {
            player.die();
            gameLost = true;
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
        checkObstacleCollision();
        board.updateObstacles();
        board.cleanDestroyedBlocks();
    }
    
    public void togglePause() {
        if (paused) {
            long pauseDuration = System.currentTimeMillis() - lastPauseStart;
            pausedTime += pauseDuration;
            paused = false;
        } else {
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
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void restart() {
        enemies.clear();
        fruits.clear();
        player = null;
        board = new Board(16, 16, currentLevel);
        initializeLevel(currentLevel);
    }
}