import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class homework {
	public final static String SELF = "self";
	public final static String OPPONENT = "opponent";
	public final static String FREE = "free";
	public final static String STAKE = "Stake";
	public final static String RAID = "Raid";
	
	public static void main(String[] args) {
		File file = new File("input.txt");
		String text, mode = null;
		String player = "", opponent = "";
		int n = 0, depth = 0;
		Cell[][] cells = null; 
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			if((text = br.readLine()) != null){
				n = Integer.parseInt(text);
				cells = new Cell[n][n];
			}
			if((text = br.readLine()) != null){
				mode = text;
			}
			if((text = br.readLine()) != null){
				player = text;
				if (player.equals("X")){
					opponent = "O";
				} else {
					opponent = "X";
				}
			}
			if((text = br.readLine()) != null){
				depth = Integer.parseInt(text);
			}
			for ( int i = 0 ; i < n; i++){
				if((text = br.readLine()) != null){
					String[] numbers = text.split(" ");
					for (int j = 0; j < numbers.length;j++){
						Cell cell = new Cell();
						cell.setValue(Integer.parseInt(numbers[j]));
						cell.setRow(i);
						cell.setCol(j);
						cells[i][j] = cell;
					}
				}
			}
			int value = 0;
			for ( int i = 0 ; i < n; i++){
				if((text = br.readLine()) != null){
					for (int j = 0; j < text.length();j++){
						
						if (text.charAt(j) == '.'){
							cells[i][j].setPlayer(FREE);
						} else if (player.equals(text.substring(j, j+1))){
							cells[i][j].setPlayer(SELF);
							value = value + cells[i][j].getValue();
						} else {
							cells[i][j].setPlayer(OPPONENT);
							value = value - cells[i][j].getValue();
						}
					}
				}
			}
			
			AdverserialSearch search = null;
			Move current = new Move();
			current.setValue(value);
			current.setCells(cells);
			if ( mode.equals("MINIMAX")){
				search = new Minimax();
			} else if (mode.equals("ALPHABETA")){
				search = new MinimaxWithABPruninng();
				current.setAlpha(Integer.MIN_VALUE);
				current.setBeta(Integer.MAX_VALUE);
			}
			Move bestMove = search.adverserialSearch(current,depth,true);
			cells = bestMove.getCells();			
			if (bestMove != null){
				Character columnChar =(char) (65+bestMove.getTo().getCol());
				String column = columnChar.toString();
				
				
				File outFile = new File("output.txt");
				FileWriter fileWriter = new FileWriter(outFile);
				
				fileWriter.write(column+(int)(bestMove.getTo().getRow()+1)+" "+bestMove.getMoveType()+"\n");
				for (int i = 0 ; i < cells.length; i++){
					for (int j = 0 ; j < cells.length;j++){
						if (cells[i][j].getPlayer().equals(homework.SELF)){
							fileWriter.write(player);
							
						}
						if (cells[i][j].getPlayer().equals(homework.FREE)){
							fileWriter.write(".");
						}
						if (cells[i][j].getPlayer().equals(homework.OPPONENT)){
							fileWriter.write(opponent);
						}
					}
					fileWriter.write("\n");
				}
				fileWriter.close();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
