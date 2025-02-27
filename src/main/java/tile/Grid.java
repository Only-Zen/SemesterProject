package tile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

            // Original tile index 3 (generic tree)
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[3].occupied = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/rock.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grasspath.png"));
            
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
                }
            }
        }
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
