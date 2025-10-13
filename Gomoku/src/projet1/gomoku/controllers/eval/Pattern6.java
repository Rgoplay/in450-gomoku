package projet1.gomoku.controllers.eval;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

public class Pattern6 extends EvalFunction {
	
	private int[] ids = new int[729]; // 3^6 patterns possibles
	
	// Constants
	// Ne commence pas à 0 car la valeur par défaut du tableau est 0
	private static final int W_WIN = 1;
	private static final int W_CLOSED_FOUR = 2;
	private static final int W_BROKEN_FOUR = 3;
	private static final int W_SIDE_FOUR = 4;
	private static final int W_OPEN_THREE = 5;
	private static final int W_CLOSED_THREE = 6;
	private static final int W_BROKEN_THREE = 7;
	private static final int W_SIDE_THREE = 8;
	
	private static final int B_WIN = 9;
	private static final int B_CLOSED_FOUR = 10;
	private static final int B_BROKEN_FOUR = 11;
	private static final int B_SIDE_FOUR = 12;
	private static final int B_OPEN_THREE = 13;
	private static final int B_CLOSED_THREE = 14;
	private static final int B_BROKEN_THREE = 15;
	private static final int B_SIDE_THREE = 16;
	
	private static final int SCORE_WIN = 999999999;
	private static final int SCORE_BROKEN_FOUR = 50000;
	private static final int SCORE_CLOSED_FOUR = 50000;
	private static final int SCORE_SIDE_FOUR = 50000;
	private static final int SCORE_OPEN_THREE = 10000;
	private static final int SCORE_BROKEN_THREE = 1000;
	private static final int SCORE_CLOSED_THREE = 1000;
	private static final int SCORE_SIDE_THREE = 1000;
	
