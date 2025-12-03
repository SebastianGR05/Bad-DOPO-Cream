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
    private final int CELL_SIZE = 40;
    
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
                
                if (useImages && images.containsKey("ice") && images.get("ice") != null) {
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
                
                String fruitType = fruit.getType().toLowerCase();
                
                if (useImages && images.containsKey(fruitType) && images.get(fruitType) != null) {
                    Image img = images.get(fruitType);
                    // Centrar la fruta en la celda
                    g.drawImage(img, x + 5, y + 5, 30, 30, this);
                } else {
                    // Gráficos simples según el tipo
                    drawSimpleFruit(g, x, y, fruit.getType());
                }
            }
        }
    }
    
    /**
     * Dibuja una fruta simple sin imagen
     */
    private void drawSimpleFruit(Graphics g, int x, int y, String fruitType) {
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
        } else if (fruitType.equals("PINEAPPLE")) {
            // Piña
            g.setColor(new Color(255, 215, 0));
            g.fillOval(x + 10, y + 12, 20, 20);
            g.setColor(new Color(34, 139, 34));
            int[] xp = {x + 15, x + 20, x + 25};
            int[] yp = {y + 12, y + 5, y + 12};
            g.fillPolygon(xp, yp, 3);
        } else if (fruitType.equals("CHERRY")) {
            // Cerezas
            g.setColor(Color.RED);
            g.fillOval(x + 12, y + 15, 12, 12);
            g.fillOval(x + 20, y + 17, 12, 12);
            g.setColor(new Color(139, 0, 0));
            g.drawOval(x + 12, y + 15, 12, 12);
            g.drawOval(x + 20, y + 17, 12, 12);
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
            } else {
                // Dibujar enemigo simple según tipo
                if (enemyType.equals("troll")) {
                    drawSimpleTroll(g, x, y);
                } else if (enemyType.equals("pot")) {
                    drawSimplePot(g, x, y);
                } else if (enemyType.equals("squid")) {
                    drawSimpleSquid(g, x, y);
                }
            }
        }
    }
    
    /**
     * Dibuja un troll simple
     */
    private void drawSimpleTroll(Graphics g, int x, int y) {
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
    
    /**
     * Dibuja una maceta simple
     */
    private void drawSimplePot(Graphics g, int x, int y) {
        // Maceta (trapecio marrón)
        g.setColor(new Color(139, 69, 19));
        int[] xPoints = {x + 10, x + 30, x + 28, x + 12};
        int[] yPoints = {y + 35, y + 35, y + 20, y + 20};
        g.fillPolygon(xPoints, yPoints, 4);
        
        // Borde de la maceta
        g.setColor(new Color(101, 51, 15));
        g.drawPolygon(xPoints, yPoints, 4);
        
        // Planta (círculo verde con hojas)
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 15, y + 10, 10, 10);
        
        // Hojas
        g.fillOval(x + 12, y + 8, 8, 8);
        g.fillOval(x + 20, y + 8, 8, 8);
        g.fillOval(x + 16, y + 5, 8, 8);
        
        // Ojos malvados en la planta
        g.setColor(Color.RED);
        g.fillOval(x + 17, y + 12, 3, 3);
        g.fillOval(x + 22, y + 12, 3, 3);
    }
    
    /**
     * Dibuja un calamar simple
     */
    private void drawSimpleSquid(Graphics g, int x, int y) {
        // Cuerpo del calamar (óvalo naranja)
        g.setColor(new Color(255, 140, 0));
        g.fillOval(x + 8, y + 8, 24, 20);
        
        // Tentáculos
        g.setColor(new Color(255, 100, 0));
        for (int i = 0; i < 4; i++) {
            int tentX = x + 10 + i * 6;
            g.fillRect(tentX, y + 26, 4, 10);
        }
        
        // Ojos
        g.setColor(Color.WHITE);
        g.fillOval(x + 14, y + 14, 6, 6);
        g.fillOval(x + 22, y + 14, 6, 6);
        
        g.setColor(Color.BLACK);
        g.fillOval(x + 16, y + 16, 3, 3);
        g.fillOval(x + 24, y + 16, 3, 3);
        
        // Contorno
        g.setColor(new Color(200, 100, 0));
        g.drawOval(x + 8, y + 8, 24, 20);
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