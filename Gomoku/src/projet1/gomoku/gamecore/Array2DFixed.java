package projet1.gomoku.gamecore;

import projet1.gomoku.gamecore.enums.TileState;

/**Classe générique pour stocker un tableau 2D de int
*/
public class Array2DFixed implements Cloneable {
	/**Tableau 2D*/
    private TileState[][] array;

    /**Créer un tableau 2D
     * @param width Largeur du tableau
     * @param height Hauteur du tableau
     * @param defaultValue Valeur par défaut
    */
    public Array2DFixed(int width, int height, TileState defaultValue){
        array = new TileState[width][height];

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                array[x][y] = defaultValue;
            }
        }
    }

    /**Créer un tableau 2D
     * @param width Largeur du tableau
     * @param height Hauteur du tableau
    */
    public Array2DFixed(int width, int height){
        this(width, height, TileState.Empty);
    }

    /**Obtenir la valeur d'un élément
     * @param x Numéro de colonne
     * @param y Numéro de ligne
     * @return Valeur de l'élément
    */
    public TileState get(int x, int y){
        return array[x][y];
    }

    /**Définir la valeur d'un élément
     * @param x Numéro de colonne
     * @param y Numéro de ligne
     * @param value Valeur à définir
    */
    public void set(int x, int y, TileState value){
        array[x][y] = value;
    }

    /**Obtenir la valeur d'un élément
     * @param coords Coordonnées de l'élément
     * @return Valeur de l'élément
    */
    public TileState get(Coords coords){
        return get(coords.column, coords.row);
    }

    /**Définir la valeur d'un élément
     * @param coords Coordonnées de l'élément
     * @param value Valeur à définir
    */
    public void set(Coords coords, TileState value){
        set(coords.column, coords.row, value);
    }

    /**Obtenir la largeur du tableau
     * @return Largeur du tableau
    */
    public int getWidth(){
        return array.length;
    }

    /**Obtenir la hauteur du tableau
     * @return Hauteur du tableau
    */
    public int getHeight(){
        return array[0].length;
    }


    /**Vérifier si les coordonnées sont valides
     * @param x Numéro de colonne
     * @param y Numéro de ligne
     * @return true si les coordonnées sont valides, false sinon
    */
    public boolean areCoordsValid(int x, int y){
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    /**Vérifier si les coordonnées sont valides
     * @param coords Coordonnées à tester
     * @return true si les coordonnées sont valides, false sinon
    */
    public boolean areCoordsValid(Coords coords){
        return coords.column >= 0 && coords.column < getWidth() && coords.row >= 0 && coords.row < getHeight();
    }

    /**Afficher le tableau dans la console*/
    public void print(){
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                System.out.print(get(x, y) + " ");
            }
            System.out.println();
        }
    }


    @Override
    public Array2DFixed clone(){
        Array2DFixed clone = new Array2DFixed(getWidth(), getHeight());

        for (int x = 0; x < getWidth(); x++){
            for (int y = 0; y < getHeight(); y++){
                clone.set(x, y, get(x, y));
            }
        }

        return clone;
    }
}
