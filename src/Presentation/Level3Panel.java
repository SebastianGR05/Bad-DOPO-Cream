package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Panel específico para el Nivel 3
 * Nivel 3: 1 Calamar Naranja, 8 Piñas, 8 Cerezas
 */
public class Level3Panel extends LevelPanel {
    
    public Level3Panel(Game game) {
        super(game);
        loadLevelSpecificImages();
    }
    
    @Override
    protected void loadLevelSpecificImages() {
        try {
            images.put("squid", loadImage("/images/enemies/squid.png"));
            images.put("pineapple", loadImage("/images/fruits/pineapple.png"));
            images.put("cherry", loadImage("/images/fruits/cherry.png"));
            images.put("background3", loadImage("/images/levels/level3-background.png"));
        } catch (Exception e) {
            System.err.println("Error cargando imágenes del nivel 3");
        }
    }
    
    @Override
    protected void drawBackground(Graphics g) {
        if (useImages && images.containsKey("background3") && images.get("background3") != null) {
            g.drawImage(images.get("background3"), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fondo azul oscuro para nivel 3 (el más difícil)
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(25, 25, 112),
                getWidth(), getHeight(), new Color(72, 61, 139)
            );
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    @Override
    protected void drawFruits(Graphics g) {
        for (Fruit fruit : game.getFruits()) {
            if (fruit.isCollected()) {
                continue;
            }
            
            int x = fruit.getPosition().getX() * CELL_SIZE;
            int y = fruit.getPosition().getY() * CELL_SIZE;
            String type = fruit.getType();
            
            if (useImages && images.containsKey(type.toLowerCase()) && 
                images.get(type.toLowerCase()) != null) {
                g.drawImage(images.get(type.toLowerCase()), x + 5, y + 5, 30, 30, this);
            } else {
                drawSimpleFruit(g, x, y, type);
                
                // Efectos especiales
                if (fruit instanceof Pineapple) {
                    // Indicador de movimiento
                    g.setColor(new Color(255, 255, 0, 100));
                    g.drawOval(x + 3, y + 3, 34, 34);
                } else if (fruit instanceof Cherry) {
                    // Efecto de destello para la cereza
                    g.setColor(new Color(255, 0, 255, 80));
                    g.fillOval(x, y, 40, 40);
                }
            }
        }
    }
    
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (useImages && images.containsKey("squid") && images.get("squid") != null) {
                g.drawImage(images.get("squid"), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                drawSimpleSquid(g, x, y);
            }
        }
    }
    
    /**
     * Dibuja un calamar simple sin imagen
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
        
        // Indicador de que puede romper hielo
        g.setColor(new Color(255, 0, 0, 100));
        g.drawRect(x + 3, y + 3, 34, 34);
    }
}