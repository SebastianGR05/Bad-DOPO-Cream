package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.HashMap;

/**
 * Clase base abstracta para todos los paneles de nivel
 * Contiene la funcionalidad común compartida por los tres niveles
 */
public abstract class LevelPanel extends JPanel {
    protected Game game;
    protected final int CELL_SIZE = 40;
    protected HashMap<String, Image> images;
    protected boolean useImages = true;
    
    public LevelPanel(Game game) {
        this.game = game;
        this.images = new HashMap<>();
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        setBackground(Color.BLACK);
        loadCommonImages();
    }
    
    /**
     * Carga las imágenes comunes a todos los niveles
     */
    private void loadCommonImages() {
        try {
            // Bloques y paredes
            images.put("floor", loadImage("/images/levels/floor.png"));
            images.put("wall", loadImage("/images/blocks/wall.png"));
            images.put("ice", loadImage("/images/blocks/iceBlock.png"));
            images.put("icebroken", loadImage("/images/blocks/iceBlockBroken.png"));
            
            // Helados (animados)
            images.put("vanilla", loadImage("/images/characters/vanillaAnimated.gif"));
            images.put("strawberry", loadImage("/images/characters/strawberryAnimated.png"));
            images.put("chocolate", loadImage("/images/characters/chocolateAnimated.png"));
            
            // Frutas comunes
            images.put("grape", loadImage("/images/fruits/grapes.png"));
            images.put("banana", loadImage("/images/fruits/banana.png"));
            images.put("pineapple", loadImage("/images/fruits/pineapple.png"));
            images.put("cherry", loadImage("/images/fruits/cherry.png"));
            
            // Enemigos 
            images.put("troll", loadImage("/images/enemies/troll.png"));
            images.put("pot", loadImage("/images/enemies/pot.png"));
            images.put("squid", loadImage("/images/enemies/orangeSquid.png"));
            
            // Fondos
            images.put("background", loadImage("/images/levels/background.png"));
            images.put("iglu", loadImage("/images/levels/iglu.png"));
            
            System.out.println("Imágenes comunes cargadas correctamente");
        } catch (Exception e) {
            System.err.println("Error cargando imágenes comunes: " + e.getMessage());
            useImages = false;
        }
    }
    
    /**
     * Carga una imagen desde los recursos
     */
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
        
