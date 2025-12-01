package Domain;

import java.util.ArrayList;

/**
 * Representa el tablero del juego
 */
public class Board {
    private int width;
    private int height;
    private int[][] grid; // 0=vacío, 1=pared, 2=bloque hielo
    private ArrayList<IceBlock> iceBlocks;
    
    public Board(int width, int height) {
        this.width = 20;  // Tamaño fijo 20x20
        this.height = 20;
        this.grid = new int[this.height][this.width];
        this.iceBlocks = new ArrayList<>();
        createLevel1();
    }
    
    /**
     * Crea el diseño del nivel 1
     */
    private void createLevel1() {
        // Crear paredes del borde
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height-1 || j == 0 || j == width-1) {
                    grid[i][j] = 1; // Pared
                } else {
                    grid[i][j] = 0; // Vacío
                }
            }
        }
        
        // Agregar algunas paredes internas para crear un laberinto simple
        // Pared vertical central
        for (int i = 2; i < height-2; i++) {
            if (i != height/2) {
                grid[i][width/2] = 1;
            }
        }
        
        // Paredes horizontales
        for (int j = 2; j < width/2-1; j++) {
            grid[height/3][j] = 1;
        }
        for (int j = width/2+2; j < width-2; j++) {
            grid[2*height/3][j] = 1;
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
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
     * Crea un bloque de hielo en la dirección que mira el helado
     */
    public void createIceBlock(int x, int y) {
        if (isValidPosition(x, y) && !hasWall(x, y) && !hasIceBlock(x, y)) {
            IceBlock newBlock = new IceBlock(x, y);
            iceBlocks.add(newBlock);
        }
    }
    
    /**
     * Destruye bloques de hielo en efecto dominó
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
        
        // Destruir bloques en la dirección hasta encontrar algo diferente
        while (isValidPosition(x, y) && !hasWall(x, y)) {
            boolean found = false;
            for (IceBlock block : iceBlocks) {
                if (block.exists() && block.getPosition().getX() == x 
                    && block.getPosition().getY() == y) {
                    block.destroy();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                break; // No hay más bloques en esta dirección
            }
            
            x += dx;
            y += dy;
        }
    }
    
    public ArrayList<IceBlock> getIceBlocks() {
        return iceBlocks;
    }
}
