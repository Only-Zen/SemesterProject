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

    public boolean triggerReadFromDisk = false; //used to prompt loading of savegame elements inside the game loop

    protected String mapFilepath = "maps/map.txt";
    protected String saveFilepath = "save.txt";

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
                triggerReadFromDisk = false;
                title.StartGame();

                break;
            case "Load":
                System.out.println("Load Game");
                //Load game logic
                triggerReadFromDisk = true;
                title.StartGame();
                break;
            
            //Pause menu actions
            case "Play":
                System.out.println("Continue");
                gp.isPaused = false;
                break;
            case "Save":
                System.out.println("Save Game");
                // Save game logic
                gp.writeToDisk(saveFilepath, mapFilepath);
                break;
            case "Exit":
                System.out.println("Exit");
                System.exit(0);
                //Exit game logic
                break;
            case "map1":
                System.out.println("Selected Map 1.");
                break;
            case "map2":
                System.out.println("Selected Map 2.");
                break;
            case "map3":
                System.out.println("Selected Map 3.");
                break;
        }
    }
}
