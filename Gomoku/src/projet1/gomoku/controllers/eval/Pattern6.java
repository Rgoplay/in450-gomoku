package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;


// NOTE: Ne fonctionne pas, ne pas utiliser
public class Pattern6 extends EvalFunction {
	
	private int[] ids = new int[729]; // 3^6 patterns possibles
	
	// Constants
	// Ne commence pas à 0 car la valeur par défaut du tableau est 0
	private static final int W_WIN = 1;
	private static final int W_OPEN_FOUR = 2;
	private static final int W_CLOSED_FOUR = 3;
	private static final int W_BROKEN_FOUR = 4;
	private static final int W_SIDE_FOUR = 5;
	private static final int W_OPEN_THREE = 6;
	private static final int W_CLOSED_THREE = 7;
	private static final int W_BROKEN_THREE = 8;
	private static final int W_SIDE_THREE = 9;
	
	private static final int B_WIN = 10;
	private static final int B_OPEN_FOUR = 11;
	private static final int B_CLOSED_FOUR = 12;
	private static final int B_BROKEN_FOUR = 13;
	private static final int B_SIDE_FOUR = 14;
	private static final int B_OPEN_THREE = 15;
	private static final int B_CLOSED_THREE = 16;
	private static final int B_BROKEN_THREE = 17;
	private static final int B_SIDE_THREE = 18;
	
	private static final int SCORE_WIN = 999999999;
	private static final int SCORE_OPEN_FOUR = 500000;
	private static final int SCORE_BROKEN_FOUR = 50000;
	private static final int SCORE_CLOSED_FOUR = 50000;
	private static final int SCORE_SIDE_FOUR = 50000;
	private static final int SCORE_OPEN_THREE = 10000;
	private static final int SCORE_BROKEN_THREE = 1000;
	private static final int SCORE_CLOSED_THREE = 1000;
	private static final int SCORE_SIDE_THREE = 1000;
	
	public Pattern6() {

		// Les coups recoivent un id en fonction de leur force
		int a = 1;
		int b = 2;
		
		// Blancs puis noirs
		for(int i = 0; i < 2; i++) {
			int offset = i*9;

		    // 5 alignés
			ids[encode(0,a,a,a,a,a)] = W_WIN+offset;
			ids[encode(a,a,a,a,a,0)] = W_WIN+offset;
			
			// 4
			ids[encode(0,a,a,a,a,0)] = W_OPEN_FOUR+offset;
			
			ids[encode(b,a,a,a,a,0)] = W_CLOSED_FOUR+offset;
			ids[encode(0,a,a,a,a,b)] = W_CLOSED_FOUR+offset;
			
			ids[encode(0,a,a,a,0,a)] = W_BROKEN_FOUR+offset;
			ids[encode(a,0,a,a,a,0)] = W_BROKEN_FOUR+offset;
			ids[encode(b,a,a,a,0,a)] = W_BROKEN_FOUR+offset;
			ids[encode(a,0,a,a,a,b)] = W_BROKEN_FOUR+offset;
			
			ids[encode(a,a,a,0,a,0)] = W_BROKEN_FOUR+offset;
			ids[encode(0,a,0,a,a,a)] = W_BROKEN_FOUR+offset;
			ids[encode(a,a,a,0,a,b)] = W_BROKEN_FOUR+offset;
			ids[encode(b,a,0,a,a,a)] = W_BROKEN_FOUR+offset;
			
			
			ids[encode(0,a,a,0,a,a)] = W_BROKEN_FOUR+offset;
			ids[encode(a,a,0,a,a,0)] = W_BROKEN_FOUR+offset;
			ids[encode(b,a,a,0,a,a)] = W_BROKEN_FOUR+offset;
			ids[encode(a,a,0,a,a,b)] = W_BROKEN_FOUR+offset;
			
			ids[encode(a,a,a,a,0,0)] = W_SIDE_FOUR+offset;
			ids[encode(0,0,a,a,a,a)] = W_SIDE_FOUR+offset;
			ids[encode(a,a,a,a,0,b)] = W_SIDE_FOUR+offset;
			ids[encode(b,0,a,a,a,a)] = W_SIDE_FOUR+offset;
			
			// 3
			ids[encode(0,0,a,a,a,0)] = W_OPEN_THREE+offset;
			ids[encode(0,a,a,a,0,0)] = W_OPEN_THREE+offset;
			
			ids[encode(b,a,a,a,0,0)] = W_CLOSED_THREE+offset;
			ids[encode(0,0,a,a,a,b)] = W_CLOSED_THREE+offset;
			
			ids[encode(0,a,a,0,a,0)] = W_BROKEN_THREE+offset;
			ids[encode(0,a,0,a,a,0)] = W_BROKEN_THREE+offset;
			
			ids[encode(a,a,a,0,0,0)] = W_SIDE_THREE+offset;
			ids[encode(0,0,0,a,a,a)] = W_SIDE_THREE+offset;
			
			b = 1;
			a = 2;
		}
		
		
		
	}
	
	
	private int getId(int pattern) {
		return ids[pattern];
	}
	
