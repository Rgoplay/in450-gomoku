package projet1.gomoku.test;

import projet1.gomoku.gamecore.Array2DFixed;
import projet1.gomoku.gamecore.enums.TileState;

public class EvalFuncTest {

	public static void main(String[] args) {
		

	}
	
	private static Array2DFixed stringToBoard(String[] s) {
		Array2DFixed board = new Array2DFixed(15, 15);
		
		for(int x = 0; x < 15; x++) {
			for(int y = 0; y < 15; y++) {
				if(s[x].charAt(y) == 'W') {
					board.set(x, y, TileState.White);
				} 
				else if(s[x].charAt(y) == 'B') {
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
