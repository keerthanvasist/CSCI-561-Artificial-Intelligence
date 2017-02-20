import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepthFirstSearch extends Search {
	private String goal;
	private HashMap<String,Node> explored = new HashMap<String,Node>();

	public DepthFirstSearch(Node root, Node goal) {
		super(root,goal);
		explored.put(root.getName(), root);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Node> ExpandQueue(HashMap<String,Node> map) {
		if (getRoot().getName().equals(goal)){
			List<Node> path = new ArrayList<Node>();
			path.add(getRoot());
			return path;
		}
		while(true){
			// If queue is empty, it implies that all nodes have been expanded and the 
			// goal state node has not been found. Failure case.
			if (isQueueEmpty()){
				System.out.println("Queue Empty");
				return null;
			} else {
				// Normal run of the algorithm. Dequeue a node from the queue to
				// expand according to search strategy
				Node removedNode = dequeue();
				System.out.println(removedNode.getName());
				// All children of the node being expanded
				List<String> children = removedNode.getChildren();		
				
				// Looping through each child
				for (int i = children.size()-1;i >= 0;i--){
					//Check if the current child is a goal. If it is, return the name of the child
					if (children.get(i).equals(goal)){
						Node childNode = map.get(children.get(i));
						if (childNode == null){
							childNode = new Node();
							childNode.setChildren(null);
							childNode.setDistancesToChildren(null);
							childNode.setName(children.get(i));
							map.put(children.get(i), childNode);
						}
						childNode.setDistance(1);
						childNode.setParent(removedNode);
						Node node = childNode;
						List<Node> bestPath = new ArrayList<Node>();
						while (node != null){
							bestPath.add(node);
							node = node.getParent();
						}
						return bestPath;
					}
					
					// If it's not goal node, enqueue child to front of the queue (for DFS)
					Node child = explored.get(children.get(i));
					if (child == null){
						Node childNode = map.get(children.get(i));
						if (childNode != null){
							childNode.setDistance(1);
							explored.put(children.get(i), childNode);
							QueueNode first = getFirst();
							QueueNode nextFirst = new QueueNode();
							childNode.setParent(removedNode);
							nextFirst.setNext(first);
							nextFirst.setNode(childNode);
							setFirst(nextFirst);
						}
					}
				}
			}
		}
	}

	@Override
	public void setGoal(String goal) {
		this.goal = goal;
		
	}

}
