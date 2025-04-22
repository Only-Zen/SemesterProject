package tower;

import entity.Projectile;
import main.Coordinate;
import main.GamePanel;

/**
 * Represents a bomber type of tower in the game.
 * <p>
 * The {@code BomberTower} is a implementation of a {@link Tower} with predefined
 * stats for range, damage, fire rate, and cost. It shoots projectiles outward in 
 * each of the 8 cardinal directions. It is meant to be more expensive and have a
 * shorter range with the benefit of higher damage
 * </p>
 *
 * @author mrsch
 */
public class BomberTower extends Tower {
    
    /**
     * Constructs a bomber tower with default values.
     *
     * @param position The initial position of the tower.
     * @param gp       The game panel instance.
     */
    public BomberTower(Coordinate position, GamePanel gp){
        this("bomber_tower",
                position, 
                96, //range
                50, //damage
                2,   //firerate
                300,  //cost
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
    public BomberTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
    /**
     * Fires 8 projectiles in each of the cardinal directions.
     *
     * @param target The coordinate to shoot toward.
     */
    @Override
    public void shoot(Coordinate target) {
        // Create a projectile starting from the center of the tower.
        gp.playMusic(4, 35);
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE / 2, 
                                              position.getY() + gp.TILESIZE / 2, gp);
        
        Coordinate[] multishot = {  new Coordinate(projPos.getX() + range, projPos.getY(),         gp),
                                    new Coordinate(projPos.getX() + range, projPos.getY() + range, gp),
                                    new Coordinate(projPos.getX(),         projPos.getY() + range, gp),
                                    new Coordinate(projPos.getX() - range, projPos.getY() + range, gp),
                                    new Coordinate(projPos.getX() - range, projPos.getY(),         gp),
                                    new Coordinate(projPos.getX() - range, projPos.getY() - range, gp),
                                    new Coordinate(projPos.getX(),         projPos.getY() - range, gp),
                                    new Coordinate(projPos.getX() + range, projPos.getY() - range, gp)
        };
        
        
        for (int i = 0; i < multishot.length; i++) {
            double angle = Math.toRadians(i * 45);  // 0°, 45°, 90°, ..., 315°
            int targetX = (int)(projPos.getX() + Math.cos(angle) * range);
            int targetY = (int)(projPos.getY() - Math.sin(angle) * range);
            Coordinate targetCoord = new Coordinate(targetX, targetY, gp);
            Coordinate startCoord = new Coordinate(projPos.getX(), projPos.getY(), gp);
            //gp.projectile.add(new Projectile(new Coordinate(projPos.getX() , projPos.getY(), gp), multishot[i], 4, damage, gp));
            gp.projectile.add(new Projectile(startCoord, targetCoord, 12, damage, 0, gp));
        }     
    }
    
    /**
     * Returns a string representation of the tower's state, mainly for saving.
     *
     * @return A string with position, range, damage, and firerate info.
     */
    @Override
    public String getString() {
        return "Tower,Bomber Tower"       +
                ",PosX="     + String.valueOf(position.getX()) +
                ",PosY="     + String.valueOf(position.getY()) +
                ",Range="    + String.valueOf(range)           +
                ",Damage="   + String.valueOf(damage)          +
                ",Firerate=" + String.valueOf(firerate)        +
                ",\n";
    }
}