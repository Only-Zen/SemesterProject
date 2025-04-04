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
public class TowerButton extends Button {
    
    private int towerId;
    
    public TowerButton(int buttonX, int buttonY, int buttonWidth, int buttonHeight, int towerId) {
        super(buttonX, buttonY, buttonWidth, buttonHeight);
        this.towerId = towerId;
    }
    
    public void draw(Graphics2D g2){
        //
    }
    
    public void draw(Graphics2D g2, int id){
        //
        if(id == towerId){
            g2.setColor(Color.RED);
        }
        else{
            g2.setColor(Color.BLUE);
        }
        g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
    }
    
    public void update(){
        //
    }
}
