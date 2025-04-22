package button;

import java.awt.Graphics2D;
import main.Coordinate;

/**
 * An abstract representation of a clickable UI button.
 * Contains basic geometry and click detection logic.
 * Subclasses are responsible for implementing rendering behavior.
 * 
 * @author mrsch
 */
public abstract class Button {
    /** The x-coordinate of the top-left corner of the button. */
    protected int buttonX;

    /** The y-coordinate of the top-left corner of the button. */
    protected int buttonY;

    /** The width of the button. */
    protected int buttonWidth;

    /** The height of the button. */
    protected int buttonHeight;

    /**
     * Constructs a new Button with specified position and size.
     *
     * @param buttonX The x-coordinate of the button's top-left corner.
     * @param buttonY The y-coordinate of the button's top-left corner.
     * @param buttonWidth The width of the button.
     * @param buttonHeight The height of the button.
     */
    public Button(int buttonX, int buttonY, int buttonWidth, int buttonHeight){
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
    }
    
    /**
     * Determines whether the given coordinate is within the bounds of the button.
     *
     * @param coord The coordinate to test.
     * @return {@code true} if the coordinate is inside the button's area; {@code false} otherwise.
     */
    public boolean isClicked(Coordinate coord){
        return coord.getX() > buttonX && 
                coord.getX() < buttonX + buttonWidth &&
                coord.getY() > buttonY &&
                coord.getY() < buttonY + buttonHeight;
    }
    
    /**
     * Draws the button on the provided Graphics2D context.
     * This method must be implemented by subclasses to define how the button appears.
     *
     * @param g2 The graphics context to draw the button on.
     */
    public abstract void draw(Graphics2D g2);
}