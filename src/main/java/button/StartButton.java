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
public class StartButton extends Button {
    private BufferedImage playImage;

    public StartButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        try {
            playImage = ImageIO.read(getClass().getResourceAsStream("/icons/playButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
        //
    }
    
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
    
    public void update(){
        //
    }
}
