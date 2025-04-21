package button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents a speed-up button in the UI, typically used to increase game speed or enable fast-forward.
 * 
 * The button displays a different image based on whether the speed-up (auto-play) mode is active.
 * The images are expected to be located at:
 * <ul>
 *   <li>{@code /icons/speedUpOn.png} – for the active (on) state</li>
 *   <li>{@code /icons/speedUpOff.png} – for the inactive (off) state</li>
 * </ul>
 * 
 * If the speed-up mode is off, a translucent overlay is drawn over the image to visually indicate its disabled state.
 * 
 * @author mrsch
 */
public class SpeedUpButton extends Button {
    private BufferedImage speedUpImageOn;
    private BufferedImage speedUpImageOff;

    /**
     * Constructs a SpeedUpButton with the given position and size.
     * Loads the image resources for both enabled and disabled states.
     *
     * @param buttonX      The x-coordinate of the button's top-left corner.
     * @param buttonY      The y-coordinate of the button's top-left corner.
     * @param buttonWidth  The width of the button.
     * @param buttonHeight The height of the button.
     */
    public SpeedUpButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            speedUpImageOn = ImageIO.read(getClass().getResourceAsStream("/icons/speedUpOn.png"));
            speedUpImageOff = ImageIO.read(getClass().getResourceAsStream("/icons/speedUpOff.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Placeholder method required by the {@link Button} superclass.
     * This method is currently not used because this button relies on a parameterized draw method.
     *
     * @param g2 The graphics context used for rendering.
     */
    @Override
    public void draw(Graphics2D g2){
        //
    };
    
    /**
     * Draws the speed-up button based on whether auto-play (speed-up) is enabled.
     * If auto-play is disabled, a dark overlay is drawn to visually indicate this.
     *
     * @param g2        The graphics context used for rendering.
     * @param autoPlay  {@code true} if speed-up mode is active, {@code false} otherwise.
     */
    public void draw(Graphics2D g2, boolean autoPlay){
        //
        if (autoPlay == true){
            g2.drawImage(speedUpImageOn, buttonX, buttonY, 
                     buttonWidth, buttonHeight, null);
        } else {
            g2.drawImage(speedUpImageOff, buttonX, buttonY, 
                         buttonWidth, buttonHeight, null);
            g2.setColor(new Color(0, 0, 0, 102));
            g2.fillRect(buttonX, buttonY, 
                        buttonWidth, buttonHeight);
        }
    }
}
