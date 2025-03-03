package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
    public LinkedList enemyRoute = new LinkedList();

    public Grid(GamePanel gp){
        this.gp = gp;
        // Make sure the array is big enough for all variants
        tile = new Tile[100]; 
        mapTileNum = new int[gp.MAXSCREENROW][gp.MAXSCREENCOL];

        getTileImage();
        loadMap("/maps/map.txt");
        boolean dummy = generatePath();
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

            // Original tile index 3 (generic tree)
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[3].occupied = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/rock.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grasspath.png"));
            
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle.png"));
            
            // New water variants for randomization
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water/0.png"));
            
            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water/1.png"));
            
            // New grass variants for randomization
            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/0.png"));

            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/1.png"));

            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass/2.png"));
            
            tile[30] = new Tile();
            tile[30].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree/0.png"));
            tile[30].occupied = true;
            
            tile[31] = new Tile();
            tile[31].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree/1.png"));
            tile[31].occupied = true;
            
            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree/2.png"));
            tile[32].occupied = true;
            
            tile[50] = new Tile();
            tile[50].image = ImageIO.read(getClass().getResourceAsStream("/tiles/path/cross.png"));
            
            tile[51] = new Tile();
            tile[51].image = ImageIO.read(getClass().getResourceAsStream("/tiles/path/straight.png"));
            
            tile[52] = new Tile();
            tile[52].image = ImageIO.read(getClass().getResourceAsStream("/tiles/path/corner.png"));
            
            tile[60] = new Tile();
            tile[60].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle/1.png"));
            
            tile[61] = new Tile();
            tile[61].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle/2.png"));
            
            tile[62] = new Tile();
            tile[62].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle/3.png"));
            
            tile[63] = new Tile();
            tile[63].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle/4.png"));
            
            tile[64] = new Tile();
            tile[64].image = ImageIO.read(getClass().getResourceAsStream("/tiles/puddle/5.png"));


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

                    // If the map says '2', randomly pick one of the new grass variants
                    if (value == 1) {
                        int randomVariant = gp.random.nextInt(2); // 0..2
                        value = 10 + randomVariant; // shift to tile index 20-23
                        gp.occupiedTiles[col][row] = true;
                    }
                    
                    if (value == 2) {
                        int randomVariant = gp.random.nextInt(3); // 0..2
                        value = 20 + randomVariant; // shift to tile index 20-23
                    }
                    
                    if (value == 3) {
                        int randomVariant = gp.random.nextInt(3); // 0..2
                        value = 30 + randomVariant; // shift to tile index 30-32
                        gp.occupiedTiles[col][row] = true;
                    }
                    if (value == 4){
                        gp.occupiedTiles[col][row] = true;
                    }
                    if (value == 5){
                        gp.occupiedTiles[col][row] = true;
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

    public boolean generatePath(){ // builds a set of waypoints that enemies can navagate
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
        
        int row      = 0;   int col      = 0;       
        int nextRow  = 0;   int nextCol  = 0;
        int leftRow  = 0;   int leftCol  = 0;       
        int rightRow = 0;   int rightCol = 0;
        
        //LinkedList enemyRoute = new LinkedList(); 
        
        // Find begining of path
        for (row = 0; row < gp.MAXSCREENROW; row++) {
            if(isPath(row, 0) == true){
                enemyRoute.add(row);
                // Print Waypoint for debug
                System.out.print('('); System.out.print(row); System.out.print(','); System.out.print(col); System.out.print(") \n");
                
                break;
            }
            
        }
        //create spawn-point
         
        //Generate rest of map
        boolean areWeThereYet = false;
        int loopCounter = 0;
        while (areWeThereYet == false){
            loopCounter++;
            if(loopCounter  > 500) {
                System.out.println("Path incomplete due to timeout! \n     Check for cycle");
                return false;
            }
            
            //move one tile in current direction of travel
           switch (currentDirection) {
               
               case 5: //overflow into NORTH
                   currentDirection = NORTH;
               case NORTH:
                   //System.out.println("Northward!");
                   row--;
                   nextRow  = row - 1;
                   leftRow  = row;
                   rightRow = row;
                   
                   nextCol  = col;
                   leftCol  = col - 1;
                   rightCol = col + 1;
                   //------------
                   
                   break;
                   
               case EAST:
                   //System.out.println("Eastward!");
                   col++;
                   nextCol = col + 1;
                   leftCol  = col;
                   rightCol = col;
                   
                   nextRow  = row;
                   leftRow  = row - 1;
                   rightRow = row + 1;
                   break;
                   
               case SOUTH:
                   //System.out.println("Southbound!");
                   row++;
                   nextRow  = row + 1;
                   leftRow  = row;
                   rightRow = row;
                   
                   nextCol  = col;
                   leftCol  = col + 1;
                   rightCol = col - 1;
                   break;
                   
               case 0: //underflow into WEST
                   currentDirection = WEST;
               case WEST:
                  // System.out.println("Westward!");
                   col--;
                   nextCol = col - 1;
                   leftCol  = col;
                   rightCol = col;
                   
                   nextRow  = row;
                   leftRow  = row + 1;
                   rightRow = row - 1;
                   break;
           }
           //check for edge of map (will try to get rid of this)
           int isDestination = 0;
           if(row >= gp.MAXSCREENROW - 1 || col >= gp.MAXSCREENCOL - 1) {
               isDestination = 1; //accept edge of map as destination
           }
           else if(mapTileNum [nextRow][nextCol] == DESTINATION){
               isDestination = 1;
           }
           
           //Evaluate turns and stubs
           if(isPath(nextRow, nextCol) == false) { //Path cannot continue streight 
                if(isDestination == 1) { 
                    areWeThereYet = true;
                    currentDirection++; //this is a silly way to ensure waypoint logic works as intended
                }
                else if(isPath(leftRow, leftCol) == true) { //Path shall bend to the left
                    currentDirection--;
                }
                else if(isPath(rightRow, rightCol) == true) { //Path shall bend to the right
                    currentDirection++;
                }
                else { //Dead-end?
                    areWeThereYet = true; // give-up and don't backtrack
                    currentDirection++; //this is a silly way to ensure waypoint logic works as intended
                    
                }
                
                //Add waypoint to list
                if(currentDirection % 2 == 0) { //An Even dir post imcrement/decrement => previously traveling vertically
                    enemyRoute.add(-row);                    
                }
                else { //Odd dir => previously traveling horizontially 
                    enemyRoute.add(col);
                }
                // Print Waypoint for debug
                System.out.print('('); System.out.print(row); System.out.print(','); System.out.print(col); System.out.print(") \n");
            }
            
        }
        
        System.out.println("Path generated!");
        return true;    
    }
      
    public boolean isPath (int row, int col){
        //all tile codes repersenting paths
        final int PATH_SAND  = -1;
        final int PATH_GRASS = 5;
        
        //read in tile
        if (row >= gp.MAXSCREENROW || col >= gp.MAXSCREENCOL) {
            //System.out.print("Out Of Bounds"); System.out.print(row); System.out.print(", "); System.out.print(col);
            return false; //out of bounds tile cannot be path
        }
        int intarigatedTile = mapTileNum[row][col];
        //System.out.print(intarigatedTile);System.out.print(", ");
        if(intarigatedTile > 9){
            intarigatedTile = (intarigatedTile/10); //alows for path tile variations such as 50 or 45
            // Print Waypoint for debug
                //System.out.println(intarigatedTile);
        }
        //check file against known path codes
        if(intarigatedTile == PATH_SAND || intarigatedTile == PATH_GRASS) {
            //System.out.println("Path!");
            return true;
        }
        else {
            //System.out.println("Not Path!");
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
                    
                    // Draw normal tile
                    g2.drawImage(tile[tileNum].image, screenX, screenY, 
                                     gp.TILESIZE, gp.TILESIZE, null);
                    
                    // Check for path block
                    if (tileNum == 5) {
                        PathVariant variant = getPathVariant(row, col);
                        // Save current transform
                        AffineTransform old = g2.getTransform();

                        // Apply rotation if needed.
                        // We rotate about the center of the tile.
                        g2.rotate(Math.toRadians(variant.rotation), 
                                  screenX + gp.TILESIZE/2, screenY + gp.TILESIZE/2);

                        // Draw the selected variant tile image
                        g2.drawImage(tile[variant.tileIndex].image, screenX, screenY, 
                                     gp.TILESIZE, gp.TILESIZE, null);

                        // Restore transform
                        g2.setTransform(old);
                    }
                    
                    if (tileNum == 6){
                        int animatedFrame = Math.floorDiv(gp.getFrame(), 12);
                        if (animatedFrame == 5){
                            animatedFrame = 4;
                        }
                        //System.out.println(60+animatedFrame);
                        g2.drawImage(tile[60 + animatedFrame].image, screenX, screenY, 
                                     gp.TILESIZE, gp.TILESIZE, null);
                    }
                }
            }
        }
        /*
        
        ############################################################
            Debug to show waypoints created by the path function
        ############################################################
        
        //Draw blue squares at each waypoint using enemyRoute
        g2.setColor(Color.BLUE);

        if (!enemyRoute.isEmpty()) {
            // The starting waypoint is (first element as row, 0 as col)
            int currRow = ((Integer) enemyRoute.get(0)).intValue();
            int currCol = 0;

            // Draw the starting waypoint
            int screenX = currCol * gp.TILESIZE;
            int screenY = currRow * gp.TILESIZE;
            g2.fillRect(screenX, screenY, gp.TILESIZE, gp.TILESIZE);

            // Process each subsequent waypoint in enemyRoute
            for (int i = 1; i < enemyRoute.size(); i++) {
                int value = ((Integer) enemyRoute.get(i)).intValue();
                if (value < 0) {
                    // Negative value indicates a vertical change (row update)
                    currRow = -value;
                } else {
                    // Positive value indicates a horizontal change (column update)
                    currCol = value;
                }
                screenX = currCol * gp.TILESIZE;
                screenY = currRow * gp.TILESIZE;
                g2.fillRect(screenX, screenY, gp.TILESIZE, gp.TILESIZE);
            }
        }
        */
    }
    
    public class PathVariant {
        public int tileIndex; // index in your Tile array
        public double rotation; // in degrees (or radians) if you want to rotate the image

        public PathVariant(int tileIndex, double rotation) {
            this.tileIndex = tileIndex;
            this.rotation = rotation;
        }
    }

    public PathVariant getPathVariant(int row, int col) {
        // Check neighbors. Ensure you don't go out-of-bounds:
        boolean up = (row > 0 && mapTileNum[row - 1][col] == 5);
        boolean down = (row < gp.MAXSCREENROW - 1 && mapTileNum[row + 1][col] == 5);
        boolean left = (col > 0 && mapTileNum[row][col - 1] == 5);
        boolean right = (col < gp.MAXSCREENCOL - 1 && mapTileNum[row][col + 1] == 5);

        // Example logic:
        if (up && down && left && right) {
            // All directions: Cross
            return new PathVariant(50, 0); // e.g., tile index 60 for a cross variant
        } else if ((left && right) && !(up || down)) {
            // Horizontal straight
            return new PathVariant(51, 0);
        } else if ((up && down) && !(left || right)) {
            // Vertical straight: if your base image is horizontal, rotate 90°
            return new PathVariant(51, 90);
        } else if (up && right) {
            // Corner: for instance, the image drawn as a corner facing top and right
            return new PathVariant(52, 0);
        } else if (right && down) {
            // Rotate the corner image by 90° to match right and down
            return new PathVariant(52, 90);
        } else if (down && left) {
            // Rotate by 180°
            return new PathVariant(52, 180);
        } else if (left && up) {
            // Rotate by 270°
            return new PathVariant(52, 270);
        }

        // Default: if it doesn’t match any pattern, return a generic path block
        return new PathVariant(5, 0);
    }

}
