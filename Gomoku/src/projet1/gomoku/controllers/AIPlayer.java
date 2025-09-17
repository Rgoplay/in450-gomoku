package projet1.gomoku.controllers;

import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;

/**
 * Class that represents all AI players
 */
public abstract class AIPlayer extends PlayerController {
	
	protected int depthMax = 0;
	protected EvalFunction eval;
	
	public AIPlayer(int minimaxDepth, EvalFunction eval){
	       depthMax = minimaxDepth;
	       this.eval = eval;
	}

	public abstract Coords play(GomokuBoard board, Player player);

}
