package projet1.gomoku.controllers.ai;

import java.util.ArrayList;


import projet1.gomoku.controllers.eval.EvalFunction;
import projet1.gomoku.gamecore.Coords;
import projet1.gomoku.gamecore.GomokuBoard;
import projet1.gomoku.gamecore.Pair;
import projet1.gomoku.gamecore.enums.Player;
import projet1.gomoku.gamecore.enums.TileState;
import projet1.gomoku.gamecore.enums.WinnerState;



/**
 * Classe équivalente à AI_MinMaxAB2LS_Opti, mais héritant de Thread et adaptée à cette utilisation
 */
public class Thread_MinMaxAB2LSO_Iterative extends Thread {
	private int nbNodeLeafEvaluated = 0;
	private int[][] boundaryBoard;
	private EvalFunction eval;
	private int depthMax;
	private GomokuBoard board;
	private Player player;
	private Coords bestCoords;
	private boolean threadFinishedCleanly;
	private volatile boolean running = true;
	
	public Thread_MinMaxAB2LSO_Iterative(int depthMax, EvalFunction eval, GomokuBoard board, Player player){
        this.eval = eval;
        this.depthMax = depthMax;
        this.board = board;
        this.player = player;
        bestCoords = new Coords();
        threadFinishedCleanly = false;
    }

    private int minMax(GomokuBoard board, int depth, Player minMaxPlayer, Player player, int alpha, int beta) {
    	
    	if(!running) {
    		return 0;
    	}
    	
    	ArrayList<Pair> tab = new ArrayList<>();
    	
    	int coef = player == minMaxPlayer ? 1 : -1;
    	Player inverseMinMaxPlayer = minMaxPlayer == Player.White ? Player.Black : Player.White;
        if (depth == 0 || board.getWinnerState() != WinnerState.None) {
        	nbNodeLeafEvaluated += 1;
        	return coef*(eval.evaluateBoard(board, player)+2*depth); // On rajoute depth afin de privilégier les victoire rapide aux longues
        }
        int value = Integer.MIN_VALUE + 1; //nbr max
        Coords currentCellCoords = new Coords();
        
        TileState playerCellState = minMaxPlayer == Player.White ? TileState.White : TileState.Black;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (boundaryBoard[currentCellCoords.column][currentCellCoords.row] > 0){ // Si la case est vide
                    if(depth > 1) {
                    	board.set(currentCellCoords, playerCellState); // Jouer le coup
	                    int tempValue = eval.evaluateBoard(board, minMaxPlayer); // Evaluer le coup
	                    board.set(currentCellCoords, TileState.Empty); // Annuler le coup
	                    
	                    tab.add(new Pair(tempValue, currentCellCoords.clone()));
	                    
                    }
                    else {
                    	tab.add(new Pair(0,currentCellCoords.clone()));
                    }
                }
                
            }
        }
        tab.sort((Pair a,Pair b) -> b.getScore().compareTo(a.getScore()));
        
        for (Pair pair : tab) {
        	
        	board.set(pair.getCoords(), playerCellState); // Jouer le coup
            addPlayableSquares(pair.getCoords().column, pair.getCoords().row);
            value = Math.max(value, -minMax(board,depth-1, inverseMinMaxPlayer, player, -beta, -alpha)); // Evaluer le coup
            board.set(pair.getCoords(), TileState.Empty); // Annuler le coup
            removePlayableSquares(pair.getCoords().column, pair.getCoords().row);
            
        	if(value >= beta) {
            	return value;
            }
            alpha = Math.max(alpha, value); 
		}
        	
        return value;
    }
    
    
    
    
    
    public void startMinMax(){
    	nbNodeLeafEvaluated = 0;
    	ArrayList<Pair> tab = new ArrayList<>();
    	
    	int alpha = Integer.MIN_VALUE + 1;
    	int beta  = -alpha;
    	
    	initBoundaryBoard(board);
        Coords currentCellCoords = new Coords();
        int bestScore = Integer.MIN_VALUE + 1;
        TileState playerCellState = player == Player.White ? TileState.White : TileState.Black;
        
        Player inversePlayer = player == Player.White ? Player.Black : Player.White;
        for (currentCellCoords.row = 0; currentCellCoords.row < GomokuBoard.size; currentCellCoords.row++){
            for (currentCellCoords.column = 0; currentCellCoords.column < GomokuBoard.size; currentCellCoords.column++){
                if (boundaryBoard[currentCellCoords.column][currentCellCoords.row] > 0){ // Si la case est vide
                    
                	 board.set(currentCellCoords, playerCellState); // Jouer le coup
                     int score = eval.evaluateBoard(board, player); // Evaluer le coup
                     board.set(currentCellCoords, TileState.Empty); // Annuler le coup              
                     
                     tab.add(new Pair(score, currentCellCoords.clone()));
                    
                    
                }
            }
        }
        
        tab.sort((Pair a,Pair b) -> b.getScore().compareTo(a.getScore()));
        
        int score = 0;
        for (Pair pair : tab) {
        	
        	board.set(pair.getCoords(), playerCellState); // Jouer le coup
            addPlayableSquares(pair.getCoords().column, pair.getCoords().row);
            score = -minMax(board,depthMax-1, inversePlayer, player, -beta, -alpha); // Evaluer le coup
            board.set(pair.getCoords(), TileState.Empty); // Annuler le coup
            removePlayableSquares(pair.getCoords().column, pair.getCoords().row);
        
	        if(score > bestScore) {
	        	bestScore = score;
	        	bestCoords = pair.getCoords().clone();
	        }
	        if(bestScore >= beta) {
	        	break;
	        }
	        alpha = Math.max(alpha, bestScore); 
		}
        
    }
    
    public void run() {
    	startMinMax();
    	if(running) {
    		threadFinishedCleanly = true;
    	}
    	
    }
    
    public void terminate() {
        running = false;
    }
    
	
	public int getNbNodeLeafEvaluated() {
		return nbNodeLeafEvaluated;
	}

	public Coords getBestCoords() {
		return bestCoords;
	}
	
	
	public boolean isThreadFinishedCleanly() {
		return threadFinishedCleanly;
	}

	private void initBoundaryBoard(GomokuBoard board) {
		boundaryBoard = new int[15][15];
		boundaryBoard[7][7] = 1; // autorise une case à jouer (si on commence la partie)
		for(int x = 0; x < 15; x++) {
			for(int y = 0; y < 15; y++) {
				if(board.get(x, y) != TileState.Empty) {
					addPlayableSquares(x, y);	
				}
			}
		}
	}
	
	private void addPlayableSquares(int xOrig, int yOrig) {
		for(int x = xOrig-2; x <= xOrig+2; x++) {
			for(int y = yOrig-2; y <= yOrig+2; y++) {
				if(x >= 0 && x < 15 && y >= 0 && y < 15) {
					boundaryBoard[x][y] += 1;
				}
			}
		}
		boundaryBoard[xOrig][yOrig] -= 2000;
	}
	
	private void removePlayableSquares(int xOrig, int yOrig) {
		for(int x = xOrig-2; x <= xOrig+2; x++) {
			for(int y = yOrig-2; y <= yOrig+2; y++) {
				if(x >= 0 && x < 15 && y >= 0 && y < 15) {
					boundaryBoard[x][y] -= 1;
				}
			}
		}
		boundaryBoard[xOrig][yOrig] += 2000;
	}
}

