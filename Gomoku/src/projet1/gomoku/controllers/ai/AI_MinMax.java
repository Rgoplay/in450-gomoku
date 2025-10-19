package projet1.gomoku.controllers.ai;

import projet1.gomoku.controllers.AIPlayer;
import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

/**
 * Classe représentant un MinMax (negamax) classique
 */
public class AI_MinMax extends AIPlayer {

	private int nbNodeLeafEvaluated = 0;
	
	public AI_MinMax(int minimaxDepth, EvalFunction eval){
        super(minimaxDepth, eval);
    }

    public int minMax(GomokuBoard board, Player player, int depth, Player minMaxPlayer) {
    	int coef = player == minMaxPlayer ? 1 : -1;
    	Player inverseMinMaxPlayer = minMaxPlayer == Player.White ? Player.Black : Player.White;
        if (depth == 0 || board.getWinnerState() != WinnerState.None) {
        	nbNodeLeafEvaluated++;
        	return coef*(eval.evaluateBoard(board, player)+depth); // On rajoute depth afin de privilégier les victoire rapide aux longues
        }
        int value = -2147483647; //nbr max
        Coords currentCellCoords = new Coords();
        TileState playerCellState = minMaxPlayer == Player.White ? TileState.White : TileState.Black;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (board.get(currentCellCoords) == TileState.Empty){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    value = Math.max(value, -minMax(board,player,depth-1, inverseMinMaxPlayer)); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup

                }
            }
        }
        return value;
    }
    
    public Coords startMinMax(GomokuBoard board, Player player){
    	nbNodeLeafEvaluated = 0;
        Coords currentCellCoords = new Coords();
        Coords bestCoords = new Coords();
        int bestScore = -2147483647;
        TileState playerCellState = player == Player.White ? TileState.White : TileState.Black;
        //Map<Coords, Integer> moves = new HashMap<>();
        Player inversePlayer = player == Player.White ? Player.Black : Player.White;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (board.get(currentCellCoords) == TileState.Empty){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    int score = -minMax(board,player,depthMax-1,inversePlayer); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup
                    
                    
                    if(score > bestScore) {
                    	bestScore = score;
                    	bestCoords = currentCellCoords.clone();
                    }
                }
            }
        }

        return bestCoords; // Retourner les coordonnées du meilleur coup
    }

	@Override
	public Coords play(GomokuBoard board, Player player) {
		Coords temp = startMinMax(board, player);
		System.out.println("Nb board eval: "+ nbNodeLeafEvaluated);
		return temp;
	}
}
