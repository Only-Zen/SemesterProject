package tower;

import entity.Projectile;
import main.Coordinate;
import main.GamePanel;

/**
 *
 * @author mrsch
 */
public class BomberTower extends Tower {
    
    public BomberTower(Coordinate position, GamePanel gp){
        this("bomber_tower",
                position, 
                75, //range
                50, //damage
                1,   //firerate
                125,  //cost
                gp);
    }
    
    public BomberTower(String name, Coordinate position, int range, int damage, int firerate, int cost, GamePanel gp){
        super(name, position, range, damage, firerate, cost, gp);
    }
    
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
            gp.projectile.add(new Projectile(startCoord, targetCoord, 6, damage, 0, gp));
        }     
    }
    
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
