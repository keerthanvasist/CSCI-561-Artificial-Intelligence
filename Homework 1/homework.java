import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class homework {
	public static void main(String[] args) {
		File file = new File("input.txt");
		Search search = null;
		HashMap<String,Node> map = new HashMap<String,Node>();
		Node  goal = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String text = null, searchStrategy = null;
			
			if((text = br.readLine()) != null){
				searchStrategy = text;
			}
			String startState = null, goalState = null;
			int numLiveLines = 0, numSunLines = 0;
			if((text = br.readLine()) != null){
				startState = text;
			}
			if((text = br.readLine()) != null){
				goalState = text;
			}
			if((text = br.readLine()) != null){
				 numLiveLines = Integer.parseInt(text);
			}
			
			for (int i = 0; i < numLiveLines; i++){
				if((text = br.readLine()) != null){
					String[] textArray = text.split(" ");
					Node parent = map.get(textArray[0]);
					if ( parent == null){
						parent = new Node();
						parent.setName(textArray[0]);
					}
					map.put(textArray[0], parent);
					int distance = Integer.parseInt(textArray[2]);
					parent.addChild(textArray[1],distance);
				}
			}
			
			if((text = br.readLine()) != null){
				 numSunLines = Integer.parseInt(text);
			}
			
			
			for (int i = 0; i < numSunLines; i++){
				if((text = br.readLine()) != null){
					String[] textArray = text.split(" ");
					Node parent = map.get(textArray[0]);
					if (parent != null){
						parent.setSundayDistance(Integer.parseInt(textArray[1]));
						if (searchStrategy.equals("UCS")){
							parent.setSundayDistance(0);
						}
					}
				}
			}
			System.out.println("1");
			
			Node root = map.get(startState);
			if ( searchStrategy.equals("BFS")){
				search = new BreadthFirstSearch(root,goal);
			} else if ( searchStrategy.equals("DFS")){
				search = new DepthFirstSearch(root,goal);
			} else if ( searchStrategy.equals("UCS")){
				search = new UniformCostSearch(root,goal);
			} else if ( searchStrategy.equals("A*")){
				search = new AStarSearch(root,goal);
			} else if ( searchStrategy.equals("UCST")){
				search = new UCSTest(root,goal);
			}
			
			System.out.println("2");
			
			search.setRoot(root);
			search.setGoal(goalState);
			
			Set<String> keyset = map.keySet();
			for(String key : keyset){
				System.out.println(map.get(key).getName()+":");
				List<String> children = map.get(key).getChildren();
				for (String child:children){
					System.out.print(child+" ");
				}
				System.out.println();
				System.out.println();
			}
			
			File outFile = new File("output.txt");
			FileWriter fileWriter = new FileWriter(outFile);
			
			
			List<Node> path = search.search(map);
			if (path != null){
				System.out.println("The path is: "+path.size());
				int totalDistance = 0;
				for (int i = path.size()-1; i >= 0; i--){
					String from = path.get(i).getName();
					int distance = path.get(i).getDistance();
					totalDistance = totalDistance + distance;
					if (search instanceof BreadthFirstSearch || search instanceof DepthFirstSearch){
						fileWriter.write(from+" "+totalDistance+"\n");
						System.out.println(from+" "+totalDistance);
					}
					if (search instanceof UCSTest || search instanceof AStarSearch || search instanceof UniformCostSearch){
						fileWriter.write(from+" "+distance+"\n");
						System.out.println(from+" "+distance);
					}
				}
			} else {
				System.out.println("No path found");
			}
			System.out.println("Done");
			br.close();
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
