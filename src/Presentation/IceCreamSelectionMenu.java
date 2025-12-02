package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Menú de selección de sabor de helado
 * Permite elegir entre Vainilla, Fresa y Chocolate
 */
public class IceCreamSelectionMenu extends JFrame {
    
    private String selectedFlavor = null;
    private JButton btnContinue;
    private JButton btnBack;
    private Image backgroundImage;
    
    // Paneles de helados
    private IceCreamPanel vanillaPanel;
    private IceCreamPanel strawberryPanel;
    private IceCreamPanel chocolatePanel;
    
    public IceCreamSelectionMenu() {
        prepareElements();
        prepareActions();
    }
    
    private void prepareElements() {
        setTitle("Selecciona tu Helado");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Intentar cargar imagen de fondo
        try {
            backgroundImage = new ImageIcon(getClass().getResource(
                "/images/menu/icecream-selection-background.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se pudo cargar el fondo de selección de helado");
        }
        
        // Panel principal
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(200, 230, 255),
                        0, getHeight(), new Color(135, 206, 250)
                    );
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        
        // Título
        JLabel title = new JLabel("Elige tu Helado");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(50, 50, 150));
        title.setBounds(250, 50, 400, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(title);
        
        // Paneles de helados
        vanillaPanel = new IceCreamPanel(
            "/images/icecreams/vanilla.png", 
            "Vainilla", 
            "VANILLA"
        );
        vanillaPanel.setBounds(150, 180, 150, 220);
        mainPanel.add(vanillaPanel);
        
        strawberryPanel = new IceCreamPanel(
            "/images/icecreams/strawberry.png", 
            "Fresa", 
            "STRAWBERRY"
        );
        strawberryPanel.setBounds(375, 180, 150, 220);
        mainPanel.add(strawberryPanel);
        
        chocolatePanel = new IceCreamPanel(
            "/images/icecreams/chocolate.png", 
            "Chocolate", 
            "CHOCOLATE"
        );
        chocolatePanel.setBounds(600, 180, 150, 220);
        mainPanel.add(chocolatePanel);
        
        // Botón continuar
        btnContinue = new JButton("CONTINUAR");
        btnContinue.setBounds(350, 480, 200, 60);
        btnContinue.setFont(new Font("Arial", Font.BOLD, 24));
        btnContinue.setBackground(new Color(50, 200, 50));
        btnContinue.setForeground(Color.WHITE);
        btnContinue.setFocusPainted(false);
        btnContinue.setBorderPainted(false);
        btnContinue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinue.setEnabled(false); // Deshabilitado hasta que seleccionen
        mainPanel.add(btnContinue);
        
        // Botón volver
        btnBack = createSmallButton("← Volver", 20, 20);
        mainPanel.add(btnBack);
        
        add(mainPanel);
    }
    
    private JButton createSmallButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 40);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(80, 150, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 170, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(80, 150, 220));
            }
        });
        
        return button;
    }
    
    private void prepareActions() {
        // Listeners para selección de helados
        vanillaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("VANILLA", vanillaPanel);
            }
        });
        
        strawberryPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("STRAWBERRY", strawberryPanel);
            }
        });
        
        chocolatePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectIceCream("CHOCOLATE", chocolatePanel);
            }
        });
        
        btnContinue.addActionListener(e -> {
            if (selectedFlavor != null) {
                // Abrir menú de selección de nivel
                LevelSelectionMenu levelMenu = new LevelSelectionMenu(selectedFlavor);
                levelMenu.setVisible(true);
                dispose();
            }
        });
        
        btnBack.addActionListener(e -> {
            ModalityMenu modalityMenu = new ModalityMenu();
            modalityMenu.setVisible(true);
            dispose();
        });
    }
    
    private void selectIceCream(String flavor, IceCreamPanel selectedPanel) {
        selectedFlavor = flavor;
        
        // Deseleccionar todos
        vanillaPanel.setSelected(false);
        strawberryPanel.setSelected(false);
        chocolatePanel.setSelected(false);
        
        // Seleccionar el clickeado
        selectedPanel.setSelected(true);
        
        // Habilitar botón continuar
        btnContinue.setEnabled(true);
        btnContinue.setBackground(new Color(50, 200, 50));
    }
    
    /**
     * Clase interna para representar un panel de helado seleccionable
     */
    private class IceCreamPanel extends JPanel {
        private Image image;
        private String name;
        private String flavorCode;
        private boolean selected = false;
        
        public IceCreamPanel(String imagePath, String name, String flavorCode) {
            this.name = name;
            this.flavorCode = flavorCode;
            
            try {
                image = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                System.out.println("No se pudo cargar: " + imagePath);
            }
            
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Fondo del panel
            if (selected) {
                g.setColor(new Color(200, 255, 200));
            } else {
                g.setColor(new Color(255, 255, 255, 200));
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Dibujar imagen del helado
            if (image != null) {
                int imageSize = 120;
                int x = (getWidth() - imageSize) / 2;
                int y = 20;
                g.drawImage(image, x, y, imageSize, imageSize, this);
            } else {
                // Dibujar helado simple si no hay imagen
                drawSimpleIceCream(g);
            }
            
            // Nombre del helado
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(name);
            g.drawString(name, (getWidth() - textWidth) / 2, getHeight() - 30);
            
            // Indicador de selección
            if (selected) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                String check = "✓";
                int checkWidth = g.getFontMetrics().stringWidth(check);
                g.drawString(check, (getWidth() - checkWidth) / 2, getHeight() - 5);
            }
        }
        
        private void drawSimpleIceCream(Graphics g) {
            Color color;
            switch(flavorCode) {
                case "VANILLA": color = new Color(255, 250, 240); break;
                case "STRAWBERRY": color = new Color(255, 182, 193); break;
                case "CHOCOLATE": color = new Color(139, 69, 19); break;
                default: color = Color.WHITE;
            }
            
            int centerX = getWidth() / 2;
            int centerY = 80;
            
            // Bola de helado
            g.setColor(color);
            g.fillOval(centerX - 40, centerY - 40, 80, 80);
            g.setColor(color.darker());
            g.drawOval(centerX - 40, centerY - 40, 80, 80);
            
            // Cono
            g.setColor(new Color(210, 180, 140));
            int[] xPoints = {centerX, centerX - 30, centerX + 30};
            int[] yPoints = {centerY + 40, centerY + 80, centerY + 80};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
}