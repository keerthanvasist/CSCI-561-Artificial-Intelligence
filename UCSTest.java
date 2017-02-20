import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UCSTest extends Search{

	private String goal;
	private HashMap<String,Node> explored = new HashMap<String,Node>();
	private HashMap<Node, Integer> previousBest = new HashMap<Node,Integer>();
	
	public UCSTest(Node root, Node goal) {
		super(root,goal);
		explored.put(root.getName(), root);
	}

	@Override
	public List<Node> ExpandQueue(HashMap<String,Node> map) {
		List<Node> bestPath = null;
		if (getRoot().getName().equals(goal)){
			List<Node> path = new ArrayList<Node>();
			path.add(getRoot());
			return path;
		}
		while(true){
			System.out.println();
			System.out.println();
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
				
				// If removed name is same as goal, trace back the path through the parent reference in node
				// and get the path to the goal from source(root)

				System.out.println("Expanding node "+removedNode.getName()+" ");
				if (removedNode.getName().equals(goal)){
					System.out.println("It's the goal!!");
					Node node = removedNode;
					bestPath = new ArrayList<Node>();
					while (node != null){
						bestPath.add(node);
						node = node.getParent();
					}
					return bestPath;
				}
				
				explored.put(removedNode.getName(),removedNode);
				// All children of the node being expanded
				
				List<String> children = removedNode.getChildren();
				List<Integer> distances = removedNode.getDistancesToChildren();
				// Looping through each child
				if (children != null){
					for (int i = 0;i < children.size();i++){
						System.out.println("Child found "+children.get(i));
						//Check if the current child is a goal. If it is, return the name of the child
						if (children.get(i).equals(goal)){
							Node childNode = explored.get(children.get(i));
							int childDistance = 0;
							boolean found = true;
							if (childNode == null){
								found = false;
								childNode = new Node();
								childNode.setChildren(null);
								childNode.setDistancesToChildren(null);
								childNode.setName(children.get(i));
								childNode.setParent(removedNode);
								childNode.setDistance(removedNode.getDistance()+distances.get(i));
							}
							childDistance = removedNode.getDistance()+distances.get(i);
							Integer previous = previousBest.get(childNode);
							if (previous == null){
								previous = 0;
							}
							if (childDistance + childNode.getSundayDistance() < previous || previous == 0){
								childNode.setDistance(childDistance);
								childNode.setParent(removedNode);
							}
							System.out.println("Going to enqueue");
							this.enqueueUCS(childNode, childDistance+childNode.getSundayDistance(),found);
							map.put(children.get(i), childNode);
							continue;
						}
							
						// If it's not goal node, enqueue child to queue in order of the distance from source (for UCS)
						Node childNode = explored.get(children.get(i));
						// If not explored, set distance and put in explored and add to queue
						if ( childNode == null){
							childNode = map.get(children.get(i));
							if (childNode != null){
								int childDistance = removedNode.getDistance()+distances.get(i);
								childNode.setDistance(childDistance);
								childNode.setParent(removedNode);
								this.enqueueUCS(childNode, childDistance+childNode.getSundayDistance(),false);
								map.put(children.get(i), childNode);
								explored.put(children.get(i), childNode);
							} else {
								if (children.get(i).equals(goal)){
									childNode = new Node();
									int childDistance = removedNode.getDistance()+distances.get(i);
									childNode.setDistance(childDistance);
									childNode.setParent(removedNode);
									this.enqueueUCS(childNode, childDistance,false);
									explored.put(children.get(i), childNode);
									map.put(children.get(i), childNode);
								}
							}
						} else {
							childNode = map.get(children.get(i));
							Node test = removedNode;
							boolean shouldAddToQueue = true;
							while (test != null){
								if (test.getName().equals(childNode.getName())){
									shouldAddToQueue = false;
									break;
								}
								test = test.getParent();
							}
							if (childNode != null && shouldAddToQueue){
								int childDistance = removedNode.getDistance() + distances.get(i);
								int previous = previousBest.get(childNode);
								if (childDistance + childNode.getSundayDistance() < previous || previous == 0){
									System.out.println(childNode.getName()+" should be enqueued.");
									if (!isRoot(childNode)){
										childNode.setParent(removedNode);
										childNode.setDistance(childDistance);
										explored.put(children.get(i), childNode);
										this.enqueueUCS(childNode, childDistance+childNode.getSundayDistance(),true);
									}
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	public void enqueueUCS(Node childNode, int childDistance, boolean isFound){	
		System.out.println("Enqueueing "+childNode.getName());
		/*if (previousBest.get(childNode) < childDistance){
			return;
		}*/
		
		if (isQueueEmpty()){
			QueueNode next = new QueueNode();
			next.setNext(null);
			next.setNode(childNode);
			setFirst(next);
			setLast(next);
			incrementQueueSize();
			previousBest.put(childNode, childDistance);
			System.out.println("Enqueued in empty queue "+childNode.getName());
			return;
		}
		
		QueueNode queueNode = getFirst();
		boolean isInQueue = false;
		QueueNode previousNode = null;
		QueueNode currentNode = null;
		while (queueNode != null){
			if (queueNode.getNode().getName().equals(childNode.getName())){
				isInQueue  = true;
				currentNode = queueNode;
				System.out.println(previousBest.get(currentNode.getNode()));
				break;
			}
			previousNode = queueNode;
			queueNode = queueNode.getNext();
		}
		
	
		
		if (isInQueue){
			System.out.println(childNode.getName()+" is in queue");
			if (childDistance < previousBest.get(currentNode.getNode())){
				currentNode.getNode().setDistance(childNode.getDistance());
				currentNode.getNode().setParent(childNode.getParent());
				previousNode.setNext(currentNode.getNext());
				queueNode = getFirst();
				previousNode = null;
				while (queueNode != null){
					if (childDistance < queueNode.getNode().getDistance()+queueNode.getNode().getSundayDistance()){
						QueueNode newNode = new QueueNode();
						if (previousNode != null){
							newNode.setNext(queueNode);
							newNode.setNode(childNode);
							previousNode.setNext(newNode);
							System.out.println("Moved in the queue");
							previousBest.put(childNode, childDistance);
						} else {
							newNode.setNext(queueNode);
							newNode.setNode(childNode);
							setFirst(newNode);
							System.out.println("Moved to the front");
							previousBest.put(childNode, childDistance);
						}
						return;
					}

					previousNode = queueNode;
					queueNode = queueNode.getNext();
				}
			}
		} else {
			previousNode = null;
			queueNode = getFirst();
			while (queueNode != null){
				if (childDistance < queueNode.getNode().getDistance()+queueNode.getNode().getSundayDistance()){
					System.out.println("Adding "+childNode.getName() +" to the queue");
					QueueNode newNode = new QueueNode();
					if (previousNode != null){
						newNode.setNext(queueNode);
						newNode.setNode(childNode);
						previousNode.setNext(newNode);
						incrementQueueSize();
						previousBest.put(childNode, childDistance);
						System.out.println("Added to the queue");
						return;
					} else {
						newNode.setNext(queueNode);
						newNode.setNode(childNode);
						setFirst(newNode);
						incrementQueueSize();
						previousBest.put(childNode, childDistance);
						System.out.println("Added to the front");
					}
					return;
				}
				
				previousNode = queueNode;
				queueNode = queueNode.getNext();
			}
			
			QueueNode last = getLast();
			QueueNode next = new QueueNode();
			next.setNext(null);
			next.setNode(childNode);
			last.setNext(next);
			setLast(next);
			incrementQueueSize();
			previousBest.put(childNode, childDistance);
			System.out.println("Enqueued at the end "+childNode.getName());
			
		}
		
	}

	@Override
	public void setGoal(String goal) {
		this.goal = goal;
	}

}
