package tt;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tt.Formes.FormesPieces;;

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



    public Surface(Tetris parent) {

       setFocusable(true);
       pieceCourante = new Formes();
       timer = new Timer(400, this);
       timer.start(); 

       
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
            statusbar.setText("pause");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(nombreLigneDetruite));
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
                FormesPieces forme = emplacememtPiece(j, hauteurSurface - i - 1);
                if (forme != FormesPieces.NoShape)
                    dessinerCarree(g, 0 + j * largeurCarree(),
                               surfaceHaut + i * hauteurCarree(), forme);
            }
        }

        if (pieceCourante.getForme() != FormesPieces.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + pieceCourante.x(i);
                int y = curY - pieceCourante.y(i);
                dessinerCarree(g, 0 + x * largeurCarree(),
                           surfaceHaut + (hauteurSurface - y - 1) * hauteurCarree(),
                           pieceCourante.getForme());
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
            surf[(y * largeurSurface) + x] = pieceCourante.getForme();
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
        pieceCourante.setRandomForme();
        curX = largeurSurface / 2 + 1;
        curY = hauteurSurface - 1 + pieceCourante.minY();

        if (!deplacementPossible(pieceCourante, curX, curY)) {
            pieceCourante.creerForme(FormesPieces.NoShape);
            timer.stop();
            estCommence = false;
            statusbar.setText("game over");
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
            statusbar.setText(String.valueOf(nombreLigneDetruite));
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


        Color couleur = couleurs[forme.ordinal()];

        g.setColor(couleur);
        g.fillRect(x + 1, y + 1, largeurCarree() - 2, hauteurCarree() - 2);

        g.setColor(couleur.brighter());
        g.drawLine(x, y + hauteurCarree() - 1, x, y);
        g.drawLine(x, y, x + largeurCarree() - 1, y);

        g.setColor(couleur.darker());
        g.drawLine(x + 1, y + hauteurCarree() - 1,
                         x + largeurCarree() - 1, y + hauteurCarree() - 1);
        g.drawLine(x + largeurCarree() - 1, y + hauteurCarree() - 1,
                         x + largeurCarree() - 1, y + 1);
    }
    
    /**
     * 
     * Mise en place des évenements de clavier
     *
     */

    class TAdapter extends KeyAdapter {
         public void keyPressed(KeyEvent e) {

             if (!estCommence || pieceCourante.getForme() == FormesPieces.NoShape) {  
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