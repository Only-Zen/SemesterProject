package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class BasicEnemy extends Enemy {

    public BasicEnemy(Coordinate position, GamePanel gp){
        this ("Basic", position, 3, 100, gp);
    }
    public BasicEnemy(String name, Coordinate position, int speed, int health, GamePanel gp){
        super(name, position, speed, health, gp);
    }
}

