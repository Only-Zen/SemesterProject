package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
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
    
    //TitleScreen can be a different size if desired.
    //Right now, it's just the size of GamePanel.
    private final int TITLEWIDTH = 1536;
    private final int TITLEHEIGHT = 768;
    
    public TitleScreen(JFrame window) {
        this.window = window;
        
        //Instantiate title screen
        setLayout(null);
        this.setPreferredSize(new Dimension(TITLEWIDTH,TITLEHEIGHT));
        this.setBackground(new Color(207,198,184));
        
        //Create buttonsPanel and instantiate
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonsPanel.setBounds(TITLEWIDTH/4, TITLEHEIGHT/2, TITLEWIDTH/2, TITLEHEIGHT/3);
      
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
        
        //Create an action listener
        MenuHandler handler = new MenuHandler(this);
        
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
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTitleScreen(g);
    }
    
    public void drawTitleScreen(Graphics g) {
        //Draw dark gray rectangle over the entire screen as a backdrop to the menu
        g.setColor(new Color(125,112,113));
        g.fillRect(0, 0, TITLEWIDTH, TITLEHEIGHT);
        
        //Draw a smaller white rectangle over "Paused" text and buttonsPanel
        g.setColor(new Color(207,198,184));
        int x = (TITLEWIDTH/3 * 2); int y = (TITLEHEIGHT/3 * 2);
        g.fillRoundRect(TITLEWIDTH/6, TITLEHEIGHT/5, x, y, 10, 10);
      
        //Draw the game's name
        String textToShow = "RAT TOWER DEFENSE"; 
        g.setColor(Color.BLACK);
        g.setFont(cascadia_header);
        int textLength = (int)g.getFontMetrics().getStringBounds(textToShow, g).getWidth(); //Used to draw text to the center of the screen
        x = TITLEWIDTH/2 - textLength/2; y = TITLEHEIGHT/3;
        g.drawString(textToShow, x, y);
    }
    
    public void StartGame(){
        //Hide title screen
        this.getParent().remove(this);
        this.setVisible(false);
        
        //Disable buttons, for good measure
        startButton.setEnabled(false);
        loadButton.setEnabled(false);
        
        //Create GamePanel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.revalidate();
        window.repaint();
        //Start game
        gamePanel.startGameThread();
    }
}