	int encode(int... cells) { // ... veut dire n argument de type int
	    int key = 0;
	    for (int c : cells) {
	        key = key * 3 + c;
	    }
	    return key;
	}

	@Override
	public int evaluateBoard(GomokuBoard board, Player player) {
		
		TileState playerTile = player == Player.White ? TileState.White : TileState.Black;
		TileState enemyTile = playerTile == TileState.Black ? TileState.White : TileState.Black;
		int score = 0;
		
		// Compte le nombre de patterns de chaque categorie
		int[] patternsCounter = new int[19];
		
		for(int i = 0; i < 19; i++) {
			patternsCounter[i] = 0;
		}
		
		for(int y = 0; y <= 9; y++) {
            for(int x = 0; x <= 9; x++) {
            	patternsCounter[getId(convint_horiz(x,y, board))] += 1;
            	
                patternsCounter[getId(convint_vert(x,y, board))] += 1;
		
                patternsCounter[getId(convint_diagSE(x,y, board))] += 1;

                // de 4 à 14 en x
                patternsCounter[getId(convint_diagSW(x+5,y, board))] += 1;
		
                if(x <= 9 && x >= 5 && y >= 5 && y <= 9) {
                	if(board.get(x, y) == playerTile) {
                		//score += 1;
                	} else if(board.get(x, y) == enemyTile) {
                		//score -= 1;
                	}
                }
            }
		}

		// Calcul du score
		// Attention les combinaisons de 3 et de 4 peuvent etre confondue (memes pièces)
		if(player == Player.White) { // Blancs
			// Victoire
			if(patternsCounter[W_WIN] >= 1) {
				score += SCORE_WIN;
			}
			// Défaite
			else if(patternsCounter[B_WIN] >= 1) {
				score -= SCORE_WIN;
			}
			else {
				score += Math.pow(patternsCounter[W_OPEN_THREE], 2)*SCORE_OPEN_THREE;
				score += Math.pow(patternsCounter[W_BROKEN_THREE], 1.5)*SCORE_BROKEN_THREE;
				score += Math.pow(patternsCounter[W_CLOSED_THREE], 1.5)*SCORE_CLOSED_THREE;
				score += Math.pow(patternsCounter[W_SIDE_THREE], 1.5)*SCORE_SIDE_THREE;
				
				score += Math.pow(patternsCounter[W_OPEN_FOUR], 3)*SCORE_OPEN_FOUR;
				score += Math.pow(patternsCounter[W_CLOSED_FOUR], 2)*SCORE_CLOSED_FOUR;
				score += Math.pow(patternsCounter[W_BROKEN_FOUR], 2)*SCORE_BROKEN_FOUR;
				score += Math.pow(patternsCounter[W_SIDE_FOUR], 2)*SCORE_SIDE_FOUR;
				
				
				score -= Math.pow(patternsCounter[B_OPEN_THREE], 2)*SCORE_OPEN_THREE;
				score -= Math.pow(patternsCounter[B_BROKEN_THREE], 1.5)*SCORE_BROKEN_THREE;
				score -= Math.pow(patternsCounter[B_CLOSED_THREE], 1.5)*SCORE_CLOSED_THREE;
				score -= Math.pow(patternsCounter[B_SIDE_THREE], 1.5)*SCORE_SIDE_THREE;
				
				score -= Math.pow(patternsCounter[B_OPEN_FOUR], 3)*SCORE_OPEN_FOUR;
				score -= Math.pow(patternsCounter[B_CLOSED_FOUR], 2)*SCORE_CLOSED_FOUR;
				score -= Math.pow(patternsCounter[B_BROKEN_FOUR], 2)*SCORE_BROKEN_FOUR;
				score -= Math.pow(patternsCounter[B_SIDE_FOUR], 2)*SCORE_SIDE_FOUR;
			}
		
		} else { // Noirs
			// Victoire
			if(patternsCounter[B_WIN] >= 1) {
				score += SCORE_WIN;
			}
			// Défaite
			else if(patternsCounter[W_WIN] >= 1) {
				score -= SCORE_WIN;
			}
			else {
				score -= Math.pow(patternsCounter[W_OPEN_THREE], 2)*SCORE_OPEN_THREE;
				score -= Math.pow(patternsCounter[W_BROKEN_THREE], 1.5)*SCORE_BROKEN_THREE;
				score -= Math.pow(patternsCounter[W_CLOSED_THREE], 1.5)*SCORE_CLOSED_THREE;
				score -= Math.pow(patternsCounter[W_SIDE_THREE], 1.5)*SCORE_SIDE_THREE;
				
				score -= Math.pow(patternsCounter[W_OPEN_FOUR], 3)*SCORE_OPEN_FOUR;
				score -= Math.pow(patternsCounter[W_CLOSED_FOUR], 2)*SCORE_CLOSED_FOUR;
				score -= Math.pow(patternsCounter[W_BROKEN_FOUR], 2)*SCORE_BROKEN_FOUR;
				score -= Math.pow(patternsCounter[W_SIDE_FOUR], 2)*SCORE_SIDE_FOUR;
				
				
				score += Math.pow(patternsCounter[B_OPEN_THREE], 2)*SCORE_OPEN_THREE;
				score += Math.pow(patternsCounter[B_BROKEN_THREE], 1.5)*SCORE_BROKEN_THREE;
				score += Math.pow(patternsCounter[B_CLOSED_THREE], 1.5)*SCORE_CLOSED_THREE;
				score += Math.pow(patternsCounter[B_SIDE_THREE], 1.5)*SCORE_SIDE_THREE;
				
				score += Math.pow(patternsCounter[B_OPEN_FOUR], 3)*SCORE_OPEN_FOUR;
				score += Math.pow(patternsCounter[B_CLOSED_FOUR], 2)*SCORE_CLOSED_FOUR;
				score += Math.pow(patternsCounter[B_BROKEN_FOUR], 2)*SCORE_BROKEN_FOUR;
				score += Math.pow(patternsCounter[B_SIDE_FOUR], 2)*SCORE_SIDE_FOUR;
			}
		}
		
		return score;
	}
	
	
	// Ordinal() permet de correspondre l'ordre des TileState a un entier
	
