package button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A button that represents an auto-start (or autoplay) toggle.
 * Displays different icons based on whether autoplay is enabled or disabled.
 * 
 * This button relies on two images: one for the "on" state and one for the "off" state,
 * both of which are expected to be found in the `/icons/` directory of the classpath.
 * 
 * Note: The {@code draw(Graphics2D)} method is left empty because this button requires a boolean flag
 * to determine its visual state. Use {@code draw(Graphics2D g2, boolean autoPlay)} instead.
 * 
 * @author mrsch
 */
public class AutoStartButton extends Button {
    private BufferedImage autoPlayImageOn;
    private BufferedImage autoPlayImageOff;

    
    /**
     * Constructs a new AutoStartButton with the specified position and size.
     * Loads the images for the "on" and "off" states.
     *
     * @param buttonX The x-coordinate of the button's top-left corner.
     * @param buttonY The y-coordinate of the button's top-left corner.
     * @param buttonWidth The width of the button.
     * @param buttonHeight The height of the button.
     */
    public AutoStartButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            autoPlayImageOn = ImageIO.read(getClass().getResourceAsStream("/icons/autoPlayOn.png"));
            autoPlayImageOff = ImageIO.read(getClass().getResourceAsStream("/icons/autoPlayOff.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void draw(Graphics2D g2){
        //
    };
    
    /**
     * Draws the button based on the current autoplay state.
     * Displays the appropriate icon and applies a dimming overlay when autoplay is off.
     *
     * @param g2 The graphics context used for rendering.
     * @param autoPlay Indicates whether autoplay is currently enabled.
     */
    public void draw(Graphics2D g2, boolean autoPlay){
        //
        if (autoPlay == true){
            g2.drawImage(autoPlayImageOn, buttonX, buttonY, 
                     buttonWidth, buttonHeight, null);
        } else {
            g2.drawImage(autoPlayImageOff, buttonX, buttonY, 
                         buttonWidth, buttonHeight, null);
            g2.setColor(new Color(0, 0, 0, 102));
            g2.fillRect(buttonX, buttonY, 
                        buttonWidth, buttonHeight);
        }
    }
}