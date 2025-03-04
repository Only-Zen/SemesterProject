package tower;

import java.awt.Graphics2D;
import entity.Enemy;
import entity.Projectile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

public class Tower {
    private Coordinate position;
    private int range;
    private int damage;
    private int firerate;
    private double cooldownTimer;
    private GamePanel gp;
    private BufferedImage towerImage;
    
    public Tower(Coordinate position, int range, int damage, int firerate, double cooldownTimer, GamePanel gp){
        this.position = position;
        this.range = range;
        this.damage = damage;
        this.firerate = firerate;
        this.cooldownTimer = 0; //cooldownTimer; I believe cooldownTimer constructor parameter not needed
        this.gp = gp;
        try {
            towerImage = ImageIO.read(getClass().getResourceAsStream("/tower/proto.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
        //draws a blue tower square
        /*
        g2.setColor(Color.BLUE);
        g2.fillRect(position.getX(), position.getY(),48,48);
        */
        g2.drawImage(towerImage, position.getX(), position.getY(), 
                                     gp.TILESIZE, gp.TILESIZE, null);
    }
    
    public void update(){
        // countsdown cooldownTimer if it isn't 0. If countdownTimer is 0, it can iterate through enemy list
        
        if (cooldownTimer > 0){
            cooldownTimer--;
        }
        else{
            for (Enemy enemy: gp.enemies){
            //iterates through all enemies and checks if they are in range. Will stop if enemy targeted
            
                if(this.isEnemyInRange(enemy)){
                    this.shoot(enemy.getPosition());
                    cooldownTimer = gp.FPS/firerate;
                    break;
                }
            }
        }
        
    }
    
    public void shoot(Coordinate target){
        //adds a projectile to the projectile array list
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE/2, position.getY() + gp.TILESIZE/2, gp);
        int projDam = damage;
        gp.projectile.add(new Projectile(projPos, target, 4, projDam, gp));
    }
    
    public void place(Coordinate mouseCoordinate){
        //places and orientates the tower to the center of the tile
        
        position.setX(Math.floorDiv(mouseCoordinate.getX(), gp.TILESIZE) * gp.TILESIZE);
        position.setY(Math.floorDiv(mouseCoordinate.getY(), gp.TILESIZE) * gp.TILESIZE);
        
        gp.towers.add(this);
        
    }
    
    public boolean isEnemyInRange(Enemy enemy){
        //calculates the hypotanus of the x difference and y difference of distance between tower and enemy
        //returns if it is less than or equal the range
        
        double xDiff = enemy.getPosition().getX() - this.position.getX();
        double yDiff = enemy.getPosition().getY() - this.position.getY();
        return Math.hypot(xDiff,yDiff) <= this.range;
    }
}