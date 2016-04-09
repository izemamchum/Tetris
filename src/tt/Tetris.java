package tt;


import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * Classe principale du jeu
 * Elle permet de dessiner la surface du jeu et lancer le jeu
 *
 */

public class Tetris extends JFrame {
    
	JPanel panneauStats;
    JLabel statusbar;

    
    public Tetris() {
    	
    	
        Surface surface = new Surface(this);
        add(surface, BorderLayout.CENTER);
        surface.start();

        setSize(300, 500);
        setResizable(false);
        setTitle("Mon Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }


    public static void main(String[] args) {

        Tetris jeu = new Tetris();
        jeu.setLocationRelativeTo(null);
        jeu.setVisible(true);

    } 
}