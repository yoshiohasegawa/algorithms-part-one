import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int trials;
    private double[] thresholds;
    private static final double CONFIDENCE_95 = 1.96;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("Grid size and trials must be larger than 0.");
        }

        this.trials = trials;
        this.thresholds = new double[this.trials];

        // Run trials
        for (int i = 0; i < this.trials; i++) {
            int threshold = runTrial(n);
            this.thresholds[i] = (double) threshold / (n * n);
        }
    }

    // Runs a single percolation trial on an n x n grid
    // Returns the number of open sites when the system percolates
    private int runTrial(int n) {
        Percolation trialPerc = new Percolation(n);

        while (trialPerc.percolates() == false) {
            int randRow = StdRandom.uniformInt(n) + 1;
            int randCol = StdRandom.uniformInt(n) + 1;
            trialPerc.open(randRow, randCol);
        }

        return trialPerc.numberOfOpenSites();
    }

    // sample mean of percolation thresholds
    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation thresholds
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials));
    }

    public static void main(String[] args) {
        // Validate input length
        if (args.length != 2) {
            System.out.println("PercolationStats expects 2 arguments n and t...");
            return;
        }

        // Parse input arguments to integers
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        // Create PercolationStats object and print results
        PercolationStats percStats = new PercolationStats(n, t);

        StdOut.printf("mean                    = %.16f\n", percStats.mean());
        StdOut.printf("stddev                  = %.16f\n", percStats.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]\n",
                      percStats.confidenceLo(), percStats.confidenceHi());
    }
}
