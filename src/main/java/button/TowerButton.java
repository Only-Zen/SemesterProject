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
public class TowerButton extends Button {
    
    private int towerId;
    private BufferedImage towerImage;
    private String towerIcon;
    
    public TowerButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight, int towerId) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        this.towerId = towerId; 
        
        switch (this.towerId){
            default:
                towerIcon = "/icons/basicTowerIcon.png";
                break;
            case 1:
                towerIcon = "/icons/basicTowerIcon.png";
                break;
            case 2:
                towerIcon = "/icons/bomberTowerIcon.png";
                break;
            case 3:
                towerIcon = "/icons/basicTowerIcon.png";
                break;
        }
        
        try {
            towerImage = ImageIO.read(getClass().getResourceAsStream(towerIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
        //
    }
    
    public void draw(Graphics2D g2, int id){
        //
        if(id == towerId){
            g2.drawImage(towerImage, buttonX, buttonY, 
                                     buttonWidth, buttonHeight, null);
        }
        else{
            g2.drawImage(towerImage, buttonX, buttonY, 
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
