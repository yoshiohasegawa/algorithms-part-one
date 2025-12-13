import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int count = 1;
        String champ = "";

        while (!StdIn.isEmpty()) {
            String challenger = StdIn.readString();
            if (StdRandom.bernoulli(1.0/count)) {
                champ = challenger;
            }
            count++;
        }

        StdOut.println(champ);
    }
}