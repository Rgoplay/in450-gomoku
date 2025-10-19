package projet1.gomoku.controllers.ai;

import java.util.concurrent.TimeUnit;

import projet1.gomoku.controllers.AIPlayer;
import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;


/**
 * Classe représentant une approche itérative de la recherche et qui lance successivement un minmax de profondeur croissante
 * Le meilleur coup trouvé est retourné après un certains temps
 */
public class AI_MinMaxAB2LSO_Iterative extends AIPlayer {
	private int maxSearchTimeMs;
	
	public AI_MinMaxAB2LSO_Iterative(int minimaxDepth, EvalFunction eval){
        super(minimaxDepth, eval);
        maxSearchTimeMs = 25000;
    }
	
	
	public AI_MinMaxAB2LSO_Iterative(int minimaxDepth, EvalFunction eval, int maxSearchTimeMs){
        super(minimaxDepth, eval);
        this.maxSearchTimeMs = maxSearchTimeMs;
    }
    
    
    public Coords startMinMax(GomokuBoard board, Player player){
    	return iterativeDeepening(board, player, false);
    }

	// Note: On ne sauvegarde pas l'arbre entre deux recherches
    // Ne prends pas en compte minmaxDepth
    public Coords iterativeDeepening(GomokuBoard board, Player player, boolean print) {
    	
    	long startTime = System.currentTimeMillis();
    	long timeElapsed = 0;
    	int nbNodeEvaluated = 0;
    	
    	int depth = 3; // profondeur minimale de 3
    	if(print) {
    		System.out.printf("Running | Depth %d | Nb node: %d\r", depth, nbNodeEvaluated);
    	}
    	
    	Thread_MinMaxAB2LSO_Iterative thread = new Thread_MinMaxAB2LSO_Iterative(depth, eval, board, player);
    	thread.start();
    	
    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error in thread, using fallback function");
			return new AI_MinMaxAB2LS_Opti(3, eval).play(board, player);
		}
    	
    	Coords bestCoords = thread.getBestCoords();
    	nbNodeEvaluated += thread.getNbNodeLeafEvaluated();
    	
    	timeElapsed = System.currentTimeMillis() - startTime;
    	
    	while(timeElapsed < maxSearchTimeMs && depth < 25) {
    		if(!thread.isAlive()) {
    			if(thread.isThreadFinishedCleanly()) {
    				depth += 1;
	    			nbNodeEvaluated += thread.getNbNodeLeafEvaluated();
	    			bestCoords = thread.getBestCoords();
	    			
	    			if(depth < 25) {
	    				thread = new Thread_MinMaxAB2LSO_Iterative(depth, eval, board, player);
	    				thread.start();
	    			}
    			}
    			else { // Si le thread a planté
    				System.out.println("Error in thread, stopping deepening");
    				return bestCoords;
    			}
    			
    		}
    		if(print) {
        		System.out.printf("Running (%.1fs) | Searching depth %d | Nb node current: % ,10d | Nb node total: % ,10d\r", (float) (timeElapsed/1000.0), depth, thread.getNbNodeLeafEvaluated(), nbNodeEvaluated);
        	}
    		
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {	
				e.printStackTrace();
			}
    		
    		timeElapsed = System.currentTimeMillis() - startTime;
    	}
    		
    	
    	try {
    		thread.terminate();
			thread.join(200); // Attend 200ms que le thread se termine
		} catch (InterruptedException e) {
			System.out.println("Error in while terminating thread");
		}
    	
    	if(print) {
    		System.out.printf("Finished | Depth %d | Nb node total: % ,10d						\n", depth-1, nbNodeEvaluated);	
    	}
    	
    	return bestCoords;
    }
  
    
    
    @Override
	public Coords play(GomokuBoard board, Player player) {

		return iterativeDeepening(board, player, true);
	}
	
}

