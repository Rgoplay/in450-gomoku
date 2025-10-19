package projet1.gomoku.genetic;

import java.util.Random;

import projet1.gomoku.controllers.AIPlayer;
import projet1.gomoku.controllers.ai.AI_MinMaxAB2LS_Opti;
import projet1.gomoku.controllers.eval.PatternEvalW6;
import projet1.gomoku.controllers.eval.PatternEvalGenetic;
import projet1.gomoku.controllers.eval.RandomEval;
import projet1.gomoku.controllers.eval.WinLossEval;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

class FitnessCalculator {
	/**Lancer 100 parties pour evaluer un individu
     * @param player1 
	 * @return 
    */
    static int calculateFitness(Individual ind){
    	
    	int player2Victory = 0;
    	int player2Defeat = 0;
    	int nbTie = 0;
    	
    	AIPlayer player1 = new AI_MinMaxAB2LS_Opti(2, new PatternEvalW6());
    	AIPlayer player2 = new AI_MinMaxAB2LS_Opti(2, new PatternEvalGenetic(ind.getGenes()));
    	
    	for(int i = 0; i < 50; i++) {
    		
    		if(i < 6) {
    			player1 = new AI_MinMaxAB2LS_Opti(2, new RandomEval());
    		} else if(i < 15) {
    			player1 = new AI_MinMaxAB2LS_Opti(3, new WinLossEval());
    		} else {
    			player1 = new AI_MinMaxAB2LS_Opti(2, new PatternEvalW6());
    		}
    		
	        GomokuBoard board = new GomokuBoard();
	        WinnerState winnerState;
	        Player playerIndividu = Math.random() < 0.5 ? Player.White : Player.Black;
	        Player oldEval = playerIndividu == Player.White ? Player.Black : Player.White;
	        
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
	        	
	            Coords move = currentPlayer == playerIndividu ? player2.startMinMax(board, playerIndividu) : player1.startMinMax(board, oldEval);
	            
	            board.set(move, currentPlayer == Player.White ? TileState.White : TileState.Black); // Jouer le coup
	
	            currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White; // Changer de joueur
	        }
	
	        if (winnerState == WinnerState.Tie) {
	        	nbTie++;
	        }
	        else {
	        	WinnerState winStateIndividu = playerIndividu == Player.White ? WinnerState.White : WinnerState.Black;
	        	if(winnerState == winStateIndividu) {
	        		player2Victory++;   		
	        	} else {
	        		player2Defeat++;
	        	}
	        }
	
    	}
    	
    	return 10*(player2Victory-player2Defeat) + nbTie;
    }
    
    
}

