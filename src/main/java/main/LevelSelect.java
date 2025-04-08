package main;

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

public class LevelSelect extends TitleScreen {

    JFrame window;
    // New panel for level selection buttons
    JPanel levelSelectPanel;
    JButton map1Button, map2Button, map3Button;
    Sound sound = new Sound();
    private BufferedImage menuImage;
    
    private final int TITLEWIDTH = 1536;
    private final int TITLEHEIGHT = 768;
    MenuHandler handler;
    
    // Fonts for headings and buttons
    Font cascadia_header = new Font("Cascadia Code", Font.BOLD, 70);
    Font cascadia_body = new Font("Cascadia Code", Font.PLAIN, 40);
    
    public LevelSelect(JFrame window) {
        // Call superclass constructor to initialize common elements
        super(window);
        this.window = window;
        
        // Remove the inherited buttons panel (with Start Game and Load Game buttons)
        remove(super.buttonsPanel);
        
        // Load background image
        try {
            menuImage = ImageIO.read(getClass().getResourceAsStream("/icons/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Set up the panel properties
        setLayout(null);
        setPreferredSize(new Dimension(TITLEWIDTH, TITLEHEIGHT));
        setBackground(new Color(207, 198, 184));
        
        // Create a new panel for the level select buttons and configure it
        levelSelectPanel = new JPanel();
        levelSelectPanel.setLayout(new GridLayout(3, 1, 10, 10));
        levelSelectPanel.setBounds(TITLEWIDTH / 4, TITLEHEIGHT / 2, TITLEWIDTH / 2, TITLEHEIGHT / 3);
        levelSelectPanel.setOpaque(false);
        
        // Create the map selection buttons
        map1Button = new JButton("Map 1");
        map2Button = new JButton("Map 2");
        map3Button = new JButton("Map 3");
        
        // Set fonts for the buttons
        map1Button.setFont(cascadia_body);
        map2Button.setFont(cascadia_body);
        map3Button.setFont(cascadia_body);
        
        // Instantiate a MenuHandler (this level select screen passes itself)
        handler = new MenuHandler(this);
        
        // Add action listeners and set action commands for each button
        map1Button.addActionListener(handler);
        map2Button.addActionListener(handler);
        map3Button.addActionListener(handler);
        map1Button.setActionCommand("Map1");
        map2Button.setActionCommand("Map2");
        map3Button.setActionCommand("Map3");
        
        // Add buttons to the level select panel
        levelSelectPanel.add(map1Button);
        levelSelectPanel.add(map2Button);
        levelSelectPanel.add(map3Button);
        
        // Add the level select panel to this container
        add(levelSelectPanel);
        
        setVisible(true);
        levelSelectPanel.setVisible(true);
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTitleScreen(g);
    }
    
    @Override
    public void drawTitleScreen(Graphics g) {
        // Draw the background image
        g.drawImage(menuImage, 0, 0, TITLEWIDTH, TITLEHEIGHT, null);
      
        // Draw the "Level Select" title text centered at one-third height
        String textToShow = "Level Select";
        g.setColor(Color.BLACK);
        g.setFont(cascadia_header);
        int textLength = (int) g.getFontMetrics().getStringBounds(textToShow, g).getWidth();
        int x = TITLEWIDTH / 2 - textLength / 2;
        int y = TITLEHEIGHT / 3;
        g.drawString(textToShow, x, y);
    }
}
