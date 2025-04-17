package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class KingEnemy extends Enemy {

    public KingEnemy(Coordinate position, GamePanel gp){
        this ("King", position, 1, 10000, 50, 100, gp);
    }
    public KingEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
    }
}

