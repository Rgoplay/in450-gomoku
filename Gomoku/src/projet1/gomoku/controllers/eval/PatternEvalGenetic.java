package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

/**
 * Fonction se basant sur des patterns de 5 pour son évaluation, et adaptée à l'utilisation des poids de l'algo génétique
 */
public class PatternEvalGenetic extends EvalFunction {
	
	private int[] scores = new int[243]; // 3^5 patterns possibles
	
	/**
	 * Attention : on doit passer un tableau de 15 de long
	 * @param patternValues
	 */
	public PatternEvalGenetic(int[] patternValues) {
		
		// 3 blancs
	    scores[encode(0,0,1,1,1)] = patternValues[0]*100;
	    scores[encode(0,1,0,1,1)] = patternValues[1]*100;
	    scores[encode(0,1,1,0,1)] = patternValues[2]*100;
	    scores[encode(0,1,1,1,0)] = patternValues[3]*100;
	    scores[encode(1,0,0,1,1)] = patternValues[4]*100;
	    scores[encode(1,0,1,0,1)] = patternValues[5]*100;
	    scores[encode(1,0,1,1,0)] = patternValues[6]*100;
	    scores[encode(1,1,0,0,1)] = patternValues[7]*100;
	    scores[encode(1,1,0,1,0)] = patternValues[8]*100;
	    scores[encode(1,1,1,0,0)] = patternValues[9]*100;

	    // 3 noirs
	    scores[encode(0,0,2,2,2)] = -patternValues[0]*100;
	    scores[encode(0,2,0,2,2)] = -patternValues[1]*100;
	    scores[encode(0,2,2,0,2)] = -patternValues[2]*100;
	    scores[encode(0,2,2,2,0)] = -patternValues[3]*100;
	    scores[encode(2,0,0,2,2)] = -patternValues[4]*100;
	    scores[encode(2,0,2,0,2)] = -patternValues[5]*100;
	    scores[encode(2,0,2,2,0)] = -patternValues[6]*100;
	    scores[encode(2,2,0,0,2)] = -patternValues[7]*100;
	    scores[encode(2,2,0,2,0)] = -patternValues[8]*100;
	    scores[encode(2,2,2,0,0)] = -patternValues[9]*100;

	    // 4 blancs
	    scores[encode(0,1,1,1,1)] = patternValues[10]*1000;
	    scores[encode(1,0,1,1,1)] = patternValues[11]*1000;
	    scores[encode(1,1,0,1,1)] = patternValues[12]*1000;
	    scores[encode(1,1,1,0,1)] = patternValues[13]*1000;
	    scores[encode(1,1,1,1,0)] = patternValues[14]*1000;

	    // 4 noirs
	    scores[encode(0,2,2,2,2)] = -patternValues[10]*1000;
	    scores[encode(2,0,2,2,2)] = -patternValues[11]*1000;
	    scores[encode(2,2,0,2,2)] = -patternValues[12]*1000;
	    scores[encode(2,2,2,0,2)] = -patternValues[13]*1000;
	    scores[encode(2,2,2,2,0)] = -patternValues[14]*1000;

	    // 5 blancs
	    scores[encode(1,1,1,1,1)] = 999_999_999;

	    // 5 noirs
	    scores[encode(2,2,2,2,2)] = -999_999_999;
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
		TileState enemyTile = playerTile == TileState.Black ? TileState.White : TileState.Black;
		
		int score = 0;
		for(int y = 0; y <= 10; y++) {
            for(int x = 0; x <= 10; x++) {
            	
                score += coef*getScore(convint_horiz(x,y, board));
              
                score += coef*getScore(convint_vert(x,y, board));

                score += coef*getScore(convint_diagSE(x,y, board));

                // de 4 à 14 en x
                score += coef*getScore(convint_diagSW(x+4,y, board));
                
                if(x >= 4 && y >= 4) { // x <= 10 && y <= 10 implicite des boucles for
                	if(board.get(x, y) == playerTile) {
                		score += 1;
                	} else if(board.get(x, y) == enemyTile) {
                		score -= 1;
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