        drawBackground(g);
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
     * Dibuja el fondo del nivel
     */
    protected void drawBackground(Graphics g) {
        if (useImages && images.containsKey("background") && images.get("background") != null) {
            g.drawImage(images.get("background"), 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(10, 20, 40));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    /**
     * Dibuja el tablero completo
     */
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
    
    /**
     * Dibuja una pared
     */
    protected void drawWall(Graphics g, int x, int y) {
        if (useImages && images.containsKey("wall") && images.get("wall") != null) {
            g.drawImage(images.get("wall"), x, y, CELL_SIZE, CELL_SIZE, this);
        } else {
            g.setColor(new Color(101, 67, 33));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(new Color(139, 90, 43));
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            
            // Textura de madera
            g.setColor(new Color(80, 50, 20));
            for (int i = 0; i < 5; i++) {
                g.drawLine(x, y + i * 8, x + CELL_SIZE, y + i * 8);
            }
        }
    }
    
    /**
     * Dibuja el piso
     */
    protected void drawFloor(Graphics g, int x, int y) {
        if (useImages && images.containsKey("floor") && images.get("floor") != null) {
            g.drawImage(images.get("floor"), x, y, CELL_SIZE, CELL_SIZE, this);
        } else {
            g.setColor(new Color(200, 230, 255));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(new Color(150, 200, 255));
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }
    
    /**
     * Dibuja todos los bloques de hielo
     */
    protected void drawIceBlocks(Graphics g) {
        for (IceBlock block : game.getBoard().getIceBlocks()) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                
                if (useImages && images.containsKey("ice") && images.get("ice") != null) {
                    g.drawImage(images.get("ice"), x, y, CELL_SIZE, CELL_SIZE, this);
                } else {
                    // Dibujo simple del bloque de hielo
                    g.setColor(new Color(173, 216, 230, 200));
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.drawLine(x, y, x + CELL_SIZE, y + CELL_SIZE);
                    g.drawLine(x + CELL_SIZE, y, x, y + CELL_SIZE);
                    
                    g.setColor(new Color(255, 255, 255, 150));
                    g.fillRect(x + 2, y + 2, 10, 10);
                }
            }
        }
    }
    
    /**
     * Dibuja todas las frutas
     */
    protected void drawFruits(Graphics g) {
        for (Fruit fruit : game.getFruits()) {
            if (fruit.isCollected()) {
                continue;
            }
            
            int x = fruit.getPosition().getX() * CELL_SIZE;
            int y = fruit.getPosition().getY() * CELL_SIZE;
            String type = fruit.getType().toLowerCase();
            
            if (useImages && images.containsKey(type) && images.get(type) != null) {
                g.drawImage(images.get(type), x + 5, y + 5, 30, 30, this);
            } else {
                drawSimpleFruit(g, x, y, fruit.getType());
            }
        }
    }
    
    /**
     * Dibuja el jugador (helado)
     */
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
        } else {
            drawSimpleIceCream(g, x, y, player.getFlavor());
        }
    }
    
    /**
     * Dibuja un helado simple sin imagen
     */
    protected void drawSimpleIceCream(Graphics g, int x, int y, String flavor) {
        Color color;
        switch(flavor) {
            case "VANILLA": color = new Color(255, 250, 240); break;
            case "STRAWBERRY": color = new Color(255, 182, 193); break;
            case "CHOCOLATE": color = new Color(139, 69, 19); break;
            default: color = Color.WHITE;
        }
        
        // Bola de helado
        g.setColor(color);
        g.fillOval(x + 8, y + 8, 24, 24);
        g.setColor(color.darker());
        g.drawOval(x + 8, y + 8, 24, 24);
        
        // Cono
        g.setColor(new Color(210, 180, 140));
        int[] xPoints = {x + 20, x + 12, x + 28};
        int[] yPoints = {y + 30, y + 38, y + 38};
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(new Color(160, 130, 90));
        g.drawPolygon(xPoints, yPoints, 3);
        
        // Ojos
        g.setColor(Color.BLACK);
        g.fillOval(x + 15, y + 16, 3, 3);
        g.fillOval(x + 22, y + 16, 3, 3);
        
        // Sonrisa
        g.drawArc(x + 14, y + 20, 12, 8, 0, -180);
    }
    
    /**
     * Dibuja los enemigos (método abstracto, cada nivel lo implementa)
     */
    protected abstract void drawEnemies(Graphics g);
    
    /**
     * Dibuja el mensaje de pausa
     */
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
    
    /**
     * Dibuja una fruta genérica de forma simple
     */
    protected void drawSimpleFruit(Graphics g, int x, int y, String type) {
        switch(type) {
            case "GRAPE":
                g.setColor(new Color(128, 0, 128));
                g.fillOval(x + 10, y + 10, 20, 20);
                g.setColor(new Color(75, 0, 130));
                g.drawOval(x + 10, y + 10, 20, 20);
                g.setColor(new Color(128, 0, 128));
                g.fillOval(x + 8, y + 6, 12, 12);
                g.fillOval(x + 20, y + 6, 12, 12);
                break;
                
            case "BANANA":
                g.setColor(Color.YELLOW);
                g.fillArc(x + 5, y + 10, 30, 20, 0, 180);
                g.setColor(new Color(200, 180, 0));
                g.drawArc(x + 5, y + 10, 30, 20, 0, 180);
                g.drawLine(x + 10, y + 20, x + 30, y + 20);
                break;
                
            case "PINEAPPLE":
                g.setColor(new Color(255, 215, 0));
                g.fillOval(x + 10, y + 12, 20, 20);
                g.setColor(new Color(34, 139, 34));
                int[] xp = {x + 15, x + 20, x + 25};
                int[] yp = {y + 12, y + 5, y + 12};
                g.fillPolygon(xp, yp, 3);
                g.setColor(new Color(200, 160, 0));
                for (int i = 0; i < 3; i++) {
                    g.drawLine(x + 12 + i * 5, y + 15, x + 12 + i * 5, y + 28);
                }
                break;
                
            case "CHERRY":
                g.setColor(Color.RED);
                g.fillOval(x + 12, y + 15, 12, 12);
                g.fillOval(x + 20, y + 17, 12, 12);
                g.setColor(new Color(139, 0, 0));
                g.drawOval(x + 12, y + 15, 12, 12);
                g.drawOval(x + 20, y + 17, 12, 12);
                g.setColor(new Color(34, 139, 34));
                g.drawLine(x + 18, y + 15, x + 20, y + 10);
                g.drawLine(x + 26, y + 17, x + 28, y + 12);
                break;
        }
    }
}