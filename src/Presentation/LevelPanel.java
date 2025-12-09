package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.HashMap;

/**
 * Clase base abstracta para todos los paneles de nivel
 * CORRECCIÓN: Ahora el fondo se ve correctamente sin ser cubierto por el tablero
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
        loadCommonImages();
    }
    
    /**
     * Carga las imágenes comunes a todos los niveles
     */
    private void loadCommonImages() {
        try {
            images.put("floor", loadImage("/images/levels/floor.png"));
            images.put("wall", loadImage("/images/blocks/wall.png"));
            images.put("iceBlock", loadImage("/images/blocks/iceBlock.png"));
            images.put("icebroken", loadImage("/images/blocks/iceBlockBroken.png"));
            
            images.put("vanilla", loadImage("/images/characters/vanillaAnimated.gif"));
            images.put("strawberry", loadImage("/images/characters/strawberryAnimated.png"));
            images.put("chocolate", loadImage("/images/characters/chocolateAnimated.png"));
            
            images.put("grape", loadImage("/images/fruits/grapes.png"));
            images.put("banana", loadImage("/images/fruits/banana.png"));
            images.put("pineapple", loadImage("/images/fruits/pineapple.png"));
            images.put("cherry", loadImage("/images/fruits/cherry.png"));
            
            images.put("troll", loadImage("/images/enemies/troll.gif"));
            images.put("pot", loadImage("/images/enemies/pot.png"));
            images.put("squid", loadImage("/images/enemies/orangeSquid.png"));
            
            images.put("background", loadImage("/images/levels/background.png"));
            images.put("iglu", loadImage("/images/levels/iglu.png"));
            
            System.out.println("Imágenes comunes cargadas correctamente");
        } catch (Exception e) {
            System.err.println("Error cargando imágenes comunes: " + e.getMessage());
            useImages = false;
        }
    }
    
    protected Image loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("No se pudo cargar: " + path);
            return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar el fondo primero, una sola vez para toda la pantalla
        drawBackground(g);
        
        // Luego dibujar todos los elementos del juego encima
        drawBoard(g);
        drawIceBlocks(g);
        drawFruits(g);
        drawPlayer(g);
        drawEnemies(g);
        
        if (game.isPaused()) {
            drawPausedMessage(g);
        }
    }
    
    /**
     * CORRECCIÓN: Dibuja el fondo una sola vez para toda la pantalla
     */
    protected void drawBackground(Graphics g) {
        if (useImages && images.containsKey("background") && images.get("background") != null) {
            // Dibujar la imagen de fondo escalada a toda la pantalla
            g.drawImage(images.get("background"), 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /**
     * CORRECCIÓN: Dibuja todo el tablero (piso y paredes)
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
     * Dibuja el piso (celdas vacías del tablero)
     */
    protected void drawFloor(Graphics g, int x, int y) {
        if (useImages && images.containsKey("floor") && images.get("floor") != null) {
            g.drawImage(images.get("floor"), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    protected void drawWall(Graphics g, int x, int y) {
        if (useImages && images.containsKey("wall") && images.get("wall") != null) {
            g.drawImage(images.get("wall"), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    protected void drawIceBlocks(Graphics g) {
        for (IceBlock block : game.getBoard().getIceBlocks()) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                
                if (useImages && images.containsKey("iceBlock") && images.get("iceBlock") != null) {
                    g.drawImage(images.get("iceBlock"), x, y, CELL_SIZE, CELL_SIZE, this);
                }
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
            
            if (useImages && images.containsKey(type) && images.get(type) != null) {
                g.drawImage(images.get(type), x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, this);
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
        
        if (useImages && images.containsKey(flavorKey) && images.get(flavorKey) != null) {
            g.drawImage(images.get(flavorKey), x, y, CELL_SIZE, CELL_SIZE, this);
        }
    }
    
    
    protected abstract void drawEnemies(Graphics g);
    
    protected void drawPausedMessage(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String msg = "PAUSA";
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        g.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String instruction = "Presiona P o ESC para continuar";
        fm = g.getFontMetrics();
        int instrWidth = fm.stringWidth(instruction);
        g.drawString(instruction, (getWidth() - instrWidth) / 2, getHeight() / 2 + 50);
    }
    
}