package tower;

import entity.Projectile;
import main.Coordinate;
import main.GamePanel;

/**
 *
 * @author mrsch
 */
public class BasicTower extends Tower {
    
    public BasicTower(Coordinate position, GamePanel gp){
        this("basic_tower",
                position, 
                192, //range
                20, //damage
                4,   //firerate
                100,  //cost
                gp);
    }
    
    public BasicTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
    public void shoot(Coordinate target) {
        // Create a projectile starting from the center of the tower.
        gp.playMusic(3, 63);
        Coordinate projPos = new Coordinate(position.getX() + gp.TILESIZE / 2, 
                                              position.getY() + gp.TILESIZE / 2, gp);
        gp.projectile.add(new Projectile(projPos, target, 10, damage, gp.TILESIZE / 2, gp));
    }
    
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
