package projet1.gomoku.controllers.ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import projet1.gomoku.controllers.PlayerController;
import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

/**Représente un IA qui cherche les coups en se positionnant sur chaque case, puis en vérifiant le contenu des 4 cases autour dans les 8 directions */
public class AI_MinMax extends PlayerController {


	private int depthMax = 0;
	private EvalFunction eval;
	
    public AI_MinMax(int minimaxDepth, EvalFunction eval){
       depthMax = minimaxDepth;
       this.eval = eval;
    }

    public AI_MinMax(){
        super();
    }

    public int MinMax(GomokuBoard board, Player player, int depth, Player minMaxPlayer) {
    	int coef = player == minMaxPlayer ? 1 : -1;
    	Player inverseMinMaxPlayer = minMaxPlayer == Player.White ? Player.Black : Player.White;
        if (depth == 0 || board.getWinnerState() != WinnerState.None) {
        	return coef*eval.evaluateBoard(board, player);
        }
        int value = -2147483648; //nbr max
        Coords currentCellCoords = new Coords();
        TileState playerCellState = minMaxPlayer == Player.White ? TileState.White : TileState.Black;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (board.get(currentCellCoords) == TileState.Empty){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    value = Math.max(value, -MinMax(board,player,depth-1, inverseMinMaxPlayer)); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup

                }
            }
        }
        return value;
    }
    
    public Coords[] StartMinMax(GomokuBoard board, Player player, int depth){ //scan les coups possible du tableau et les joue -> a itilisé récursivement dans le minmax
    	
        Coords currentCellCoords = new Coords();
        TileState playerCellState = player == Player.White ? TileState.White : TileState.Black;
        Map<Coords, Integer> moves = new HashMap<>();
        Player inversePlayer = player == Player.White ? Player.Black : Player.White;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (board.get(currentCellCoords) == TileState.Empty){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    int score = -MinMax(board,player,depth-1,inversePlayer); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup

                    moves.put(currentCellCoords.clone(), score); // Enregistrer le coup
                }
            }
        }

        Stream<Map.Entry<Coords, Integer>> sorted = moves.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())); // Trier les coups par ordre de priorité décroissante
   
        return sorted.map(Map.Entry::getKey).toArray(Coords[]::new); // Retourner les coordonnées des coups
    }

	@Override
	public Coords play(GomokuBoard board, Player player) {
		// on retournelepremier coup
		return StartMinMax(board, player, depthMax)[0];
	}
}
