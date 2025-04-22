package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import tower.*;
import tile.Grid;

/**
 * Listens for mouse movements and clicks on the game panel,
 * translates pixel coordinates into tile coordinates, 
 * handles UI button interactions, and places towers on valid tiles.
 */
public class MouseHandler extends MouseAdapter implements MouseMotionListener {

    /**
     * Current mouse position in pixel coordinates.
     */
    private Coordinate mouseCoordinate;

    /**
     * Current mouse position snapped to the tile grid.
     */
    private Coordinate tileCoordinate;

    /**
     * Reference to the main game panel for accessing game state and settings.
     */
    private final GamePanel gp;

    /**
     * Grid helper used for additional grid-based calculations (if needed).
     */
    private final Grid grid;

    /**
     * Constructs a MouseHandler for the given game panel.
     *
     * @param gp the {@link GamePanel} this handler will control
     */
    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        this.mouseCoordinate = new Coordinate(0, 0, gp);
        this.tileCoordinate = mouseCoordinate.getGrid();
        this.grid = new Grid(gp);
    }

    /**
     * Invoked when the mouse is moved over the game panel.
     * Updates both the raw pixel coordinate and the corresponding grid coordinate,
     * and updates which tower button (if any) the cursor is hovering over.
     *
     * @param e the mouse event containing the new cursor position
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseCoordinate.setX(e.getX());
        mouseCoordinate.setY(e.getY());
        tileCoordinate = mouseCoordinate.getGrid();

        if (gp.info.basicTowerButton.isClicked(mouseCoordinate)) {
            gp.info.setTowerHoveredOver(1);
        } else if (gp.info.bomberTowerButton.isClicked(mouseCoordinate)) {
            gp.info.setTowerHoveredOver(2);
        } else if (gp.info.rapidTowerButton.isClicked(mouseCoordinate)) {
            gp.info.setTowerHoveredOver(3);
        } else {
            gp.info.setTowerHoveredOver(0);
        }
    }

    /**
     * Invoked when a mouse button is pressed.
     * If the left button is clicked and the game is not paused, this will:
     * <ul>
     *   <li>Toggle pause state when the pause button is clicked.</li>
     *   <li>Start a new round if the start button is clicked.</li>
     *   <li>Select a tower type if a tower‐button is clicked.</li>
     *   <li>Place a tower on the grid if the tile is free and the player has enough money.</li>
     *   <li>Toggle auto‐play or speed‐up if those buttons are clicked.</li>
     * </ul>
     *
     * @param e the mouse event containing button and position info
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!gp.isPaused && e.getButton() == MouseEvent.BUTTON1) {
            if (gp.info.pauseButton.isClicked(mouseCoordinate)) {
                gp.isPaused = true;
            } else if (gp.info.startButton.isClicked(mouseCoordinate)) {
                gp.info.startRound();
            } else if (gp.info.basicTowerButton.isClicked(mouseCoordinate)) {
                gp.info.setTowerInHand(1);
            } else if (gp.info.bomberTowerButton.isClicked(mouseCoordinate)) {
                gp.info.setTowerInHand(2);
            } else if (gp.info.rapidTowerButton.isClicked(mouseCoordinate)) {
                gp.info.setTowerInHand(3);
            } else if (gp.info.autoStartButton.isClicked(mouseCoordinate)) {
                gp.info.toggleAutoPlay();
            } else if (!checkIfOccupied()) {
                Tower newTower;
                switch (gp.info.getTowerInHand()) {
                    case 2: newTower = new BomberTower(tileCoordinate, gp); break;
                    case 3: newTower = new RapidTower(tileCoordinate, gp); break;
                    default: newTower = new BasicTower(tileCoordinate, gp); break;
                }
                if (gp.info.getPlayerMoney() >= newTower.getCost()) {
                    gp.towers.add(newTower);
                    int tx = tileCoordinate.getX() / gp.TILESIZE;
                    int ty = tileCoordinate.getY() / gp.TILESIZE;
                    gp.occupiedTiles[tx][ty] = true;
                    gp.info.spendMoney(newTower.getCost());
                }
            }
        }
    }

    /** Unused but required by MouseMotionListener. */
    @Override public void mouseDragged(MouseEvent e) { }

    /** Unused but required by MouseAdapter. */
    @Override public void mouseReleased(MouseEvent e) { }

    /** Unused but required by MouseAdapter. */
    @Override public void mouseEntered(MouseEvent e) { }

    /** Unused but required by MouseAdapter. */
    @Override public void mouseExited(MouseEvent e) { }

    /**
     * Returns the current mouse position in pixel coordinates.
     *
     * @return the {@link Coordinate} of the mouse
     */
    public Coordinate getMouseCoordinate() {
        return mouseCoordinate;
    }

    /**
     * Returns the current mouse position snapped to the tile grid.
     *
     * @return the {@link Coordinate} of the tile under the mouse
     */
    public Coordinate getTileCoordinate() {
        return tileCoordinate;
    }

    /**
     * Checks whether the tile under the current mouse position is already occupied by a tower.
     *
     * @return {@code true} if the tile is occupied; {@code false} otherwise
     */
    public boolean checkIfOccupied() {
        int tx = tileCoordinate.getX() / gp.TILESIZE;
        int ty = tileCoordinate.getY() / gp.TILESIZE;
        return gp.occupiedTiles[tx][ty];
    }
}