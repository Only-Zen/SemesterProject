package main;

import button.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameInfo {
    public int playerMoney;
    public int playerHealth;
    public int round;
    public int towerInHand;
    public boolean isRoundGoing; //variable to see if the current round is still going.
    public boolean isGameOver;
    public boolean isGameWon;
    public StartButton startButton;
    public PauseButton pauseButton;
    public TowerButton basicTowerButton;
    public TowerButton bomberTowerButton;
    public TowerButton rapidTowerButton;
    private BufferedImage infoOverlay;
    GamePanel gp;
    GameEndMenu gameover;
    
    public GameInfo(GamePanel gp){
        playerMoney = 250;
        playerHealth = 100;
        round = 0;
        towerInHand = 1;
        isRoundGoing = false;
        isGameOver = false;
        isGameWon = false;
        startButton = new StartButton(10, 126, 144, 48);
        pauseButton = new PauseButton(1474,10,48,48);
        basicTowerButton = new TowerButton(164, 10, 50, 50, 1);
        bomberTowerButton = new TowerButton(224, 10, 50, 50, 2);
        rapidTowerButton = new TowerButton(284, 10, 50, 50, 3);
        
        this.gp = gp;
        
        try {
            // Load the item overlay
            infoOverlay = ImageIO.read(getClass().getResourceAsStream("/icons/Info2up.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public void draw(Graphics2D g2){
        //
        g2.drawImage(infoOverlay,10,10,144,96,null);
        
        g2.drawString(playerMoney + "",60,48);
        g2.drawString(playerHealth + "",60,91);
        
        
        startButton.draw(g2,isRoundGoing);
        g2.drawString("Wave: " + (round+1), 40,160);
        pauseButton.draw(g2);
        basicTowerButton.draw(g2,towerInHand);
        bomberTowerButton.draw(g2,towerInHand);
        rapidTowerButton.draw(g2,towerInHand);
    }
    
    public void update(){
        if (playerHealth <= 0){
            //dead
            isGameOver = true;
        }
        else if(gp.enemies.isEmpty() && isRoundGoing && gp.enemySpawner.enemyQueues.get(round).isEmpty()){
            this.endRound();
                if (round % 2 == 0){
                    gp.enemySpawner.speed -= 5;
                }
        }
    }
    
    public void startRound(){
        isRoundGoing = true;
    }
    
    public void endRound(){
        isRoundGoing = false;
        round++;
        
        if (round >= 20) {
            isGameWon = true;
            isGameOver = true;
        }
    }
}
