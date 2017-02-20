

public abstract class AdverserialSearch {

	public abstract Move adverserialSearch(Move current, int depth, boolean maxPlayer);
	
	
	public int getEvaluation(Move move, boolean maxPlayer){
		String player;
		String opponent;
		int value = 0;
		player = homework.SELF;
		opponent = homework.OPPONENT;
		if (move != null){
			Cell[][] cells = move.getCells();
			for (int i = 0; i <  cells.length; i++){
				for (int j = 0; j <  cells.length; j++){
					if (cells[i][j].getPlayer().equals(player)){
						value = value + cells[i][j].getValue();
					}
					
					if (cells[i][j].getPlayer().equals(opponent)){
						value = value - cells[i][j].getValue();
					}
				}
			}
		}
		return value;
	}
	
	public boolean isGameOver(Cell[][] cells){
		int n = cells.length;
		for (int i = 0; i < n; i++){
			for ( int j = 0; j < n; j++){
				if (cells[i][j].getPlayer().equals(homework.FREE)){
					return false;
				}
			}
		}
		return true;
	}
	
	
}
