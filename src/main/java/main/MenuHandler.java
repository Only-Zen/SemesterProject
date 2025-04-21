package main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MenuHandler implements ActionListener {
    private GamePanel gp;
    private TitleScreen title;
    private LevelSelect levelSelect;

    public boolean triggerReadFromDisk = false; // Used to prompt loading of savegame elements inside the game loop

    protected String mapFilepath = "maps/map.txt";
    protected String saveFilepath = "save.txt";

    // Constructor for TitleScreen actions
    public MenuHandler(TitleScreen title){
        this.title = title;
    }
    
    // Constructor for LevelSelect actions
    public MenuHandler(LevelSelect levelSelect) {
        this.levelSelect = levelSelect;
    }
    
    // Constructor for GamePanel (pause menu) actions
    public MenuHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        
        switch(action){
            // TitleScreen actions
            case "Start":
                if (title != null) {
                    System.out.println("Start Game - opening Level Select screen");
                    // Create a new LevelSelect panel using the same window and pass the current sound instance.
                    LevelSelect newLevelSelect = new LevelSelect(title.window, title.sound);
                    // Remove the current title screen from its parent container.
                    title.getParent().remove(title);
                    // (Do not stop the music here so that it continues into the level select screen.)
                    // Add the level select screen to the window.
                    title.window.add(newLevelSelect);
                    title.window.revalidate();
                    title.window.repaint();
                }
                break;
            case "Load":
                if (title != null) {
                    System.out.println("Load Game");
                    // Read the map location from the save file
                    String loadedMapLocation = readMapLocation(saveFilepath);
                    System.out.println("Loaded Map Location: " + loadedMapLocation);
                    // Set the title's map location or default
                    title.mapToLoad = loadedMapLocation.isEmpty() ? "/maps/map.txt" : loadedMapLocation;
                    triggerReadFromDisk = true;
                    // The StartGame() call stops the music.
                    title.StartGame();
                }
                break;

            // Pause menu actions for GamePanel
            case "Play":
                if (gp != null) {
                    System.out.println("Continue");
                    gp.isPaused = false;
                }
                break;
            case "Save":
                if (gp != null) {
                    System.out.println("Save Game");
                    gp.writeToDisk(saveFilepath, gp.mapLocation);
                }
                break;
            case "Exit":
                System.out.println("Exit");
                System.exit(0);
                break;
            
            // LevelSelect actions
            case "Map1":
                if (levelSelect != null) {
                    System.out.println("Selected Map 1.");
                    levelSelect.setMapToLoad("/maps/map.txt");
                    levelSelect.startGame(); // This method should stop the sound
                }
                break;
            case "Map2":
                if (levelSelect != null) {
                    System.out.println("Selected Map 2.");
                    levelSelect.setMapToLoad("/maps/map2.txt");
                    levelSelect.startGame();
                }
                break;
            case "Map3":
                if (levelSelect != null) {
                    System.out.println("Selected Map 3.");
                    levelSelect.setMapToLoad("/maps/map3.txt");
                    levelSelect.startGame();
                }
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
