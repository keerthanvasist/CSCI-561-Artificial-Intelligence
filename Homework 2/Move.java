
public class Move {
	private Cell[][] cells;
	private int value;
	private String moveType;
	private Cell to;
	private int alpha;
	private int beta;
	
	
	
	
	public int getAlpha() {
		return alpha;
	}
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	public int getBeta() {
		return beta;
	}
	public void setBeta(int beta) {
		this.beta = beta;
	}
	public Cell[][] getCells() {
		return cells;
	}
	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
	public String getMoveType() {
		return moveType;
	}
	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}
	
	
	
	
	public Cell getTo() {
		return to;
	}
	public void setTo(Cell to) {
		this.to = to;
	}
	
}
