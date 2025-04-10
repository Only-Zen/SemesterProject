package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class SpeedyEnemy extends Enemy {

    public SpeedyEnemy(Coordinate position, GamePanel gp) {
        this("Speedy", position, 5, 90, gp);
    }

    public SpeedyEnemy(String name, Coordinate position, int speed, int health, GamePanel gp) {
        super(name, position, speed, health, gp);
    }
}
