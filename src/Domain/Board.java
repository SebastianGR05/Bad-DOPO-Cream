package Domain;

import java.util.ArrayList;

/**
 * Representa el tablero del juego con soporte para diferentes niveles
 */
public class Board {
    private int width;
    private int height;
    private int[][] grid; // 0=vacío, 1=pared, 2=bloque hielo
    private ArrayList<IceBlock> iceBlocks;
    private int currentLevel;
    
    public Board(int width, int height) {
        // Usar tamaño fijo de 20x20 para todos los niveles
        // Los parámetros se mantienen por compatibilidad pero no se usan
        this.width = 20;
        this.height = 20;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        this.currentLevel = 1;
        createLevel1();
    }
    
    /**
     * Constructor alternativo que permite especificar el nivel
     */
    public Board(int width, int height, int level) {
        this.width = 20;
        this.height = 20;
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
     * Crea el diseño del nivel 1 - Laberinto básico
     */
    private void createLevel1() {
        // Primero llenar todo de vacío
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = 0;
            }
        }
        
        // Crear paredes del borde exterior
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height-1 || j == 0 || j == width-1) {
                    grid[i][j] = 1; // Pared
                }
            }
        }
        
        // Crear un laberinto simple con paredes internas
        // Pared vertical central con abertura
        for (int i = 3; i < height-3; i++) {
            if (i != height/2 && i != height/2 + 1) {
                grid[i][width/2] = 1;
            }
        }
        
        // Paredes horizontales para crear secciones
        for (int j = 3; j < width/2-2; j++) {
            grid[height/3][j] = 1;
        }
        for (int j = width/2+3; j < width-3; j++) {
            grid[2*height/3][j] = 1;
        }
        
        // Algunas paredes adicionales para hacer el laberinto más interesante
        for (int i = 5; i < 8; i++) {
            grid[i][5] = 1;
        }
        for (int i = 12; i < 15; i++) {
            grid[i][14] = 1;
        }
    }
    
    /**
     * Crea el diseño del nivel 2 - Laberinto intermedio con más obstáculos
     */
    private void createLevel2() {
        // Similar a nivel 1 pero con más complejidad
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height-1 || j == 0 || j == width-1) {
                    grid[i][j] = 1;
                } else {
                    grid[i][j] = 0;
                }
            }
        }
        
        // Paredes más complejas para nivel 2
        // Crear una cruz central
        for (int i = height/3; i < 2*height/3; i++) {
            grid[i][width/2] = 1;
        }
        for (int j = width/3; j < 2*width/3; j++) {
            grid[height/2][j] = 1;
        }
        
        // Aberturas en la cruz
        grid[height/2][width/2] = 0;
        grid[height/2 - 2][width/2] = 0;
        grid[height/2 + 2][width/2] = 0;
        grid[height/2][width/2 - 2] = 0;
        grid[height/2][width/2 + 2] = 0;
    }
    
    /**
     * Crea el diseño del nivel 3 - Laberinto difícil
     */
    private void createLevel3() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height-1 || j == 0 || j == width-1) {
                    grid[i][j] = 1;
                } else {
                    grid[i][j] = 0;
                }
            }
        }
        
        // Laberinto estilo espiral para nivel 3
        for (int i = 2; i < height-2; i++) {
            grid[i][3] = 1;
            grid[i][width-4] = 1;
        }
        for (int j = 3; j < width-3; j++) {
            grid[3][j] = 1;
            grid[height-4][j] = 1;
        }
        
        // Crear aberturas estratégicas
        grid[height/2][3] = 0;
        grid[height/2][width-4] = 0;
        grid[3][width/2] = 0;
        grid[height-4][width/2] = 0;
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