package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JPanel;
import entity.Enemy;
import entity.Projectile;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
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
    EnemySpawner enemySpawner = new EnemySpawner(("/entities/enemy/enemyWaves.txt"), this);

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectile = new ArrayList<>();
    public ArrayList<Tower> towers = new ArrayList<>();

    // Use Coordinate to store the mouse's position
    private Coordinate mouseCoord;
    protected int frame = 1;
    private BufferedImage mouseImage;
    private Image tavernImage;

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
        try {
            // Load the image you want to use as the cursor
            mouseImage = ImageIO.read(getClass().getResourceAsStream("/icons/shovel.png"));
            tavernImage = ImageIO.read(getClass().getResourceAsStream("/tiles/tavern.png"));
            
            // Determine the new size (e.g., double the original width and height)
            int newWidth = mouseImage.getWidth() * 2;
            int newHeight = mouseImage.getHeight() * 2;
            
            // Scale the image
            Image scaledMouseImage = mouseImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            
            // Create a custom cursor using the scaled image
            // Adjust the hotspot as needed; here it remains at (0, 0)
            Cursor customCursor = Toolkit.getDefaultToolkit()
                    .createCustomCursor(scaledMouseImage, new Point(0, 0), "custom cursor");
            this.setCursor(customCursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        
        enemies.removeIf(enemy -> !enemy.isAlive);
        
        enemySpawner.update();
        
        if (frame == 60){
            frame = 1;
        }
        else {
            frame++;
        }
        //System.out.println(frame); //(debug)
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        grid.draw(g2); // Draw tiles

        // Draw a small red circle at the mouse's current position
        // g2.setColor(Color.RED);
        // g2.fillOval(mouseCoord.getX() - 5, mouseCoord.getY() - 5, 10, 10);
        // g2.drawImage(mouseImage, mouseCoord.getX() - 5, mouseCoord.getY() - 5, 
                                    // TILESIZE / 2, TILESIZE / 2, null);
        g2.setColor(Color.WHITE);
        g2.drawString("MouseX: " + mouseCoord.getX() + " MouseY: " + mouseCoord.getY(),
                mouseCoord.getX() + 8, mouseCoord.getY() - 8);

        // Highlight the tile currently hovered over by the mouse.
        // (We use the tile coordinate from the MouseHandler and multiply by the tile size.)
        Coordinate tileCoord = mouseH.getTileCoordinate();
        g.setColor(new Color(255, 0, 0, 80));
        g.fillRect(tileCoord.getX(), tileCoord.getY(), TILESIZE, TILESIZE);
        
        ArrayList<Coordinate> waypoints = getWaypoints();
        Coordinate lastWaypoint = waypoints.get(waypoints.size() - 1);

        // Calculate the pixel position using TILESIZE (not TILESIZE - 1)
        int baseX = lastWaypoint.getX() * TILESIZE;
        int baseY = lastWaypoint.getY() * TILESIZE;

        // If your tavern image is larger than one tile (here it's drawn at 3*TILESIZE),
        // adjust the coordinates so it is centered over the final tile.
        int offset = (3 * TILESIZE - TILESIZE) / 2;
        int tavernX = baseX - offset;
        int tavernY = baseY - offset - TILESIZE;

        g2.drawImage(tavernImage, tavernX, tavernY, 3 * TILESIZE, 3 * TILESIZE, null);
        
        // Draw towers
        for (Tower tower : new ArrayList<>(towers)) {
            tower.draw(g2);
        }
        // Draw enemies
        for (Enemy enemy : new ArrayList<>(enemies)) {
            enemy.draw(g2);
        }
        // Draw projectiles
        for (Projectile projectile : new ArrayList<>(projectile)) {
            projectile.draw(g2);
        }

        g2.dispose();
    }
    
    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
    }
    
    public int getFrame(){
        return frame;
    }
    
    public ArrayList<Coordinate> getWaypoints(){
        return grid.getWaypoints();
    }

    public void writeToDisk (String filename) {
        //  single type format: Header=data,/n
        //multiple type format: Header,Title1=data1,Title2=data2,...,\n
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            //Map Filename
            writer.write("Filename=/maps/map.txt,\n"); //To be replaced with a getter function if multiple maps are supported
            //Round
            writer.write(enemySpawner.getRoundString()); //This may be the incorrect place to find this information when multi-wave is supported
            //Score

            //Currency

            //Placed Towers
            for (Tower tower : towers) {
                writer.write(tower.getString());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
