package GraphAlgorithms;

public class BinaryHeap {

    private int[] nodes;
    private int pos;

    public BinaryHeap() {
        this.nodes = new int[32];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = Integer.MAX_VALUE;
        }
        this.pos = 0;
    }

    public void resize() {
        int[] tab = new int[this.nodes.length + 32];
        for (int i = 0; i < nodes.length; i++) {
            tab[i] = Integer.MAX_VALUE;
        }
        System.arraycopy(this.nodes, 0, tab, 0, this.nodes.length);
        this.nodes = tab;
    }

    public boolean isEmpty() {
        return pos == 0;
    }

    /**
     * insert an new element in the heap
     * 
     * @param element the element to insert
     */
    public void insert(int element) {
        if (this.pos >= this.nodes.length) {
            this.resize();
        }

        int i = this.pos;
        this.nodes[i] = element;

        int parentPos;
        while ((parentPos = this.getParentPos(i)) >= 0 && this.nodes[parentPos] > element) {
            this.swap(parentPos, i);
            i = parentPos;
        }

        this.pos++;
    }

    /**
     * remove the root element of the heap and return the new size of the heap
     * 
     * @return the updated size of the heap
     */
    public int remove() {
        int i = 0;
        this.swap(i, this.pos - 1);

        this.nodes[this.pos - 1] = Integer.MAX_VALUE;

        int bestChild;
        while ((bestChild = this.getBestChildPos(i)) != Integer.MAX_VALUE && this.nodes[bestChild] < this.nodes[i]) {
            swap(i, bestChild);
            i = bestChild;
        }

        return this.pos--;
    }

    /**
     * getBestChildPos compute the position of the lowest child of the given source
     * 
     * @param src the parent position to explore
     * 
     * @return the position of the lowest child return Integer.MAX_VALUE if the
     *         given source is a leaf
     */
    private int getBestChildPos(int src) {
        if (isLeaf(src)) { // the leaf is a stopping case, then we return a default value
            return Integer.MAX_VALUE;
        }

        int leftPos = 2 * src + 1;
        int rightPos = 2 * src + 2;

        if (rightPos >= this.pos) {
            return leftPos;
        }

        int left = this.nodes[leftPos];
        int right = this.nodes[rightPos];

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

    /**
     * Test if the node is a leaf in the binary heap
     * 
     * @returns true if it's a leaf or false else
     * 
     */
    private boolean isLeaf(int src) {
        return 2 * src + 1 >= this.pos;
    }

    /**
     * swap two elements in the heap
     * 
     * @param father the father's index
     * @param child  the child's index
     */
    private void swap(int father, int child) {
        int temp = nodes[father];
        nodes[father] = nodes[child];
        nodes[child] = temp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pos; i++) {
            s.append(nodes[i]).append(", ");
        }
        return s.toString();
    }

    /**
     * Recursive test to check the validity of the binary heap
     * 
     * @returns a boolean equal to True if the binary tree is compact from left to
     *          right
     * 
     */
    public boolean test() {
        return this.isEmpty() || testRec(0);
    }

    private boolean testRec(int root) {
        if (isLeaf(root)) {
            return true;
        } else {
            int left = 2 * root + 1;
            int right = 2 * root + 2;
            if (right >= pos) {
                return nodes[left] >= nodes[root] && testRec(left);
            } else {
                return nodes[left] >= nodes[root] && testRec(left) && nodes[right] >= nodes[root] && testRec(right);
            }
        }
    }

    public static void main(String[] args) {
        BinaryHeap jarjarBin = new BinaryHeap();
        System.out.println(jarjarBin.isEmpty() + "\n");
        int k = 20;
        int m = k;
        int min = 2;
        int max = 20;
        while (k > 0) {
            int rand = min + (int) (Math.random() * ((max - min) + 1));
            System.out.print("insert " + rand);
            jarjarBin.insert(rand);
            k--;
        }
        System.out.println("\n" + jarjarBin);
        jarjarBin.insert(0);
        System.out.println("\n" + jarjarBin);
        jarjarBin.remove();
        System.out.println("\n" + jarjarBin);
        System.out.println(jarjarBin.test());
    }

}
