import java.util.ArrayList;

public class MinimaxWithABPruninng extends AdverserialSearch{
	public int count = 0;

	@Override
	public Move adverserialSearch(Move current, int depth, boolean maxPlayer) {
		count++;		
		Move bestMove = null;
		if (depth == 0){
			return current;
		} else {
			ArrayList<ArrayList<Move>> moves = generateMoves(current, maxPlayer);
			if (moves != null){
				int bestSoFar;
				if (maxPlayer)
					bestSoFar = Integer.MIN_VALUE;
				else 
					bestSoFar = Integer.MAX_VALUE;
				for (int i = 0; i < moves.size();i++){
					for (int j = 0; j < moves.get(i).size();j++){
						if (current.getAlpha() < current.getBeta()){
							Move nextMove = moves.get(i).get(j);
							nextMove.setAlpha(current.getAlpha());
							nextMove.setBeta(current.getBeta());
							Move move = adverserialSearch(nextMove, depth-1, !maxPlayer);
							if (move != null){
								if (maxPlayer){
									if (move.getValue() > bestSoFar){
										bestMove  = moves.get(i).get(j);
										bestSoFar = move.getValue();
									}
									current.setAlpha(bestSoFar);
								} else {
									if (move.getValue() < bestSoFar){
										bestMove  = moves.get(i).get(j);
										bestSoFar = move.getValue();
									}
									current.setBeta(bestSoFar);
								}
							}
						} else {
						}
					}
				}
				bestMove.setValue(bestSoFar);
				System.out.println("Count is "+count);
				return bestMove;
			}
		}
		return current;
	}
	
	public ArrayList<ArrayList<Move>> generateMoves(Move current, boolean maxPlayer){
		if (!isGameOver(current.getCells())){
			ArrayList<Move> stakes = addStakesToQueue(current,maxPlayer);
			ArrayList<Move> raids = addRaidsToQueue(current,maxPlayer);
			ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
			moves.add(stakes);
			moves.add(raids);
			return moves;
		}
		return null;
	}
	
	private ArrayList<Move> addRaidsToQueue(Move current, boolean maxPlayer) {
		Cell[][] cells = current.getCells();
		int n = cells.length;
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (cells[i][j].getPlayer().equals(homework.FREE)){
					if (checkNeighboursForRaid(cells, i,j,maxPlayer)){
						String player = null;
						String opponent = null;
						Cell[][] newCells = new Cell[n][n];
						
						for (int k = 0; k < n; k++){
							for (int l = 0; l < n; l++){
								Cell cell = new Cell();
								cell.setCol(l);
								cell.setRow(k);
								cell.setPlayer(cells[k][l].getPlayer());
								cell.setValue(cells[k][l].getValue());
								newCells[k][l] = cell;
							}
						}
						
						
						if (maxPlayer){
							player = homework.SELF;
							opponent = homework.OPPONENT;
						} else {
							player = homework.OPPONENT;
							opponent = homework.SELF;
						}
						Move move = new Move();
						move.setTo(cells[i][j]);
						move.setMoveType(homework.RAID);
						
						if (i > 0){
							if (cells[i-1][j].getPlayer().equals(opponent)){
								//value = value + cells[i-1][j].getValue();
								newCells[i-1][j].setPlayer(player);
							}
						}
						
						if (j > 0){
							if (cells[i][j-1].getPlayer().equals(opponent)){
								//value = value + cells[i][j-1].getValue();
								newCells[i][j-1].setPlayer(player);
									
							}
						}
						
						if (i < n-1){
							if (cells[i+1][j].getPlayer().equals(opponent)){
								//value = value + cells[i+1][j].getValue();
								newCells[i+1][j].setPlayer(player);
							}
						}
						
						if (j < n-1){
							if (cells[i][j+1].getPlayer().equals(opponent)){
								newCells[i][j+1].setPlayer(player);
							}
						}
						newCells[i][j].setPlayer(player);
						move.setCells(newCells);
						move.setValue(getEvaluation(move, maxPlayer));
						moves.add(move);
					}
				}
			}
		}
		return moves;
	}

	public ArrayList<Move> addStakesToQueue(Move current,boolean maxPlayer){
		Cell[][] cells = current.getCells();
		int n = cells.length;
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (cells[i][j].getPlayer().equals(homework.FREE)){
					Move move =  new Move();
					move.setTo(cells[i][j]);
					move.setMoveType(homework.STAKE);

					
					Cell[][] newCells = new Cell[n][n];
					for (int k = 0; k < n; k++){
						for (int l = 0; l < n; l++){
							Cell cell = new Cell();
							cell.setCol(l);
							cell.setRow(k);
							cell.setPlayer(cells[k][l].getPlayer());
							cell.setValue(cells[k][l].getValue());
							newCells[k][l] = cell;
						}
					}
					if (maxPlayer)
						newCells[i][j].setPlayer(homework.SELF);
					else 
						newCells[i][j].setPlayer(homework.OPPONENT);
					move.setCells(newCells);		
					move.setValue(getEvaluation(move, maxPlayer));
					moves.add(move);
				}
			}
		}
		return moves;
	}

	
	public boolean checkNeighboursForRaid(Cell[][] cells,int i,  int j,boolean maxPlayer){
		String player = null;
		if (maxPlayer){
			player = homework.SELF;
		} else {
			player = homework.OPPONENT;
		}
		int n = cells.length;
		if (i > 0){
			if (cells[i-1][j].getPlayer().equals(player)){
				return true;
			}
		}
		
		if (j > 0){
			if (cells[i][j-1].getPlayer().equals(player)){
				return true;
			}
		}
		
		if (i < n-1){
			if (cells[i+1][j].getPlayer().equals(player)){
				return true;
			}
		}
		
		if (j < n-1){
			if (cells[i][j+1].getPlayer().equals(player)){
				return true;
			}
		}
		
		return false;
	}
	
	
}
