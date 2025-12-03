package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel donde se dibuja el juego con soporte para imágenes o gráficos simples
 */
public class GamePanel extends JPanel {
    private Game game;
    private final int CELL_SIZE = 30; // Tamaño de 30 píxeles por celda
    
    // Mapa de imágenes cargadas
    private HashMap<String, Image> images;
    private boolean useImages = true;
    
    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        setBackground(Color.BLACK);
        
        // Cargar imágenes
        images = new HashMap<>();
        loadImages();
    }
    
    /**
     * Intenta cargar todas las imágenes del juego
     */
    private void loadImages() {
        try {
            // Fondos y pisos
            images.put("floor", loadImage("/images/levels/floor.png"));
            images.put("background", loadImage("/images/levels/background.png"));
            
            // Bloques
            images.put("wall", loadImage("/images/blocks/wall.png"));
            images.put("ice", loadImage("/images/blocks/iceBlock.png"));
            images.put("iceBroken", loadImage("/images/blocks/iceBlockBroken.png"));
            
            // Frutas
            images.put("grape", loadImage("/images/fruits/grapes.png"));
            images.put("banana", loadImage("/images/fruits/banana.png"));
            images.put("pineapple", loadImage("/images/fruits/pineapple.png"));
            images.put("cherry", loadImage("/images/fruits/cherry.png"));
            
            // Helados
            images.put("vanilla", loadImage("/images/characters/vanillaAnimated.gif"));
            images.put("strawberry", loadImage("/images/characters/strawberryAnimated.png"));
            images.put("chocolate", loadImage("/images/characters/chocolateAnimated.png"));
            
            // Enemigos
            images.put("troll", loadImage("/images/enemies/troll.gif"));
            images.put("pot", loadImage("/images/enemies/pot.png"));
            images.put("squid", loadImage("/images/enemies/orangeSquid.png"));
            
            System.out.println("Imágenes del GamePanel cargadas correctamente");
        } catch (Exception e) {
            System.out.println("Error al cargar algunas imágenes: " + e.getMessage());
            System.out.println("Se usarán gráficos simples");
            useImages = false;
        }
    }
    
    /**
     * Carga una imagen desde los recursos
     */
    private Image loadImage(String path) {
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(path));
            if (img == null) {
                System.out.println("No se pudo cargar: " + path);
            }
            return img;
        } catch (Exception e) {
            System.out.println("Error cargando: " + path + " - " + e.getMessage());
            return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar fondo si está disponible
        if (useImages && images.containsKey("background") && images.get("background") != null) {
            g.drawImage(images.get("background"), 0, 0, getWidth(), getHeight(), this);
        }
        
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
     * Dibuja el tablero usando imágenes o gráficos primitivos
     */
    private void drawBoard(Graphics g) {
        Board board = game.getBoard();
        
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                int cellType = board.getCellType(x, y);
                int screenX = x * CELL_SIZE;
                int screenY = y * CELL_SIZE;
                
                if (cellType == 1) {
                    // Dibujar pared
                    if (useImages && images.containsKey("wall") && images.get("wall") != null) {
                        g.drawImage(images.get("wall"), screenX, screenY, 
                                  CELL_SIZE, CELL_SIZE, this);
                    } else {
                        // Gráfico simple de pared
                        g.setColor(new Color(139, 69, 19)); // Marrón
                        g.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(101, 51, 15)); // Marrón más oscuro para borde
                        g.drawRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                        
                        // Agregar textura simple con líneas
                        g.setColor(new Color(160, 82, 23));
                        for (int i = 0; i < CELL_SIZE; i += 8) {
                            g.drawLine(screenX, screenY + i, screenX + CELL_SIZE, screenY + i);
                        }
                    }
                } else {
                    // Dibujar piso
                    if (useImages && images.containsKey("floor") && images.get("floor") != null) {
                        g.drawImage(images.get("floor"), screenX, screenY, 
                                  CELL_SIZE, CELL_SIZE, this);
                    }
                }
            }
        }
    }
    
    /**
     * Dibuja los bloques de hielo
     */
    private void drawIceBlocks(Graphics g) {
        ArrayList<IceBlock> blocks = game.getBoard().getIceBlocks();
        
        for (IceBlock block : blocks) {
            if (block.exists()) {
                int x = block.getPosition().getX() * CELL_SIZE;
                int y = block.getPosition().getY() * CELL_SIZE;
                
                if (useImages && images.containsKey("ice") && images.get("ice") != null) {
                    g.drawImage(images.get("ice"), x, y, CELL_SIZE, CELL_SIZE, this);
                }
            }
        }
    }
    
    /**
     * Dibuja las frutas usando imágenes o formas simples
     */
    private void drawFruits(Graphics g) {
        for (Fruit fruit : game.getFruits()) {
            if (!fruit.isCollected()) {
                int x = fruit.getPosition().getX() * CELL_SIZE;
                int y = fruit.getPosition().getY() * CELL_SIZE;
                
                String fruitType = fruit.getType().toLowerCase();
                
                if (useImages && images.containsKey(fruitType) && images.get(fruitType) != null) {
                    Image img = images.get(fruitType);
                    // Centrar la fruta en la celda
                    g.drawImage(img, x + 5, y + 5, CELL_SIZE, CELL_SIZE, this);
                }
            }
        }
    }
    
    
    /**
     * Dibuja al jugador (helado)
     */
    private void drawPlayer(Graphics g) {
        IceCream player = game.getPlayer();
        if (player.isAlive()) {
            int x = player.getPosition().getX() * CELL_SIZE;
            int y = player.getPosition().getY() * CELL_SIZE;
            
            String flavorKey = player.getFlavor().toLowerCase();
            
            if (useImages && images.containsKey(flavorKey) && images.get(flavorKey) != null) {
                g.drawImage(images.get(flavorKey), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
    
    /**
     * Dibuja los enemigos
     */
    private void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            String enemyType = enemy.getType().toLowerCase();
            
            // Mapear tipos de enemigos
            String imageKey = null;
            if (enemyType.equals("troll")) {
                imageKey = "troll";
            } else if (enemyType.equals("pot")) {
                imageKey = "pot";
            } else if (enemyType.equals("squid")) {
                imageKey = "squid";
            }
            
            if (useImages && imageKey != null && images.containsKey(imageKey) && images.get(imageKey) != null) {
                g.drawImage(images.get(imageKey), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }
    }
    
    /**
     * Dibuja un mensaje semitransparente de pausa
     */
    private void drawPausedMessage(Graphics g) {
        // Capa semitransparente oscura
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Texto de PAUSA
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String msg = "PAUSA";
        FontMetrics fm = g.getFontMetrics();
        int msgWidth = fm.stringWidth(msg);
        int msgX = (getWidth() - msgWidth) / 2;
        int msgY = getHeight() / 2;
        
        // Sombra del texto
        g.setColor(new Color(0, 0, 0, 200));
        g.drawString(msg, msgX + 3, msgY + 3);
        
        // Texto principal
        g.setColor(Color.WHITE);
        g.drawString(msg, msgX, msgY);
        
        // Instrucción para reanudar
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String instruction = "Presiona P o ESC para continuar";
        fm = g.getFontMetrics();
        int instrWidth = fm.stringWidth(instruction);
        g.drawString(instruction, (getWidth() - instrWidth) / 2, msgY + 50);
    }
}