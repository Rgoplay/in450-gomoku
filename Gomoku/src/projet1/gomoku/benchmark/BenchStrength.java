package projet1.gomoku.benchmark;

import java.util.Random;

import projet1.gomoku.controllers.AIPlayer;
import projet1.gomoku.controllers.ai.AI_MinMaxAB;
import projet1.gomoku.controllers.ai.AI_MinMaxAB2L_Sorted;
import projet1.gomoku.controllers.ai.AI_MinMaxAB_2Limit;
import projet1.gomoku.controllers.eval.PatternEval;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

public class BenchStrength {
	/**Lancer 100 parties entre deux joueurs de Gomoku, affiche le nb de victoires/defaites
     * @param player1 Joueur 1
     * @param player2 Joueur 2
	 * @return 
    */
    public static void startStrengthBench(AIPlayer player1, AIPlayer player2){
    	
    	int player1Victory = 0;
    	int player1Defeat = 0;
    	int player2Victory = 0;
    	int player2Defeat = 0;
    	int nbTie = 0;
    	
    	for(int i = 0; i < 100; i++) {
    		System.out.printf("Game %d/100\n", i);
	        GomokuBoard board = new GomokuBoard();
	        WinnerState winnerState;
	        
	        // On joue deux coups au hasard
	        int x = new Random().nextInt(0, 14);
	        int y = new Random().nextInt(0, 14);
	        board.set(x, y, TileState.White);
	        
	        x = new Random().nextInt(0, 14);
	        y = new Random().nextInt(0, 14);
	        while(board.get(x, y) == TileState.White) {
	        	x = new Random().nextInt(0, 14);
		        y = new Random().nextInt(0, 14);
	        }

	        board.set(x,y, TileState.Black);
	        
	        Player currentPlayer = Player.White;
	
	        while ((winnerState = board.getWinnerState()) == WinnerState.None){ // Tant que la partie n'est pas finie
	            Coords move = currentPlayer == Player.White ? player1.startMinMax(board, Player.White) : player2.startMinMax(board, Player.Black); // Obtenir le coup du joueur
	            if(move.column == -1) {
	            	board.print();
	            	System.out.println(currentPlayer);
	            }
	            board.set(move, currentPlayer == Player.White ? TileState.White : TileState.Black); // Jouer le coup
	
	            currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White; // Changer de joueur
	        }
	
	        if (winnerState == WinnerState.Tie) {
	        	nbTie++;
	        }
	        else {
	        	if(winnerState == WinnerState.White) {
	        		player1Victory++;
	        		player2Defeat++;
	        	} else {
	        		player1Defeat++;
	        		player2Victory++;
	        	}
	        }
	
    	}
    	
    	System.out.printf("P1 : V=%d D=%d\n", player1Victory, player1Defeat);
    	System.out.printf("P2 : V=%d D=%d\n", player2Victory, player2Defeat);
    	System.out.printf("Tie=%d\n", nbTie);
    }

    public static void main(String[] args) {
    	startStrengthBench(new AI_MinMaxAB(2,new PatternEval()), new AI_MinMaxAB2L_Sorted(2,new PatternEval())); // Lancer une partie entre un joueur humain et une IA Sweep
    }
}

/**
 * Résulatat notable: a meme profondeur (2) AI_MinMaxAB gagne contre AI_MinMaxAB_2Limit, si AI_MinMaxAB joue en premier, meme équitable si AI_MinMaxAB_2Limit joue en premier
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
*/
