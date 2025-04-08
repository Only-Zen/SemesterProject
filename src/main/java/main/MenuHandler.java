package main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                System.out.println("Start Game - opening Level Select screen");
                // Create a new LevelSelect panel using the same window from the current TitleScreen.
                LevelSelect levelSelect = new LevelSelect(title.window);
                // Remove the current title screen from its parent container.
                title.getParent().remove(title);
                // Add the level select screen to the window.
                title.window.add(levelSelect);
                title.window.revalidate();
                title.window.repaint();
                break;
            case "Load":
                System.out.println("Load Game");
                // Use the readMapLocation function to get the map location from the save file
                String loadedMapLocation = readMapLocation(saveFilepath);
                System.out.println("Loaded Map Location: " + loadedMapLocation);
                // Set the title's map location (mapToLoad) before starting the game:
                title.mapToLoad = loadedMapLocation.isEmpty() ? "/maps/map.txt" : loadedMapLocation;
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
                gp.writeToDisk(saveFilepath, gp.mapLocation);
                break;
            case "Exit":
                System.out.println("Exit");
                System.exit(0);
                //Exit game logic
                break;
            case "Map1":
                System.out.println("Selected Map 1.");
                title.mapToLoad = "/maps/map.txt";
                triggerReadFromDisk = false;
                title.StartGame();
                break;
            case "Map2":
                System.out.println("Selected Map 2.");
                title.mapToLoad = "/maps/map2.txt";
                triggerReadFromDisk = false;
                title.StartGame();
                break;
            case "Map3":
                System.out.println("Selected Map 3.");
                title.mapToLoad = "/maps/map.txt";
                triggerReadFromDisk = false;
                title.StartGame();
                break;
        }
    }
    public String readMapLocation(String filename) {
        String mapLocation = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line starts with "Filename,"
                if (line.startsWith("Filename,")) {
                    String[] fields = line.split(",");
                    if (fields.length > 1) {
                        // fields[1] contains the map location; assign it to mapLocation
                        mapLocation = fields[1].trim();
                    }
                    break; // Exit once the map location is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapLocation;
    }


}
