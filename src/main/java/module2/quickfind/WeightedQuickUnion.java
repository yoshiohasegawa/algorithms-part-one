public class WeightedQuickUnion {
    private int[] id;
    private int[] sz;

    public WeightedQuickUnion(int n) {
        id = new int[n];
        sz = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

	private int root(int i) {
		while (i != id[i]) {
				id[i] = id[id[i]]; // Path compression
				i = id[i];
		}
		return i;
}

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;

		// Weighted union:
		// link root of smaller tree to root of larger tree.
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

	// Find the largest element in the connected component containing p
    public int find(int p) {
        int largest = p;
        int rootP = root(p);

        for (int i = 0; i < id.length; i++) {
            if (root(i) == rootP) {
                if (i > largest) largest = i;
            }
        }
        return largest;
    }

    public static void main(String[] args) {
        WeightedQuickUnion wqu = new WeightedQuickUnion(10);
        wqu.union(1, 2);
        wqu.union(2, 3);
        wqu.union(4, 5);
        wqu.union(5, 6);
        wqu.union(3, 6);

        System.out.println(wqu.find(2)); // 6
        System.out.println(wqu.find(4)); // 6
        System.out.println(wqu.find(7)); // 7
    }
}