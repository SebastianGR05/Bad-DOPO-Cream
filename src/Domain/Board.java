package Domain;

import java.util.ArrayList;

/**
 * Represents the game board.
 * It is a 16x16 grid and contains the level layout, manages obstacles.
 */
public class Board {
    private int width;
    private int height;
    private int[][] grid;
    private ArrayList<IceBlock> iceBlocks;
    private ArrayList<Campfire> campfires;
    private ArrayList<HotTile> hotTiles;
    private int currentLevel;
    
    /**
     * Creates a new board with default dimensions and loads the specified level.
     * @param width the defined width
     * @param height the defined height
     * @param level the level number to load: 1, 2 or 3
     */
    public Board(int width, int height, int level) {
        this.width = 16;
        this.height = 16;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        this.campfires = new ArrayList<>();
        this.hotTiles = new ArrayList<>();
        this.currentLevel = level;
        
        switch(level) {
            case 1:
                createLevel1();
                break;
            case 2:
                createLevel2();
                break;
            case 3:
                createLevel3();
                break;
            default:
                createLevel1();
        }
    }
    
    /**
     * Creates the layout for level 1.
     * The level matrix uses numbers to represent different elements:
     * 0 = empty space, 1 = wall, 2 = ice block, 3 = player spawn,
     * 4 = enemy, 5 = banana, 6 = grape
     */
    private void createLevel1() {
        int[][] level1Matrix = {
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
        
        loadMatrixToGrid(level1Matrix);
    }
    
    /**
     * Creates the layout for level 2.
     * The level matrix uses numbers to represent different elements:
     * 0 = empty space, 1 = wall, 2 = ice block, 3 = player spawn,
     * 4 = enemy, 5 = banana, 7 = pineapple, 9 = hot tile
     */
    private void createLevel2() {
        int[][] level2Matrix = {
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
        
        loadMatrixToGrid(level2Matrix);
    }
    
    /**
     * Creates the layout for level 3.
     * The level matrix uses numbers to represent different elements:
     * 0 = empty space, 1 = wall, 2 = ice block, 3 = player spawn,
     * 4 = enemy, 7 = pineapple, 8 = cherry, 10 = campfire
     */
    private void createLevel3() {
        int[][] level3Matrix = {
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
        
        loadMatrixToGrid(level3Matrix);
    }
    
    /**
     * Loads a level matrix into the game board grid and creates obstacles.
     * Matrix value meanings:
     * 1 = wall
     * 2 = ice block 
     * 9 = hot tile
     * 10 = campfire
     * Other values = empty space
     * @param matrix the array representing the level layout
     */
    private void loadMatrixToGrid(int[][] matrix) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = matrix[i][j];
                
                if (value == 1) {
                    grid[i][j] = 1;
                } else if (value == 2) {
                    grid[i][j] = 0; 
                    IceBlock block = new IceBlock(j, i);
                    iceBlocks.add(block);
                } else if (value == 9) {
                    grid[i][j] = 0;
                    HotTile tile = new HotTile(j, i);
                    hotTiles.add(tile);
                } else if (value == 10) {
                    grid[i][j] = 0;
                    Campfire fire = new Campfire(j, i);
                    campfires.add(fire);
                } else {
                    grid[i][j] = 0;
                }
            }
        }
    }
    
    /**
     * Gets the width of the board.
     * @return the width 
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the board.
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the current level number.
     * @return the level number: 1, 2 or 3
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Gets the type of cell at a specific position.
     * @param x the horizontal position
     * @param y the vertical position
     * @return 1 if wall or an invalid position, 0 if empty
     */
    public int getCellType(int x, int y) {
        if (!isValidPosition(x, y)) {
            return 1;
        }
        return grid[y][x];
    }
    
    /**
     * Checks if a position is within the board boundaries.
     * @param x the horizontal position to check
     * @param y the vertical position to check
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Checks if there's a wall at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     * @return true if there's a wall, false otherwise
     */
    public boolean hasWall(int x, int y) {
        return getCellType(x, y) == 1;
    }
    
