package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.HashMap;

/**
 * Clase base abstracta para todos los paneles de nivel
 * Solo usa excepciones para la carga de imágenes
 */
public abstract class LevelPanel extends JPanel {
    protected Game game;
    protected final int CELL_SIZE = 30;
    protected HashMap<String, Image> images;
    
    public LevelPanel(Game game) {
        this.game = game;
        this.images = new HashMap<>();
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        loadAllImages();
    }
    
    /**
     * Carga todas las imágenes necesarias
     */
    private void loadAllImages() {
        // Imágenes del tablero
        loadImage("floor", "/images/levels/floor.png");
        loadImage("wall", "/images/blocks/wall.png");
        loadImage("background", "/images/levels/background.png");
        
        // Bloques
        loadImage("iceBlock", "/images/blocks/iceBlock.png");
        
        // Obstáculos
        loadImage("hotTile", "/images/blocks/hotTile.png");
        loadImage("campfireOn", "/images/blocks/campfireOn.png");
        loadImage("campfireOff", "/images/blocks/campfireOff.png");
        
        // Helados
        loadImage("vanilla", "/images/characters/vanillaAnimated.gif");
        loadImage("strawberry", "/images/characters/strawberryAnimated.png");
        loadImage("chocolate", "/images/characters/chocolateAnimated.png");
        
        // Frutas
        loadImage("grape", "/images/fruits/grapes.png");
        loadImage("banana", "/images/fruits/banana.png");
        loadImage("pineapple", "/images/fruits/pineapple.png");
        loadImage("cherry", "/images/fruits/cherry.png");
        
        // Enemigos
        loadImage("troll", "/images/enemies/troll.gif");
        loadImage("pot", "/images/enemies/pot.png");
        loadImage("squid", "/images/enemies/orangeSquid.png");
    }
    
    /**
     * Intenta cargar una imagen
     */
    private void loadImage(String key, String path) {
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(path));
            if (img != null) {
                images.put(key, img);
            } else {
                System.err.println("Warning: Could not load image: " + path);
            }
        } catch (Exception e) {
            System.err.println("Warning: Error loading image " + path + ": " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar en orden de capas
        drawBackground(g);
        drawBoard(g);
        drawIceBlocks(g);
        drawObstacles(g);
        drawFruits(g);
        drawPlayer(g);
        drawEnemies(g);
        
        if (game.isPaused()) {
            drawPausedMessage(g);
        }
    }
    
    protected void drawBackground(Graphics g) {
        Image bg = images.get("background");
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    protected void drawObstacles(Graphics g) {
        Board board = game.getBoard();
        
        // Dibujar baldosas calientes
        for (HotTile tile : board.getHotTiles()) {
            if (tile.exists()) {
                int x = tile.getPosition().getX() * CELL_SIZE;
                int y = tile.getPosition().getY() * CELL_SIZE;
                Image img = images.get("hotTile");
                if (img != null) {
                    g.drawImage(img, x, y, CELL_SIZE, CELL_SIZE, this);
                }
            }
        }
        
        // Dibujar fogatas
        for (Campfire fire : board.getCampfires()) {
            if (fire.exists()) {
                int x = fire.getPosition().getX() * CELL_SIZE;
                int y = fire.getPosition().getY() * CELL_SIZE;
                String imageKey = fire.isOn() ? "campfireOn" : "campfireOff";
                Image img = images.get(imageKey);
                if (img != null) {
                    g.drawImage(img, x, y, CELL_SIZE, CELL_SIZE, this);
                }
            }
        }
    }
    
    protected void drawBoard(Graphics g) {
        Board board = game.getBoard();
        
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                int cellType = board.getCellType(x, y);
                int screenX = x * CELL_SIZE;
                int screenY = y * CELL_SIZE;
                
                if (cellType == 1) {
                    drawWall(g, screenX, screenY);
                } else {
                    drawFloor(g, screenX, screenY);
                }
            }
        }
    }
    
    protected void drawFloor(Graphics g, int x, int y) {
        Image floor = images.get("floor");
        if (floor != null) {
            g.drawImage(floor, x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    protected void drawWall(Graphics g, int x, int y) {
        Image wall = images.get("wall");
        if (wall != null) {
            g.drawImage(wall, x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    protected void drawIceBlocks(Graphics g) {
        Image ice = images.get("iceBlock");
        if (ice == null) return;
        
        for (IceBlock block : game.getBoard().getIceBlocks()) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                g.drawImage(ice, x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
    
    protected void drawFruits(Graphics g) {
        for (Fruit fruit : game.getFruits()) {
            if (fruit.isCollected()) {
                continue;
            }
            
            int x = fruit.getPosition().getX() * CELL_SIZE;
            int y = fruit.getPosition().getY() * CELL_SIZE;
            String type = fruit.getType().toLowerCase();
            
            Image fruitImg = images.get(type);
            if (fruitImg != null) {
                g.drawImage(fruitImg, x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, this);
            }
        }
    }
    
    protected void drawPlayer(Graphics g) {
        IceCream player = game.getPlayer();
        if (!player.isAlive()) {
            return;
        }
        
        int x = player.getPosition().getX() * CELL_SIZE;
        int y = player.getPosition().getY() * CELL_SIZE;
        
        String flavorKey = player.getFlavor().toLowerCase();
        Image playerImg = images.get(flavorKey);
        
        if (playerImg != null) {
            g.drawImage(playerImg, x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    protected abstract void drawEnemies(Graphics g);
    
    protected void drawPausedMessage(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String msg = "PAUSE";
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        g.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String instruction = "Press P or ESC to continue";
        fm = g.getFontMetrics();
        int instrWidth = fm.stringWidth(instruction);
        g.drawString(instruction, (getWidth() - instrWidth) / 2, getHeight() / 2 + 50);
    }
}