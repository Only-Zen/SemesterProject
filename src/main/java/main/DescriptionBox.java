package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
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
    
    public DescriptionBox(Coordinate boxCoord, String name, String price, String description){
        //
        nameFont = new Font("Dialog", Font.PLAIN, 15);
        priceFont = new Font("Dialog", Font.BOLD, 18);
        descFont = new Font("Dialog", Font.PLAIN, 12);
        
        this.name = name;
        this.price = price;
        this.description = description;
        
        this.boxCoord = boxCoord;
    }
    
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
        
        //String text = "This lovely flower shoots petals at intruding rats";
        
        
        FontMetrics fm = g2.getFontMetrics();

        int lineHeight = fm.getHeight();
        int curY = boxCoord.getY() + 58;

        String[] words = description.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line + word + " ";
            int lineWidth = fm.stringWidth(testLine);

            if (lineWidth > 140) {
                // Draw current line and start new one
                g2.drawString(line.toString(), boxCoord.getX() + 5, curY);
                line = new StringBuilder(word + " ");
                curY += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }

        // Draw any remaining text
        if (!line.toString().isEmpty()) {
            g2.drawString(line.toString(), boxCoord.getX() + 5, curY);
        }
    }
    
}
