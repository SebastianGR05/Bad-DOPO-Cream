package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel donde se dibuja el juego con soporte para imágenes o gráficos simples
 */
public class GamePanel extends JPanel {
    private Game game;
    private final int CELL_SIZE = 40;
    
    // Mapa de imágenes cargadas
    private HashMap<String, Image> images;
    private boolean useImages = false; // Cambiar a true cuando tengas las imágenes
    
    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(
            game.getBoard().getWidth() * CELL_SIZE, 
            game.getBoard().getHeight() * CELL_SIZE
        ));
        setBackground(Color.BLACK);
        
        // Intentar cargar imágenes
        images = new HashMap<>();
        loadImages();
    }
    
    /**
     * Intenta cargar todas las imágenes del juego
     */
    private void loadImages() {
        try {
            // Intentar cargar cada imagen, si falla usaremos gráficos primitivos
            images.put("floor", loadImage("/images/levels/floor.png"));
            images.put("background", loadImage("/images/levels/background.png"));
            images.put("wall", loadImage("/images/blocks/wall.png"));
            images.put("ice", loadImage("/images/blocks/iceBlock.png"));
            images.put("iceBroken", loadImage("/images/blocks/iceBlockBroken.png"));
            images.put("grapes", loadImage("/images/fruits/grapes.png"));
            images.put("banana", loadImage("/images/fruits/banana.png"));
            images.put("vanilla", loadImage("/images/characters/vanilla/vanillaAnimated.png"));
            images.put("strawberry", loadImage("/images/characters/strawberry/strawberryAnimated.png"));
            images.put("chocolate", loadImage("/images/characters/chocolate/chocolateAnimated.png"));
            images.put("troll", loadImage("/images/enemies/troll.png"));
            
            // Si llegamos aquí, todas las imágenes se cargaron
            useImages = true;
            System.out.println("Imágenes cargadas correctamente");
        } catch (Exception e) {
            System.out.println("No se pudieron cargar las imágenes, usando gráficos simples");
            useImages = false;
        }
    }
    
    /**
     * Carga una imagen desde los recursos
     */
    private Image loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream(path));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar fondo si está disponible
        if (useImages && images.containsKey("background")) {
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
                    if (useImages && images.containsKey("wall")) {
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
                    if (useImages && images.containsKey("floor")) {
                        g.drawImage(images.get("floor"), screenX, screenY, 
                                  CELL_SIZE, CELL_SIZE, this);
                    } else {
                        // Gráfico simple de piso
                        g.setColor(new Color(200, 230, 255)); // Azul claro
                        g.fillRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
                        g.setColor(new Color(150, 200, 255)); // Azul medio para grid
                        g.drawRect(screenX, screenY, CELL_SIZE, CELL_SIZE);
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
                
                if (useImages && images.containsKey("ice")) {
                    g.drawImage(images.get("ice"), x, y, CELL_SIZE, CELL_SIZE, this);
                } else {
                    // Gráfico simple de hielo
                    g.setColor(new Color(173, 216, 230)); // Celeste
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    
                    // Efecto de cristal con líneas blancas
                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.drawLine(x, y, x + CELL_SIZE, y + CELL_SIZE);
                    g.drawLine(x + CELL_SIZE, y, x, y + CELL_SIZE);
                    
                    // Brillo adicional
                    g.setColor(new Color(255, 255, 255, 150));
                    g.fillRect(x + 2, y + 2, 10, 10);
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
                
                String fruitType = fruit.getType();
                
                if (useImages && images.containsKey(fruitType.toLowerCase())) {
                    Image img = images.get(fruitType.toLowerCase());
                    // Centrar la fruta en la celda (asumiendo que la imagen es 30x30)
                    g.drawImage(img, x + 5, y + 5, 30, 30, this);
                } else {
                    // Gráficos simples según el tipo
                    if (fruitType.equals("GRAPE")) {
                        // Racimo de uvas (3 círculos)
                        g.setColor(new Color(128, 0, 128)); // Morado
                        g.fillOval(x + 10, y + 10, 20, 20);
                        g.setColor(new Color(75, 0, 130)); // Morado oscuro
                        g.drawOval(x + 10, y + 10, 20, 20);
                        
                        // Círculos más pequeños arriba
                        g.setColor(new Color(128, 0, 128));
                        g.fillOval(x + 8, y + 6, 12, 12);
                        g.fillOval(x + 20, y + 6, 12, 12);
                    } else if (fruitType.equals("BANANA")) {
                        // Plátano con forma de media luna
                        g.setColor(Color.YELLOW);
                        g.fillArc(x + 5, y + 10, 30, 20, 0, 180);
                        g.setColor(new Color(200, 180, 0)); // Amarillo más oscuro
                        g.drawArc(x + 5, y + 10, 30, 20, 0, 180);
                        
                        // Línea central del plátano
                        g.drawLine(x + 10, y + 20, x + 30, y + 20);
                    }
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
            
            if (useImages && images.containsKey(flavorKey)) {
                g.drawImage(images.get(flavorKey), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                // Gráfico simple del helado
                Color color;
                switch(player.getFlavor()) {
                    case "VANILLA": color = new Color(255, 250, 240); break;
                    case "STRAWBERRY": color = new Color(255, 182, 193); break;
                    case "CHOCOLATE": color = new Color(139, 69, 19); break;
                    default: color = Color.WHITE;
                }
                
                // Bola de helado (círculo)
                g.setColor(color);
                g.fillOval(x + 8, y + 8, 24, 24);
                g.setColor(color.darker());
                g.drawOval(x + 8, y + 8, 24, 24);
                
                // Cono (triángulo)
                g.setColor(new Color(210, 180, 140)); // Tostado
                int[] xPoints = {x + 20, x + 12, x + 28};
                int[] yPoints = {y + 30, y + 38, y + 38};
                g.fillPolygon(xPoints, yPoints, 3);
                g.setColor(new Color(160, 130, 90));
                g.drawPolygon(xPoints, yPoints, 3);
                
                // Patrón de rejilla en el cono
                g.drawLine(x + 16, y + 34, x + 24, y + 34);
                
                // Ojos
                g.setColor(Color.BLACK);
                g.fillOval(x + 15, y + 16, 3, 3);
                g.fillOval(x + 22, y + 16, 3, 3);
                
                // Sonrisa
                g.drawArc(x + 14, y + 20, 12, 8, 0, -180);
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
            
            if (useImages && images.containsKey(enemyType)) {
                g.drawImage(images.get(enemyType), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                if (enemy.getType().equals("TROLL")) {
                    // Cara del troll (círculo verde con expresión enojada)
                    g.setColor(new Color(34, 139, 34)); // Verde bosque
                    g.fillOval(x + 5, y + 5, 30, 30);
                    
                    // Ojos rojos enojados
                    g.setColor(Color.RED);
                    g.fillOval(x + 12, y + 13, 6, 6);
                    g.fillOval(x + 22, y + 13, 6, 6);
                    
                    // Cejas enojadas
                    g.setColor(new Color(20, 100, 20)); // Verde más oscuro
                    g.drawLine(x + 11, y + 12, x + 17, y + 14);
                    g.drawLine(x + 23, y + 14, x + 29, y + 12);
                    
                    // Boca fruncida
                    g.setColor(Color.BLACK);
                    g.drawArc(x + 12, y + 20, 16, 10, 0, -180);
                    
                    // Borde del rostro
                    g.setColor(new Color(20, 100, 20));
                    g.drawOval(x + 5, y + 5, 30, 30);
                }
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