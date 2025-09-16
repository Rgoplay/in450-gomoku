package projet1.gomoku.controllers.eval;


import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;

public abstract class EvalFunction {
	
	 public abstract int evaluateBoard(GomokuBoard board, Player player);

}
