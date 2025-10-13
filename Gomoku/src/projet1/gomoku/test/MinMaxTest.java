package projet1.gomoku.test;

import java.util.Random;

import projet1.gomoku.controllers.ai.AI_MinMax;
import projet1.gomoku.controllers.ai.AI_MinMaxAB;
import projet1.gomoku.controllers.ai.AI_MinMaxAB2LS_Opti;
import projet1.gomoku.controllers.ai.AI_MinMaxAB2L_Sorted;
import projet1.gomoku.controllers.ai.AI_MinMaxAB_2Limit;
import projet1.gomoku.controllers.eval.Pattern6;
import projet1.gomoku.controllers.eval.PatternEval;
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
		
		for(int i = 0; i < 24; i++) {
			int x = new Random().nextInt(0, 14);
	        int y = new Random().nextInt(0, 14);
	        while(board.get(x, y) != TileState.Empty) {
	        	x = new Random().nextInt(0, 14);
		        y = new Random().nextInt(0, 14);
	        }
	
	        board.set(x,y, TileState.White);
	        
	        x = new Random().nextInt(0, 14);
	        y = new Random().nextInt(0, 14);
	        while(board.get(x, y) != TileState.Empty) {
	        	x = new Random().nextInt(0, 14);
		        y = new Random().nextInt(0, 14);
	        }
	
	        board.set(x,y, TileState.Black);
		}
		
		board.print();
		//Coords move = new AI_MinMax(3,new PatternEval()).play(board, Player.White);
		//System.out.println("1 | Column: " + move.column + " Row: " + move.row);
		//move = new AI_MinMaxAB(3,new PatternEval()).play(board, Player.White);
		//System.out.println("2 | Column: " + move.column + " Row: " + move.row);
		Coords move = new AI_MinMaxAB_2Limit(3,new PatternEval()).play(board, Player.White);
		System.out.println("3 | Column: " + move.column + " Row: " + move.row);
		move = new AI_MinMaxAB2L_Sorted(3,new PatternEval()).play(board, Player.White);
		System.out.println("4 | Column: " + move.column + " Row: " + move.row);
		move = new AI_MinMaxAB2LS_Opti(3,new PatternEval()).play(board, Player.White);
		System.out.println("5 | Column: " + move.column + " Row: " + move.row);
		move = new AI_MinMaxAB2LS_Opti(3,new Pattern6()).play(board, Player.White);
		System.out.println("6 | Column: " + move.column + " Row: " + move.row);
		int score = new PatternEval().evaluateBoard(board, Player.White);
		System.out.println("PatternEval: " + score);
		score = new Pattern6().evaluateBoard(board, Player.White);
		System.out.println("Pattern6: " + score);
		
	}

}
