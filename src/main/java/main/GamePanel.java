package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import entity.enemy.Enemy;
import entity.Projectile;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
import tile.Grid;
import tower.*;


public class GamePanel extends JPanel implements Runnable {

    // Tile and screen settings
    final int ORIGINALTILESIZE = 16;
    final int SCALE = 3;
    public final int TILESIZE = ORIGINALTILESIZE * SCALE;
    public final int MAXSCREENCOL = 32;
    public final int MAXSCREENROW = 16;
    public final int SCREENWIDTH = TILESIZE * MAXSCREENCOL;
    public final int SCREENHEIGHT = TILESIZE * MAXSCREENROW;

    public String mapLocation;
    public int round = 0;
    public Random random = new Random();
    Sound sound = new Sound();
    public boolean[][] occupiedTiles = new boolean[MAXSCREENCOL][MAXSCREENROW];

    Grid grid = new Grid(this);
    
    public final int FPS = 60;
    Thread gameThread;
    MouseHandler mouseH;
    EnemySpawner enemySpawner;

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectile = new ArrayList<>();
    public ArrayList<Tower> towers = new ArrayList<>();
    public GameInfo info;

    // Use Coordinate to store the mouse's position
    private Coordinate mouseCoord;
    private BufferedImage mouseImage;
    
    protected int frame = 1;
    private Image tavernImage;

    Dimension preferredsize = new Dimension(SCREENWIDTH, SCREENHEIGHT);
    // Manage game state
    public Boolean isPaused = false;
    Pause pause = new Pause(this);
    
    GameEndMenu gameover = new GameEndMenu(this);

    MenuHandler mh;

