package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

public class PatternEval extends EvalFunction {
	
	private int[] scores = new int[243]; // 3^5 patterns possibles
	
	public PatternEval() {
		int p3Value = 500;
		int p4Value = 10000;
		int p5Value = 999999999;
		
		// 3 blancs
	    scores[encode(0,0,1,1,1)] = 2*p3Value;
	    scores[encode(0,1,0,1,1)] = p3Value;
	    scores[encode(0,1,1,0,1)] = p3Value;
	    scores[encode(0,1,1,1,0)] = 5*p3Value;
	    scores[encode(1,0,0,1,1)] = p3Value;
	    scores[encode(1,0,1,0,1)] = p3Value;
	    scores[encode(1,0,1,1,0)] = p3Value;
	    scores[encode(1,1,0,0,1)] = p3Value;
	    scores[encode(1,1,0,1,0)] = p3Value;
	    scores[encode(1,1,1,0,0)] = 2*p3Value;

	    // 3 noirs
	    scores[encode(0,0,2,2,2)] = -2*p3Value;
	    scores[encode(0,2,0,2,2)] = -p3Value;
	    scores[encode(0,2,2,0,2)] = -p3Value;
	    scores[encode(0,2,2,2,0)] = -5*p3Value;
	    scores[encode(2,0,0,2,2)] = -p3Value;
	    scores[encode(2,0,2,0,2)] = -p3Value;
	    scores[encode(2,0,2,2,0)] = -p3Value;
	    scores[encode(2,2,0,0,2)] = -p3Value;
	    scores[encode(2,2,0,2,0)] = -p3Value;
	    scores[encode(2,2,2,0,0)] = -2*p3Value;

	    // 4 blancs
	    scores[encode(0,1,1,1,1)] = 2*p4Value;
	    scores[encode(1,0,1,1,1)] = p4Value;
	    scores[encode(1,1,0,1,1)] = p4Value;
	    scores[encode(1,1,1,0,1)] = p4Value;
	    scores[encode(1,1,1,1,0)] = 2*p4Value;

	    // 4 noirs
	    scores[encode(0,2,2,2,2)] = -2*p4Value;
	    scores[encode(2,0,2,2,2)] = -p4Value;
	    scores[encode(2,2,0,2,2)] = -p4Value;
	    scores[encode(2,2,2,0,2)] = -p4Value;
	    scores[encode(2,2,2,2,0)] = -2*p4Value;

	    // 5 blancs
	    scores[encode(1,1,1,1,1)] = p5Value;

	    // 5 noirs
	    scores[encode(2,2,2,2,2)] = -p5Value;
	}
	
	
	private int getScore(int pattern) {
		return scores[pattern];
	}
	
	private int encode(int... cells) { // ... veut dire n argument de type int
	    int key = 0;
	    for (int c : cells) {
	        key = key * 3 + c;
	    }
	    return key;
	}

	@Override
	public int evaluateBoard(GomokuBoard board, Player player) {
		
		// On calcule pour les blancs, donc il faut inverser si le joueur est noir
		int coef = player == Player.White ? 1 : -1;
		TileState playerTile = player == Player.White ? TileState.White : TileState.Black;
		int score = 0;
		for(int y = 0; y <= 10; y++) {
            for(int x = 0; x <= 10; x++) {
            	
                score += coef*getScore(convint_horiz(x,y, board));
              
                score += coef*getScore(convint_vert(x,y, board));

                score += coef*getScore(convint_diagSE(x,y, board));

                // de 4 à 14 en x
                score += coef*getScore(convint_diagSW(x+4,y, board));
                
                if(x <= 9 && x >= 5 && y >= 5 && y <= 9) {
                	if(board.get(x, y) == playerTile) {
                		score += 1;
                	}
                }
            }
		}
		return score;
	}
	
	
	// Ordinal() permet de correspondre l'ordre des TileState a un entier
	
	private int convint_horiz(int x, int y, GomokuBoard board) {
	    return 81 * board.get(x, y).ordinal() + 27 * board.get(x+1, y).ordinal() + 9 * board.get(x+2, y).ordinal() + 3 * board.get(x+3, y).ordinal() + board.get(x+4, y).ordinal();
	}
	private int convint_vert(int x, int y, GomokuBoard board) {
		return 81 * board.get(x, y).ordinal() + 27 * board.get(x, y+1).ordinal() + 9 * board.get(x, y+2).ordinal() + 3 * board.get(x, y+3).ordinal() + board.get(x, y+4).ordinal();
	}
	private int convint_diagSE(int x, int y, GomokuBoard board) {
		return 81 * board.get(x, y).ordinal() + 27 * board.get(x+1, y+1).ordinal() + 9 * board.get(x+2, y+2).ordinal() + 3 * board.get(x+3, y+3).ordinal() + board.get(x+4, y+4).ordinal();
	}
	private int convint_diagSW(int x, int y, GomokuBoard board) {
		return 81 * board.get(x, y).ordinal() + 27 * board.get(x-1, y+1).ordinal() + 9 * board.get(x-2, y+2).ordinal() + 3 * board.get(x-3, y+3).ordinal() + board.get(x-4, y+4).ordinal();
	}
}
