package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

/**
 * Fonction d'évaluation évaluant uniquement les lignes et colonnes
 */
public class LigneColEval extends EvalFunction{

	@Override
	public int evaluateBoard(GomokuBoard board, Player player) {
		int score = 0;
		Coords currentCellCoords = new Coords();
		
		int val = 0;
		TileState lastColor = TileState.Empty;
        TileState playerCellState = player == Player.White ? TileState.White : TileState.Black;
        
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                
                if(board.get(currentCellCoords) == lastColor) {
                	val = val*10;
                }
                else {
                	score += val;
                	if (playerCellState == board.get(currentCellCoords)) {
                		val = 1;
                	}
                	else if(board.get(currentCellCoords) != TileState.Empty){
                		// Couleur adverse
                		val = -1;
                	}
                	else {
                		val = 0;
                	}
                }
                lastColor = board.get(currentCellCoords);
            }
        }
        val = 0;
		lastColor = TileState.Empty;
        for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
        	for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
                if(board.get(currentCellCoords) == lastColor) {
                	val = val*10;
                }
                else {
                	score += val;
                	if (playerCellState == board.get(currentCellCoords)) {
                		val = 1;
                	}
                	else if(board.get(currentCellCoords) != TileState.Empty){
                		// Couleur adverse
                		val = -1;
                	}
                	else {
                		val = 0;
                	}
                }
                
                lastColor = board.get(currentCellCoords);
            }
        }
        
        WinnerState winner = board.getWinnerState();
        if(winner == WinnerState.White && player == Player.White) {
        	score = 999999;
        }
        else if(winner == WinnerState.Black && player == Player.Black) {
        	score = 999999;
        }
        else if(winner != WinnerState.None && winner != WinnerState.Tie) {
        	score = -999999;
        }
	return score;
	}
}
