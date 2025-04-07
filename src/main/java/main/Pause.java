package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.GridLayout;
import javax.swing.BorderFactory;

/**
 *
 * @author lmm0060
 */
public class Pause extends JLayeredPane {
        
    GamePanel gp;
    
    Font cambria_header, cambria_body;

    JPanel buttonsPanel;
    JButton contButton, saveButton, exitButton;
    
    public Pause(GamePanel gp) {
        this.gp = gp;
        
        //Instantiate pause menu
        setLayout(null);
        setBounds(0,0,gp.SCREENWIDTH,gp.SCREENHEIGHT);
        
        //Create buttonsPanel and instantiate
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBounds(gp.SCREENWIDTH/4, gp.SCREENHEIGHT/2, gp.SCREENWIDTH/2, gp.SCREENHEIGHT/3);
        buttonsPanel.setBackground(Color.WHITE);
      //  buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //Without these, the pause menu will not show.
        setOpaque(true);
        buttonsPanel.setOpaque(false);
        
        //Create fonts
        cambria_header = new Font ("Cambria", Font.BOLD, 60);
        cambria_body = new Font ("Cambria", Font.PLAIN, 40);
        
        //Create buttons
        contButton = new JButton("Continue");
        saveButton = new JButton("Save Game");
        exitButton = new JButton("Exit");
        
        //Give buttons fonts
        contButton.setFont(cambria_body);
        saveButton.setFont(cambria_body);
        exitButton.setFont(cambria_body);
        
        //Create an action listener
        MenuHandler handler = new MenuHandler(gp);
        
        //Add action listeners to buttons to detect clicks
        contButton.addActionListener(handler);
        saveButton.addActionListener(handler);
        exitButton.addActionListener(handler);
        
        //Set action commands for actionListener to detect
        contButton.setActionCommand("Play");
        saveButton.setActionCommand("Save");
        exitButton.setActionCommand("Exit");
        
        //Add buttons to buttonsPanel
        buttonsPanel.add(contButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(exitButton);

        //Add buttonsPanel to frame
        add(buttonsPanel);

        //Make pause menu visible
        setVisible(true);
        buttonsPanel.setVisible(true);
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    public void drawPauseScreen(Graphics g) {
        //Draw a smaller white rectangle over "Paused" text and buttonsPanel
        g.setColor(Color.WHITE);
        int x = (gp.SCREENWIDTH/3 * 2); int y = (gp.SCREENHEIGHT/3 * 2);
        g.fillRoundRect(gp.SCREENWIDTH/6, gp.SCREENHEIGHT/5, x, y, 10, 10);
        
        //Draw the text: "Paused"
        String textToShow = "Paused";
        g.setColor(Color.BLACK);
        g.setFont(cambria_header);
        int textLength = (int)g.getFontMetrics().getStringBounds(textToShow, g).getWidth(); //Used to draw text to the center of the screen
        x = gp.SCREENWIDTH/2 - textLength/2; y = gp.SCREENHEIGHT/3;
        g.drawString(textToShow, x, y);
    }
    
     public void showPauseMenu(boolean show) {
         //Toggles pause menu
        setVisible(show);
        buttonsPanel.setVisible(show);
        revalidate();
        repaint();
    }
}
