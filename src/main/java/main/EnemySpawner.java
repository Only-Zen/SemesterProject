package main;

import java.io.*;
import java.util.*;
import java.util.Queue;

import entity.enemy.BasicEnemy;
import entity.enemy.Enemy;
import entity.enemy.KingEnemy;
import entity.enemy.LateEnemy;
import entity.enemy.SpeedyEnemy;
import entity.enemy.TankEnemy;

public class EnemySpawner {
    public List<Queue<Enemy>> enemyQueues;
    private int round;
    private int cooldownTimer;
    public int speed = 60; //lower number = faster. based on frames of cooldown
    GamePanel gp;

    public EnemySpawner(String filePath, GamePanel gp) {
        this.enemyQueues = new ArrayList<>();
        this.round = 0;
        this.cooldownTimer = speed;
        this.gp = gp;
        
        loadSpawner(filePath);
    }

    public void loadSpawner(String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Enemies file not found!");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("[PASS] Enemy file found!");
            
            int rows = Integer.parseInt(br.readLine()); //a singular int at the top of the txt is read so the program knows how many rounds there are

            // Read each row
            for (int row = 0; row < rows; row++) {
                String line = br.readLine();
                if (line == null) {
                    System.out.println("[FAIL] Enemy file is incomplete. Possibly not enough rows");
                    return;
                }
                enemyQueues.add(new LinkedList<Enemy>());

                // Split each line into columns
                String[] numbers = line.split(" ");

                for (int col = 0; col < numbers.length; col++) {
                    int value = Integer.parseInt(numbers[col]);

                    if (value == 1) {
                        enemyQueues.get(row).add(new BasicEnemy(
                                new Coordinate((gp.getWaypoints().get(0).getX() - 1) * gp.TILESIZE, gp.getWaypoints().get(0).getY() * gp.TILESIZE,gp),
                                gp));
                    } //ask about coordinate and default enemy settings
                    
                    if (value == 2) {
                        enemyQueues.get(row).add(new SpeedyEnemy(
                                new Coordinate((gp.getWaypoints().get(0).getX() - 1) * gp.TILESIZE, gp.getWaypoints().get(0).getY() * gp.TILESIZE,gp),
                                gp));

                    }
                    
                    if (value == 3) {
                        enemyQueues.get(row).add(new TankEnemy(
                                new Coordinate((gp.getWaypoints().get(0).getX() - 1) * gp.TILESIZE, gp.getWaypoints().get(0).getY() * gp.TILESIZE,gp),
                                gp));
                    }
                    if (value == 4){
                        enemyQueues.get(row).add(new LateEnemy(
                                new Coordinate((gp.getWaypoints().get(0).getX() - 1) * gp.TILESIZE, gp.getWaypoints().get(0).getY() * gp.TILESIZE,gp),
                                gp));
                    }
                    if (value == 5){
                        enemyQueues.get(row).add(new KingEnemy(
                                new Coordinate((gp.getWaypoints().get(0).getX() - 1) * gp.TILESIZE, gp.getWaypoints().get(0).getY() * gp.TILESIZE,gp),
                                gp));
                    }
                }
            }

            br.close();
            System.out.println("[PASS] Enemies successfully loaded successfully!");

        } catch (Exception e) {
            System.out.println("[FAIL] Error loading enemy!");
            e.printStackTrace();
        }
    }

    public void update() {
        //
        round = gp.info.round;
        cooldownTimer--;
        spawnEnemy();
    }

    private void spawnEnemy() {
        // System.out.println("Spawning enemy: ");
        if (enemyQueues != null && cooldownTimer <= 0 && gp.info.isRoundGoing) {
            Queue<Enemy> currentQueue = enemyQueues.get(round);
            if (!currentQueue.isEmpty()) {
                // Remove and return the head of the queue
                Enemy enemyToSpawn = currentQueue.poll();
                if (enemyToSpawn != null) {
                    gp.enemies.add(enemyToSpawn);
                }
            }
            cooldownTimer = speed;
        } 
    }


}

