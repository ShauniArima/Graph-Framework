package GraphAlgorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList  extends GraphTools {

	private static int _DEBBUG =0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt=0;

	//--------------------------------------------------
	// 				Constructors
	//--------------------------------------------------

	public GraphToolsList(){
		super();
	}

	// ------------------------------------------
	// 				Accessors
	// ------------------------------------------



	// ------------------------------------------
	// 				Methods
	// ------------------------------------------

	public static List<Boolean> initMarks(IGraph graph, AbstractNode start) {
		int nodeCount = graph.getNbNodes();

		ArrayList<Boolean> marks = new ArrayList<>();
		IntStream.range(0, nodeCount)
			.forEach(i -> marks.add(false));
		
		marks.set(start.getLabel(), true);

		return marks;
	}

	/**
	 * breathFirstSearch traverses the graph and displays what he is doing
	 * 
	 * @param graph the graph that will be traversed
	 * @param start the node from where to start
	 */
	public static void breathFirstSearch(IGraph graph, AbstractNode start) {
		List<Boolean> marks = initMarks(graph, start);

		ConcurrentLinkedQueue<AbstractNode> toVisit = new ConcurrentLinkedQueue<>();
		toVisit.add(start);

		AbstractNode currentNode;
		while ((currentNode = toVisit.poll()) != null) {
			System.out.println("Visiting: " + currentNode);

			ArrayList<AbstractNode> neighbours = new ArrayList<>();

			// This part can be simplified by using the same method for undirected and directed nodes
			if (currentNode.getClass() == UndirectedNode.class) {
				((UndirectedNode) currentNode).getNeighbours().keySet().forEach(neighbours::add);
			}

			if (currentNode.getClass() == DirectedNode.class) {
				((DirectedNode) currentNode).getSuccs().keySet().forEach(neighbours::add);
			}

			for (AbstractNode neighbour : neighbours) {
				if (!marks.get(neighbour.getLabel())) {
					marks.set(neighbour.getLabel(), true);
					toVisit.add(neighbour);
				}
			}
		}

		System.out.println("Final marks: " + marks);
	}

	/**
	 * dethFirstSearch traverses the graph and displays what he is doing
	 * 
	 * @param graph the graph that will be traversed
	 * @param start the node from where to start
	 */
	public static void depthFirstSearch(IGraph graph, AbstractNode start) {
		List<Boolean> marks = initMarks(graph, start);

		Stack<AbstractNode> toVisit = new Stack<>();
		toVisit.add(start);

		while (!toVisit.isEmpty()) {
			AbstractNode currentNode = toVisit.pop();
			System.out.println("Visiting: " + currentNode);

			ArrayList<AbstractNode> neighbours = new ArrayList<>();

			// This part can be simplified by using the same method for undirected and directed nodes
			if (currentNode.getClass() == UndirectedNode.class) {
				((UndirectedNode) currentNode).getNeighbours().keySet().forEach(neighbours::add);
			}

			if (currentNode.getClass() == DirectedNode.class) {
				((DirectedNode) currentNode).getSuccs().keySet().forEach(neighbours::add);
			}

			for (AbstractNode neighbour : neighbours) {
				if (!marks.get(neighbour.getLabel())) {
					marks.set(neighbour.getLabel(), true);
					toVisit.push(neighbour);
				}
			}
		}

		System.out.println("Final marks: " + marks);
	}

	// A completer


	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph al = new DirectedGraph(Matrix);
		System.out.println(al);

		// A completer
		System.out.println("BFS Traversal");
		breathFirstSearch(al, al.getNodes().get(0));

		System.out.println("\nDFS Traversal");
		depthFirstSearch(al, al.getNodes().get(0));
	}
}
