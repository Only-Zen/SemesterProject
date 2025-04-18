package tower;

import java.awt.Graphics2D;
import entity.enemy.Enemy;
import entity.Projectile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

public abstract class Tower {
    protected Coordinate position;
    protected int range;
    protected int damage;
    protected int firerate;
    protected double cooldownTimer;
    protected int cost;
    protected String name;
    protected GamePanel gp;
    
    // Images for idle state and shooting animation
    private BufferedImage idleImage;
    private BufferedImage[] shootFrames;
    
    // Animation state variables
    private int shootFrameIndex = 0;
    private int shootAnimationSpeed = 3; // Number of update ticks per frame
    private int shootAnimationCounter = 0;
    private boolean isCharging = false;
    
    public Tower(){
        //
    }
    
    public Tower(Coordinate position, GamePanel gp){
        this("",position,120,3,3,30,gp);
    }
    
    public Tower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp) {
        this.position = position;
        this.range = range;
        this.damage = damage;
        this.firerate = firerate;
        this.cooldownTimer = 0; // Start ready to shoot.
        this.cost = cost;
        this.gp = gp;
        try {
            // Load the idle tower image.
            idleImage = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/idle.png"));
            
            // Load the shoot animation frames.
            shootFrames = new BufferedImage[5];
            shootFrames[0] = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/shoot1.png"));
            shootFrames[1] = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/shoot2.png"));
            shootFrames[2] = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/shoot3.png"));
            shootFrames[3] = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/shoot4.png"));
            shootFrames[4] = ImageIO.read(getClass().getResourceAsStream("/tower/" + name + "/shoot5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        // Draw the appropriate image depending on whether the tower is charging.
        if (isCharging) {
            g2.drawImage(shootFrames[shootFrameIndex], position.getX(), position.getY(), 
                         gp.TILESIZE, gp.TILESIZE, null);
        } else {
            g2.drawImage(idleImage, position.getX(), position.getY(), 
                         gp.TILESIZE, gp.TILESIZE, null);
        }
    }
    
    public void update() {
        // Decrement cooldown if still cooling down.
        
        if (cooldownTimer > 0) {
            cooldownTimer--;
        } else {
            // Look for an enemy in range.
            boolean enemyInRange = false;
            for (Enemy enemy : gp.enemies) {
                if (isEnemyInRange(enemy)) {
                    enemyInRange = true;
                    break;
                }
            }
            // If an enemy is in range and we're not already charging, start the shoot animation.
            if (enemyInRange && !isCharging) {
                isCharging = true;
                shootFrameIndex = 0;
                shootAnimationCounter = 0;
                gp.playMusic(6, 20);
            }
        }
        
        // If in shooting animation, progress through frames.
        if (isCharging) {
            shootAnimationCounter++;
            if (shootAnimationCounter >= shootAnimationSpeed) {
                shootAnimationCounter = 0;
                shootFrameIndex++;
                // When we've reached the last frame, fire a projectile.
                if (shootFrameIndex >= shootFrames.length) {
                    shootFrameIndex = shootFrames.length -1;
                    // For simplicity, shoot at the first enemy found in range.
                    for (Enemy enemy : gp.enemies) {
                        if (isEnemyInRange(enemy)) {
                            shoot(enemy.getPosition());
                            break;
                        }
                    }
                    // Reset animation and set cooldown.
                    isCharging = false;
                    shootFrameIndex = 0;
                    cooldownTimer = gp.FPS / firerate;
                }
            }
        }
    }
    
    public void shoot(Coordinate target) {
        // Create a projectile starting from the center of the tower.
        gp.playMusic(3, 63);
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE / 2, 
                                              position.getY() + gp.TILESIZE / 2, gp);
        gp.projectile.add(new Projectile(projPos, target, 8, damage, gp.TILESIZE / 2, gp));
    }
    
    public void place(Coordinate mouseCoordinate) {
        // Place the tower at the center of the tile.
        position.setX(Math.floorDiv(mouseCoordinate.getX(), gp.TILESIZE) * gp.TILESIZE);
        position.setY(Math.floorDiv(mouseCoordinate.getY(), gp.TILESIZE) * gp.TILESIZE);
        gp.towers.add(this);
    }
    
    public boolean isEnemyInRange(Enemy enemy) {
        // Calculate the distance from the tower to the enemy.
        double xDiff = enemy.getPosition().getX() - this.position.getX();
        double yDiff = enemy.getPosition().getY() - this.position.getY();
        return Math.hypot(xDiff, yDiff) <= this.range;
    }
    
    public int getCost(){
        return cost;
    }
    
    public String getString() {
        return "Tower,Tower"       +
                ",PosX="     + String.valueOf(position.getX()) +
                ",PosY="     + String.valueOf(position.getY()) +
                ",Range="    + String.valueOf(range)           +
                ",Damage="   + String.valueOf(damage)          +
                ",Firerate=" + String.valueOf(firerate)        +
                ",\n";
    }
}
