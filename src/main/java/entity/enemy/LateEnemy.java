package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class LateEnemy extends Enemy {

    /**
     * Derived from Enemy
     * replaces BasicEnemy in mid-game.
     * It has slightly increased starts the reduce the number of rats needed to achieve a set round-difficulty
     */
    public LateEnemy(Coordinate position, GamePanel gp){
        this ("Late", position, 4, 200, 15, 5, gp);
    }
    public LateEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
    }
}

