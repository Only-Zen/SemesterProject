package button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A UI button that represents a specific tower selection in a tower defense game.
 * 
 * This button displays a tower icon corresponding to a tower ID. When selected, the icon is drawn normally;
 * otherwise, a semi-transparent overlay is drawn to indicate it is not currently selected.
 * 
 * Supported tower IDs:
 * <ul>
 *   <li>1 - Basic Tower</li>
 *   <li>2 - Bomber Tower</li>
 *   <li>3 - Rapid Tower</li>
 * </ul>
 * 
 * Any other tower ID will default to the Basic Tower icon.
 * 
 * @author mrsch
 */
public class TowerButton extends Button {
    
    private int towerId;
    private BufferedImage towerImage;
    private String towerIcon;
    
    /**
     * Constructs a TowerButton with the specified dimensions and associated tower ID.
     *
     * @param buttonX      The x-coordinate of the button.
     * @param buttonY      The y-coordinate of the button.
     * @param buttonWidth  The width of the button.
     * @param buttonHeight The height of the button.
     * @param towerId      The ID of the tower this button represents.
     */
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
                towerIcon = "/icons/rapidTowerIcon.png";
                break;
        }
        
        try {
            towerImage = ImageIO.read(getClass().getResourceAsStream(towerIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Placeholder method required by the {@link Button} superclass.
     * This version of draw is unused, as this button depends on a parameterized version.
     *
     * @param g2 The graphics context used for rendering.
     */
    public void draw(Graphics2D g2){
        //
    }
    
    /**
     * Draws the tower button.
     * If the given ID matches this tower's ID, the button is shown fully.
     * Otherwise, a semi-transparent overlay indicates it is unselected.
     *
     * @param g2       The graphics context used for rendering.
     * @param id       The ID of the currently selected tower.
     */
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
}
