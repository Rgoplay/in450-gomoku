package projet1.gomoku.test;

import projet1.gomoku.controllers.ai.AI_MinMax;
import projet1.gomoku.controllers.eval.WinLossEval;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;

public class MinMaxTest {

	public static void main(String[] args) {
		testTakeWinnable();

	}
	
	public static void testTakeWinnable() {
		GomokuBoard board = new GomokuBoard();
		board.set(new Coords(1,1), TileState.White);
		board.set(new Coords(1,2), TileState.White);
		board.set(new Coords(1,3), TileState.White);
		board.set(new Coords(1,4), TileState.White);
		//board.set(new Coords(0,0), TileState.White);
		//board.set(new Coords(1,0), TileState.Black);
		board.print();
		Coords move = new AI_MinMax(3,new WinLossEval()).play(board, Player.White);
		System.out.println("Column: " + move.column + " Row: " + move.row);
	}

}
