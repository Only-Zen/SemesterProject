package main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * @author lmm0060
 */
public class MenuHandler implements ActionListener {
    GamePanel gp;
    
    public MenuHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        
        switch(action){
            case "Play":
                System.out.println("Continue");
                gp.isPaused = false;
                break;
            case "Save":
                System.out.println("Save Game");
                // Save game logic
                break;
            case "Exit":
                System.out.println("Exit");
                //Exit game logic
                break;
        }
    }
}
