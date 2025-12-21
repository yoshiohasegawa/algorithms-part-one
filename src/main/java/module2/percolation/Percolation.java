import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] idxGrid;
    private boolean[][] vacancyGrid;
    public WeightedQuickUnionUF qu;
    private int size;
    
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be larger than 0");
        
        this.size = n;
        this.idxGrid = new int[n][n];
        this.vacancyGrid = new boolean[n][n];

        int count = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.idxGrid[i][j] = count;
                count++;
                this.vacancyGrid[i][j] = false;
            }
        }

        this.qu = new WeightedQuickUnionUF((n * n) + 2);
        for (int i = 0; i < n; i++){
            this.qu.union(0, i + 1);
            this.qu.union((n * n) + 1, (n * n) - i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        // Get the index for the vacancy grid
        int x = row - 1;
        int y = col - 1;

        // Open the site in the Vacancy Grid
        this.vacancyGrid[x][y] = true;

        // Get the WeightedQuickUnionUF index for [row, col]
        int idx = (row - 1) * this.size + (col - 1) + 1;

        // Union the site with the top site, if available
        if (row > 1){
            if (this.isOpen(row - 1, col)) {
                // Get the WeightedQuickUnionUF index for top neighbor
                int top = (row - 2) * this.size + (col - 1) + 1;
                this.qu.union(idx, top);
            }
        }
        // Union the site with the right site, if available
        if (col < this.size){
            if (this.isOpen(row, col + 1)) {
                // Get the WeightedQuickUnionUF index for right neighbor
                int right = (row - 1) * this.size + (col) + 1;
                this.qu.union(idx, right);
            }
        }
        // Union the site with the bottom site, if available
        if (row < this.size){
            if (this.isOpen(row + 1, col)) {
                // Get the WeightedQuickUnionUF index for bottom neighbor
                int bottom = (row) * this.size + (col - 1) + 1;
                this.qu.union(idx, bottom);
            }
        }
        // Union the site with the left site, if available
        if (col > 1){
            if (this.isOpen(row, col - 1)) {
                // Get the WeightedQuickUnionUF index for left neighbor
                int left = (row - 1) * this.size + (col - 2) + 1;
                this.qu.union(idx, left);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        // Get the index for the vacancy grid
        int x = row - 1;
        int y = col - 1;

        return this.vacancyGrid[x][y];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        // Base case; the site is closed
        if (!this.isOpen(row, col)) return false;
        // Base case; we are at the top row
        if (row == 1) return true;
        // Base case; the above site is open
        if (this.isOpen(row - 1, col)) return true;

        // Get the WeightedQuickUnionUF index for [row, col]
        int idx = (row - 1) * this.size + (col - 1) + 1;

        // The site is full if it is connected to the virtual top
        return this.qu.find(idx) == this.qu.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        // Count the number of open sites found in the vacancy grid
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                 if (this.vacancyGrid[i][j]) count++;
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.qu.find(0) == this.qu.find(((this.size * this.size) + 1));
    }

    // Print the grid to standard output
    public void print() {
        System.out.println("Grid of size " + this.size);
        System.out.println();

        System.out.println("Index:");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(this.idxGrid[i][j]);
                if (this.idxGrid[i][j] < 10) {
                    System.out.print("   ");
                } else if (this.idxGrid[i][j] < 100) {
                        System.out.print("  ");
                } else if (this.idxGrid[i][j] < 1000) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Vacancy:");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.vacancyGrid[i][j]) {
                    System.out.print("o ");
                } else {
                    System.out.print("x ");
                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Represented as Quick Union:");
        int count = 1;
        for (int i = 1; i <= this.size; i++){
            for (int j = 1; j <= this.size; j++) {
                int parent = this.qu.find(count);
                System.out.print(this.qu.find(count));
                if (parent < 10) {
                    System.out.print("   ");
                } else if (parent < 100) {
                        System.out.print("  ");
                } else if (parent < 1000) {
                    System.out.print(" ");
                }
                count++;
            }
            System.out.println();
        }
        System.out.println();
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 3;
        Percolation p = new Percolation(n);

        System.out.println();
        p.print();

        // Total number of components
        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [3, 1]...");
        p.open(3, 1);

        System.out.println();
        p.print();

        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 1]...");
        p.open(2, 1);

        System.out.println();
        p.print();

        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [1, 3]...");
        p.open(1, 3);

        System.out.println();
        p.print();

        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 3]...");
        p.open(2, 3);

        System.out.println();
        p.print();

        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 2]...");
        p.open(2, 2);

        System.out.println();
        p.print();

        System.out.println("Number of components: " + p.qu.count());
        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("New Percolation instance");
        Percolation newP = new Percolation(n);

        System.out.println();
        newP.print();

        System.out.print("Site [2, 2] status: ");
        if (newP.isFull(2, 2)) {
            System.out.println("Full");
        } else {
            System.out.println("Empty");
        }
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 2]...");
        newP.open(2, 2);

        System.out.println();
        newP.print();
        
        System.out.print("Site [2, 2] status: ");
        if (newP.isFull(2, 2)) {
            System.out.println("Full");
        } else {
            System.out.println("Empty");
        }
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 3]...");
        newP.open(2, 3);

        System.out.println();
        newP.print();
        
        System.out.print("Site [2, 2] status: ");
        if (newP.isFull(2, 2)) {
            System.out.println("Full");
        } else {
            System.out.println("Empty");
        }
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [1, 3]...");
        newP.open(1, 3);

        System.out.println();
        newP.print();
        
        System.out.print("Site [2, 2] status: ");
        if (newP.isFull(2, 2)) {
            System.out.println("Full");
        } else {
            System.out.println("Empty");
        }
        System.out.println();
    }
}
    