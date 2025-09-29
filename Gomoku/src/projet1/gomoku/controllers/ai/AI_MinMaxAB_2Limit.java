package projet1.gomoku.controllers.ai;

import projet1.gomoku.controllers.AIPlayer;
import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;


/**Représente un IA qui cherche les coups en se positionnant sur chaque case, puis en vérifiant le contenu des 4 cases autour dans les 8 directions */
public class AI_MinMaxAB_2Limit extends AIPlayer {

	private int nbNodeLeafEvaluated = 0;
	private int[][] boundaryBoard;
	
	public AI_MinMaxAB_2Limit(int minimaxDepth, EvalFunction eval){
        super(minimaxDepth, eval);
        boundaryBoard = new int[15][15];
    }

    int minMax(GomokuBoard board, Player player, int depth, Player minMaxPlayer, int alpha, int beta) {

    	
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
                if (boundaryBoard[currentCellCoords.column][currentCellCoords.row] > 0){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    addPlayableSquares(currentCellCoords.column, currentCellCoords.row);
                    value = Math.max(value, -minMax(board,player,depth-1, inverseMinMaxPlayer,-beta, -alpha)); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup
                    removePlayableSquares(currentCellCoords.column, currentCellCoords.row);
                    
                    if(value >= beta) {
                    	return value;
                    }
                    alpha = Math.max(alpha, value); 
                }
                
            }
        }
        return value;
    }
    
    private Coords startMinMax(GomokuBoard board, Player player, int depth){ //scan les coups possible du tableau et les joue -> a utiliser récursivement dans le minmax
    	nbNodeLeafEvaluated = 0;
    	
    	int alpha = -2147483647;
    	int beta  = -alpha;
    	
        Coords currentCellCoords = new Coords();
        Coords bestCoords = new Coords();
        int bestScore = -999999999	;
        TileState playerCellState = player == Player.White ? TileState.White : TileState.Black;
        
        Player inversePlayer = player == Player.White ? Player.Black : Player.White;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (boundaryBoard[currentCellCoords.column][currentCellCoords.row] > 0){ // Si la case est vide
                    
                    board.set(currentCellCoords, playerCellState); // Jouer le coup
                    addPlayableSquares(currentCellCoords.column, currentCellCoords.row);
                    int score = -minMax(board,player,depth-1,inversePlayer, -beta, -alpha); // Evaluer le coup
                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup
                    removePlayableSquares(currentCellCoords.column, currentCellCoords.row);
                    
                    
                    if(score > bestScore) {
                    	bestScore = score;
                    	bestCoords = currentCellCoords.clone();
                    }
                    if(bestScore >= beta) {
                    	return bestCoords;
                    }
                    alpha = Math.max(alpha, bestScore); 
                }
            }
        }

       
        return bestCoords;//sorted.map(Map.Entry::getKey).toArray(Coords[]::new); // Retourner les coordonnées des coups
    }

	@Override
	public Coords play(GomokuBoard board, Player player) {
		// on retourne le premier coup
		
		initBoundaryBoard(board);
		Coords temp = startMinMax(board, player, depthMax);
		System.out.println("Nb board eval: "+ nbNodeLeafEvaluated);
		return temp;
	}
	
	
	private void initBoundaryBoard(GomokuBoard board) {
		boundaryBoard[7][7] = 1; // autorise une case à jouer (si on commence la partie)
		for(int x = 0; x < 15; x++) {
			for(int y = 0; y < 15; y++) {
				if(board.get(x, y) != TileState.Empty) {
					addPlayableSquares(x, y);	
				}
			}
		}
	}
	
	private void addPlayableSquares(int xOrig, int yOrig) {
		for(int x = xOrig-2; x <= xOrig+2; x++) {
			for(int y = yOrig-2; y <= yOrig+2; y++) {
				if(x >= 0 && x < 15 && y >= 0 && y < 15) {
					boundaryBoard[x][y] += 1;
				}
			}
		}
		boundaryBoard[xOrig][yOrig] -= 2000;
	}
	
	private void removePlayableSquares(int xOrig, int yOrig) {
		for(int x = xOrig-2; x <= xOrig+2; x++) {
			for(int y = yOrig-2; y <= yOrig+2; y++) {
				if(x >= 0 && x < 15 && y >= 0 && y < 15) {
					boundaryBoard[x][y] -= 1;
				}
			}
		}
		boundaryBoard[xOrig][yOrig] += 2000;
	}
}
