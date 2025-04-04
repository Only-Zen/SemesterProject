package tower;

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
                150, //range
                20, //damage
                4,   //firerate
                75,  //cost
                gp);
    }
    
    public BasicTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
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
