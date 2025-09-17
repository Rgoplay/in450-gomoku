package projet1.gomoku.gamecore.enums;

/**Liste des états possibles pour une case du plateau de jeu
 * On assigne un id a chaque valeur afin de pouvoir faire des calculs directement
 * Accessible avec <TileState>.id
 * */
public enum TileState{
    /**Case occupée par une pièce blanche */
    White(1),

    /**Case occupée par une pièce noire */
    Black(2),

    /**Case vide */
    Empty(0);
    
    public final int id;

    private TileState(int id) {
        this.id = id;
    }
}
