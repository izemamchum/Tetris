package tt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    	
    	String []tableauScores= new String [5];
    	
    	InputStream ips;
		try {
			ips = new FileInputStream("fichier");
		 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			int i=0;
		
			while ((ligne=br.readLine())!=null){
				tableauScores[i]=ligne;
				i++;
			}
				br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		
		
    	statusbar = new JLabel("0");
    	panneauStats=new JPanel();
    	panneauStats.setSize(300, 200);
    	panneauStats.setBackground(Color.GRAY);
    	
    	JLabel meilleur=new JLabel("Meilleurs scores");
    	JLabel premier=new JLabel(tableauScores[0]);
    	JLabel deuxieme=new JLabel(tableauScores[1]);
    	JLabel troisieme=new JLabel(tableauScores[2]);
    	JLabel quatrieme=new JLabel(tableauScores[3]);
    	JLabel cinquieme=new JLabel(tableauScores[4]);
    	
    	GridBagLayout gb = new GridBagLayout();
    	 
    	
    	GridBagConstraints gbc = new GridBagConstraints();
    	
    	panneauStats.setLayout(gb);
    	gbc.fill = GridBagConstraints.BOTH;
    	
    	gbc.weightx = 1;
    	
    	gbc.weighty = 0;
    	gbc.gridx=1;
    	gb.setConstraints(meilleur, gbc); // mise en forme des objets
    	
    	gb.setConstraints(premier, gbc);
    	gb.setConstraints(deuxieme, gbc);
    	gb.setConstraints(troisieme, gbc);
    	gb.setConstraints(quatrieme, gbc);
    	gb.setConstraints(cinquieme, gbc);
    	
    	panneauStats.add(meilleur);
    	panneauStats.add(premier);
    	panneauStats.add(deuxieme);
    	panneauStats.add(troisieme);
    	panneauStats.add(quatrieme);
    	panneauStats.add(cinquieme);
    
    	
        
        add(statusbar, BorderLayout.SOUTH);
        add(panneauStats, BorderLayout.EAST);
        Surface board = new Surface(this);
        add(board, BorderLayout.CENTER);
        board.start();

        setSize(400, 500);
        setResizable(false);
        setTitle("Mon Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getStatusBar() {
       return statusbar;
   }

    public static void main(String[] args) {

    	
    	
        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        

    } 
}