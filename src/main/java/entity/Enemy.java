package entity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

public class Enemy
{
    /**
     * The abstract class for a general enemy.
     */
    protected Coordinate position;
    protected Coordinate nextCoord;
    protected int coordinateCounter = 0;
    protected int speed;
    protected int health;
    protected int size;
    public boolean isAlive = true;
    protected ArrayList<Coordinate> waypoints;
    GamePanel gp;
    private BufferedImage enemyImage;
    private String enemyUp;
    private String enemyDown;
    private String enemyLeft;
    private String enemyRight;
    private String direction = "/entities/enemy/Right.png";
    
    public Enemy(Coordinate position, int speed, int health, GamePanel gp){
        // Instantiate the coordinate with the given x and y values.
        this.position   = position;
        // Optionally, if you need nextCoord for pathfinding or movement, initialize it:
        this.waypoints = gp.getWaypoints();
        this.nextCoord  = new Coordinate(waypoints.get(0).getX() * gp.TILESIZE, waypoints.get(0).getY() * gp.TILESIZE, gp);
        this.speed      = speed;
        this.health     = health;
        this.gp         = gp;
        this.size       = gp.TILESIZE;
        try {
            enemyImage = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/rat.png"));
            enemyUp = "/entities/enemy/Up.png";
            enemyDown = "/entities/enemy/Down.png";
            enemyLeft = "/entities/enemy/Left.png";
            enemyRight = "/entities/enemy/Right.png";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2)
    {
        /**
         * Draws the enemy to the screen.
         * @param g2 Provides tools for drawing graphics.
         */
        
        //g2.setColor(Color.RED);
        //g2.fillRect(position.getX(), position.getY(), size, size);
        // Draw the enemy as a red square
        
        g2.drawImage(enemyImage, position.getX(), position.getY(), 
                                     gp.TILESIZE, gp.TILESIZE, null);
        
        // Draw health bar
        g2.setColor(Color.GREEN);
        g2.fillRect(position.getX(), position.getY() - 10, (int) ((health / 100.0) * size), 5);
    }
    
    public void update() {
        int dx = nextCoord.getX() - position.getX();
        int dy = nextCoord.getY() - position.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (dx > 0){ direction = enemyRight;
        }   else if (dx < 0) { direction = enemyLeft;
        }   else if (dy > 0) { direction = enemyDown;
        }   else if (dy < 0) { direction = enemyUp;
        }
        
        try {
            enemyImage = ImageIO.read(getClass().getResourceAsStream(direction));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (distance >= speed) {
            double unitX = dx / distance;
            double unitY = dy / distance;
            position.setX(position.getX() + (int)(unitX * speed));
            position.setY(position.getY() + (int)(unitY * speed));
        } else {
            // Snap to the next coordinate:
            position = new Coordinate(
                nextCoord.getX(), 
                nextCoord.getY(), 
                gp);

            // If we're at the last waypoint, remove the enemy.
            if (coordinateCounter >= waypoints.size() - 1) {
                onDeath();
                return; // Exit update to prevent further processing.
            } else {
                // Otherwise, increment the counter and update nextCoord.
                coordinateCounter++;
                nextCoord = new Coordinate(
                    waypoints.get(coordinateCounter).getX() * gp.TILESIZE, 
                    waypoints.get(coordinateCounter).getY() * gp.TILESIZE, 
                    gp);
            }
        }
    }

    
    public void takeDamage(int damage)
    {
        /**
         * Updates health and checks for death when an enemy takes damage.
         * @param damage Sent by projectile and determines damage taken.
         */
        
        health = (health - damage);
        if (health <= 0)
        {
            onDeath();
        }
    }
            
    public void onDeath()
    {
        /**
         * Handles deleting an enemy upon death.
         */
        
        isAlive = false;
    }
    
    public int getHealth()
    {
        /**
         * Getter that returns current health value of an enemy.
         * @return the health value as an int
         */
        
        return health;
    }
    
    public Coordinate getPosition(){
        /**
         * Getter that returns the position of an enemy.
         * @return the position value as a Coordinate
         */
        
        return position;
    }
    
    public int getSize()
    {
        /**
         * Getter that returns the size of an enemy.
         * @return the size value as an int
         */
        
        return size;
    }
}