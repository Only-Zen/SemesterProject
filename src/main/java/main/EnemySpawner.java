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

/**
 * The {@code EnemySpawner} class is responsible for loading and managing enemy waves (or rounds) in the game.
 * It reads enemy data from a file and spawns enemies onto the game panel during active rounds based on a cooldown timer.
 * 
 * @author mrsch
 */
public class EnemySpawner {
    /** A list of queues where each queue represents a wave (round) of enemies. */
    public List<Queue<Enemy>> enemyQueues;
    
    /** The cooldown speed for spawning enemies. Lower value means faster spawn rate. */
    public int speed = 60;
    
    private int round;
    private int cooldownTimer;
    
    /** Reference to the game panel where enemies are spawned. */
    GamePanel gp;

    /**
     * Constructs a new {@code EnemySpawner} and loads enemy wave data from a specified file.
     *
     * @param filePath the path to the enemy wave file
     * @param gp       the game panel reference
     */
    public EnemySpawner(String filePath, GamePanel gp) {
        this.enemyQueues = new ArrayList<>();
        this.round = 0;
        this.cooldownTimer = speed;
        this.gp = gp;
        
        loadSpawner(filePath);
    }

    /**
     * Loads enemy wave data from the specified file.
     * Each line in the file corresponds to a wave and contains space-separated numbers.
     * Each number corresponds to a specific enemy type:
     * <ul>
     *     <li>1 - BasicEnemy</li>
     *     <li>2 - SpeedyEnemy</li>
     *     <li>3 - TankEnemy</li>
     *     <li>4 - LateEnemy</li>
     *     <li>5 - KingEnemy</li>
     * </ul>
     *
     * @param filePath the path to the wave file resource
     */
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

    /**
     * Updates the enemy spawner each frame.
     * Decrements the cooldown timer and attempts to spawn the next enemy if conditions are met.
     */
    public void update() {
        //
        round = gp.info.getRound();
        cooldownTimer--;
        spawnEnemy();
    }

    /**
     * Spawns an enemy from the current round's queue if the cooldown timer has expired and the round is active.
     */
    private void spawnEnemy() {
        if (enemyQueues != null && cooldownTimer <= 0 && gp.info.isRoundGoing()) {
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

