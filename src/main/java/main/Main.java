package main;

import javax.swing.JFrame;

/**
 *
 * @author 12568
 */
public class Main {
    
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("The Last Pint");
        

        TitleScreen title = new TitleScreen(window);
        window.add(title);
        
        window.pack();
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