    /**
     * Checks if there's an ice block at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     * @return true if there's an ice block, false otherwise
     */
    public boolean hasIceBlock(int x, int y) {
        for (IceBlock block : iceBlocks) {
            if (block.exists() && block.getPosition().getX() == x 
                && block.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if there's a campfire on, at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     * @return true if there's a campfire on, false otherwise
     */
    public boolean hasCampfireOn(int x, int y) {
        for (Campfire fire : campfires) {
            if (fire.exists() && fire.isOn() &&
                fire.getPosition().getX() == x && 
                fire.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if there's a hot tile at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     * @return true if there's a hot tile, false otherwise
     */
    public boolean hasHotTile(int x, int y) {
        for (HotTile tile : hotTiles) {
            if (tile.exists() &&
                tile.getPosition().getX() == x && 
                tile.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates a line of ice blocks in the specified direction.
     * The line continues until it hits a wall or an existing ice block.
     * @param startX the horizontal starting position
     * @param startY the vertical starting position
     * @param direction the direction to create blocks: "UP", "DOWN", "LEFT", "RIGHT"
     */
    public void createIceBlocks(int startX, int startY, String direction) {
        if (!isValidPosition(startX, startY)) {
            return;
        }
        
        int dx = 0; 
        int dy = 0;
        
        switch(direction) {
            case "UP": 
                dy = -1; 
                break;
            case "DOWN": 
                dy = 1; 
                break;
            case "LEFT": 
                dx = -1; 
                break;
            case "RIGHT": 
                dx = 1; 
                break;
        }
        
        int x = startX;
        int y = startY;
        
        // Create blocks until finding an obstacle
        while (isValidPosition(x, y) && !hasWall(x, y)) {
            // If there's already a block, stop creating
            if (hasIceBlock(x, y)) {
                break;
            }
            
            // Create the block
            IceBlock newBlock = new IceBlock(x, y);
            iceBlocks.add(newBlock);
            
            // If there's a hot tile, melt immediately
            if (hasHotTile(x, y)) {
                newBlock.destroy();
            }
            
            // If there's a lit campfire, extinguish it
            for (Campfire fire : campfires) {
                if (fire.exists() && fire.isOn() &&
                    fire.getPosition().getX() == x && 
                    fire.getPosition().getY() == y) {
                    fire.extinguish();
                    break;
                }
            }
            
            // Advance to next position
            x += dx;
            y += dy;
        }
    }
    
    /**
     * Destroys ice blocks in a line in the specified direction.
     * The destruction continues in a domino effect until no more ice blocks
     * are found in that direction.
     * @param startX the horizontal starting position
     * @param startY the vertical starting position
     * @param direction the direction to destroy blocks: "UP", "DOWN", "LEFT", "RIGHT"
     */
    public void destroyIceBlocks(int startX, int startY, String direction) {
        if (!isValidPosition(startX, startY)) {
            return;
        }
        
        int dx = 0; 
        int dy = 0;
        
        switch(direction) {
            case "UP": 
                dy = -1; 
                break;
            case "DOWN": 
                dy = 1; 
                break;
            case "LEFT": 
                dx = -1; 
                break;
            case "RIGHT": 
                dx = 1; 
                break;
        }
        
        int x = startX + dx;
        int y = startY + dy;
        
        while (isValidPosition(x, y) && !hasWall(x, y)) {
            boolean blockFound = false;
            
            for (IceBlock block : iceBlocks) {
                if (block.exists() && block.getPosition().getX() == x 
                    && block.getPosition().getY() == y) {
                    block.destroy();
                    
                    // Reignite campfire if there was one underneath
                    for (Campfire fire : campfires) {
                        if (fire.exists() && !fire.isOn() &&
                            fire.getPosition().getX() == x && 
                            fire.getPosition().getY() == y) {
                            fire.extinguish();
                            break;
                        }
                    }
                    
                    blockFound = true;
                    break;
                }
            }
            
            if (!blockFound) {
                break;
            }
            
            x += dx;
            y += dy;
        }
    }
    
    /**
     * Updates all obstacles on the board.
     */
    public void updateObstacles() {
        for (Campfire fire : campfires) {
            if (fire.exists()) {
                fire.update();
            }
        }
        for (HotTile tile : hotTiles) {
            if (tile.exists()) {
                tile.meltIceBlockIfPresent(this);
            }
        }
    }
    
    /**
     * Removes destroyed ice blocks from the ice blocks list.
     */
    public void cleanDestroyedBlocks() {
        iceBlocks.removeIf(block -> !block.exists());
    }
    
    /**
     * Gets the list of all ice blocks on the board.
     * @return the ArrayList of ice blocks
     */
    public ArrayList<IceBlock> getIceBlocks() {
        return iceBlocks;
    }
    
    /**
     * Gets the list of all campfires on the board.
     * @return the ArrayList of campfires
     */
    public ArrayList<Campfire> getCampfires() {
        return campfires;
    }
    
    /**
     * Gets the list of all hot tiles on the board.
     * @return the ArrayList of hot tiles
     */
    public ArrayList<HotTile> getHotTiles() {
        return hotTiles;
    }
    
    /**
     * Removes all ice blocks from the board.
     */
    public void clearAllIceBlocks() {
        iceBlocks.clear();
    }
    
    /**
     * Adds a new campfire at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     */
    public void addCampfire(int x, int y) {
        campfires.add(new Campfire(x, y));
    }
    
    /**
     * Removes all campfires from the board.
     */
    public void clearAllCampfires() {
        campfires.clear();
    }
    
    /**
     * Adds a new hot tile at the specified position.
     * @param x the horizontal position
     * @param y the vertical position
     */
    public void addHotTile(int x, int y) {
        hotTiles.add(new HotTile(x, y));
    }
    
    /**
     * Removes all hot tiles from the board.
     */
    public void clearAllHotTiles() {
        hotTiles.clear();
    }
}