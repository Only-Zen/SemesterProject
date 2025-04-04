/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package button;

import java.awt.Graphics2D;
import main.Coordinate;

/**
 *
 * @author mrsch
 */
public abstract class Button {
    protected int buttonX;
    protected int buttonY;
    protected int buttonWidth;
    protected int buttonHeight;
    
    public Button(int buttonX, int buttonY, int buttonWidth, int buttonHeight){
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
    }
    
    public boolean isClicked(Coordinate coord){
        return coord.getX() > buttonX && 
                coord.getX() < buttonX + buttonWidth &&
                coord.getY() > buttonY &&
                coord.getY() < buttonY + buttonHeight;
    }
    
    public abstract void draw(Graphics2D g2);
    public abstract void update();
}
