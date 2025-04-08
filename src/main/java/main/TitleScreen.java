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
import javax.swing.JLayeredPane;

/**
 *
 * @author lmm0060
 */

public class TitleScreen extends JLayeredPane {
    
    Font cascadia_header, cascadia_body;
    
    JFrame window;
    JPanel buttonsPanel;
    JButton startButton, loadButton;
    Sound sound = new Sound();
    private BufferedImage menuImage;
    
    //TitleScreen can be a different size if desired.
    //Right now, it's just the size of GamePanel.
    private final int TITLEWIDTH = 1536;
    private final int TITLEHEIGHT = 768;
    MenuHandler handler;
    
    
    public TitleScreen(JFrame window) {
        //Set up title screen background image
        try {
            menuImage = ImageIO.read(getClass().getResourceAsStream("/icons/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.window = window;
        
        //Instantiate title screen
        setLayout(null);
        this.setPreferredSize(new Dimension(TITLEWIDTH,TITLEHEIGHT));
        this.setBackground(new Color(207,198,184));
        
        //Create buttonsPanel and instantiate
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonsPanel.setBounds(TITLEWIDTH/4, TITLEHEIGHT/2, TITLEWIDTH/2, TITLEHEIGHT/3);
     //   buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        setOpaque(true);
        buttonsPanel.setOpaque(false);
        
        //Create font
        cascadia_header = new Font("Cascadia Code", Font.BOLD, 70);
        cascadia_body = new Font ("Cascadia Code", Font.PLAIN, 40);
        
        //Create buttons
        startButton = new JButton("Start Game");
        loadButton = new JButton("Load Game");
        
        //Give buttons fonts
        startButton.setFont(cascadia_body);
        loadButton.setFont(cascadia_body);
        
        //Create an action listener ===========================================================
        handler = new MenuHandler(this);
        
        //Add action listeners to buttons to detect clicks
        startButton.addActionListener(handler);
        loadButton.addActionListener(handler);
        
        //Set action commands for actionListener to detect
        startButton.setActionCommand("Start");
        loadButton.setActionCommand("Load");
        
        //Add buttons to buttonsPanel
        buttonsPanel.add(startButton);
        buttonsPanel.add(loadButton);
        
        //Add buttonsPanel to frame
        add(buttonsPanel);
        
        //Make title screen visible
        setVisible(true);
        buttonsPanel.setVisible(true);
        revalidate();
        repaint();
        
        //Play title screen music
        playMusic(1, 45);
        sound.loop();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTitleScreen(g);
    }
    
    public void drawTitleScreen(Graphics g) {
        //Draw background image
        g.drawImage(menuImage, 0, 0, TITLEWIDTH, TITLEHEIGHT, null);
      
        //Draw the game's name
        String textToShow = "The Last Pint"; 
        g.setColor(Color.BLACK);
        g.setFont(cascadia_header);
        int textLength = (int)g.getFontMetrics().getStringBounds(textToShow, g).getWidth(); //Used to draw text to the center of the screen
        int x = TITLEWIDTH/2 - textLength/2; int y = TITLEHEIGHT/3;
        g.drawString(textToShow, x, y);
    }
    
    public void StartGame(){
        //Hide title screen
        this.getParent().remove(this);
        this.setVisible(false);
        sound.stop();
        
        //Disable buttons, for good measure
        startButton.setEnabled(false);
        loadButton.setEnabled(false);
        
        //Create GamePanel
        GamePanel gamePanel = new GamePanel(handler);
        window.add(gamePanel);
        window.revalidate();
        window.repaint();
        //Pass flag to load saveGame

        //gamePanel.mapNeedsToBeLoaded = handler.
        //Start game
        gamePanel.startGameThread();
    }
    
    public void playMusic(int i, int volume){
        sound.setFile(i);
        sound.play();
        sound.setVolume(volume);
    }
}
