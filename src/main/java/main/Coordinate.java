package main;

import java.util.Objects;

/**
 * Represents a position in pixel coordinates within a {@link GamePanel}.
 * <p>
 * Provides utility methods to get and set the raw (x, y) coordinates,
 * to obtain a copy of the coordinate, and to convert to the nearest
 * tile‐aligned grid coordinate based on the panel’s tile size.
 * </p>
 */
public class Coordinate {
    /**
     * The x‐coordinate in pixels.
     */
    private int x;
    
    /**
     * The y‐coordinate in pixels.
     */
    private int y;
    
    /**
     * Reference to the {@link GamePanel}, for querying tile size.
     */
    GamePanel gp;
    
    /**
     * Creates a new {@code Coordinate} at the specified pixel location
     * within the given game panel.
     *
     * @param x  the initial x‐coordinate in pixels
     * @param y  the initial y‐coordinate in pixels
     * @param gp the {@link GamePanel} that provides tile size context
     */
    public Coordinate(int x, int y, GamePanel gp){
        this.x = x;
        this.y = y;
        this.gp = gp;
    }

    /**
     * Sets the x‐coordinate in pixels.
     *
     * @param x the new x‐coordinate in pixels
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * Sets the y‐coordinate in pixels.
     *
     * @param y the new y‐coordinate in pixels
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     * Returns the current x‐coordinate in pixels.
     *
     * @return the x‐coordinate in pixels
     */
    public int getX(){
        return this.x;
    }

    /**
     * Returns the current y‐coordinate in pixels.
     *
     * @return the y‐coordinate in pixels
     */
    public int getY(){
        return this.y;
    }

    /**
     * Returns a new {@code Coordinate} object with the same pixel position
     * and the same {@link GamePanel} reference.
     *
     * @return a copy of this coordinate
     */
    public Coordinate getCoord(){
        return new Coordinate(this.x, this.y, gp);
    }
    
    /**
     * Converts this coordinate to the top‐left corner of the tile in which
     * it lies, by flooring both x and y to the nearest multiple of the
     * panel's {@code TILESIZE}.
     *
     * @return a new {@code Coordinate} aligned to the grid
     */
    public Coordinate getGrid(){
        Coordinate gridPosition = new Coordinate(0, 0, gp);
        gridPosition.setX(Math.floorDiv(x, gp.TILESIZE) * gp.TILESIZE);
        gridPosition.setY(Math.floorDiv(y, gp.TILESIZE) * gp.TILESIZE);
        return gridPosition;
    }

    /**
     * Returns a string representation for debugging, in the form
     * {@code Coord(x=<x>, y=<y>)}.
     *
     * @return a debug‐friendly string of this coordinate
     */
    @Override
    public String toString() {
        return "Coord(x=" + x + ", y=" + y + ")";
    }
}