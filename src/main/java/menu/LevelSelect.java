package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import main.GamePanel;
import main.Sound;

/**
 * Class that handles the graphics of the level selection screen.
 */

public class LevelSelect extends JPanel {
    
    // Constants for dimensions
    private final int TITLEWIDTH = 1536;
    private final int TITLEHEIGHT = 768;
    
    // Reference to the main window
    public JFrame window;
    
    // Background image for the menu
    private BufferedImage menuImage;
    
    // Panel and buttons for selecting levels
    private JPanel levelSelectPanel;
    private JButton map1Button, map2Button, map3Button;
    
    // Fonts for headers and buttons
    private Font cascadia_header = new Font("Cascadia Code", Font.BOLD, 70);
    private Font cascadia_body = new Font("Cascadia Code", Font.PLAIN, 40);
    
    //Colors for making things (buttons, etc) prettier 
    final Color beige = new Color(244,204,161);
    final Color lightbrown = new Color(160,91,83);
    
    // Field to hold the map location chosen
    private String mapToLoad;
    
    // Sound instance shared from TitleScreen so we can stop it when starting the game
    public Sound sound;
    
    // Menu handler to process actions
    private MenuHandler handler;
    
    // New constructor now accepts the Sound instance as well.
    /**
     * Constructs the level selection menu object and its buttons.
     * @param window The parent window; uses this to remove itself from the window and add a GamePanel object.
     * @param sound Used to ensure all sounds have stopped.
     */
    public LevelSelect(JFrame window, Sound sound) {       
        // Store the window, default map location, and the shared sound instance.
        this.window = window;
        this.mapToLoad = "/maps/map.txt";
        this.sound = sound;
        
        // Load background image
        try {
            menuImage = ImageIO.read(getClass().getResourceAsStream("/icons/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Set up the panel
        setLayout(null);
        setPreferredSize(new Dimension(TITLEWIDTH, TITLEHEIGHT));
        setBackground(beige);
        
        // Create and configure the level select panel
        levelSelectPanel = new JPanel();
        levelSelectPanel.setLayout(new GridLayout(3, 1, 10, 10));
        levelSelectPanel.setBounds(TITLEWIDTH / 4, TITLEHEIGHT / 2, TITLEWIDTH / 2, TITLEHEIGHT / 3);
        levelSelectPanel.setOpaque(false);
        
        // Create level selection buttons
        map1Button = new JButton("A New Beginning");
        map2Button = new JButton("A Drink Worth Having");
        map3Button = new JButton("The Final Hope");
        
        // Set button fonts
        map1Button.setFont(cascadia_body);
        map2Button.setFont(cascadia_body);
        map3Button.setFont(cascadia_body);
        //Make buttons prettier
        map1Button.setBackground(lightbrown);
        map1Button.setForeground(beige);
        map2Button.setBackground(lightbrown);
        map2Button.setForeground(beige);
        map3Button.setBackground(lightbrown);
        map3Button.setForeground(beige);
        
        // Instantiate the MenuHandler with this LevelSelect instance.
        handler = new MenuHandler(this);
        
        // Set action commands and register the handler
        map1Button.setActionCommand("Map1");
        map2Button.setActionCommand("Map2");
        map3Button.setActionCommand("Map3");
        map1Button.addActionListener(handler);
        map2Button.addActionListener(handler);
        map3Button.addActionListener(handler);
        
        // Add buttons to the level select panel
        levelSelectPanel.add(map1Button);
        levelSelectPanel.add(map2Button);
        levelSelectPanel.add(map3Button);
        
        // Add the level select panel to this container
        add(levelSelectPanel);
        
        // Ensure proper display of components
        setVisible(true);
        levelSelectPanel.setVisible(true);
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
    }
    
    // Draw the background and the "Level Select" header text
    /**
     * Draws the background and text of the level selection menu.
     * @param g The graphics component used to draw.
     */
    public void drawBackground(Graphics g) { 
        // Draw the background image
        g.drawImage(menuImage, 0, 0, TITLEWIDTH, TITLEHEIGHT, null);
        
        // Draw header text centered at one-third of the panel's height
        String textToShow = "Level Select";
        g.setColor(Color.BLACK);
        g.setFont(cascadia_header);
        int textLength = (int) g.getFontMetrics().getStringBounds(textToShow, g).getWidth();
        int x = TITLEWIDTH / 2 - textLength / 2;
        int y = TITLEHEIGHT / 3;
        g.drawString(textToShow, x, y);
    }
    
    // Helper method to update the map selection
    /**
     * The menu handler gets the map to be loaded, and this function syncs it with the level select object.
     * @param mapToLoad The map passed by LevelSelect
     */

    public void setMapToLoad(String mapToLoad) {

        this.mapToLoad = mapToLoad;
    }
    
    // Trigger the game startup: stop the music, remove this panel, and add the GamePanel.
    /**
     * Hides the level select menu and begins the game.
     */

    public void startGame() {

        // Debug print before stopping sound
        System.out.println("LevelSelect.startGame() called. Stopping sound...");
        if (sound != null) {
            sound.stop();
        }
        // Remove LevelSelect from the window
        window.getContentPane().remove(this);
        window.revalidate();
        window.repaint();
        
        // Create and add GamePanel
        GamePanel gamePanel = new GamePanel(handler, mapToLoad);
        window.add(gamePanel);
        window.revalidate();
        window.repaint();
        
        // Start game loop or thread
        gamePanel.startGameThread();
    }
}