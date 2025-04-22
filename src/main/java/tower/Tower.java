package tower;

import java.awt.Graphics2D;
import entity.enemy.Enemy;
import entity.Projectile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

/**
 * An abstract base class representing a generic tower in the game.
 * Towers are stationary entities that detect enemies within range and shoot projectiles at them.
 * 
 * @author mrsch
 */
public abstract class Tower {
    /** The position of the tower on the game grid. */
    protected Coordinate position;

    /** The range of the tower, determining how far it can detect and target enemies. */
    protected int range;

    /** The damage dealt by each projectile fired by the tower. */
    protected int damage;

    /** The number of shots the tower can fire per second. */
    protected int firerate;

    /** A cooldown timer controlling when the tower can next shoot. */
    protected double cooldownTimer;

    /** The cost to place this tower on the battlefield. */
    protected int cost;

    /** The name of the tower, used for loading resources.*/
    protected String name;

    /** Reference to the main game panel. */
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
    
    /**
     * Constructs a basic tower with a default name and preset attributes.
     *
     * @param position The initial position of the tower.
     * @param gp       The game panel reference.
     */
    public Tower(Coordinate position, GamePanel gp){
        this("",position,120,3,3,30,gp);
    }
    
    /**
     * Constructs a tower with specified attributes and attempts to load its assets.
     *
     * @param name     The name of the tower (used for asset loading).
     * @param position The initial position of the tower.
     * @param range    The detection range of the tower.
     * @param damage   The damage per shot.
     * @param firerate The firing rate (shots per second).
     * @param cost     The cost to place this tower.
     * @param gp       The game panel reference.
     */
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
    
    /**
     * Draws the tower on the screen based on its current state (idle or shooting).
     *
     * @param g2 The graphics context.
     */
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
    
    /**
     * Updates the tower’s state, including cooldown and animation logic.
     */
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
    
    /**
     * Fires a projectile toward the specified target coordinate.
     *
     * @param target The position of the target enemy.
     */
    public void shoot(Coordinate target) {
        // Create a projectile starting from the center of the tower.
        gp.playMusic(3, 63);
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE / 2, 
                                              position.getY() + gp.TILESIZE / 2, gp);
        gp.projectile.add(new Projectile(projPos, target, 8, damage, gp.TILESIZE / 2, gp));
    }
    
    /**
     * Places the tower at a specified mouse coordinate by aligning to the grid.
     *
     * @param mouseCoordinate The mouse click location for placement.
     */
    public void place(Coordinate mouseCoordinate) {
        // Place the tower at the center of the tile.
        position.setX(Math.floorDiv(mouseCoordinate.getX(), gp.TILESIZE) * gp.TILESIZE);
        position.setY(Math.floorDiv(mouseCoordinate.getY(), gp.TILESIZE) * gp.TILESIZE);
        gp.towers.add(this);
    }
    
    /**
     * Checks whether a given enemy is within the tower's attack range.
     *
     * @param enemy The enemy to check.
     * @return {@code true} if the enemy is within range, {@code false} otherwise.
     */
    public boolean isEnemyInRange(Enemy enemy) {
        // Calculate the distance from the tower to the enemy.
        double xDiff = enemy.getPosition().getX() - this.position.getX();
        double yDiff = enemy.getPosition().getY() - this.position.getY();
        return Math.hypot(xDiff, yDiff) <= this.range;
    }
    
    /**
     * Returns the cost of placing this tower.
     *
     * @return The tower's cost.
     */
    public int getCost(){
        return cost;
    }
    
    /**
     * Returns a string representation of the tower's state, mainly for saving.
     *
     * @return A string with position, range, damage, and firerate info.
     */
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