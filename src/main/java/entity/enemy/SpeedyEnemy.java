package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class SpeedyEnemy extends Enemy {

    public SpeedyEnemy(Coordinate position, GamePanel gp) {
        this("Speedy", position, 4, 70, 5, 5, gp);
    }

    public SpeedyEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
    }
}
