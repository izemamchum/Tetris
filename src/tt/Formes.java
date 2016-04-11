package tt;

import java.util.Random;
import java.lang.Math;


/***
 * 
 * Cette classe contient la difinition des différentes pièces de jeu
 *
 */

public class Formes {

    enum FormesPieces { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape };

    private FormesPieces formePiece;
    private int coordonnees[][];
    private int[][][] coordsTable;


    public Formes() {

        coordonnees = new int[4][2];
        creerForme(FormesPieces.NoShape);

    }

    public void creerForme(FormesPieces forme) {

         coordsTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coordonnees[i][j] = coordsTable[forme.ordinal()][i][j];
            }
        }
        formePiece = forme;

    }

    private void setX(int index, int x) { coordonnees[index][0] = x; }
    private void setY(int index, int y) { coordonnees[index][1] = y; }
    public int x(int index) { return coordonnees[index][0]; }
    public int y(int index) { return coordonnees[index][1]; }
    public FormesPieces getShape()  { return formePiece; }

    public void setRandomShape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        FormesPieces[] values = FormesPieces.values(); 
        creerForme(values[x]);
    }

    public int minX()
    {
      int m = coordonnees[0][0];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coordonnees[i][0]);
      }
      return m;
    }


    public int minY() 
    {
      int m = coordonnees[0][1];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coordonnees[i][1]);
      }
      return m;
    }

    /**
     * 
     * Méthode permettant de faire tourner la pièce
     */
    
    public Formes tourner() 
    {
        if (formePiece == FormesPieces.SquareShape)
            return this;

        Formes result = new Formes();
        result.formePiece = formePiece;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Formes rotateRight()
    {
        if (formePiece == FormesPieces.SquareShape)
            return this;

        Formes result = new Formes();
        result.formePiece = formePiece;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}