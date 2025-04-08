package main;

import button.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class GameInfo {
    public int playerMoney;
    public int playerHealth;
    public int round;
    public int towerInHand;
    public int towerHoveredOver;
    public boolean isRoundGoing; //variable to see if the current round is still going.
    public boolean isGameOver;
    public boolean isGameWon;
    public StartButton startButton;
    public PauseButton pauseButton;
    public TowerButton basicTowerButton;
    public TowerButton bomberTowerButton;
    public TowerButton rapidTowerButton;
    private DescriptionBox[] descriptions;
    private DescriptionBox desc1;
    private BufferedImage infoOverlay;
    GamePanel gp;
    GameEndMenu gameover;
    
    public GameInfo(GamePanel gp){
        playerMoney = 200;
        playerHealth = 100;
        round = 0;
        towerInHand = 1;
        towerHoveredOver = 0;
        isRoundGoing = false;
        isGameOver = false;
        isGameWon = false;
        startButton = new StartButton(10, 126, 144, 48);
        pauseButton = new PauseButton(1474,10,48,48);
        basicTowerButton = new TowerButton(164, 10, 50, 50, 1);
        bomberTowerButton = new TowerButton(224, 10, 50, 50, 2);
        rapidTowerButton = new TowerButton(284, 10, 50, 50, 3);
        
        desc1 = new DescriptionBox(new Coordinate(164,70,gp),"","","");
        loadDescriptions(("/descriptions/descriptions.txt"));
        
        
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
        
        //desc1.draw(g2);
        if(towerHoveredOver != 0){
            descriptions[towerHoveredOver -1 ].draw(g2);
        }
        
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
    
    public void loadDescriptions(String filePath){
        //
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Enemies file not found!");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("[PASS] Enemy file found!");
            
            int rows = Integer.parseInt(br.readLine()); //a singular int at the top of the txt is read so the program knows how many rounds there are

            // Read each row
            descriptions = new DescriptionBox[rows + 1];
            System.out.println("I made it mom");
            for (int row = 0; row < rows; row++) {
                String name = br.readLine();
                String cost = br.readLine();
                String desc = br.readLine();
                br.readLine();
                Coordinate loc = new Coordinate(164 + row * 60,70,gp);
                descriptions[row] = new DescriptionBox(loc,name,cost,desc);
            }
            
            br.close();
            System.out.println("Loaded descriptions");

                

        } catch (Exception e) {
            System.out.println("[FAIL] Error loading enemy!");
            e.printStackTrace();
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
