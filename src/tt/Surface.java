package tt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tt.Formes.FormesPieces;

/**
 * 
 * Cette classe contient les méthodes de controles de la surface du jeu
 * et des différents déplacements des pièces
 * ainsi que la gestion des différents évenement applicables sur les pièces
 *
 */


public class Surface extends JPanel implements ActionListener {


    final int largeurSurface = 10;
    final int hauteurSurface = 22;
    public static int score=0;
    
    Timer timer;
    boolean isFallingFinished = false;
    
    boolean estCommence = false;
    boolean estSuspendu = false;
    int nombreLigneDetruite = 0;
    int curX = 0;
    int curY = 0;
    
    JLabel statusbar;
    Formes pieceCourante;
    FormesPieces[] surf;

    int scores[]= new int [6];
    int scoreInter;
    int indiceInter;
    boolean arret=false;

    public Surface(Tetris parent) {
    	
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
				scores[i]=Integer.valueOf(ligne);
				i++;
			}
				br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		

       setFocusable(true);
       pieceCourante = new Formes();
       timer = new Timer(400, this);
       timer.start(); 

       statusbar =  parent.getStatusBar();
       surf = new FormesPieces[largeurSurface * hauteurSurface];
       addKeyListener(new TAdapter());
       effacerSur();  
    }

    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            nouvellePiece();
        } else {
            uneLigneVersLeBas();
        }
    }


    int largeurCarree() { return (int) getSize().getWidth() / largeurSurface; }
    int hauteurCarree() { return (int) getSize().getHeight() / hauteurSurface; }
    FormesPieces emplacememtPiece(int x, int y) { return surf[(y * largeurSurface) + x]; }

    /**
     * Méthode permettant de créer une nouvelle instance de tetris
     */


    public void start()
    {
        if (estSuspendu)
            return;

        estCommence = true;
        isFallingFinished = false;
        nombreLigneDetruite = 0;
        effacerSur();

        nouvellePiece();
        timer.start();
    }

    /**
     * Méthode permettant de suspendre le jeu
     */

    
    private void pause()
    {
        if (!estCommence)
            return;

        estSuspendu = !estSuspendu;
        if (estSuspendu) {
            timer.stop();
            statusbar.setText("paused");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(Surface.score));
        }
        repaint();
    }

    /**
     * Méthode permettant de dessiner la surface du jeu
     */

    
    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();
        int surfaceHaut = (int) size.getHeight() - hauteurSurface * hauteurCarree();


        for (int i = 0; i < hauteurSurface; ++i) {
            for (int j = 0; j < largeurSurface; ++j) {
                FormesPieces shape = emplacememtPiece(j, hauteurSurface - i - 1);
                if (shape != FormesPieces.NoShape)
                    dessinerCarree(g, 0 + j * largeurCarree(),
                               surfaceHaut + i * hauteurCarree(), shape);
            }
        }

        if (pieceCourante.getShape() != FormesPieces.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + pieceCourante.x(i);
                int y = curY - pieceCourante.y(i);
                dessinerCarree(g, 0 + x * largeurCarree(),
                           surfaceHaut + (hauteurSurface - y - 1) * hauteurCarree(),
                           pieceCourante.getShape());
            }
        }
    }

    /**
     * Méthode permettant de déplacer la pièce vers le bas
     */
    
    private void deplacerBas()
    {
        int newY = curY;
        while (newY > 0) {
            if (!deplacementPossible(pieceCourante, curX, newY - 1))
                break;
            --newY;
        }
        pieceDeplacee();
    }

    
    /**
     * Méthode permettant de déplacer la pièce d'une seule ligne vers le bas
     */
    
    private void uneLigneVersLeBas()
    {
        if (!deplacementPossible(pieceCourante, curX, curY - 1))
            pieceDeplacee();
    }


    /**
     * Méthode permettant de réinitialiser la surface du jeu
     */
    
    private void effacerSur()
    {
        for (int i = 0; i < hauteurSurface * largeurSurface; ++i)
            surf[i] = FormesPieces.NoShape;
    }

    private void pieceDeplacee()
    {
        for (int i = 0; i < 4; ++i) {
            int x = curX + pieceCourante.x(i);
            int y = curY - pieceCourante.y(i);
            surf[(y * largeurSurface) + x] = pieceCourante.getShape();
        }

        supprimerToutesLesLignes();

        if (!isFallingFinished)
            nouvellePiece();
    }

    /**
     * Méthode permettant de générer aléatoirement une nouvelle pièce
     * et de la mettre en jeu
     */
    
    private void nouvellePiece()
    {
        pieceCourante.setRandomShape();
        curX = largeurSurface / 2 + 1;
        curY = hauteurSurface - 1 + pieceCourante.minY();

        if (!deplacementPossible(pieceCourante, curX, curY)) {
            pieceCourante.creerForme(FormesPieces.NoShape);
            timer.stop();
            estCommence = false;
            statusbar.setText("game over");
            
            scores[5]=score;
            
           
    		int tampon = 0;
    		boolean permut;
     
    		do {
    			// hypothèse : le tableau est trié
    			permut = false;
    			for (int i = 0; i < 6 - 1; i++) {
    				// Teste si 2 éléments successifs sont dans le bon ordre ou non
    				if (scores[i] < scores[i + 1]) {
    					// s'ils ne le sont pas, on échange leurs positions
    					tampon = scores[i];
    					scores[i] = scores[i + 1];
    					scores[i + 1] = tampon;
    					permut = true;
    				}
    			}
    		} while (permut);
            		
						PrintWriter writer;
						try {
							writer = new PrintWriter( new FileWriter("fichier") );
						
						for(int j=0;j<5;j++)
						{
							writer.write(scores[j]+"\n");
						}
						writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            
            
			}
			
			
    }

    
    /**
     * Méthode permettant de vérifier si le déplacement solicité est possible
     * 
     */

    
    private boolean deplacementPossible(Formes newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= largeurSurface || y < 0 || y >= hauteurSurface)
                return false;
            if (emplacememtPiece(x, y) != FormesPieces.NoShape)
                return false;
        }

        pieceCourante = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }
    
    
    /**
     * Méthode permettant de détruire toutes les lignes complétées
     */
    
    private void supprimerToutesLesLignes()
    {
        int numLigneComplete = 0;

        for (int i = hauteurSurface - 1; i >= 0; --i) {
            boolean lingnesCompletes = true;

            for (int j = 0; j < largeurSurface; ++j) {
                if (emplacememtPiece(j, i) == FormesPieces.NoShape) {
                    lingnesCompletes = false;
                    break;
                }
            }

            if (lingnesCompletes) {
                ++numLigneComplete;
                for (int k = i; k < hauteurSurface - 1; ++k) {
                    for (int j = 0; j < largeurSurface; ++j)
                         surf[(k * largeurSurface) + j] = emplacememtPiece(j, k + 1);
                }
            }
        }

        if (numLigneComplete > 0) {
            nombreLigneDetruite += numLigneComplete;
            Surface.score+=(numLigneComplete*50);
            statusbar.setText(String.valueOf(score));
            isFallingFinished = true;
            pieceCourante.creerForme(FormesPieces.NoShape);
            repaint();
        }
     }

    
    
    private void dessinerCarree(Graphics g, int x, int y, FormesPieces forme)
    {
        Color couleurs[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = couleurs[forme.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, largeurCarree() - 2, hauteurCarree() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + hauteurCarree() - 1, x, y);
        g.drawLine(x, y, x + largeurCarree() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + hauteurCarree() - 1,
                         x + largeurCarree() - 1, y + hauteurCarree() - 1);
        g.drawLine(x + largeurCarree() - 1, y + hauteurCarree() - 1,
                         x + largeurCarree() - 1, y + 1);
    }

    class TAdapter extends KeyAdapter {
         public void keyPressed(KeyEvent e) {

             if (!estCommence || pieceCourante.getShape() == FormesPieces.NoShape) {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }

             if (estSuspendu)
                 return;

             switch (keycode) {
             case KeyEvent.VK_LEFT:
                 deplacementPossible(pieceCourante, curX - 1, curY);
                 break;
             case KeyEvent.VK_RIGHT:
                 deplacementPossible(pieceCourante, curX + 1, curY);
                 break;
             case KeyEvent.VK_UP:
                 deplacementPossible(pieceCourante.tourner(), curX, curY);
                 break;
             case KeyEvent.VK_DOWN:
                 deplacerBas();
                 break;
             case 'd':
                 uneLigneVersLeBas();
                 break;
             case 'D':
                 uneLigneVersLeBas();
                 break;
             }

         }
     }
}