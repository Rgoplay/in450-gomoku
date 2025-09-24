package projet1.gomoku.test;

import projet1.gomoku.gamecore.Array2DFixed;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.TileState;

public class EvalFuncTest {

	public static void main(String[] args) {
		String[] strBoard = {"       W       ",
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
		GomokuBoard board = stringToBoard(strBoard);
		board.print();

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
