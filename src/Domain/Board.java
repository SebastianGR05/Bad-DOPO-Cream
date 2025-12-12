package Domain;

import java.util.ArrayList;

/**
 * Representa el tablero del juego
 */
public class Board {
    private int width;
    private int height;
    private int[][] grid;
    private ArrayList<IceBlock> iceBlocks;
    private ArrayList<Campfire> campfires;
    private ArrayList<HotTile> hotTiles;
    private int currentLevel;
    
    public Board(int width, int height) throws BadDopoCreamException {
        this.width = 16;
        this.height = 16;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        this.campfires = new ArrayList<>();
        this.hotTiles = new ArrayList<>();
        this.currentLevel = 1;
        createLevel1();
    }
    
    public Board(int width, int height, int level) throws BadDopoCreamException {
        if (level < 1 || level > 3) {
            throw new BadDopoCreamException(BadDopoCreamException.INVALID_LEVEL + ": " + level);
        }
        
        this.width = 16;
        this.height = 16;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        this.campfires = new ArrayList<>();
        this.hotTiles = new ArrayList<>();
        this.currentLevel = level;
        
        try {
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
                    throw new BadDopoCreamException(BadDopoCreamException.INVALID_LEVEL);
            }
        } catch (Exception e) {
            throw new BadDopoCreamException(BadDopoCreamException.LEVEL_NOT_LOADED + " (Nivel " + level + ")", e);
        }
    }
    
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
     * Carga una matriz en el grid y crea obstáculos según los códigos
     */
    private void loadMatrixToGrid(int[][] matrix) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = matrix[i][j];
                
                if (value == 1) {
                    grid[i][j] = 1; // Pared
                } else if (value == 2) {
                    grid[i][j] = 0; // Bloque de hielo
                    IceBlock block = new IceBlock(j, i);
                    iceBlocks.add(block);
                } else if (value == 9) {
                    grid[i][j] = 0; // Espacio vacío
                    HotTile tile = new HotTile(j, i);
                    hotTiles.add(tile);
                } else if (value == 10) {
                    grid[i][j] = 0; // Espacio vacío
                    Campfire fire = new Campfire(j, i);
                    campfires.add(fire);
                } else {
                    grid[i][j] = 0; // Vacío
                }
            }
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public int getCellType(int x, int y) {
        if (!isValidPosition(x, y)) {
            return 1;
        }
        return grid[y][x];
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public boolean hasWall(int x, int y) {
        return getCellType(x, y) == 1;
    }
    
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
     * Verifica si hay una fogata encendida en la posición
     */
    public boolean hasLitCampfire(int x, int y) {
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
     * Verifica si hay una baldosa caliente en la posición
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
    
    public void createIceBlock(int x, int y) throws BadDopoCreamException {
        if (!isValidPosition(x, y)) {
            throw new BadDopoCreamException(BadDopoCreamException.INVALID_POSITION + ": (" + x + "," + y + ")");
        }
        
        if (hasWall(x, y)) {
            throw new BadDopoCreamException(BadDopoCreamException.CANNOT_CREATE_ICE + ": hay una pared");
        }
        
        if (hasIceBlock(x, y)) {
            throw new BadDopoCreamException(BadDopoCreamException.ICE_BLOCK_EXISTS);
        }
        
        IceBlock newBlock = new IceBlock(x, y);
        iceBlocks.add(newBlock);
        
        // Si hay una baldosa caliente, derretir el bloque inmediatamente
        if (hasHotTile(x, y)) {
            newBlock.destroy();
        }
        
        // Si hay una fogata, apagarla
        for (Campfire fire : campfires) {
            if (fire.exists() && fire.isOn() &&
                fire.getPosition().getX() == x && 
                fire.getPosition().getY() == y) {
                fire.extinguish();
                break;
            }
        }
    }
    
    public void destroyIceBlocks(int startX, int startY, String direction) throws BadDopoCreamException {
        if (!isValidPosition(startX, startY)) {
            throw new BadDopoCreamException(BadDopoCreamException.INVALID_POSITION);
        }
        
        int dx = 0, dy = 0;
        
        switch(direction) {
            case "UP": dy = -1; break;
            case "DOWN": dy = 1; break;
            case "LEFT": dx = -1; break;
            case "RIGHT": dx = 1; break;
            default:
                throw new BadDopoCreamException(BadDopoCreamException.INVALID_MOVE + ": dirección desconocida");
        }
        
        int x = startX + dx;
        int y = startY + dy;
        
        while (isValidPosition(x, y) && !hasWall(x, y)) {
            boolean blockFound = false;
            
            for (IceBlock block : iceBlocks) {
                if (block.exists() && block.getPosition().getX() == x 
                    && block.getPosition().getY() == y) {
                    block.destroy();
                    
                    // Reencender fogata si había una debajo
                    for (Campfire fire : campfires) {
                        if (fire.exists() && !fire.isOn() &&
                            fire.getPosition().getX() == x && 
                            fire.getPosition().getY() == y) {
                            fire.extinguish(); // Reinicia el temporizador
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
     * Actualiza todos los obstáculos (fogatas se reencienden)
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
    
    public void cleanDestroyedBlocks() {
        iceBlocks.removeIf(block -> !block.exists());
    }
    
    public ArrayList<IceBlock> getIceBlocks() {
        return iceBlocks;
    }
    
    public ArrayList<Campfire> getCampfires() {
        return campfires;
    }
    
    public ArrayList<HotTile> getHotTiles() {
        return hotTiles;
    }
    
    public void clearAllIceBlocks() {
        iceBlocks.clear();
    }
    
    /**
     * Agrega una fogata al tablero
     */
    public void addCampfire(int x, int y) {
        campfires.add(new Campfire(x, y));
    }
    
    public void clearAllCampfires() {
        campfires.clear();
    }
    
    /**
     * Agrega una baldosa caliente al tablero
     */
    public void addHotTile(int x, int y) {
        hotTiles.add(new HotTile(x, y));
    }
    
    public void clearAllHotTiles() {
        hotTiles.clear();
    }
}