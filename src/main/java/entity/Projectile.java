package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.Coordinate;
import main.GamePanel;

/**
 *
 * @author mpk0012
 */

public class Projectile {

    // -----------------------------------------
    // Class member attributes
    // -----------------------------------------
    
    private Coordinate position;
    private Coordinate target;
    private int speed;
    private int damage;
    GamePanel gp;
    
    // Primary constructorsodefnm
    public Projectile(Coordinate position, Coordinate target, int speed, int damage, GamePanel gp) {
        this.speed       = speed;
        this.damage      = damage;
        this.position    = position;
        this.target      = target;
        this.gp          = gp;
    }
  
    // -----------------------------------------
    // Class public methods
    // -----------------------------------------
    
    public void draw (Graphics2D g2) {
        g2.setColor(Color.BLUE);
        int size = 6; // example size
        g2.fillOval(position.getX() - size / 2, position.getY() - size / 2, size, size);
    }
    
    public void update() {
        // find the distance to the target along the x and y axis
        int dx = target.getX() - position.getX() + gp.tileSize/2;
        int dy = target.getY() - position.getY() + gp.tileSize/2;
        
        // Calculate the straight line distance
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance != 0) {
            // Determine the unit vector components
            double unitX = dx / distance;
            double unitY = dy / distance;
            
            // Update the position by moving it a little closer to the target
            position.setX(position.getX() + (int)(unitX * speed));
            position.setY(position.getY() + (int)(unitY * speed));
        }
    }
    
    public int getDamage() {
        return damage;
    }
    
    public Coordinate getPosition() {
        return position;
    }
    
    public Coordinate getTarget() {
        return target;
    }

}