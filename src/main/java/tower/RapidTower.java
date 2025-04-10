package tower;

import main.Coordinate;
import main.GamePanel;

public class RapidTower extends Tower {
    
    public RapidTower(Coordinate position, GamePanel gp){
        this("rapid_tower",
                position, 
                144, //range
                25, //damage
                10,   //firerate
                150,  //cost
                gp);
    }
    
    public RapidTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
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