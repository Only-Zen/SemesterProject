package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameInfo {
    public int playerMoney;
    public int playerHealth;
    public int round;
    public boolean isRoundGoing; //variable to see if the current round is still going.
    public boolean isGameOver;
    public final int PLAY_BUTTON_X = 10;
    public final int PLAY_BUTTON_Y = 126;
    public final int PLAY_BUTTON_WIDTH = 144;
    public final int PLAY_BUTTON_HEIGHT = 50;
    private BufferedImage infoOverlay;
    GamePanel gp;
    
    public GameInfo(GamePanel gp){
        playerMoney = 250;
        playerHealth = 100;
        round = 0;
        isRoundGoing = false;
        isGameOver = false;
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
        
        if(isRoundGoing){
            g2.setColor(Color.GRAY);
        }
        else{
            g2.setColor(Color.GREEN);
        }
        g2.fillRect(PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
    }
    
    public void update(){
        //
        if(playerHealth <= 0){
            //dead
            isGameOver = true;
        }
        else if(gp.enemies.isEmpty() && isRoundGoing){
            this.endRound();
        }
    }
    
    public void startRound(){
        isRoundGoing = true;
    }
    
    public void endRound(){
        isRoundGoing = false;
        round++;
    }
    
}
