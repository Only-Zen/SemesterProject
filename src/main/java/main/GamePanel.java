package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import entity.Enemy;
import entity.Projectile;
import java.util.Iterator;
import java.util.Random;
import tile.Grid;
import tower.Tower;

public class GamePanel extends JPanel implements Runnable {

    // Tile and screen settings
    final int ORIGINALTILESIZE = 16;
    final int SCALE = 3;
    public final int TILESIZE = ORIGINALTILESIZE * SCALE;
    public final int MAXSCREENCOL = 32;
    public final int MAXSCREENROW = 16;
    public final int SCREENWIDTH = TILESIZE * MAXSCREENCOL;
    public final int SCREENHEIGHT = TILESIZE * MAXSCREENROW;
    

    public Random random = new Random();
    Sound sound = new Sound();
    public boolean[][] occupiedTiles = new boolean[MAXSCREENCOL][MAXSCREENROW];
    Grid grid = new Grid(this);
    
    public final int FPS = 60;
    Thread gameThread;
    MouseHandler mouseH;

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectile = new ArrayList<>();
    public ArrayList<Tower> towers = new ArrayList<>();

    // Use Coordinate to store the mouse's position
    private Coordinate mouseCoord;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        this.setBackground(Color.GRAY);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        
        // Initialize the mouse coordinate (starts at 0,0)
        mouseCoord = new Coordinate(0, 0, this);

        // Create the MouseHandler and add it as a listener
        mouseH = new MouseHandler(this);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        playMusic(0);
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
        
        for (Enemy enemy : enemies){
            enemy.update();
        }
        
        Iterator<Projectile> projectileIterator = projectile.iterator();
        
        while (projectileIterator.hasNext()) {
            Projectile curProjectile = projectileIterator.next();

            // Move/update the projectile
            curProjectile.update();
            
            if (curProjectile.hasReachedTarget()) {
            projectileIterator.remove();
            continue;
        }

            for (Enemy enemy : enemies) {

                // Calculate deltaX and deltaY
                int deltaX = enemy.getPosition().getX()+(enemy.getSize()/2) - curProjectile.getPosition().getX();
                int deltaY = enemy.getPosition().getY()+(enemy.getSize()/2) - curProjectile.getPosition().getY();

                
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                if (distance <= enemy.getSize()/2) {
                    // The projectile has collided with the enemy
                    enemy.takeDamage(curProjectile.getDamage());

                    // Remove the projectile from the list
                    projectileIterator.remove();
                    break; // No need to check more enemies for this projectile
                }

            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        grid.draw(g2); // Draw tiles

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
        g.fillRect(tileCoord.getX(), tileCoord.getY(), TILESIZE, TILESIZE);
        
        
        
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
    
    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
    }
}
