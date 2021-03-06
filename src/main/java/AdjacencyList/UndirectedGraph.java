package AdjacencyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Abstraction.AbstractListGraph;
import GraphAlgorithms.GraphTools;
import Nodes.UndirectedNode;
import Abstraction.IUndirectedGraph;

public class UndirectedGraph extends AbstractListGraph<UndirectedNode> implements IUndirectedGraph {

    //--------------------------------------------------
    // 				Constructors
    //--------------------------------------------------

    public UndirectedGraph() {
        this.nodes = new ArrayList<>();
    }

    public UndirectedGraph(List<UndirectedNode> nodes) {
        super(nodes);
        for (UndirectedNode i : nodes) {
            this.m += i.getNbNeigh();
        }
    }

    public UndirectedGraph(int[][] matrix) {
        this.order = matrix.length;
        this.nodes = new ArrayList<>();
        for (int i = 0; i < this.order; i++) {
            this.nodes.add(this.makeNode(i));
        }
        for (UndirectedNode n : this.getNodes()) {
            for (int j = n.getLabel(); j < matrix[n.getLabel()].length; j++) {
                UndirectedNode nn = this.getNodes().get(j);
                if (matrix[n.getLabel()][j] != 0) {
                    n.getNeighbours().put(nn,0);
                    nn.getNeighbours().put(n,0);
                    this.m++;
                }
            }
        }
    }

    public UndirectedGraph(UndirectedGraph g) {
        super();
        this.order = g.getNbNodes();
        this.m = g.getNbEdges();
        this.nodes = new ArrayList<>();
        for (UndirectedNode n : g.getNodes()) {
            this.nodes.add(makeNode(n.getLabel()));
        }
        for (UndirectedNode n : g.getNodes()) {
            UndirectedNode nn = this.getNodes().get(n.getLabel());
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
                UndirectedNode snn = this.getNodes().get(sn.getLabel());
                nn.getNeighbours().put(snn,0);
                snn.getNeighbours().put(nn,0);
            }
        }

    }

    // ------------------------------------------
    // 				Accessors
    // ------------------------------------------

    @Override
    public int getNbEdges() {
        return this.m;
    }

    @Override
    public boolean isEdge(UndirectedNode x, UndirectedNode y) {
        UndirectedNode firstNode = this.getNodeOfList(x);
        UndirectedNode secondNode = this.getNodeOfList(y);

        return firstNode.getNeighbours().containsKey(secondNode)
                && secondNode.getNeighbours().containsKey(firstNode);
    }

    @Override
    public void removeEdge(UndirectedNode x, UndirectedNode y) {
        if(this.isEdge(x,y)){
            UndirectedNode firstNode = this.getNodeOfList(x);
            UndirectedNode secondNode = this.getNodeOfList(y);

            firstNode.getNeighbours().remove(secondNode);
            secondNode.getNeighbours().remove(firstNode);
        }
    }

    @Override
    public void addEdge(UndirectedNode x, UndirectedNode y) {
        if(!isEdge(x,y)){
            UndirectedNode firstNode = this.getNodeOfList(x);
            UndirectedNode secondNode = this.getNodeOfList(y);

            firstNode.getNeighbours().put(secondNode, 0);
            secondNode.getNeighbours().put(firstNode, 0);
        }
    }

    //--------------------------------------------------
    // 					Methods
    //--------------------------------------------------

    /**
     * Method to generify node creation
     * @param label of a node
     * @return a node typed by A extends UndirectedNode
     */
    @Override
    public UndirectedNode makeNode(int label) {
        return new UndirectedNode(label);
    }

    /**
     * @return the corresponding nodes in the list this.nodes
     */
    public UndirectedNode getNodeOfList(UndirectedNode src) {
        return this.getNodes().get(src.getLabel());
    }

    /**
     * @return the adjacency matrix representation int[][] of the graph
     */
    @Override
    public int[][] toAdjacencyMatrix() {
        int[][] matrix = new int[order][order];

        for (int x = 0; x < order; x++) {
            this.getNodes().forEach(currentNode -> {
                currentNode.getNeighbours().forEach((currentNeighbour, integer) -> {
                    matrix[currentNode.getLabel()][currentNeighbour.getLabel()] = 1;
                    matrix[currentNeighbour.getLabel()][currentNode.getLabel()] = 1;
                });
            });
        }

        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (UndirectedNode n : nodes) {
            s.append("neighbours of ").append(n).append(" : ");
            for (UndirectedNode sn : n.getNeighbours().keySet()) {
                s.append(sn).append(" ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] mat = GraphTools.generateGraphData(10, 20, false, true, false, 100001);
        GraphTools.afficherMatrix(mat);
        UndirectedGraph al = new UndirectedGraph(mat);
        System.out.println(al);
        System.out.println(al.isEdge(new UndirectedNode(2), new UndirectedNode(0)));
        System.out.println(al.isEdge(new UndirectedNode(2), new UndirectedNode(1)));

        al.removeEdge(new UndirectedNode(2), new UndirectedNode(1));
        System.out.println(al.isEdge(new UndirectedNode(2), new UndirectedNode(1)));

        al.addEdge(new UndirectedNode(2), new UndirectedNode(1));
        System.out.println(al.isEdge(new UndirectedNode(2), new UndirectedNode(1)));

        System.out.println(Arrays.deepToString(al.toAdjacencyMatrix()));
    }

}
