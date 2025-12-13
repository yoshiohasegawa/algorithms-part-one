public class SuccessorWithDelete {
    private int[] id;
    private int[] sz;
    private int[] lgst;

    public SuccessorWithDelete(int n) {
        id = new int[n];
        sz = new int[n];
        lgst = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
            lgst[i] = i;    
        }
    }

	private int root(int i) {
		while (i != id[i]) {
				id[i] = id[id[i]]; // Path compression
				i = id[i];
		}
		return i;
    }

    private void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;

		// Weighted union:
		// link root of smaller tree to root of larger tree.
        if (sz[i] <= sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    // Remove p by unioning it with p+1
    public void remove(int p) {
        if (p < 0 || p >= id.length - 1) return; // No successor for the largest element
        union(p, p + 1);
    }

    // Find the successor of p
    // Returns -1 if no successor exists
    public int successor(int p) {
        // Throw exception for invalid input
        if (p < 0 || p >= id.length) throw new IllegalArgumentException("Index out of bounds");

        // Return the root of p, which is the next largest, non-removed element
        int succ = root(p);
        if (succ == p && succ == id.length - 1) return -1; // No successor
        return succ;
    }

    public static void main(String[] args) {
        SuccessorWithDelete swd = new SuccessorWithDelete(10);
        System.out.println("Initial data structure: ");
        System.out.println("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");
        // Initial data structure
        // 0 1 2 3 4 5 6 7 8 9

        swd.remove(3);
        swd.remove(5);

        System.out.println();
        System.out.println("After removing 3 and 5: ");
        System.out.println("[0, 1, 2, 4, 4, 6, 6, 7, 8, 9]");
        System.out.println();
        System.out.println("As tree structure: ");
        System.out.println("0 1 2 4 6 7 8 9");
        System.out.println("      | |");
        System.out.println("      3 5");    
        // After removals (3 and 5)
        // 0 1 2 4 4 6 6 7 8 9

        // As tree structure
        // 0 1 2 4 6 7 8 9
        //       | |
        //       3 5

        System.out.println();
        System.out.println("Succesor of 2: " + swd.successor(2)); // 2
        System.out.println("Succesor of 3: " + swd.successor(3)); // 4
        System.out.println("Succesor of 4: " + swd.successor(4)); // 4
        System.out.println("Succesor of 5: " + swd.successor(5)); // 6
        System.out.println("Succesor of 6: " + swd.successor(6)); // 6
        System.out.println("Succesor of 9: " + swd.successor(9)); // -1

        swd.remove(4);

        System.out.println();
        System.out.println("After removing 4: ");
        System.out.println("[0, 1, 2, 6, 6, 6, 6, 7, 8, 9]");
        System.out.println();
        System.out.println("As tree structure: ");
        System.out.println("0 1 2 6 7 8 9");
        System.out.println("     / \\");
        System.out.println("    4   5"); 
        System.out.println("   /");
        System.out.println("  3");

        // After removal of 4
        // 0 1 2 6 6 7 8 9

        // As tree structure
        // 0 1 2 6 7 8 9
        //      / \
        //     4   5
        //    /
        //   3  

        System.out.println();
        System.out.println("Succesor of 3: " + swd.successor(3)); // 6

        System.out.println();
        System.out.println("Finding the successor of 3 results in path compression of the tree.");
        System.out.println("3 now points directly to 6.");
        System.out.println();
        System.out.println("Updated tree structure: ");
        System.out.println("0 1 2 6 7 8 9");
        System.out.println("     /|\\");
        System.out.println("    3 4 5"); 

        // Updated tree structure after path compression
        // 0 1 2 6 7 8 9
        //      /|\
        //     3 4 5

        System.out.println("Succesor of 4: " + swd.successor(4)); // 6
        System.out.println("Succesor of 5: " + swd.successor(5)); // 6
        System.out.println("Succesor of 6: " + swd.successor(6)); // 6
    }
}
