package main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * @author lmm0060
 */
public class MenuHandler implements ActionListener {
    GamePanel gp;
    TitleScreen title;
    
    public MenuHandler(TitleScreen title){
        this.title = title;
    }
    
    public MenuHandler(GamePanel gp) {
        //Constructor for pause screen
        this.gp = gp;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        
        switch(action){
            //Title screen actions
            case "Start":
                System.out.println("Start Game");
                title.StartGame();
                break;
            case "Load":
                System.out.println("Load Game");
                //Load game logic
                break;
            
            //Pause menu actions
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
                System.exit(0);
                //Exit game logic
                break;
        }
    }
}
