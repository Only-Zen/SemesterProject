
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * A graphical UI component that displays a small rectangular box
 * containing a product or item's name, price, and description.
 * It supports drawing the box with formatted text on a 2D graphics context.
 * 
 * @author mrsch
 */
public class DescriptionBox {
    
    private Font nameFont;
    private Font priceFont;
    private Font descFont;

    private String name;
    private String price;
    private String description;

    private Coordinate boxCoord;

    /**
     * Constructs a new DescriptionBox with the specified coordinates and content.
     *
     * @param boxCoord The coordinate where the top-left corner of the box will be drawn.
     * @param name The name of the item.
     * @param price The price of the item.
     * @param description The description text of the item.
     */
    public DescriptionBox(Coordinate boxCoord, String name, String price, String description){
        nameFont = new Font("Dialog", Font.PLAIN, 15);
        priceFont = new Font("Dialog", Font.BOLD, 18);
        descFont = new Font("Dialog", Font.PLAIN, 12);
        
        this.name = name;
        this.price = price;
        this.description = description;
        this.boxCoord = boxCoord;
    }
    
    /**
     * Draws the description box and its content onto the provided Graphics2D context.
     * The box includes a semi-transparent background, the item's price in yellow,
     * the name in white, and a word-wrapped description below.
     *
     * @param g2 The graphics context to draw on.
     */
    public void draw(Graphics2D g2){
        //
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(boxCoord.getX(), boxCoord.getY(), 150, 100);
        
        g2.setFont(priceFont);
        g2.setColor(Color.yellow);
        g2.drawString(price, boxCoord.getX() + 5, boxCoord.getY() + 18);
        
        g2.setFont(nameFont);
        g2.setColor(Color.white);
        g2.drawString(name, boxCoord.getX() + 5, boxCoord.getY() + 38);
        
        
        g2.setFont(descFont);
        g2.setColor(Color.white);
        
        FontMetrics fm = g2.getFontMetrics();

        int lineHeight = fm.getHeight();
        int curY = boxCoord.getY() + 58;

        String[] words = description.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line + word + " ";
            int lineWidth = fm.stringWidth(testLine);

            if (lineWidth > 140) {
                g2.drawString(line.toString(), boxCoord.getX() + 5, curY);
                line = new StringBuilder(word + " ");
                curY += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }

        if (!line.toString().isEmpty()) {
            g2.drawString(line.toString(), boxCoord.getX() + 5, curY);
        }
    }
    
}