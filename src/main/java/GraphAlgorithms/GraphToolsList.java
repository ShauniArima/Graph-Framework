package GraphAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Abstraction.IGraph;
import AdjacencyList.DirectedGraph;
import AdjacencyList.DirectedValuedGraph;
import AdjacencyList.UndirectedGraph;
import AdjacencyList.UndirectedValuedGraph;
import Collection.Pair;
import Nodes.AbstractNode;
import Nodes.DirectedNode;
import Nodes.UndirectedNode;

public class GraphToolsList extends GraphTools {

	private static int _DEBBUG = 0;

	private static int[] visite;
	private static int[] debut;
	private static int[] fin;
	private static List<Integer> order_CC;
	private static int cpt = 0;

	// --------------------------------------------------
	// Constructors
	// --------------------------------------------------

	public GraphToolsList() {
		super();
	}

	// ------------------------------------------
	// Accessors
	// ------------------------------------------

	// ------------------------------------------
	// Methods
	// ------------------------------------------

	public static List<Boolean> initMarks(IGraph graph, AbstractNode start) {
		int nodeCount = graph.getNbNodes();

		ArrayList<Boolean> marks = new ArrayList<>();
		IntStream.range(0, nodeCount).forEach(i -> marks.add(false));

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

			// This part can be simplified by using the same method for undirected and
			// directed nodes
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
	 * depthFirstSearch traverses the graph and displays what he is doing
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

			// This part can be simplified by using the same method for undirected and
			// directed nodes
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

	/**
	 * exploreVertex allows to explore a vertex and his neighbours
	 * 
	 * @param vertex      vertex to visit
	 * @param visited     set of already visited nodes
	 * @param visiteState represent the current state of the exploration
	 * @param start       a list of integers that represents the turn when the
	 *                    exploration of node x started
	 * @param end         a list of integers that represents the turn when the
	 *                    exploration of node x ended
	 * @param turnCount   the count of turns
	 */
	public static void exploreVertex(AbstractNode vertex, Set<AbstractNode> visited, List<Integer> visiteState,
			List<Integer> start, List<Integer> end, AtomicInteger turnCount) {
		visited.add(vertex);
		visiteState.set(vertex.getLabel(), 1);
		start.set(vertex.getLabel(), turnCount.get());
		turnCount.incrementAndGet();

		BiConsumer<AbstractNode, Integer> action = (successor, value) -> {
			if (!visited.contains(successor)) {
				exploreVertex(successor, visited, visiteState, start, end, turnCount);
			}
		};

		if (vertex.getClass() == DirectedNode.class) {
			((DirectedNode) vertex).getSuccs().forEach(action);
		}

		if (vertex.getClass() == UndirectedNode.class) {
			((UndirectedNode) vertex).getNeighbours().forEach(action);
		}

		visiteState.set(vertex.getLabel(), 2);
		end.set(vertex.getLabel(), turnCount.get());
		turnCount.incrementAndGet();
	}

	/**
	 * exploreGraph allows to explore a graph and keep a state of the exploration
	 * 
	 * @param graph the graph to explore
	 */
	public static List<Integer> exploreGraph(IGraph graph) {
		Set<AbstractNode> visited = new HashSet<>();
		List<Integer> visiteState = new ArrayList<>();
		List<Integer> start = new ArrayList<>();
		List<Integer> end = new ArrayList<>();
		AtomicInteger turnCount = new AtomicInteger(0);

		IntStream.range(0, graph.getNbNodes()).forEach(i -> {
			visiteState.add(0);
			start.add(0);
			end.add(0);
		});

		Consumer<AbstractNode> action = vertex -> {
			if (!visited.contains(vertex)) {
				System.out.println("Exploring: " + vertex);
				exploreVertex(vertex, visited, visiteState, start, end, turnCount);
			}
		};

		if (graph.getClass() == DirectedGraph.class) {
			((DirectedGraph) graph).getNodes().forEach(action);
		}
		if (graph.getClass() == UndirectedGraph.class) {
			((UndirectedGraph) graph).getNodes().forEach(action);
		}
		if (graph.getClass() == DirectedValuedGraph.class) {
			((DirectedValuedGraph) graph).getNodes().forEach(action);
		}
		if (graph.getClass() == UndirectedValuedGraph.class) {
			((UndirectedValuedGraph) graph).getNodes().forEach(action);
		}

		System.out.println("Start: " + start);
		System.out.println("End: " + end);

		return end;
	}

	/**
	 * exploreGraphOrdered allows to explore a graph in a given order and keep a
	 * state of the exploration
	 * 
	 * @param graph the graph to explore
	 * @param order the order used for the exploration
	 */
	public static List<Integer> exploreGraphOrdered(IGraph graph, List<Integer> order) {
		Set<AbstractNode> visited = new HashSet<>();
		List<Integer> visiteState = new ArrayList<>();
		List<Integer> start = new ArrayList<>();
		List<Integer> end = new ArrayList<>();
		AtomicInteger turnCount = new AtomicInteger(0);

		IntStream.range(0, graph.getNbNodes()).forEach(i -> {
			visiteState.add(0);
			start.add(0);
			end.add(0);
		});

		Consumer<AbstractNode> action = vertex -> {
			if (!visited.contains(vertex)) {
				System.out.println("Exploring: " + vertex);
				exploreVertex(vertex, visited, visiteState, start, end, turnCount);
			}
		};

		if (graph.getClass() == DirectedGraph.class) {
			order.forEach(i -> {
				action.accept(((DirectedGraph) graph).getNodes().get(i));
			});
		}
		if (graph.getClass() == UndirectedGraph.class) {
			order.forEach(i -> {
				action.accept(((UndirectedGraph) graph).getNodes().get(i));
			});
		}
		if (graph.getClass() == DirectedValuedGraph.class) {
			order.forEach(i -> {
				action.accept(((DirectedValuedGraph) graph).getNodes().get(i));
			});
		}
		if (graph.getClass() == UndirectedValuedGraph.class) {
			order.forEach(i -> {
				action.accept(((UndirectedValuedGraph) graph).getNodes().get(i));
			});
		}

		System.out.println("Start: " + start);
		System.out.println("End: " + end);

		return end;
	}

	/**
	 * findShortestPathWithDijkstra use Dijkstra's algorithm to compute the shortest
	 * paths from a starting node to all others
	 * 
	 * @param graph the graph used to find the paths
	 * @param start the starting node
	 * @return a pair containing the minimal costs to each nodes and the predecessor
	 *         of each nodes
	 * @throws Exception if the graph is not valued
	 */
	public static Pair<int[], int[]> findShortestPathWithDijkstra(IGraph graph, AbstractNode start) throws Exception {
		if (graph.getClass() == DirectedGraph.class) {
			throw new Exception("Could not apply this algorithm on non valued graph.");
		}
		if (graph.getClass() == UndirectedGraph.class) {
			throw new Exception("Could not apply this algorithm on non valued graph.");
		}

		int[] costs = new int[graph.getNbNodes()];
		int[] predecessors = new int[graph.getNbNodes()];
		Arrays.fill(costs, Integer.MAX_VALUE);
		Arrays.fill(predecessors, Integer.MAX_VALUE);

		costs[start.getLabel()] = 0;
		predecessors[start.getLabel()] = start.getLabel();

		PriorityQueue<AbstractNode> queue = new PriorityQueue<>((a, b) -> {
			return Integer.compare(costs[a.getLabel()], costs[b.getLabel()]);
		});

		queue.add(start);

		while (!queue.isEmpty()) {
			AbstractNode currentNode = queue.poll();

			if (costs[currentNode.getLabel()] == Integer.MAX_VALUE) {
				break;
			}

			BiConsumer<AbstractNode, Integer> exploreSuccessor = (neighbour, cost) -> {
				int totalCost = cost + costs[currentNode.getLabel()];

				if (totalCost < costs[neighbour.getLabel()]) {
					queue.remove(neighbour);

					costs[neighbour.getLabel()] = totalCost;
					predecessors[neighbour.getLabel()] = currentNode.getLabel();
					queue.add(neighbour);
				}
			};

			if (currentNode.getClass() == UndirectedNode.class) {
				((UndirectedNode) currentNode).getNeighbours().forEach(exploreSuccessor);
			}

			if (currentNode.getClass() == DirectedNode.class) {
				((DirectedNode) currentNode).getSuccs().forEach(exploreSuccessor);
			}
		}

		return new Pair<int[], int[]>(costs, predecessors);
	}

	/**
	 * findShortestPathWithBellman use Bellman's algorithm to compute the shortest
	 * paths from a starting node to all others
	 * 
	 * @param graph the graph used to find the paths
	 * @param start the starting node
	 * @return a pair containing the minimal values to each nodes and the
	 *         predecessor of each nodes
	 * @throws Exception when the graph is not valued or if there is a
	 *                   negative-weight cycle in the graph
	 */
	public static Pair<int[], int[]> findShortestPathWithBellman(IGraph graph, AbstractNode start) throws Exception {
		if (graph.getClass() == DirectedGraph.class) {
			throw new Exception("Could not apply this algorithm on non valued graph.");
		}
		if (graph.getClass() == UndirectedGraph.class) {
			throw new Exception("Could not apply this algorithm on non valued graph.");
		}

		int numberOfIteration = 0;
		if (graph.getClass() == DirectedValuedGraph.class) {
			numberOfIteration = ((DirectedValuedGraph) graph).getNbArcs() - 1;
		}
		if (graph.getClass() == UndirectedValuedGraph.class) {
			numberOfIteration = ((UndirectedValuedGraph) graph).getNbEdges() - 1;
		}

		int[] values = new int[graph.getNbNodes()];
		int[] predecessors = new int[graph.getNbNodes()];
		Arrays.fill(values, Integer.MAX_VALUE);
		Arrays.fill(predecessors, Integer.MAX_VALUE);
		values[start.getLabel()] = 0;
		predecessors[start.getLabel()] = start.getLabel();

		LinkedList<AbstractNode> queue = new LinkedList<>();
		queue.add(start);

		LinkedList<AbstractNode> updatedNodes = new LinkedList<>();

		for (int i = 0; i < numberOfIteration; i++) {
			while (!queue.isEmpty()) {
				AbstractNode currentNode = queue.poll();

				BiConsumer<AbstractNode, Integer> exploreSuccessor = (neighbour, cost) -> {
					int totalCost = cost + values[currentNode.getLabel()];

					if (totalCost < values[neighbour.getLabel()]) {
						values[neighbour.getLabel()] = totalCost;
						predecessors[neighbour.getLabel()] = currentNode.getLabel();
						updatedNodes.add(neighbour);
					}
				};

				if (currentNode.getClass() == UndirectedNode.class) {
					((UndirectedNode) currentNode).getNeighbours().forEach(exploreSuccessor);
				}

				if (currentNode.getClass() == DirectedNode.class) {
					((DirectedNode) currentNode).getSuccs().forEach(exploreSuccessor);
				}
			}

			queue = new LinkedList<>(updatedNodes);
			updatedNodes.clear();
		}

		while (!queue.isEmpty()) {
			AbstractNode currentNode = queue.poll();

			BiConsumer<AbstractNode, Integer> exploreSuccessor = (neighbour, cost) -> {
				int totalCost = cost + values[currentNode.getLabel()];

				if (totalCost < values[neighbour.getLabel()]) {
					updatedNodes.add(neighbour);
				}
			};

			if (currentNode.getClass() == UndirectedNode.class) {
				((UndirectedNode) currentNode).getNeighbours().forEach(exploreSuccessor);
			}

			if (currentNode.getClass() == DirectedNode.class) {
				((DirectedNode) currentNode).getSuccs().forEach(exploreSuccessor);
			}
		}

		if (!updatedNodes.isEmpty()) {
			throw new Exception("Graph contains a negative-weight cycle");
		}

		return new Pair<int[], int[]>(values, predecessors);
	}

	public static void main(String[] args) {
		int[][] Matrix = GraphTools.generateGraphData(10, 20, false, false, true, 100001);
		GraphTools.afficherMatrix(Matrix);
		DirectedGraph al = new DirectedGraph(Matrix);
		System.out.println(al);

		System.out.println("BFS Traversal");
		breathFirstSearch(al, al.getNodes().get(0));

		System.out.println("\nDFS Traversal");
		depthFirstSearch(al, al.getNodes().get(0));

		System.out.println("\nExplore graph");
		long start = System.nanoTime();
		List<Integer> endState = exploreGraph(al);
		long end = System.nanoTime();
		long explorationDuration = end - start;
		System.out.println("Elapsed: " + explorationDuration + " nanoseconds");

		System.out.println("\nExplore inversed graph");
		IGraph inversedGraph = al.computeInverse();
		List<Integer> order = IntStream.range(0, endState.size())
				.mapToObj(index -> new Pair<Integer, Integer>(endState.get(index), index))
				.sorted((p1, p2) -> Integer.compare(p2.getLeft(), p1.getLeft())).map(Pair::getRight)
				.collect(Collectors.toList());
		start = System.nanoTime();
		exploreGraphOrdered(inversedGraph, order);
		end = System.nanoTime();
		long explorationInversedDuration = end - start;
		System.out.println("Elapsed: " + explorationInversedDuration + " nanoseconds");

		if (explorationDuration < explorationInversedDuration) {
			System.out.println("\nNormal exploration is the fastest");
		}

		if (explorationDuration > explorationInversedDuration) {
			System.out.println("\nInversed exploration is the fastest");
		}

		int[][] matrixValued = GraphTools.generateValuedGraphData(10, false, false, true, false, 100001);
		DirectedValuedGraph directedValuedGraph = new DirectedValuedGraph(matrixValued);
		UndirectedValuedGraph undirectedValuedGraph = new UndirectedValuedGraph(matrixValued);

		System.out.println(undirectedValuedGraph);

		try {
			findShortestPathWithDijkstra(directedValuedGraph, directedValuedGraph.getNodes().get(0));
			findShortestPathWithDijkstra(undirectedValuedGraph, undirectedValuedGraph.getNodes().get(0));

			findShortestPathWithBellman(directedValuedGraph, directedValuedGraph.getNodes().get(0));
			findShortestPathWithBellman(undirectedValuedGraph, undirectedValuedGraph.getNodes().get(0));

			// Add negative edges
			undirectedValuedGraph.addEdge(undirectedValuedGraph.getNodes().get(0),
					undirectedValuedGraph.getNodes().get(1), -10);
			undirectedValuedGraph.addEdge(undirectedValuedGraph.getNodes().get(0),
					undirectedValuedGraph.getNodes().get(2), -10);
			undirectedValuedGraph.addEdge(undirectedValuedGraph.getNodes().get(1),
					undirectedValuedGraph.getNodes().get(2), -10);

			findShortestPathWithBellman(undirectedValuedGraph, undirectedValuedGraph.getNodes().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
