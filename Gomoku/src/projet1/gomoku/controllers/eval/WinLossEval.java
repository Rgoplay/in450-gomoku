package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.WinnerState;

public class WinLossEval extends EvalFunction {

	@Override
	public int evaluateBoard(GomokuBoard board, Player player) {
		int score = 0;
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
