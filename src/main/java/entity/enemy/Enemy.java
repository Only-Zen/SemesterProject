package entity.enemy;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

public abstract class Enemy
{
    /**
     * The abstract class for a general enemy.
     * There is high degree of commonality between the derived classes, with only the arguments and images differing.
     */
    protected Coordinate position;
    protected Coordinate nextCoord;
    protected int coordinateCounter = 0;
    protected int speed;
    private int initialSpeed; // base speed at which the Enemy advances along the path. modified by rat desperation system
    protected int health;
    private int maxHealth;
    protected int size; //sets a radius used to determine if a Projectile has hit
    public boolean isAlive = true;
    protected ArrayList<Coordinate> waypoints;
    GamePanel gp;
    private BufferedImage enemyImage;
    private BufferedImage enemyUp;
    private BufferedImage enemyDown;
    private BufferedImage enemyLeft;
    private BufferedImage enemyRight;
    private String direction = "/entities/enemy/Right.png";
    public int distance = 0;
    public int damage;
    public int value;

    public Enemy(Coordinate position, GamePanel gp) {this ("", position, 10, 10, 10, 10, gp);}

    public Enemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        // Instantiate the coordinate with the given x and y values.
        this.position   = position;
        // Optionally, if you need nextCoord for pathfinding or movement, initialize it:
        this.waypoints = gp.getWaypoints();
        this.nextCoord  = new Coordinate(waypoints.get(0).getX() * gp.TILESIZE, waypoints.get(0).getY() * gp.TILESIZE, gp);
        this.speed      = speed;
        this.initialSpeed = speed;
        this.health     = health;
        this.maxHealth  = health;
        this.gp         = gp;
        this.size       = gp.TILESIZE;
        this.damage     = damage;
        this.value      = value;
        try {
            enemyImage = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/"+ name + "/Right.png"));
            enemyUp = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/"+ name + "/Up.png"));
            enemyDown = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/"+ name + "/Down.png"));
            enemyLeft = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/"+ name + "/Left.png"));
            enemyRight = ImageIO.read(getClass().getResourceAsStream("/entities/enemy/"+ name + "/Right.png"));
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
        g2.fillRect(position.getX(), position.getY() - 10, (int) (health * size / maxHealth), 5);
    }
    
    public void update() {
        int dx = nextCoord.getX() - position.getX();
        int dy = nextCoord.getY() - position.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (dx > 0){ enemyImage = enemyRight;
        }   else if (dx < 0) { enemyImage = enemyLeft;
        }   else if (dy > 0) { enemyImage = enemyDown;
        }   else if (dy < 0) { enemyImage = enemyUp;
        }
        
        this.distance += speed;

        if (health < 25){
            speed = initialSpeed + 1;
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
        
        //Enemy uses onDeath for dying and reaching the end, so this checks which one
        if(coordinateCounter >= waypoints.size() - 1){
            gp.info.playerHealth -= this.damage;
            gp.playMusic(5, 43);
        }
        else{
            gp.info.playerMoney += this.value;
            gp.playMusic(2, 38);
        }
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
    
    public int getDistance(){
        return distance;
    }
}