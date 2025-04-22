package entity.enemy;

import main.Coordinate;
import main.GamePanel;
/**
* Derived class of Enemy.
* King Enemy is the 'final boss' rat with several times the health any other rat tempered by a low speed
*/
public class KingEnemy extends Enemy {

    public KingEnemy(Coordinate position, GamePanel gp){
        this ("King", position, 1, 12500, 100, 100, gp);
    }
    public KingEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
    }
}

