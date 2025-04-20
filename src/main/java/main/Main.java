package main;

import javax.swing.JFrame;

/**
 * The entry point for The Last Pint game
 * <p>
 * Initializes the main application window (Jframe)
 * sets its properties (close operation, resizability, title),
 * instantiates the {@link TitleScreen}, adds it to the window,
 * and makes the window visible centered on screen.
 * </p>
 */
public class Main {
    /**
     * Launches the game by creating and displaying the main window.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create the main game window
        JFrame window = new JFrame();
        
        // Configure game window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("The Last Pint");
        
        // Create and add the Title Screen panel
        TitleScreen title = new TitleScreen(window);
        window.add(title);
        
        // Size the window to fit its contents
        window.pack();
        
        // Centers the window on screen
        window.setLocationRelativeTo(null);
        
        // Display the window
        window.setVisible(true);
    }
}
