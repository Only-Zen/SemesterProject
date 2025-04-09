package button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author mrsch
 */
public class AutoStartButton extends Button {
    private BufferedImage autoPlayImageOn;
    private BufferedImage autoPlayImageOff;

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
    
    };
    
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
    
    public void update(){
        //
    }
}
