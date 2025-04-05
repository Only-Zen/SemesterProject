package button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author mrsch
 */
public class PauseButton extends Button {
    private BufferedImage pauseImage;
    
    public PauseButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            pauseImage = ImageIO.read(getClass().getResourceAsStream("/icons/pause.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
        //
        g2.drawImage(pauseImage, buttonX, buttonY, 
                                     buttonWidth, buttonHeight, null);
    }
    
    public void update(){
        //
    }
    
}