    public GamePanel(MenuHandler handler,String mapLocation ) {
        this.setPreferredSize(preferredsize);
        this.setBackground(new Color(0,0,0,1));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.mh = handler;
        this.mapLocation = mapLocation;
        add(pause); //Initialize pause menu
        pause.setPreferredSize(preferredsize);
        pause.showPauseMenu(false);
        
        add(gameover); //Initialize game end menu
        gameover.setPreferredSize(preferredsize);
        gameover.showMenu(false);
        
        // Initialize the mouse coordinate (starts at 0,0)
        mouseCoord = new Coordinate(0, 0, this);
        
        //Initialize GameInfo
        info = new GameInfo(this);

        // Create the MouseHandler and add it as a listener
        mouseH = new MouseHandler(this);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        try {
            // Load the image you want to use as the cursor
            mouseImage = ImageIO.read(getClass().getResourceAsStream("/icons/shovel.png"));
            tavernImage = ImageIO.read(getClass().getResourceAsStream("/tiles/tavern.png"));
            
            // Determine the new size (e.g., double the original width and height)
            int newWidth = mouseImage.getWidth() * 2;
            int newHeight = mouseImage.getHeight() * 2;
            
            // Scale the image
            Image scaledMouseImage = mouseImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            
            // Create a custom cursor using the scaled image
            // Adjust the hotspot as needed; here it remains at (0, 0)
            Cursor customCursor = Toolkit.getDefaultToolkit()
                    .createCustomCursor(scaledMouseImage, new Point(0, 0), "custom cursor");
            this.setCursor(customCursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        grid.loadMap(mapLocation);
        boolean dummy = grid.generatePath();
        for (int i = -1; i<=1; i++){
            for (int j = -2; j <= 0; j++){
                occupiedTiles[grid.enemyWaypoints.get(grid.enemyWaypoints.size() - 1).getX() + i][grid.enemyWaypoints.get(grid.enemyWaypoints.size() - 1).getY() + j] = true;
            }
        }
        enemySpawner = new EnemySpawner(("/entities/enemy/enemyWaves.txt"), this);
        playMusic(0, 45);
        sound.loop();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (!(isPaused || info.isGameOver)) { //Check if paused or game is ended. If so, do not update
                if (delta >= 1) {
                    update();
                    repaint();
                    delta--;
                } 
            } else {
                delta = 0;
            }
        }
    }
    
    public void update() {
                // Update the mouse coordinate from the MouseHandler
            Coordinate newMouseCoord = mouseH.getMouseCoordinate();
            mouseCoord.setX(newMouseCoord.getX());
            mouseCoord.setY(newMouseCoord.getY());
            // Sort the enemies based on distance before towers target them
            
            // Sort enemies in descending order
            enemies.sort(Comparator.comparingInt(Enemy::getDistance).reversed());



            //Load Savegame if needed
            if(mh.triggerReadFromDisk) {
                readSavegame(mh.saveFilepath);
                mh.triggerReadFromDisk = false;
            }

            // Update all projectiles (so they move toward their targets)
            for (Tower tower : new ArrayList<>(towers)) {
                tower.update();
            }

            for (Enemy enemy : enemies){
                enemy.update();
            }

            Iterator<Projectile> projectileIterator = projectile.iterator();

            while (projectileIterator.hasNext()) {
                Projectile curProjectile = projectileIterator.next();

                // Move/update the projectile
                curProjectile.update();

                if (curProjectile.hasReachedTarget()) {
                projectileIterator.remove();
                continue;
            }

                for (Enemy enemy : new ArrayList<>(enemies)) {

                    // Calculate deltaX and deltaY
                    int deltaX = enemy.getPosition().getX()+(enemy.getSize()/2) - curProjectile.getPosition().getX();
                    int deltaY = enemy.getPosition().getY()+(enemy.getSize()/2) - curProjectile.getPosition().getY();


                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    if (distance <= enemy.getSize()/2) {
                        // The projectile has collided with the enemy
                        enemy.takeDamage(curProjectile.getDamage());

                        // Remove the projectile from the list
                        projectileIterator.remove();
                        break; // No need to check more enemies for this projectile
                    }

                }
            }

            enemies.removeIf(enemy -> !enemy.isAlive);

            enemySpawner.update();

            if (frame == 60){
                frame = 1;
            }
            else {
                frame++;
            }
            
            info.update();
            //System.out.println(frame); //(debug)
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        grid.draw(g2); // Draw tiles
        Font font = new Font("Dialog", Font.PLAIN, 12); //used to change fonts as needed
        g2.setFont(font);
        
        // Draw a small red circle at the mouse's current position
        // g2.setColor(Color.RED);
        // g2.fillOval(mouseCoord.getX() - 5, mouseCoord.getY() - 5, 10, 10);
        // g2.drawImage(mouseImage, mouseCoord.getX() - 5, mouseCoord.getY() - 5, 
                                    // TILESIZE / 2, TILESIZE / 2, null);
        if (isPaused) {
            pause.drawPauseScreen(g);
            pause.showPauseMenu(true);
        }
        else if (info.isGameOver) {
            gameover.drawMenu(g);
            gameover.showMenu(true);
        }
        
        else {
            pause.showPauseMenu(false);
            
            grid.draw(g2); // Draw tiles
            
            ArrayList<Coordinate> waypoints = getWaypoints();
            Coordinate lastWaypoint = waypoints.get(waypoints.size() - 1);

            // Calculate the pixel position using TILESIZE (not TILESIZE - 1)
            int baseX = lastWaypoint.getX() * TILESIZE;
            int baseY = lastWaypoint.getY() * TILESIZE;

            // If your tavern image is larger than one tile (here it's drawn at 3*TILESIZE),
            // adjust the coordinates so it is centered over the final tile.
            int offset = (3 * TILESIZE - TILESIZE) / 2;
            int tavernX = baseX - offset;
            int tavernY = baseY - offset - TILESIZE;

            g2.drawImage(tavernImage, tavernX, tavernY, 3 * TILESIZE, 3 * TILESIZE, null);

            // Draw towers
            for (Tower tower : new ArrayList<>(towers)) {
                tower.draw(g2);
            }
            // Draw enemies
            for (Enemy enemy : new ArrayList<>(enemies)) {
                enemy.draw(g2);
            }
            // Draw projectiles
            for (Projectile projectile : new ArrayList<>(projectile)) {
                projectile.draw(g2);
            }
            
            // g2.setColor(Color.WHITE);
            // g2.drawString("MouseX: " + mouseCoord.getX() + " MouseY: " + mouseCoord.getY(),
                // mouseCoord.getX() + 8, mouseCoord.getY() - 8);
            
            // Highlight the tile currently hovered over by the mouse.
            // (We use the tile coordinate from the MouseHandler and multiply by the tile size.)
            Coordinate tileCoord = mouseH.getTileCoordinate();
            if (mouseH.checkIfOccupied() == true){
                g.setColor(new Color(255, 0, 0, 80));
            } else {
                g.setColor(new Color(0, 45, 255, 80));
            }
            g.fillRect(tileCoord.getX(), tileCoord.getY(), TILESIZE, TILESIZE);
            
            //Draw game info overlay
            g2.setColor(Color.BLACK);
            font = new Font("Dialog", Font.PLAIN, 24);
            g2.setFont(font);
            info.draw(g2);
        }
        
        

        g2.dispose();
    }
    
    public void playMusic(int i, int volume){
        sound.playSound(i, volume);
    }
    
    public int getFrame(){
        return frame;
    }
    
    public ArrayList<Coordinate> getWaypoints(){
        return grid.getWaypoints();
    }

    public void writeToDisk (String saveFilename, String mapFilename) {
        //  single type format: Header=data,/n
        //multiple type format: Header,Title1=data1,Title2=data2,...,\n
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilename));

