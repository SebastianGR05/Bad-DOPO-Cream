package Domain;

import java.util.ArrayList;

/**
 * Representa el tablero del juego con soporte para diferentes niveles
 * Usa las matrices predefinidas de los niveles
 */
public class Board {
    private int width;
    private int height;
    private int[][] grid; // 0=vacío, 1=pared, 2=bloque hielo
    private ArrayList<IceBlock> iceBlocks;
    private int currentLevel;
    
    public Board(int width, int height) {
        // Usar tamaño fijo de 16x16 para todos los niveles
        this.width = 16;
        this.height = 16;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        this.currentLevel = 1;
        createLevel1();
    }
    
    /**
     * Constructor alternativo que permite especificar el nivel
     */
    public Board(int width, int height, int level) {
        this.width = 16;
        this.height = 16;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
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
     * Crea el diseño del nivel 1 según la matriz predefinida
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
     * Crea el diseño del nivel 2 según la matriz predefinida
     */
    private void createLevel2() {
        int[][] level2Matrix = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,7,0,0,0,0,0,0,0,0,7,0,0,1},
            {1,0,2,0,2,0,2,0,0,2,0,2,0,2,0,1},
            {1,7,0,0,0,0,0,0,0,0,0,0,4,0,7,1},
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
        
        loadMatrixToGrid(level2Matrix);
    }
    
    /**
     * Crea el diseño del nivel 3 según la matriz predefinida
     */
    private void createLevel3() {
        int[][] level3Matrix = {
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
        
        loadMatrixToGrid(level3Matrix);
    }
    
    /**
     * Carga una matriz en el grid del tablero y crea bloques de hielo iniciales
     */
    private void loadMatrixToGrid(int[][] matrix) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int value = matrix[i][j];
                
                // 0=empty, 1=wall, 2=ice block, 3=player, 4=enemy, 5-8=fruits
                if (value == 1) {
                    grid[i][j] = 1; // Pared
                } else if (value == 2) {
                    grid[i][j] = 0; // El espacio es vacío
                    // Crear bloque de hielo en esta posición
                    IceBlock block = new IceBlock(j, i);
                    iceBlocks.add(block);
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
            return 1; // Fuera del tablero = pared
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
     * Crea un bloque de hielo en la posición especificada
     */
    public void createIceBlock(int x, int y) {
        if (isValidPosition(x, y) && !hasWall(x, y) && !hasIceBlock(x, y)) {
            IceBlock newBlock = new IceBlock(x, y);
            iceBlocks.add(newBlock);
        }
    }
    
    /**
     * Destruye bloques de hielo en efecto dominó en una dirección
     */
    public void destroyIceBlocks(int startX, int startY, String direction) {
        int dx = 0, dy = 0;
        
        switch(direction) {
            case "UP": dy = -1; break;
            case "DOWN": dy = 1; break;
            case "LEFT": dx = -1; break;
            case "RIGHT": dx = 1; break;
        }
        
        int x = startX + dx;
        int y = startY + dy;
        
        // Destruir bloques consecutivos en la dirección hasta encontrar vacío o pared
        while (isValidPosition(x, y) && !hasWall(x, y)) {
            boolean blockFound = false;
            
            for (IceBlock block : iceBlocks) {
                if (block.exists() && block.getPosition().getX() == x 
                    && block.getPosition().getY() == y) {
                    block.destroy();
                    blockFound = true;
                    break;
                }
            }
            
            // Si no encontramos bloque, detener la destrucción
            if (!blockFound) {
                break;
            }
            
            x += dx;
            y += dy;
        }
    }
    
    /**
     * Limpia los bloques destruidos de la lista
     */
    public void cleanDestroyedBlocks() {
        iceBlocks.removeIf(block -> !block.exists());
    }
    
    public ArrayList<IceBlock> getIceBlocks() {
        return iceBlocks;
    }
    
    /**
     * Reinicia todos los bloques de hielo del tablero
     */
    public void clearAllIceBlocks() {
        iceBlocks.clear();
    }
}