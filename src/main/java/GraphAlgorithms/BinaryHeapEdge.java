package GraphAlgorithms;

import java.util.ArrayList;
import java.util.List;
import Collection.Triple;
import Nodes.UndirectedNode;

public class BinaryHeapEdge {

	/**
	 * A list structure for a faster management of the heap by indexing
	 * 
	 */
	private List<Triple<UndirectedNode,UndirectedNode,Integer>> binh;

    public BinaryHeapEdge() {
        this.binh = new ArrayList<>();
    }

    public boolean isEmpty() {
        return binh.isEmpty();
    }

    /**
	 * Insert a new edge in the binary heap
	 * 
	 * @param from one node of the edge
	 * @param to one node of the edge
	 * @param val the edge weight
	 */
    public void insert(UndirectedNode from, UndirectedNode to, int val) {
		Triple<UndirectedNode, UndirectedNode, Integer> element = new Triple<>(from, to, val);
		int i = this.binh.size();
		this.binh.add(i, element);

        int parentPos;
        while ((parentPos = this.getParentPos(i)) >= 0 && this.binh.get(parentPos).getThird() > element.getThird()) {
            this.swap(parentPos, i);
			i = parentPos;
			element = this.binh.get(i);
        }
    }

    
    /**
	 * Removes the root edge in the binary heap, and swap the edges to keep a valid binary heap
	 * 
	 * @return the edge with the minimal value (root of the binary heap)
	 * 
	 */
    public Triple<UndirectedNode,UndirectedNode,Integer> remove() {
		Triple<UndirectedNode, UndirectedNode, Integer> root = new Triple<UndirectedNode,UndirectedNode,Integer>(this.binh.get(0).getFirst(), this.binh.get(0).getSecond(), this.binh.get(0).getThird());

		int lastIndex = binh.size() - 1;
		int i = 0;
        this.swap(i, lastIndex);

        this.binh.get(lastIndex).setThird(Integer.MAX_VALUE);
		
        int bestChild;
        while ((bestChild = this.getBestChildPos(i)) != Integer.MAX_VALUE && this.binh.get(bestChild).getThird() < this.binh.get(i).getThird()) {
			swap(i, bestChild);
            i = bestChild;
        }
		
		this.binh.remove(lastIndex);
        return root;
	}

    /**
	 * From an edge indexed by src, find the child having the least weight and return it
	 * 
	 * @param src an index of the list edges
	 * @return the index of the child edge with the least weight
	 */
    private int getBestChildPos(int src) {
    	int lastIndex = binh.size()-1; 
        if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
            return Integer.MAX_VALUE;
		}
		
		int leftPos = 2 * src + 1;
        int rightPos = 2 * src + 2;

        if (rightPos >= lastIndex) {
            return leftPos;
        }

        int left = this.binh.get(leftPos).getThird();
        int right = this.binh.get(rightPos).getThird();

        return left < right ? leftPos : rightPos;
	}
	
	/**
     * getParentPos compute the parent's position of the hiven index
     * 
     * @param elementIndex the index used to compute the parent position
     * 
     * @return the position of the parent
     */
    private int getParentPos(int elementIndex) {
        return (elementIndex - 1) / 2;
    }


    private boolean isLeaf(int src) {
    	return 2 * src + 1 >= this.binh.size();
    }

    
    /**
	 * Swap two edges in the binary heap
	 * 
	 * @param father an index of the list edges
	 * @param child an index of the list edges
	 */
    private void swap(int father, int child) {         
    	Triple<UndirectedNode,UndirectedNode,Integer> temp = new Triple<>(binh.get(father).getFirst(), binh.get(father).getSecond(), binh.get(father).getThird());
    	binh.get(father).setTriple(binh.get(child));
    	binh.get(child).setTriple(temp);
    }

    /**
	 * Create the string of the visualisation of a binary heap
	 * 
	 * @return the string of the binary heap
	 */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Triple<UndirectedNode,UndirectedNode,Integer> no: binh) {
            s.append(no).append(", ");
        }
        return s.toString();
    }
    
    
    private String space(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<x; i++) {
			res.append(" ");
		}
		return res.toString();
	}
	
	/**
	 * Print a nice visualisation of the binary heap as a hierarchy tree
	 * 
	 */	
	public void lovelyPrinting(){
		int nodeWidth = this.binh.get(0).toString().length();
		int depth = 1+(int)(Math.log(this.binh.size())/Math.log(2));
		int index=0;
		
		for(int h = 1; h<=depth; h++){
			int left = ((int) (Math.pow(2, depth-h-1)))*nodeWidth - nodeWidth/2;
			int between = ((int) (Math.pow(2, depth-h))-1)*nodeWidth;
			int i =0;
			System.out.print(space(left));
			while(i<Math.pow(2, h-1) && index<binh.size()){
				System.out.print(binh.get(index) + space(between));
				index++;
				i++;
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// ------------------------------------
    // 					TEST
	// ------------------------------------

	/**
	 * Recursive test to check the validity of the binary heap
	 * 
	 * @return a boolean equal to True if the binary tree is compact from left to right
	 * 
	 */
    private boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
    	int lastIndex = binh.size()-1; 
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= lastIndex) {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left);
            } else {
                return binh.get(left).getThird() >= binh.get(root).getThird() && testRec(left)
                    && binh.get(right).getThird() >= binh.get(root).getThird() && testRec(right);
            }
        }
    }

    public static void main(String[] args) {
        BinaryHeapEdge jarjarBin = new BinaryHeapEdge();
        System.out.println(jarjarBin.isEmpty()+"\n");
        int k = 10;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));                        
            jarjarBin.insert(new UndirectedNode(k), new UndirectedNode(k+30), rand);            
            k--;
        }
        // A completer
        
		System.out.println(jarjarBin.test());
		jarjarBin.lovelyPrinting();

		jarjarBin.insert(new UndirectedNode(10), new UndirectedNode(40), 0);
		jarjarBin.lovelyPrinting();
		
		jarjarBin.remove();
		jarjarBin.lovelyPrinting();
    }

}

