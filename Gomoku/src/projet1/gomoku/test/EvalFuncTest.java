package projet1.gomoku.test;

import projet1.gomoku.controllers.eval.PatternEval;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

public class EvalFuncTest {

	public static void main(String[] args) {
		String[] strBoard1 ={"       W       ",
							 "       W       ",
							 "       W       ",
							 "       WBB     ",
							 "      B        ",
							 "      B         ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               "};
		
		GomokuBoard board = stringToBoard(strBoard1);
		int patternScore = new PatternEval().evaluateBoard(board, Player.White);
		System.out.printf("1W | PatternEval %s\n", (patternScore == 20998)?"OK":"FAIL");
		patternScore = new PatternEval().evaluateBoard(board, Player.Black);
		System.out.printf("1B | PatternEval %s\n", (patternScore == -20998)?"OK":"FAIL");
		
		String[] strBoard2 ={"               ",
							 "        B      ",
							 "     W         ",
							 "   BW  BBB     ",
							 "   W    B      ",
							 "  W     B      ",
							 " BBB           ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               ",
							 "               "};
		
		board = stringToBoard(strBoard2);
		patternScore = new PatternEval().evaluateBoard(board, Player.White);
		System.out.printf("2W | PatternEval %s\n", (patternScore == -20502)?"OK":"FAIL");
		patternScore = new PatternEval().evaluateBoard(board, Player.Black);
		System.out.printf("2B | PatternEval %s\n", (patternScore == 20502)?"OK":"FAIL");
	}
	
	private static GomokuBoard stringToBoard(String[] s) {
		GomokuBoard board = new GomokuBoard();
		
		for(int y = 0; y < 15; y++) {
			for(int x = 0; x < 15; x++) {
				if(s[y].charAt(x) == 'W') {
					board.set(x, y, TileState.White);
				} 
				else if(s[y].charAt(x) == 'B') {
					board.set(x, y, TileState.Black);
				}
				else {
					board.set(x, y, TileState.Empty);
				}
			}
		}
		return board;
	}

}
