package tower;

import main.Coordinate;
import main.GamePanel;

/**
 * Represents a rapid type of tower in the game.
 * <p>
 * The {@code RapidTower} is a simple implementation of a {@link Tower} with predefined
 * stats for range, damage, fire rate, and cost. It shoots projectiles toward enemies
 * when they enter its range. It shoots faster than the basic tower at the cost of some range.
 * </p>
 *
 * @author mrsch
 */
public class RapidTower extends Tower {
    
    /**
     * Constructs a rapid tower with default values.
     *
     * @param position The initial position of the tower.
     * @param gp       The game panel instance.
     */
    public RapidTower(Coordinate position, GamePanel gp){
        this("rapid_tower",
                position, 
                144, //range
                40, //damage
                10,   //firerate
                200,  //cost
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
    public RapidTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
    /**
     * Returns a string representation of the tower's state, mainly for saving.
     *
     * @return A string with position, range, damage, and firerate info.
     */
    @Override
    public String getString() {
        return "Tower,Rapid Tower"       +
                ",PosX="     + String.valueOf(position.getX()) +
                ",PosY="     + String.valueOf(position.getY()) +
                ",Range="    + String.valueOf(range)           +
                ",Damage="   + String.valueOf(damage)          +
                ",Firerate=" + String.valueOf(firerate)        +
                ",\n";
    }
}