	private int convint_horiz(int x, int y, GomokuBoard board) {
	    return 243 * board.get(x, y).ordinal() + 81 * board.get(x+1, y).ordinal() + 27 * board.get(x+2, y).ordinal() + 9 * board.get(x+3, y).ordinal() + 3 * board.get(x+4, y).ordinal() + board.get(x+5, y).ordinal();
	}
	private int convint_vert(int x, int y, GomokuBoard board) {
		return 243 * board.get(x, y).ordinal() + 81 * board.get(x, y+1).ordinal() + 27 * board.get(x, y+2).ordinal() + 9 * board.get(x, y+3).ordinal() + 3 * board.get(x, y+4).ordinal() + board.get(x, y+5).ordinal();
	}
	private int convint_diagSE(int x, int y, GomokuBoard board) {
		return 243 * board.get(x, y).ordinal() + 81 * board.get(x+1, y+1).ordinal() + 27 * board.get(x+2, y+2).ordinal() + 9 * board.get(x+3, y+3).ordinal() + 3 * board.get(x+4, y+4).ordinal() + board.get(x+5, y+5).ordinal();
	}
	private int convint_diagSW(int x, int y, GomokuBoard board) {
		return 243 * board.get(x, y).ordinal() + 81 * board.get(x-1, y+1).ordinal() + 27 * board.get(x-2, y+2).ordinal() + 9 * board.get(x-3, y+3).ordinal() + 3 * board.get(x-4, y+4).ordinal() + board.get(x-5, y+5).ordinal();
	}
}

