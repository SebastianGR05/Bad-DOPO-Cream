package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.HashMap;

/**
 * Clase base abstracta para todos los paneles de nivel
 */
public abstract class LevelPanel extends JPanel {
    protected Game game;
    protected final int CELL_SIZE = 30;
    protected HashMap<String, Image> images;
    protected boolean useImages = true;
    
    public LevelPanel(Game game) {
        this.game = game;
        this.images = new HashMap<>();
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        try {
            loadCommonImages();
        } catch (BadDopoCreamException e) {
            System.err.println("Error fatal cargando imágenes: " + e.getMessage());
            useImages = false;
        }
    }
    
    /**
     * Carga las imágenes comunes a todos los niveles
     * @throws BadDopoCreamException si hay error al cargar imágenes
     */
    private void loadCommonImages() throws BadDopoCreamException {
        try {
            // Imágenes del tablero
            images.put("floor", loadImage("/images/levels/floor.png"));
            images.put("wall", loadImage("/images/blocks/wall.png"));
            images.put("background", loadImage("/images/levels/background.png"));
            
            // Bloques
            images.put("iceBlock", loadImage("/images/blocks/iceBlock.png"));
            
            // Obstáculos
            images.put("hotTile", loadImage("/images/blocks/hotTile.png"));
            images.put("campfireOn", loadImage("/images/blocks/campfireOn.png"));
            images.put("campfireOff", loadImage("/images/blocks/campfireOff.png"));
            
            // Helados
            images.put("vanilla", loadImage("/images/characters/vanillaAnimated.gif"));
            images.put("strawberry", loadImage("/images/characters/strawberryAnimated.png"));
            images.put("chocolate", loadImage("/images/characters/chocolateAnimated.png"));
            
            // Frutas
            images.put("grape", loadImage("/images/fruits/grapes.png"));
            images.put("banana", loadImage("/images/fruits/banana.png"));
            images.put("pineapple", loadImage("/images/fruits/pineapple.png"));
            images.put("cherry", loadImage("/images/fruits/cherry.png"));
            
            // Enemigos
            images.put("troll", loadImage("/images/enemies/troll.gif"));
            images.put("pot", loadImage("/images/enemies/pot.png"));
            images.put("squid", loadImage("/images/enemies/orangeSquid.png"));
            
        } catch (BadDopoCreamException e) {
            System.err.println("Error cargando imágenes comunes: " + e.getMessage());
            useImages = false;
            throw e;
        }
    }
    
    /**
     * Carga una imagen desde un path
     * @param path Ruta de la imagen
     * @return La imagen cargada
     * @throws BadDopoCreamException si no se puede cargar la imagen
     */
    protected Image loadImage(String path) throws BadDopoCreamException {
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(path));
            if (img == null) {
                throw new BadDopoCreamException(BadDopoCreamException.IMAGE_LOAD_ERROR + ": " + path);
            }
            return img;
        } catch (Exception e) {
            throw new BadDopoCreamException(BadDopoCreamException.RESOURCE_NOT_FOUND + ": " + path, e);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar en orden de capas (de atrás hacia adelante)
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
    
    /**
     * Dibuja el fondo una sola vez para toda la pantalla
     */
    protected void drawBackground(Graphics g) {
        g.drawImage(images.get("background"), 0, 0, getWidth(), getHeight(), this);
    }
    
    /**
     * Dibuja los obstáculos
     */
    protected void drawObstacles(Graphics g) {
        Board board = game.getBoard();
        
        // Dibujar baldosas calientes
        for (HotTile tile : board.getHotTiles()) {
            if (tile.exists()) {
                int x = tile.getPosition().getX() * CELL_SIZE;
                int y = tile.getPosition().getY() * CELL_SIZE;
                g.drawImage(images.get("hotTile"), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
        
        // Dibujar fogatas
        for (Campfire fire : board.getCampfires()) {
            if (fire.exists()) {
                int x = fire.getPosition().getX() * CELL_SIZE;
                int y = fire.getPosition().getY() * CELL_SIZE;
                // Elegir imagen según si está encendida o apagada
                String imageKey = fire.isOn() ? "campfireOn" : "campfireOff";
                g.drawImage(images.get(imageKey), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
    
    /**
     * Dibuja todo el tablero
     */
    protected void drawBoard(Graphics g) {
        Board board = game.getBoard();
        
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                int cellType = board.getCellType(x, y);
                int screenX = x * CELL_SIZE;
                int screenY = y * CELL_SIZE;
                
                if (cellType == 1) {
                    // Dibujar pared
                    drawWall(g, screenX, screenY);
                } else {
                    // Dibujar piso (celdas vacías)
                    drawFloor(g, screenX, screenY);
                }
            }
        }
    }
    
    /**
     * Dibuja el piso
     */
    protected void drawFloor(Graphics g, int x, int y) {
        g.drawImage(images.get("floor"), x, y, CELL_SIZE, CELL_SIZE, this);
    }
    
    protected void drawWall(Graphics g, int x, int y) {
        g.drawImage(images.get("wall"), x, y, CELL_SIZE, CELL_SIZE, this);
    }
    
    protected void drawIceBlocks(Graphics g) {
        for (IceBlock block : game.getBoard().getIceBlocks()) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                
                g.drawImage(images.get("iceBlock"), x, y, CELL_SIZE, CELL_SIZE, this);
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
            
            g.drawImage(images.get(type), x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, this);
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

        g.drawImage(images.get(flavorKey), x, y, CELL_SIZE, CELL_SIZE, this);
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