package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.Coordinate;
import main.GamePanel;

public class Enemy
{
    /**
     * The abstract class for a general enemy.
     */
    protected Coordinate position;
    protected Coordinate nextCoord;
    protected int speed;
    protected int health;
    protected int size;
    
    
    public Enemy(Coordinate position, int speed, int health, GamePanel gp){
        // Instantiate the coordinate with the given x and y values.
        this.position = position;
        // Optionally, if you need nextCoord for pathfinding or movement, initialize it:
        this.nextCoord = position;
        this.speed = speed;
        this.health = health;
        this.size = gp.tileSize;
    }
    
    public void draw(Graphics2D g2)
    {
        /**
         * Draws the enemy to the screen.
         * @param g2 Provides tools for drawing graphics.
         */
        
        g2.setColor(Color.RED);
        g2.fillRect(position.getX(), position.getY(), size, size);
        // Draw the enemy as a red square
    }
    
    public void update()
    {
        /**
         * Manages movement and health updates.
         */
        
        //Leaving blank for now. Return to this later.
    }
    
    public void takeDamage(int damage)
    {
        /**
         * Updates health and checks for death when an enemy takes damage.
         * @param damage Sent by projectile and determines damage taken.
         */
        
        health = (health - damage);
        if (health < 0)
        {
            onDeath();
        }
    }
            
    public void onDeath()
    {
        /**
         * Handles deleting an enemy upon death.
         */
        
        //Leaving blank for now. Return to this later.
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