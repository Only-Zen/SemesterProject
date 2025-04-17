package entity.enemy;

import main.Coordinate;
import main.GamePanel;

public class TankEnemy extends Enemy {

        public TankEnemy(Coordinate position, GamePanel gp){
            this ("Tank", position, 2, 1000, 20, 25, gp);
        }
        public TankEnemy(String name, Coordinate position, int speed, int health, int damage, int value, GamePanel gp){
        super(name, position, speed, health, damage, value, gp);
        }
    }