	public Pattern6() {

		// Les coups recoivent un id en fonction de leur force
		
		// Blancs
	    // 5 alignés
		ids[encode(0,1,1,1,1,1)] = W_WIN;
		ids[encode(1,1,1,1,1,0)] = W_WIN;
		
		// 4
		ids[encode(0,1,1,1,1,0)] = W_WIN;
		ids[encode(2,1,1,1,1,0)] = W_CLOSED_FOUR;
		ids[encode(0,1,1,1,1,2)] = W_CLOSED_FOUR;
		ids[encode(0,1,1,1,0,1)] = W_BROKEN_FOUR;
		ids[encode(1,0,1,1,1,0)] = W_BROKEN_FOUR;
		ids[encode(1,1,1,1,0,0)] = W_SIDE_FOUR;
		ids[encode(0,0,1,1,1,1)] = W_SIDE_FOUR;
		
		// 3
		ids[encode(0,0,1,1,1,0)] = W_OPEN_THREE;
		ids[encode(0,1,1,1,0,0)] = W_OPEN_THREE;
		ids[encode(2,1,1,1,0,0)] = W_CLOSED_THREE;
		ids[encode(0,0,1,1,1,2)] = W_CLOSED_THREE;
		ids[encode(0,1,1,0,1,0)] = W_BROKEN_THREE;
		ids[encode(0,1,0,1,1,0)] = W_BROKEN_THREE;
		ids[encode(1,1,1,0,0,0)] = W_SIDE_THREE;
		ids[encode(0,0,0,1,1,1)] = W_SIDE_THREE;
		
		
		// Noirs
	    // 5 alignés
		ids[encode(0,2,2,2,2,2)] = B_WIN;
		ids[encode(2,2,2,2,2,0)] = B_WIN;
		
		// 4
		ids[encode(0,2,2,2,2,0)] = B_WIN;
		ids[encode(1,2,2,2,2,0)] = B_CLOSED_FOUR;
		ids[encode(0,2,2,2,2,1)] = B_CLOSED_FOUR;
		ids[encode(0,2,2,2,0,2)] = B_BROKEN_FOUR;
		ids[encode(2,0,2,2,2,0)] = B_BROKEN_FOUR;
		ids[encode(0,2,2,2,2,2)] = B_SIDE_FOUR;
		ids[encode(2,2,2,2,2,0)] = B_SIDE_FOUR;
		
		// 1
		ids[encode(0,0,2,2,2,0)] = B_OPEN_THREE;
		ids[encode(0,2,2,2,0,0)] = B_OPEN_THREE;
		ids[encode(1,2,2,2,0,0)] = B_CLOSED_THREE;
		ids[encode(0,0,2,2,2,1)] = B_CLOSED_THREE;
		ids[encode(0,2,2,0,2,0)] = B_BROKEN_THREE;
		ids[encode(0,2,0,2,2,0)] = B_BROKEN_THREE;
		ids[encode(2,2,2,0,0,0)] = B_SIDE_THREE;
		ids[encode(0,0,0,2,2,2)] = B_SIDE_THREE;
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
		int[] patternsCounter = new int[17];
		for(int y = 0; y <= 9; y++) {
            for(int x = 0; x <= 9; x++) {
            	
            	patternsCounter[getId(convint_horiz(x,y, board))] += 1;
              
                patternsCounter[getId(convint_vert(x,y, board))] += 1;

                patternsCounter[getId(convint_diagSE(x,y, board))] += 1;

                // de 4 à 14 en x
                patternsCounter[getId(convint_diagSW(x+5,y, board))] += 1;
                
                if(x <= 9 && x >= 5 && y >= 5 && y <= 9) {
                	if(board.get(x, y) == playerTile) {
                		score += 1;
                	} else if(board.get(x, y) == enemyTile) {
                		score -= 1;
                	}
                }
            }
		}
		
		// Calcul du score
		// Attention les combinaisons de 3 et de 4 peuvent etre confondue (memes pièces)
		if(player == Player.White) { // Blancs
			// Victoire
			if((patternsCounter[W_WIN] + patternsCounter[W_CLOSED_FOUR] + patternsCounter[W_BROKEN_FOUR] + patternsCounter[W_SIDE_FOUR]) >= 1 || patternsCounter[W_OPEN_THREE] >= 2) {
				score += SCORE_WIN;
			}
			// Défaite
			else if(patternsCounter[B_WIN] >= 1 || (patternsCounter[B_CLOSED_FOUR] + patternsCounter[B_BROKEN_FOUR] + patternsCounter[B_SIDE_FOUR]) >= 2 || patternsCounter[B_OPEN_THREE] >= 2) {
				score -= SCORE_WIN;
			}
			else {
				score += patternsCounter[W_OPEN_THREE]*SCORE_OPEN_THREE;
				score += patternsCounter[W_BROKEN_THREE]*SCORE_BROKEN_THREE;
				score += patternsCounter[W_CLOSED_THREE]*SCORE_CLOSED_THREE;
				score += patternsCounter[W_SIDE_THREE]*SCORE_SIDE_THREE;
				
				score -= patternsCounter[B_OPEN_THREE]*SCORE_OPEN_THREE;
				score -= patternsCounter[B_BROKEN_THREE]*SCORE_BROKEN_THREE;
				score -= patternsCounter[B_CLOSED_THREE]*SCORE_CLOSED_THREE;
				score -= patternsCounter[B_SIDE_THREE]*SCORE_SIDE_THREE;
				score -= patternsCounter[B_CLOSED_FOUR]*SCORE_CLOSED_FOUR;
				score -= patternsCounter[B_BROKEN_FOUR]*SCORE_BROKEN_FOUR;
				score -= patternsCounter[B_SIDE_FOUR]*SCORE_SIDE_FOUR;
			}
		
		} else { // Noirs
			// Victoire
			if((patternsCounter[B_WIN] + patternsCounter[B_CLOSED_FOUR] + patternsCounter[B_BROKEN_FOUR] + patternsCounter[B_SIDE_FOUR]) >= 1 || patternsCounter[B_OPEN_THREE] >= 2) {
				score += SCORE_WIN;
			}
			// Défaite
			else if(patternsCounter[W_WIN] >= 1 || (patternsCounter[W_CLOSED_FOUR] + patternsCounter[W_BROKEN_FOUR] + patternsCounter[W_SIDE_FOUR]) >= 2 || patternsCounter[W_OPEN_THREE] >= 2) {
				score -= SCORE_WIN;
			}
			else {
				score -= patternsCounter[W_OPEN_THREE]*SCORE_OPEN_THREE;
				score -= patternsCounter[W_BROKEN_THREE]*SCORE_BROKEN_THREE;
				score -= patternsCounter[W_CLOSED_THREE]*SCORE_CLOSED_THREE;
				score -= patternsCounter[W_SIDE_THREE]*SCORE_SIDE_THREE;
				score -= patternsCounter[W_CLOSED_FOUR]*SCORE_CLOSED_FOUR;
				score -= patternsCounter[W_BROKEN_FOUR]*SCORE_BROKEN_FOUR;
				score -= patternsCounter[W_SIDE_FOUR]*SCORE_SIDE_FOUR;
				
				score += patternsCounter[B_OPEN_THREE]*SCORE_OPEN_THREE;
				score += patternsCounter[B_BROKEN_THREE]*SCORE_BROKEN_THREE;
				score += patternsCounter[B_CLOSED_THREE]*SCORE_CLOSED_THREE;
				score += patternsCounter[B_SIDE_THREE]*SCORE_SIDE_THREE;
			}
		}
		
		return score;
	}
	
	
	// Ordinal() permet de correspondre l'ordre des TileState a un entier
	
	private int convint_horiz(int x, int y, GomokuBoard board) {
	    return board.get(x, y).ordinal() + board.get(x+1, y).ordinal() * 3 + board.get(x+2, y).ordinal() * 9 + board.get(x+3, y).ordinal() * 27 + board.get(x+4, y).ordinal() * 81 + board.get(x+5, y).ordinal() * 243;
	}
	private int convint_vert(int x, int y, GomokuBoard board) {
		return board.get(x, y).ordinal() + board.get(x, y+1).ordinal() * 3 + board.get(x, y+2).ordinal() * 9 + board.get(x, y+3).ordinal() * 27 + board.get(x, y+4).ordinal() * 81 + board.get(x, y+5).ordinal() * 243;
	}
	private int convint_diagSE(int x, int y, GomokuBoard board) {
		return board.get(x, y).ordinal() + board.get(x+1, y+1).ordinal() * 3 + board.get(x+2, y+2).ordinal() * 9 + board.get(x+3, y+3).ordinal() * 27 + board.get(x+4, y+4).ordinal() * 81 + board.get(x+5, y+5).ordinal() * 243;
	}
	private int convint_diagSW(int x, int y, GomokuBoard board) {
		return board.get(x, y).ordinal() + board.get(x-1, y+1).ordinal() * 3 + board.get(x-2, y+2).ordinal() * 9 + board.get(x-3, y+3).ordinal() * 27 + board.get(x-4, y+4).ordinal() * 81 + board.get(x-5, y+5).ordinal() * 243;
	}
}

