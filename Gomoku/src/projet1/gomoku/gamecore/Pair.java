package projet1.gomoku.gamecore;

/**
 * Classe représentant une paire score, coordonnées
 */
public class Pair {
	private Integer score;
	private Coords coords;
	
	public Pair(Integer score, Coords coords) {
		super();
		this.score = score;
		this.coords = coords;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Coords getCoords() {
		return coords;
	}

	public void setCoords(Coords coords) {
		this.coords = coords;
	}

	
	
}
