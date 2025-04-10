package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import tower.*;

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
        
        if(gp.info.basicTowerButton.isClicked(mouseCoordinate)){
            gp.info.towerHoveredOver = 1;
        }
        else if(gp.info.bomberTowerButton.isClicked(mouseCoordinate)){
            gp.info.towerHoveredOver = 2;
        }
        else if(gp.info.rapidTowerButton.isClicked(mouseCoordinate)){
            gp.info.towerHoveredOver = 3;
        }
        else{
            gp.info.towerHoveredOver = 0;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gp.isPaused){
            // Left-click: place a tower
            if (e.getButton() == MouseEvent.BUTTON1) {
                //If pause button is clicked, then pause the game
                System.out.println(mouseCoordinate + " " + gp.info.pauseButton.isClicked(mouseCoordinate));
                
                if (gp.info.pauseButton.isClicked(mouseCoordinate)){
                    System.out.println("Pause button clicked.");
                    gp.isPaused = true;
                }
                else if(gp.info.startButton.isClicked(mouseCoordinate)){
                    gp.info.startRound();
                }
                else if(gp.info.basicTowerButton.isClicked(mouseCoordinate)){
                    gp.info.towerInHand = 1;
                }
                else if(gp.info.bomberTowerButton.isClicked(mouseCoordinate)){
                    gp.info.towerInHand = 2;
                }
                else if(gp.info.rapidTowerButton.isClicked(mouseCoordinate)){
                    gp.info.towerInHand = 3;
                } else if(gp.info.autoStartButton.isClicked(mouseCoordinate)){
                    gp.info.autoPlay = !gp.info.autoPlay;
                } else if(gp.info.speedUpButton.isClicked(mouseCoordinate)){
                    gp.info.speedUp = !gp.info.speedUp;
                }
                else if(checkIfOccupied() == false){
                    // Create a new Tower instance with desired parameters.
                    // (For example, here range = 100, damage = 10, firerate = 1, cooldownTimer = 0)
                    Tower newTower = new BasicTower(tileCoordinate,gp);
                    switch (gp.info.towerInHand){
                        case 0:
                            newTower = new BasicTower(tileCoordinate,gp);
                            break;
                        case 2:
                            newTower = new BomberTower(tileCoordinate,gp);
                            break;
                        case 3:
                            newTower = new RapidTower(tileCoordinate,gp);
                            break;
                    }
                    if (gp.info.playerMoney >= newTower.getCost()){
                        gp.towers.add(newTower);
                        gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] = true;
                        System.out.println("Tower placed!");
                        gp.info.playerMoney -= newTower.getCost();
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
    public boolean checkIfOccupied(){
        if(gp.occupiedTiles[tileCoordinate.getGrid().getX()/gp.TILESIZE][tileCoordinate.getGrid().getY()/gp.TILESIZE] == true){
            return true;
        } else {
            return false;
        }
    }
}
