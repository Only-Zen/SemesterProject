package button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * A graphical pause button that displays a pause icon.
 * This button loads and renders a static image representing the pause action.
 * 
 * The pause icon image should be located at {@code /icons/pause.png} within the classpath.
 * 
 * @author mrsch
 */
public class PauseButton extends Button {
    private BufferedImage pauseImage;
    
    /**
     * Constructs a new PauseButton with specified position and dimensions.
     * Attempts to load the pause icon image from the resource path.
     *
     * @param buttonX      The x-coordinate of the button's top-left corner.
     * @param buttonY      The y-coordinate of the button's top-left corner.
     * @param buttonWidth  The width of the button.
     * @param buttonHeight The height of the button.
     */
    public PauseButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            pauseImage = ImageIO.read(getClass().getResourceAsStream("/icons/pause.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Renders the pause button on the screen using the loaded pause image.
     *
     * @param g2 The graphics context used for rendering.
     */
    public void draw(Graphics2D g2){
        //
        g2.drawImage(pauseImage, buttonX, buttonY, 
                                     buttonWidth, buttonHeight, null);
    }
}