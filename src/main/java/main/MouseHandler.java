package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import entity.Enemy;
import entity.Projectile;
import tower.Tower;

public class MouseHandler extends MouseAdapter implements MouseMotionListener {

    // Use Coordinate objects for the mouse's pixel location and its tile location
    private Coordinate mouseCoordinate;
    private Coordinate tileCoordinate;
    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        mouseCoordinate = new Coordinate(0, 0, gp);
        tileCoordinate = mouseCoordinate.getGrid();
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
        if (gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] == true){
            if (e.getButton() == MouseEvent.BUTTON3) { // Right-click to place an enemy
                Enemy newEnemy = new Enemy(
                    tileCoordinate,
                    4, 100, gp);
                gp.enemies.add(newEnemy);
                System.out.println("Enemy placed");
            }
            return;
        }
        
        // Left-click: fire a projectile if there is at least one enemy
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
        // Middle-click: place a tower
        else if (e.getButton() == MouseEvent.BUTTON1) {
            // Create a new Tower instance with desired parameters.
            // (For example, here range = 100, damage = 10, firerate = 1, cooldownTimer = 0)
            Tower newTower = new Tower(tileCoordinate, 150, 20, 3, 0.0, gp);
            gp.towers.add(newTower);
            gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] = true;
            System.out.println("Tower placed!");
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
