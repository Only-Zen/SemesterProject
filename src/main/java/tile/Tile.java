package tile;

import java.awt.image.BufferedImage;

/**
 * Represents a single grid cell (tile) in the game world.
 * <p>
 * Each tile holds its visual representation as a {@link BufferedImage}
 * and tracks whether it is currently occupied (e.g., by a tower or obstacle).
 * </p>
 */
public class Tile {
    /**
     * The image used to render this tile.
     */
    public BufferedImage image;

    /**
     * Indicates whether this tile is occupied.
     * <p>
     * When {@code true}, no new object (tower, unit, etc.) can be placed on this tile.
     * </p>
     */
    public boolean occupied = false;
}
