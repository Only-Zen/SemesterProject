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
public class StartButton extends Button {

    public StartButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
    }
    
    public void draw(Graphics2D g2){
        //
    }
    
    public void draw(Graphics2D g2, boolean roundGoing){
        //
        if(roundGoing){
            g2.setColor(Color.GRAY);
        }
        else{
            g2.setColor(Color.GREEN);
        }
        g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
    }
    
    public void update(){
        //
    }
}
