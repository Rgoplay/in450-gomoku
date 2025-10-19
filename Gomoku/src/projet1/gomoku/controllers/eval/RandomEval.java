package projet1.gomoku.controllers.eval;

import java.util.Random;

import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.enums.Player;

/**
 * Fonction d'évaluation aléatoire
 */
public class RandomEval extends EvalFunction	{

    public int evaluateBoard(GomokuBoard board, Player player){
        /* Il y a sans doute des choses à modifier ici*/
        return new Random().nextInt(2000);
    }


}
