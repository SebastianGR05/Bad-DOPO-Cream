package Domain;

import java.util.ArrayList;

/**
 * Main game controller that manages all game logic and state.
 * This class handles player movement, fruit collection, enemy behavior,
 * win/loss conditions, time limits, and level progression.
 * It coordinates all game elements (board, player, enemies, fruits) and
 * makes sure they interact correctly according to the game rules.
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
    private final long TIME_LIMIT = 180000; // 3 minutes in milliseconds
    private boolean paused;
    private int currentLevel;
    private String playerFlavor;
    private long pausedTime;
    private long lastPauseStart;
    
    /**
     * Creates a new game instance for the specified level and player flavor.
     * @param level the level number to play: 1, 2 or 3
     * @param flavor the ice cream flavor for the player: "VANILLA", "STRAWBERRY" or "CHOCOLATE"
     */
    public Game(int level, String flavor) {
        this.currentLevel = level;
        this.playerFlavor = flavor;
        this.pausedTime = 0;
        this.lastPauseStart = 0;
        board = new Board(16, 16, level);
        initializeLevel(level);
        this.paused = false;
    }
    
    /**
     * Initializes all game elements for a specific level.
     * @param level the level number to initialize
     */
    private void initializeLevel(int level) {
        enemies = new ArrayList<>();
        fruits = new ArrayList<>();
        totalScore = 0;
        
        int[][] levelMatrix = getLevelMatrix(level);
        
        // Create player first
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
        
        // Create enemies and fruits
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int value = levelMatrix[y][x];
                Fruit newFruit = null;
                
                switch(value) {
                    case 3: // Player already created
                        break;
                    case 4: // Enemy
                        createEnemy(x, y, level);
                        break;
                    case 5: // Banana
                        newFruit = new Banana(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 6: // Grapes
                        newFruit = new Grape(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 7: // Pineapple
                        newFruit = new Pineapple(x, y);
                        fruits.add(newFruit);
                        totalScore += newFruit.getPoints();
                        break;
                    case 8: // Cherry
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
    
    /**
     * Creates an enemy for the level at the specified position.
     * @param x the horizontal spawn position
     * @param y the vertical spawn position
     * @param level the level number determining enemy type
     */
    private void createEnemy(int x, int y, int level) {
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
     * Gets the level matrix for the specified level number.
     * @param level the level number (1, 2, or 3)
     * @return a 2D array representing the level layout
     */
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
                return new int[][] {
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
    
    /**
     * Moves the player in the specified direction if possible.
     * @param direction the direction to move: "UP", "DOWN", "LEFT", "RIGHT"
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
            checkObstacleCollision();
        }
    }
    
    /**
     * Handles ice block creation or destruction based on the player's facing direction.
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
            board.createIceBlocks(targetX, targetY, direction);
        }
    }
    
    /**
     * Checks if the player is standing on a fruit to collect it.
     */
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
    
    /**
     * Checks if the player is touching an enemy.
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
     * Checks if the player is touching a lethal obstacle.
     */
    private void checkObstacleCollision() {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        if (board.hasCampfireOn(x, y)) {
            player.die();
            gameLost = true;
        }
    }
    
    /**
     * Updates all dynamic game elements.
     */
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
    
    /**
     * Toggles the pause state of the game.
     */
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
    
    /**
     * Checks if the game is currently paused.
     * @return true if paused, false if running
     */
    public boolean isPaused() {
        return paused;
    }
    
    /**
     * Gets the game board.
     * @return the Board object
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Gets the player's ice cream character.
     * @return the IceCream object representing the player
     */
    public IceCream getPlayer() {
        return player;
    }
    
    /**
     * Gets the array of all the enemies in the game.
     * @return ArrayList of Enemy objects
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    
    /**
     * Gets the array of all the fruits in the game.
     * @return ArrayList of Fruit objects
     */
    public ArrayList<Fruit> getFruits() {
        return fruits;
    }
    
    /**
     * Checks if the game has been won.
     * @return true if game is won, false otherwise
     */
    public boolean isGameWon() {
        return gameWon;
    }
    
    /**
     * Checks if the game has been lost.
     * @return true if game is lost, false otherwise
     */
    public boolean isGameLost() {
        return gameLost;
    }
    
    /**
     * Gets the time remaining in the level in milliseconds.
     * @return time remaining in milliseconds (minimum 0)
     */
    public long getTimeRemaining() {
        long elapsed = System.currentTimeMillis() - startTime - pausedTime;
        if (paused) {
            long currentPauseDuration = System.currentTimeMillis() - lastPauseStart;
            elapsed -= currentPauseDuration;
        }
        long remaining = TIME_LIMIT - elapsed;
        return Math.max(0, remaining);
    }
    
    /**
     * Gets the total number of fruits in the level.
     * @return the total fruit count
     */
    public int getTotalFruits() {
        return totalFruits;
    }
    
    /**
     * Gets the total score for the level.
     * @return the total possible score
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Gets the current level number.
     * @return the level number: 1, 2 or 3
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Restarts the current level.
     */
    public void restart() {
        enemies.clear();
        fruits.clear();
        player = null;
        board = new Board(16, 16, currentLevel);
        initializeLevel(currentLevel);
    }
}