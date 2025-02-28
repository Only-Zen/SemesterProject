package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Grid {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public Grid(GamePanel gp){
        this.gp = gp;
        // Make sure the array is big enough for all variants
        tile = new Tile[100]; 
        mapTileNum = new int[gp.MAXSCREENROW][gp.MAXSCREENCOL];

        getTileImage();
        loadMap("/maps/map.txt");
    }

    public void getTileImage(){
        try {
            // Existing tiles
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[1].occupied = true;

            // Original tile index 2 (generic grass)
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[3].occupied = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sandpath.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grasspath.png"));
            
            // New grass variants for randomization
            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/0.png"));

            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/1.png"));

            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/2.png"));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                throw new IOException("Map file not found!");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("[PASS] Map file found!");

            // Read each row
            for (int row = 0; row < gp.MAXSCREENROW; row++) {
                String line = br.readLine();
                if (line == null) {
                    System.out.println("[FAIL] Map file is incomplete.");
                    return;
                }

                // Split each line into columns
                String[] numbers = line.split(" ");

                for (int col = 0; col < gp.MAXSCREENCOL; col++) {
                    int value = Integer.parseInt(numbers[col]);

                    // If the map says '2', randomly pick one of the new grass variants (6,7,8)
                    if (value == 2) {
                        int randomVariant = gp.random.nextInt(3); // 0..2
                        value = 20 + randomVariant; // shift to tile index 6, 7, or 8
                    }

                    // Store the final tile index in the map array
                    mapTileNum[row][col] = value;
                }
            }

            br.close();
            System.out.println("[PASS] Map loaded successfully!");

        } catch (Exception e) {
            System.out.println("[FAIL] Error loading map!");
            e.printStackTrace();
        }
    }

    public boolean generatePath(int objectiveRow, int objectiveCol){ // builds a set of waypoints that enemies can navagate
        /**
        * There are some requirements for this program to work:
        *   The algorithm assumes there is only a single possible and correct path
        *   Except for the start, the path must not reach the edge
        *   The path must have no dead-ends
        *   The path might get stuck in cycles, although loop-de-loops should work
        */
        final int NORTH = 1;
        final int EAST  = 2;
        final int SOUTH = 3;
        final int WEST  = 4;
        final int DESTINATION = -123; //SET MEE!!!
        int currentDirection = EAST;
        
        int row = 0;
        int col = 0;
        
        int nextRow = 0;
        int nextCol = 0;
        
        int leftRow = 0;
        int leftCol = 0;
        
        int rightRow = 0;
        int rightCol = 0;
        
        LinkedList enemyRoute = new LinkedList(); 
        
        // Find begining of path
        for (row = 0; row < gp.MAXSCREENROW; row++) {
            if(isPath(row, 0) == true){
                enemyRoute.add(row);
                
            }
            
        }
        //create spawn-point
         
        //Generate rest of map
        boolean areWeThereYet = false;
        int loopCounter = 0;
        while (areWeThereYet == false){
            loopCounter++;
            if(loopCounter  > 500) {return false;}
            
            //move one tile in current direction of travel
           switch (currentDirection) {
               
               case 5: //overflow into NORTH
                   currentDirection = NORTH;
               case NORTH:
                   row++;
                   nextRow  = row + 1;
                   leftRow  = row;
                   rightRow = row;
                   
                   nextCol  = col;
                   leftCol  = col - 1;
                   rightCol = col + 1;
                   break;
                   
               case EAST:
                   col++;
                   nextCol++;
                   leftCol  = col + 1;
                   rightCol = col - 1;
                   
                   nextRow  = row;
                   leftRow  = row;
                   rightRow = row;
                   break;
                   
               case SOUTH:
                   row--;
                   nextRow  = row - 1;
                   leftRow  = row;
                   rightRow = row;
                   
                   nextCol  = col;
                   leftCol  = col + 1;
                   rightCol = col - 1;
                   break;
                   
               case 0: //underflow into WEST
                   currentDirection = WEST;
               case WEST:
                   col--;
                   nextCol--;
                   leftCol--;
                   rightCol--;
                   
                   nextRow  = row;
                   leftRow  = row;
                   rightRow = row;
                   break;
           }
           
            if(isPath(nextRow, nextCol) == false) { //Path cannot continue streight 
                if(mapTileNum [nextRow][nextCol] == DESTINATION) { 
                    areWeThereYet = true;
                    currentDirection++; //this is a silly way to ensure waypoint logic works as intended
                }
                else if(isPath(leftRow, leftCol) == true) { //Path shall bend to the left
                    currentDirection++;
                }
                else if(isPath(rightRow, rightCol) == true) { //Path shall bend to the right
                    currentDirection--;
                }
                //Add waypoint to list
                if(currentDirection % 2 == 0) { //An Even dir post imcrement/decrement => previously traveling vertically
                    enemyRoute.add(row);                    
                }
                else { //Odd dir => previously traveling horizontially 
                    enemyRoute.add(col);
                }
            }
            
            
        }
        return true;    
    }
      
    public boolean isPath (int row, int col){
        //all tile codes repersenting paths
        final int PATH_SAND  = 4;
        final int PATH_GRASS = 5;
        //read in tile
        int intarigatedTile = mapTileNum[row][col];
        
        if(intarigatedTile > 9){
            intarigatedTile = (intarigatedTile/10); //alows for path tile variations such as 50 or 45
        }
        //check file against known path codes
        if(intarigatedTile == PATH_SAND || intarigatedTile == PATH_GRASS) {
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.MAXSCREENROW; row++) {
            for (int col = 0; col < gp.MAXSCREENCOL; col++) {
                int tileNum = mapTileNum[row][col];

                int screenX = col * gp.TILESIZE;
                int screenY = row * gp.TILESIZE;

                // A simple cull check, so we don't draw offscreen tiles
                if (screenX + gp.TILESIZE > 0 && screenX < gp.SCREENWIDTH &&
                    screenY + gp.TILESIZE > 0 && screenY < gp.SCREENHEIGHT) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, 
                                 gp.TILESIZE, gp.TILESIZE, null);
                }
            }
        }
    }
}
