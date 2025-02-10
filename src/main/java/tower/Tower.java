package tower;

import java.awt.Color;
import java.awt.Graphics2D;
import main.Coordinate;
import main.GamePanel;

public class Tower {
    private Coordinate position;
    private int range;
    private int damage;
    private int firerate;
    private double cooldownTimer;
    private GamePanel gp;
    
    public Tower(Coordinate position, int range, int damage, int firerate, double cooldownTimer, GamePanel gp){
        this.position = position;
        this.range = range;
        this.damage = damage;
        this.firerate = firerate;
        this.cooldownTimer = cooldownTimer;
        this.gp = gp;
    }
    
    public void draw(Graphics2D g2){
        //draws a blue tower square
        
        g2.setColor(Color.BLUE);
        g2.fillRect(position.getX(), position.getY(),48,48);
    }
    
    public void update(){
        // to do
    }
    
    public void shoot(){
        //to do
    }
    
    public void place(Coordinate mouseCoordinate){
        //places and orientates the tower to the center of the tile
        
        position.setX(Math.floorDiv(mouseCoordinate.getX(), gp.tileSize) * gp.tileSize);
        position.setY(Math.floorDiv(mouseCoordinate.getY(), gp.tileSize) * gp.tileSize);
        
    }
    
    public boolean isEnemyInRange(){
        //to do
        return false;
    }
}