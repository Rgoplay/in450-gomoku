package projet1.gomoku.controllers.eval;


import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

/**
 * Fonction se basant sur des patterns de 5 et quelques de 6 pour son évaluation
 */
public class PatternEvalW6 extends EvalFunction {
	
	private final int[] scores = new int[243]; // 3^5 patterns possibles
	
	public PatternEvalW6() {
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
		//TileState playerTile = player == Player.White ? TileState.White : TileState.Black;
		//TileState enemyTile = playerTile == TileState.Black ? TileState.White : TileState.Black;
		
		int score = 0;
		for(int y = 0; y <= 10; y++) {
            for(int x = 0; x <= 10; x++) {
            	
            	score += detectPatterns(board, convint_horiz(x,y, board), x, y, 1, 0);
                
              
            	score += detectPatterns(board, convint_vert(x,y, board), x, y, 0, 1);
                
                

            	score += detectPatterns(board, convint_diagSE(x,y, board), x, y, 1, 1);

                // de 4 à 14 en x
            	score += detectPatterns(board, convint_diagSW(x+4,y, board), x+4, y, -1, 1);
                
                // On ajoute 1 pour chaque pion au centre
                if(x >= 4 && y >= 4) { // x <= 10 && y <= 10 implicite des boucles for
                	if(board.get(x, y) == TileState.White) {
                		score += 1;
                	} else if(board.get(x, y) == TileState.Black) {
                		score -= 1;
                	}
                }
            }
		}
		return coef*score;
	}
	
	
	private int detectPatterns(GomokuBoard board, int patternId, int x, int y, int dx, int dy) {
		if(patternId == 39 || patternId == 78) { // 3 ouvert
			int tx = x + dx*5;
			int ty = y + dy*5;
			x = x - dx;
			y = y - dy;
    		if((x >= 0 && x < 15 && y >= 0 && y < 15 && board.get(x, y) == TileState.Empty) || (tx >= 0 && tx < 15 && y >= 0 && ty < 15 && board.get(tx, ty) == TileState.Empty)) {
    			if(patternId == 39) {
    				return 9000;
    			} else {
    				return -9000;
    			}
    			
    		}
    	} else if(patternId == 40 || patternId == 80) { // 4 ouvert
    		int tx = x + dx*5;
			int ty = y + dy*5;
    		if((tx >= 0 && tx < 15 && ty >= 0 && ty < 15 && board.get(tx, ty) == TileState.Empty)) {
    			if(patternId == 40) {
    				return 100000;
    			} else {
    				return -100000;
    			}
    		}
    	}
    	return getScore(patternId);
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
