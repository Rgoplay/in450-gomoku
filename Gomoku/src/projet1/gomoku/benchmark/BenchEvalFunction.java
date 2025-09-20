package projet1.gomoku.benchmark;

import projet1.gomoku.controllers.PlayerController;
import projet1.gomoku.controllers.ai.AI_Depth1;
import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.controllers.eval.PatternEval;
import projet1.gomoku.controllers.eval.WinLossEval;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;

public class BenchEvalFunction {
	    
    /**Lancer un benchmark de perfromance d'une fonction d'évaluation
    */
    public static void startBench(EvalFunction eval, int nbOfRuns){
        GomokuBoard board = new GomokuBoard();
        Player currentPlayer = Player.White;
        PlayerController p = new AI_Depth1(0, new WinLossEval());
        int evalScore = 0;

        while (board.getWinnerState() == WinnerState.None){ // Tant que la partie n'est pas finie
            Coords move = currentPlayer == Player.White ? p.play(board, Player.White) : p.play(board, Player.Black); // Obtenir le coup du joueur

            board.set(move, currentPlayer == Player.White ? TileState.White : TileState.Black); // Jouer le coup

            currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White; // Changer de joueur
        }
        
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < nbOfRuns; i++) {
        	evalScore = eval.evaluateBoard(board, currentPlayer);
        }
        long functTotalEvalTime = System.currentTimeMillis() - startTime;
        double avgTime = ((double)functTotalEvalTime)/((double)nbOfRuns); 

        System.out.println("Benchmark :");
        System.out.println("Eval: " + evalScore);
        System.out.printf("Temps moyen: %f us", avgTime*1000);// on passe en us
        board.print();
        
    }

    public static void main(String[] args) {
        startBench(new PatternEval(), 1000000);
    }


}
