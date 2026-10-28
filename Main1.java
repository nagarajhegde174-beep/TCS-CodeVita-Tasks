
import java.util.*;

public class Main1 {

    static double area(double[] x, double[] y) {
        int n = x.length;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            sum += x[i] * y[j] - x[j] * y[i];
        }

        return Math.abs(sum) / 2.0;
    }

    static int sign(int value) {
        return value > 0 ? 1 : -1;
    }

    static double getVolume(
            int[] x, int[] y, int n, int step, int orientation) {

        double h = step / 10.0;
        double[] nx = new double[n];
        double[] ny = new double[n];

        for (int i = 0; i < n; i++) {
            int prev = (i - 1 + n) % n;
            int next = (i + 1) % n;

            // Find the shifted vertical edge.
            if (x[prev] == x[i]) {
                nx[i] = x[i]
                        - orientation * sign(y[i] - y[prev]) * h;
            } else {
                nx[i] = x[i]
                        - orientation * sign(y[next] - y[i]) * h;
            }

            // Find the shifted horizontal edge.
            if (y[prev] == y[i]) {
                ny[i] = y[i]
                        + orientation * sign(x[i] - x[prev]) * h;
            } else {
                ny[i] = y[i]
                        + orientation * sign(x[next] - x[i]) * h;
            }
        }

        return area(nx, ny) * h;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }

        double signedArea = 0;
        int minEdge = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;

            signedArea += (long) x[i] * y[j]
                    - (long) x[j] * y[i];

            int length = Math.abs(x[i] - x[j])
                    + Math.abs(y[i] - y[j]);

            minEdge = Math.min(minEdge, length);
        }

        int orientation = signedArea > 0 ? 1 : -1;

        // H is a multiple of 0.1.
        // Every edge must retain at least 0.1 units.
        int maxStep = 5 * minEdge - 1;

        double maxVolume = 0;

        for (int step = 1; step <= maxStep; step++) {
            double volume = getVolume(
                    x, y, n, step, orientation);

            maxVolume = Math.max(maxVolume, volume);
        }

        System.out.printf(Locale.US, "%.2f%n", maxVolume);

        sc.close();
    }
}

