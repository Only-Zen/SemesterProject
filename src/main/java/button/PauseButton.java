/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package button;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author mrsch
 */
public class PauseButton extends Button {
    
    public PauseButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
    }
    
    public void draw(Graphics2D g2){
        //
        g2.setColor(Color.RED);
        g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
    }
    
    public void update(){
        //
    }
    
}
