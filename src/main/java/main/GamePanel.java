package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import entity.Enemy;
import entity.Projectile;
import tower.Tower;

public class GamePanel extends JPanel implements Runnable {

    // Tile and screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 32;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    public final int FPS = 60;
    Thread gameThread;
    MouseHandler mouseH;

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectile = new ArrayList<>();
    public ArrayList<Tower> towers = new ArrayList<>();

    // Use Coordinate to store the mouse's position
    private Coordinate mouseCoord;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Initialize the mouse coordinate (starts at 0,0)
        mouseCoord = new Coordinate(0, 0, this);

        // Create the MouseHandler and add it as a listener
        mouseH = new MouseHandler(this);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        // Update the mouse coordinate from the MouseHandler
        Coordinate newMouseCoord = mouseH.getMouseCoordinate();
        mouseCoord.setX(newMouseCoord.getX());
        mouseCoord.setY(newMouseCoord.getY());
        
        // Update all projectiles (so they move toward their targets)
        for (Tower tower : towers) {
            tower.update();
        }
        for (Projectile projectile : projectile) {
            projectile.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw a small red circle at the mouse's current position
        g2.setColor(Color.RED);
        g2.fillOval(mouseCoord.getX() - 5, mouseCoord.getY() - 5, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString("MouseX: " + mouseCoord.getX() + " MouseY: " + mouseCoord.getY(),
                mouseCoord.getX() + 8, mouseCoord.getY() - 8);

        // Highlight the tile currently hovered over by the mouse.
        // (We use the tile coordinate from the MouseHandler and multiply by the tile size.)
        Coordinate tileCoord = mouseH.getTileCoordinate();
        g.setColor(new Color(255, 0, 0, 80));
        g.fillRect(tileCoord.getX(), tileCoord.getY(), tileSize, tileSize);
        
        
        
        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
        // Draw projectiles
        for (Projectile projectile : projectile) {
            projectile.draw(g2);
        }
        // Draw towers
        for (Tower tower : towers) {
            tower.draw(g2);
        }

        g2.dispose();
    }
}
