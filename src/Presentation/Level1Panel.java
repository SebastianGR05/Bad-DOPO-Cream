package Presentation;

import Domain.*;
import java.awt.*;

/**
 * Panel específico para el Nivel 1
 * Nivel 1: 2 Trolls, 8 Uvas, 8 Plátanos
 */
public class Level1Panel extends LevelPanel {
    
    public Level1Panel(Game game) {
        super(game);
        loadLevelSpecificImages();
    }
    
    @Override
    protected void loadLevelSpecificImages() {
        try {
            images.put("troll", loadImage("/images/enemies/troll.png"));
            images.put("background1", loadImage("/images/levels/level1-background.png"));
        } catch (Exception e) {
            System.err.println("Error cargando imágenes del nivel 1");
        }
    }
    
    @Override
    protected void drawBackground(Graphics g) {
        if (useImages && images.containsKey("background1") && images.get("background1") != null) {
            g.drawImage(images.get("background1"), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fondo azul claro para nivel 1 (el más fácil)
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(135, 206, 250),
                getWidth(), getHeight(), new Color(70, 130, 180)
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
            }
        }
    }
    
    @Override
    protected void drawEnemies(Graphics g) {
        for (Enemy enemy : game.getEnemies()) {
            int x = enemy.getPosition().getX() * CELL_SIZE;
            int y = enemy.getPosition().getY() * CELL_SIZE;
            
            if (useImages && images.containsKey("troll") && images.get("troll") != null) {
                g.drawImage(images.get("troll"), x, y, CELL_SIZE, CELL_SIZE, this);
            } else {
                drawSimpleTroll(g, x, y);
            }
        }
    }
    
    /**
     * Dibuja un troll simple sin imagen
     */
    private void drawSimpleTroll(Graphics g, int x, int y) {
        // Cara verde del troll
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + 5, y + 5, 30, 30);
        
        // Ojos rojos enojados
        g.setColor(Color.RED);
        g.fillOval(x + 12, y + 13, 6, 6);
        g.fillOval(x + 22, y + 13, 6, 6);
        
        // Cejas
        g.setColor(new Color(20, 100, 20));
        g.drawLine(x + 11, y + 12, x + 17, y + 14);
        g.drawLine(x + 23, y + 14, x + 29, y + 12);
        
        // Boca fruncida
        g.setColor(Color.BLACK);
        g.drawArc(x + 12, y + 20, 16, 10, 0, -180);
        
        // Contorno
        g.setColor(new Color(20, 100, 20));
        g.drawOval(x + 5, y + 5, 30, 30);
    }
}