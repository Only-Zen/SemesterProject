package button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Represents a Start button in the user interface, typically used to begin a new round or game session.
 * 
 * The button displays a static "play" image located at {@code /icons/playButton.png}. 
 * When a round is currently ongoing, a translucent overlay is drawn over the button
 * to visually indicate that it is inactive or locked.
 * 
 * This class extends the {@link Button} abstract class and provides a parameterized 
 * draw method to reflect the button's current state.
 * 
 * @author mrsch
 */
public class StartButton extends Button {
    private BufferedImage playImage;

    /**
     * Constructs a StartButton with specified position and dimensions.
     * Loads the play button image resource.
     *
     * @param buttonX      The x-coordinate of the button's top-left corner.
     * @param buttonY      The y-coordinate of the button's top-left corner.
     * @param buttonWidth  The width of the button.
     * @param buttonHeight The height of the button.
     */
    public StartButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            playImage = ImageIO.read(getClass().getResourceAsStream("/icons/playButton.png"));
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
    }
    
    /**
     * Draws the Start button.
     * If a round is currently ongoing, a translucent dark overlay is drawn to indicate inactivity.
     *
     * @param g2          The graphics context used for rendering.
     * @param roundGoing  {@code true} if a round is currently in progress, {@code false} otherwise.
     */
    public void draw(Graphics2D g2, boolean roundGoing){
        //
        if(roundGoing){
            g2.drawImage(playImage, buttonX, buttonY, 
                                     buttonWidth, buttonHeight, null);
            g2.setColor(new Color(0, 0, 0, 102));
            g2.fillRect(buttonX, buttonY, 
                                     buttonWidth, buttonHeight);
        }
        else{
            g2.drawImage(playImage, buttonX, buttonY, 
                                     buttonWidth, buttonHeight, null);
        }
    }
}