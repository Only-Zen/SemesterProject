package tower;

import entity.Projectile;
import main.Coordinate;
import main.GamePanel;

/**
 * Represents a basic type of tower in the game.
 * <p>
 * The {@code BasicTower} is a simple implementation of a {@link Tower} with predefined
 * stats for range, damage, fire rate, and cost. It shoots projectiles toward enemies
 * when they enter its range.
 * </p>
 *
 * @author mrsch
 */
public class BasicTower extends Tower {
    
    /**
     * Constructs a basic tower with default values.
     *
     * @param position The initial position of the tower.
     * @param gp       The game panel instance.
     */
    public BasicTower(Coordinate position, GamePanel gp){
        this("basic_tower",
                position, 
                192, //range
                20, //damage
                4,   //firerate
                100,  //cost
                gp);
    }
    
    /**
     * Constructs a tower with specified attributes.
     *
     * @param name     The name of the tower (used to load images).
     * @param position The position of the tower.
     * @param range    The attack range of the tower.
     * @param damage   The damage dealt per shot.
     * @param firerate How many times per second the tower fires.
     * @param cost     The cost of placing the tower.
     * @param gp       The game panel instance.
     */
    public BasicTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
    /**
     * Fires a projectile at the target coordinate.
     *
     * @param target The coordinate to shoot toward.
     */
    @Override
    public void shoot(Coordinate target) {
        // Create a projectile starting from the center of the tower.
        gp.playMusic(3, 63);
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE / 2, 
                                              position.getY() + gp.TILESIZE / 2, gp);
        gp.projectile.add(new Projectile(projPos, target, 10, damage, gp.TILESIZE / 2, gp));
    }
    
    /**
     * Returns a string representation of the tower's state, mainly for saving.
     *
     * @return A string with position, range, damage, and firerate info.
     */
    @Override
    public String getString() {
        return "Tower,Basic Tower"       +
                ",PosX="     + String.valueOf(position.getX()) +
                ",PosY="     + String.valueOf(position.getY()) +
                ",Range="    + String.valueOf(range)           +
                ",Damage="   + String.valueOf(damage)          +
                ",Firerate=" + String.valueOf(firerate)        +
                ",\n";
    }
}