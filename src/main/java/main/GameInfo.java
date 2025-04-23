package main;

import menu.GameEndMenu;
import button.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

/**
 * Manages the current game state, including player stats, game progress,
 * button controls, and description boxes.
 * 
 * @author mrsch
 */
public class GameInfo {
    /** The button used to manually start a new wave of enemies. */
    public StartButton startButton;

    /** The button used to toggle automatic wave starting. */
    public AutoStartButton autoStartButton;

    /** The button used to pause and resume the game. */
    public PauseButton pauseButton;

    /** The button used to select the basic tower for placement. */
    public TowerButton basicTowerButton;

    /** The button used to select the bomber tower for placement. */
    public TowerButton bomberTowerButton;

    /** The button used to select the rapid-fire tower for placement. */
    public TowerButton rapidTowerButton;
    
    private int playerMoney;
    private int playerHealth;
    private int round;
    private int towerInHand;
    private int towerHoveredOver;
    
    private boolean isRoundGoing; //variable to see if the current round is still going.
    private boolean isGameOver;
    private boolean isGameWon;
    private boolean autoPlay;
    
    private DescriptionBox[] descriptions;
    private DescriptionBox desc1;
    private BufferedImage infoOverlay;
    GamePanel gp;
    GameEndMenu gameover;
    
    /**
     * Constructs a GameInfo object and initializes game state and UI components.
     * 
     * @param gp the main game panel
     */
    public GameInfo(GamePanel gp){
        playerMoney = 250;
        playerHealth = 100;
        round = 0;
        towerInHand = 1;
        towerHoveredOver = 0;
        isRoundGoing = false;
        isGameOver = false;
        isGameWon = false;
        autoPlay = false;
        startButton = new StartButton(10, 126, 144, 48);
        autoStartButton = new AutoStartButton(1474, 68, 48, 48);
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
        
    /**
     * Renders game information and buttons to the screen.
     * 
     * @param g2 the graphics context
     */
    public void draw(Graphics2D g2){
        //
        g2.drawImage(infoOverlay,10,10,144,96,null);
        
        g2.drawString(playerMoney + "",60,48);
        g2.drawString(playerHealth + "",60,91);
        
        startButton.draw(g2,isRoundGoing);
        autoStartButton.draw(g2, autoPlay);
        g2.drawString("Wave: " + (round+1), 45,158);
        pauseButton.draw(g2);
        basicTowerButton.draw(g2,towerInHand);
        bomberTowerButton.draw(g2,towerInHand);
        rapidTowerButton.draw(g2,towerInHand);
        
        //desc1.draw(g2);
        if(towerHoveredOver != 0){
            descriptions[towerHoveredOver -1 ].draw(g2);
        } 
    }
    
    /**
     * Updates the game state each frame.
     * Responsible for checking if a round can end or not as well as game speed.
     */
    public void update(){
        if (playerHealth <= 0){
            //dead
            isGameOver = true;
        }
        else if(gp.enemies.isEmpty() && isRoundGoing && gp.enemySpawner.enemyQueues.get(round).isEmpty()){
            this.endRound();
                if (round % 2 == 0){
                    if (round % 5 == 0){
                        gp.enemySpawner.speed -= 5;
                    }
                    gp.enemySpawner.speed -= 5;
                }
            if (autoPlay == true){
                gp.info.startRound();
            }
        }
    }
    
    /**
     * Loads tower descriptions from a file.
     * 
     * @param filePath the path to the description file
     */
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
            // System.out.println("I made it mom");
            for (int row = 0; row < rows; row++) {
                String name = br.readLine();
                String cost = br.readLine();
                String desc = br.readLine();
                br.readLine();
                Coordinate loc = new Coordinate(164 + row * 60,70,gp);
                descriptions[row] = new DescriptionBox(loc,name,cost,desc);
            }
            
            br.close();
            System.out.println("[PASS] Descriptions loaded successfully ");

                

        } catch (Exception e) {
            System.out.println("[FAIL] Error loading enemy!");
            e.printStackTrace();
        }
    }
    
    /** Begins a new round. */
    public void startRound(){
        isRoundGoing = true;
    }
    
    /** Ends the current round and applies money/round logic. */
    public void endRound(){
        isRoundGoing = false;
        if ((round + 1) > 10){
            playerMoney += 100;
        } else {
            playerMoney += (round + 1) * 10;
        }
        round++;
        
        if (round >= 20) {
            isGameWon = true;
            isGameOver = true;
        }
    }
   
   /** @return current player money */
    public int getPlayerMoney() { return playerMoney; }

    /** @param i sets player money */
    public void setPlayerMoney(int i) { playerMoney = i; }

    /** @param i adds money to player */
    public void gainMoney(int i) { playerMoney += i; }

    /** @param i subtracts money from player */
    public void spendMoney(int i) { playerMoney -= i; }

    /** @return current player health */
    public int getPlayerHealth() { return playerHealth; }

    /** @param i sets player health */
    public void setPlayerHealth(int i) { playerHealth = i; }

    /** @param i reduces player health by i */
    public void takeDamage(int i) { playerHealth -= i; }

    /** @return current round number */
    public int getRound() { return round; }

    /** @param i sets current round */
    public void setRound(int i) { round = i; }

    /** @return the currently selected tower */
    public int getTowerInHand() { return towerInHand; }

    /** @param i sets the tower in hand */
    public void setTowerInHand(int i) { towerInHand = i; }

    /** @param i sets the currently hovered tower */
    public void setTowerHoveredOver(int i) { towerHoveredOver = i; }

    /** @return true if a round is in progress */
    public boolean isRoundGoing() { return isRoundGoing; }

    /** @return true if the game is over */
    public boolean isGameOver() { return isGameOver; }

    /** @return true if the game is won */
    public boolean isGameWon() { return isGameWon; }

    /** Toggles auto-play mode */
    public void toggleAutoPlay() { autoPlay = !autoPlay; }
}