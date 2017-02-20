import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BreadthFirstSearch extends Search {
	private String goal;
	private HashMap<String,Node> explored = new HashMap<String,Node>();
	
	public BreadthFirstSearch(Node root, Node goal) {
		super(root,goal);
		explored.put(root.getName(), root);
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}


	@Override
	public List<Node> ExpandQueue(HashMap<String,Node> map) {
		Node child = null;
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
			} 
			else {
				// Normal run of the algorithm. Dequeue a node from the queue to
				// expand according to search strategy
				Node removedNode = dequeue();
				System.out.println(removedNode.getName());
				// All children of the node being expanded
				List<String> children = removedNode.getChildren();
				
				// Looping through each child
				for (int i = 0;i < children.size();i++){
					
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
						List<Node>  bestPath = new ArrayList<Node>();
						while (node != null){
							bestPath.add(node);
							node = node.getParent();
						}
						return bestPath;
					}
					// If it's not goal node, enqueue child to back of the queue (for BFS)
					// For BFS, don't do anything if it is already explored. If it is not explored,
					// then enqueue.
					child = explored.get(children.get(i));
					if ( child == null){
						Node childNode = map.get(children.get(i));
						if (childNode != null){
							childNode.setDistance(1);
							explored.put(children.get(i), childNode);
							childNode.setParent(removedNode);
							QueueNode last = getLast();
							QueueNode next = new QueueNode();
							next.setNext(null);
							next.setNode(childNode);
							last.setNext(next);
							setLast(next);
							if (isQueueEmpty()){
								setFirst(next);
							}
						}
					}
				}
			}
		}
	}

}
