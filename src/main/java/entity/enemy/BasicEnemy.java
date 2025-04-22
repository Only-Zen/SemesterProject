package entity.enemy;

import main.Coordinate;
import main.GamePanel;
/**
 * Derived class of Enemy.
 * This is the first rat the player encounters it's stats make it easily defeatable
 */
public class BasicEnemy extends Enemy {

    public BasicEnemy(Coordinate position, GamePanel gp){
        this ("Basic", position, 3, 120, 10, 5, gp);
    }
    public BasicEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
    }
}

