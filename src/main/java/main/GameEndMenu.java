package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.GridLayout;

/**
 * Class that handles the graphics of the two game ending menus: game over and a victory.
 * @author lmm0060
 */
public class GameEndMenu extends JLayeredPane {
    GamePanel gp;
    MenuHandler handler;
    
    JPanel buttonsPanel;
    JButton exitButton;
    
    Font cambria_header, cambria_body; 
    
    String textToShow = "Not set";
    
    //Colors to make things (buttons, etc) prettier
    final Color beige = new Color(244,204,161);
    final Color lightbrown = new Color(160,91,83);
    final Color darkbrown = new Color(122,68,74);
    
    public GameEndMenu(GamePanel gp) {
        /**
         * Constructs the game end menu object and its buttons.
         * @param gp Uses GamePanel to get the size of the window and sync game data with the menu handler.
         */
        
        this.gp = gp;
        
        //Instantiate pause menu
        setLayout(null);
        setBounds(0,0,gp.SCREENWIDTH,gp.SCREENHEIGHT);
        
        //Create buttonsPanel and instantiate
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 1, 10, 10));
        buttonsPanel.setBounds(gp.SCREENWIDTH/4, gp.SCREENHEIGHT/2, gp.SCREENWIDTH/2, gp.SCREENHEIGHT/5);
        
        setOpaque(true);
        buttonsPanel.setOpaque(false);
        
        //Create fonts
        cambria_header = new Font ("Cambria", Font.BOLD, 60);
        cambria_body = new Font ("Cambria", Font.PLAIN, 40);
        
        //Create button
        exitButton = new JButton("Close Game");
        
        //Give button font
        exitButton.setFont(cambria_body);
        //Make button prettier
        exitButton.setBackground(darkbrown);
        exitButton.setForeground(beige);
        
        //Create an action listener
        handler = new MenuHandler(gp);
        
        //Add action listeners to button to detect clicks
        exitButton.addActionListener(handler);
        
        //Set action commands for actionListener to detect
        exitButton.setActionCommand("Exit");
        
        //Add button to buttonsPanel
        buttonsPanel.add(exitButton);

        //Add buttonsPanel to frame
        add(buttonsPanel);
        
        revalidate();
        repaint();
    }
    
        @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMenu(g);
    }
    
    public void drawMenu(Graphics g) {
        /**
         * Draws the background and text of the game end menu.
         * @param g The graphics component used to draw.
         */
        
        //Draw a small white rectangle below buttonsPanel
        g.setColor(beige);
        int x = (gp.SCREENWIDTH/3 * 2); int y = (gp.SCREENHEIGHT/3 * 2);
        g.fillRoundRect(gp.SCREENWIDTH/6, gp.SCREENHEIGHT/5, x, y, 10, 10);
        
        if (gp.info.isGameWon == true) {
            textToShow = "The tavern is safe! Congratulations!"; }
        else {
            textToShow = "The rats have seized the tavern!"; }
        
        //Draw the text as set above
        g.setColor(lightbrown);
        g.setFont(cambria_header);
        int textLength = (int)g.getFontMetrics().getStringBounds(textToShow, g).getWidth(); //Used to draw text to the center of the screen
        x = gp.SCREENWIDTH/2 - textLength/2; y = gp.SCREENHEIGHT/3;
        g.drawString(textToShow, x, y);
    }
    
    public void showMenu(boolean show) {
        /**
          * Toggles the visibility of the game end menu.
          * @param show A Boolean that determines the whether the game end menu should be toggled visible (true) or invisible (false).
          */
        
         //Toggles menu
        setVisible(show);
        buttonsPanel.setVisible(show);
        exitButton.setVisible(show);
        revalidate();
        repaint();
    }
}
