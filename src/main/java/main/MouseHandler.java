package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import entity.Enemy;
import entity.Projectile;
import tower.Tower;

import tile.Grid; //For identifying the pause button. If you know a more elegant way of doing this let me know.

public class MouseHandler extends MouseAdapter implements MouseMotionListener {

    // Use Coordinate objects for the mouse's pixel location and its tile location
    private Coordinate mouseCoordinate;
    private Coordinate tileCoordinate;
    GamePanel gp;
    private final Grid grid;
    
    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        mouseCoordinate = new Coordinate(0, 0, gp);
        tileCoordinate = mouseCoordinate.getGrid();
        grid = new Grid(gp);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update the mouse coordinate using your Coordinate class
        mouseCoordinate.setX(e.getX());
        mouseCoordinate.setY(e.getY());
        // Calculate the tile coordinate based on the current mouse position and tile size
        tileCoordinate = mouseCoordinate.getGrid();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gp.isPaused){
            if (gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] == true){
                if (e.getButton() == MouseEvent.BUTTON3) { // Right-click to place an enemy
                    Enemy newEnemy = new Enemy(
                        tileCoordinate,
                        4, 100, gp);
                    gp.enemies.add(newEnemy);
                    System.out.println("Enemy placed");
                }
            }

            // Middle-click: fire a projectile if there is at least one enemy
            else if (e.getButton() == MouseEvent.BUTTON2) {
                if (!gp.enemies.isEmpty()) {
                    Projectile newProjectile = new Projectile(
                        new Coordinate(e.getX(), e.getY(), gp),
                        gp.enemies.get(0).getPosition(), // target from first enemy's position
                        5,  // projectile speed
                        25, // projectile damage
                        gp
                    );
                    gp.projectile.add(newProjectile);
                    System.out.println("Projectile fired!");
                } else {
                    System.out.println("No enemy available to target!");
                }
            }
            // Left-click: place a tower
            if (e.getButton() == MouseEvent.BUTTON1) {
                //If pause button is clicked, then pause the game
                System.out.println(mouseCoordinate);
                if ((grid.mapTileNum[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE]) == 70){
                    System.out.println("Pause button clicked.");
                    gp.isPaused = true;
                }
                else if(mouseCoordinate.getX() > gp.info.PLAY_BUTTON_X &&
                        mouseCoordinate.getX() < gp.info.PLAY_BUTTON_X + gp.info.PLAY_BUTTON_WIDTH &&
                        mouseCoordinate.getY() > gp.info.PLAY_BUTTON_Y &&
                        mouseCoordinate.getY() < gp.info.PLAY_BUTTON_Y + gp.info.PLAY_BUTTON_HEIGHT){
                    //
                    gp.info.startRound();
                }
                else if(gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] == false){
                    // Create a new Tower instance with desired parameters.
                    // (For example, here range = 100, damage = 10, firerate = 1, cooldownTimer = 0)
                    if (gp.info.playerMoney >= 75){
                        Tower newTower = new Tower(tileCoordinate, 150, 15, 3, 0.0, gp);
                        gp.towers.add(newTower);
                        gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] = true;
                        System.out.println("Tower placed!");
                        gp.info.playerMoney -= 75;
                        }
                }
                System.out.println(gp.info.isRoundGoing);
            }
        }
    }
    
    @Override public void mouseDragged(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    // Getter methods to expose the mouse and tile coordinates
    public Coordinate getMouseCoordinate() {
        return mouseCoordinate;
    }

    public Coordinate getTileCoordinate() {
        return tileCoordinate;
    }
}