            //Map Filename
            writer.write("Filename," + mapFilename +",\n");
            //Round
            writer.write("Round," + info.round + ",\n");
            //Health
            writer.write("Health," + info.playerHealth + ",\n");
            //Money
            writer.write("Money," + info.playerMoney + ",\n");
            //Placed Towers
            for (Tower tower : towers) {
                writer.write(tower.getString());
                System.out.println("Tower Saved!");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readSavegame(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) != null){
                parseLineString(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void parseLineString(String line) {
        String[] fields = line.split(",", 2); //isolates the line header
        if(fields.length > 1) { // valid lines have at least two substrings
            switch (fields[0]) {
                case "Filename":
                    mapLocation = fields[1] ;
                    System.out.println("The map is: " + mapLocation + "!\n");
                    break;
                case "Round":
                    String[] rData = line.split(",");
                    info.round = Integer.parseInt(rData[1]) ;
                    System.out.println("Round" + info.round + "!\n");

                    break;
                case "Health":
                    String[] hData = line.split(",");
                    info.playerHealth = Integer.parseInt(hData[1]) ;
                    System.out.println("Round" + info.playerHealth + "!\n");
                    break;
                case "Money":
                    String[] mData = line.split(",");
                    info.playerMoney = Integer.parseInt(mData[1]) ;
                    System.out.println("Money" + info.playerMoney + "!\n");
                    break;
                case "Tower":
                    parseTowerString(line);//fields[1]);
                    break;
                default:
            }
        }

    }
    public void parseTowerString(String data) {
        int posX = 0;
        int posY = 0;
        int range = 0;
        int damage = 0;
        int firerate = 0;
        boolean goodParse = true;

        System.out.println(data);
        String[] fields = data.split("[,\\=]");
        System.out.println(fields[0]+ "... " + fields[1]);
        for(int i = 2; i < (fields.length - 1); i = i+2){
            
            


            switch (fields[i]) {
                case "PosX":
                    posX = Integer.parseInt(fields[i + 1]);
                    System.out.print(posX + ", ");
                    break;
                case "PosY":
                    posY = Integer.parseInt(fields[i + 1]);
                    System.out.print(posY + ", ");
                    break;
                case "Range":
                    range = Integer.parseInt(fields[i + 1]);
                    System.out.print(range + ", ");
                    break;
                case "Damage":
                    damage = Integer.parseInt(fields[i + 1]);
                    System.out.print(damage + ", ");
                    break;
                case "Firerate":
                    firerate = Integer.parseInt(fields[i + 1]);
                    System.out.println(firerate);
                    break;
                default:
                    goodParse = false;
            }

        }
        if(goodParse) {
            
            
            Tower newTower;
            
            switch(fields[1]){
                case "Tower":
                    newTower = new BasicTower(new Coordinate(posX, posY, this),this);
                    break;
                case "Basic Tower":
                    newTower = new BasicTower(new Coordinate(posX, posY, this),this);
                    break;
                case "Bomber Tower":
                    newTower = new BomberTower(new Coordinate(posX, posY, this),this);
                    break;
                default:
                    newTower = new BasicTower(new Coordinate(posX, posY, this),this);
            }
            
            
            towers.add(newTower);
        }
        else
            System.out.println("Tower failed to load!");
    }
}
