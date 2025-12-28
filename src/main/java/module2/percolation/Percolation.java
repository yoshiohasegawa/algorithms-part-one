import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // Grid to hold the indices of WeightedQuickUnionUF objects
    private int[][] idxGrid;
    // Grid to hold the vacancy status of each site
    private boolean[][] vacancyGrid;
    // WeightedQuickUnionUF object to determine percolation
    private WeightedQuickUnionUF percolationQU;
    // WeightedQuickUnionUF object to determine fullness
    private WeightedQuickUnionUF fullnessQU;
    // size of the grid
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

        this.percolationQU = new WeightedQuickUnionUF((n * n) + 2);
        this.fullnessQU = new WeightedQuickUnionUF((n * n) + 1);
    }

    // This is a private help method to connect a site to its neighbors upon calling `open()`
    private void tryConnect(int row, int col, int nRow, int nCol) {
        // Get the WeightedQuickUnionUF index for [row, col]
        int idx = (row - 1) * this.size + (col - 1) + 1;

        if (this.isOpen(nRow, nCol)) {
            // Get the WeightedQuickUnionUF index for the neighbor
                int neighbor = (nRow - 1) * this.size + (nCol - 1) + 1;
                this.percolationQU.union(idx, neighbor);
                this.fullnessQU.union(idx, neighbor);
        }
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        // Get the index for the vacancy grid
        int x = row - 1;
        int y = col - 1;

        // Open the site in the Vacancy Grid, if not already open
        if (this.vacancyGrid[x][y]) return; // Site is already open
        this.vacancyGrid[x][y] = true;

        // If the site is in the top row, connect it to the virtual top
        if (row == 1) {
            int idx = (col - 1) + 1; // Note, row is always 1 here
            // Connect in both percolation and fullness UF structures
            this.percolationQU.union(idx, 0);
            this.fullnessQU.union(idx, 0);
        }
        // If the site is in the bottom row, connect it to the virtual bottom
        if (row == this.size) {
            int idx = (row - 1) * this.size + (col - 1) + 1;
            // Connect only in the percolation UF structure
            this.percolationQU.union(idx, (this.size * this.size) + 1);
        }


        // Try connecting the site to its neighbors
        // Top neighbor, if available
        if (row > 1){
            this.tryConnect(row, col, row - 1, col);
        }
        // Right neighbor, if available
        if (col < this.size){
            this.tryConnect(row, col, row, col + 1);
        }
        // Bottom neighbor, if available
        if (row < this.size){
            this.tryConnect(row, col, row + 1, col);
        }
        // Left neighbor, if available
        if (col > 1){
            this.tryConnect(row, col, row, col - 1);
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

        // The site is full if it is connected to
        // the virtual top in the fullness UF structure
        return this.fullnessQU.find(idx) == this.fullnessQU.find(0);
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
        // The system percolates if the virtual top is connected
        // to the virtual bottom in the percolation UF structure
        return this.percolationQU.find(0) == this.percolationQU.find(((this.size * this.size) + 1));
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
                int parent = this.percolationQU.find(count);
                System.out.print(this.percolationQU.find(count));
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

        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [3, 1]...");
        p.open(3, 1);

        System.out.println();
        p.print();

        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 1]...");
        p.open(2, 1);

        System.out.println();
        p.print();

        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [1, 3]...");
        p.open(1, 3);

        System.out.println();
        p.print();

        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 3]...");
        p.open(2, 3);

        System.out.println();
        p.print();

        System.out.println("Number of open sites: " + p.numberOfOpenSites());
        System.out.println("Percolates: " + p.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 2]...");
        p.open(2, 2);

        System.out.println();
        p.print();

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

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("New, bigger Percolation instance");
        int m = 4;
        Percolation biggerP = new Percolation(m);

        System.out.println();
        biggerP.print();

        System.out.println("Number of open sites: " + biggerP.numberOfOpenSites());
        System.out.println("Percolates: " + biggerP.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [1, 1]...");
        biggerP.open(1, 1);

        System.out.println();
        biggerP.print();

        System.out.println("Number of open sites: " + biggerP.numberOfOpenSites());
        System.out.println("Percolates: " + biggerP.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [2, 1]...");
        biggerP.open(2, 1);

        System.out.println();
        biggerP.print();

        System.out.println("Number of open sites: " + biggerP.numberOfOpenSites());
        System.out.println("Percolates: " + biggerP.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [3, 1]...");
        biggerP.open(3, 1);

        System.out.println();
        biggerP.print();

        System.out.println("Number of open sites: " + biggerP.numberOfOpenSites());
        System.out.println("Percolates: " + biggerP.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [4, 1]...");
        biggerP.open(4, 1);

        System.out.println();
        biggerP.print();

        System.out.println("Number of open sites: " + biggerP.numberOfOpenSites());
        System.out.println("Percolates: " + biggerP.percolates());
        System.out.println();

        System.out.println("---------------------------");
        System.out.println();

        System.out.println("Opening site [4, 3]...");
        biggerP.open(4, 3);

        System.out.println();
        biggerP.print();

        System.out.println("Is [4, 3] full?: " + biggerP.isFull(4, 3));
    }
}
    