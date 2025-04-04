package tower;

import java.awt.Graphics2D;
import entity.Enemy;
import entity.Projectile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Coordinate;
import main.GamePanel;

/**
 *
 * @author mrsch
 */
public class RapidTower extends Tower {
    
    public RapidTower(Coordinate position, GamePanel gp){
        this("basic_tower",
                position, 
                75, //range
                20, //damage
                10,   //firerate
                100,  //cost
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