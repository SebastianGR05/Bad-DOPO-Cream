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