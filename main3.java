
import java.util.*;

public class main3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[] x1 = new int[n];
        int[] y1 = new int[n];
        int[] x2 = new int[n];
        int[] y2 = new int[n];

        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt();
            y1[i] = sc.nextInt();
            x2[i] = sc.nextInt();
            y2[i] = sc.nextInt();
        }

        int px1 = sc.nextInt();
        int py1 = sc.nextInt();
        int px2 = sc.nextInt();
        int py2 = sc.nextInt();

        int minXGap = Integer.MAX_VALUE;
        int minYGap = Integer.MAX_VALUE;
        int previous;

        // Find the smallest gap between valid vertical creases.
        previous = -1;

        for (int x = px1; x <= px2; x++) {
            boolean valid = true;

            for (int i = 0; i < n; i++) {
                if (x1[i] < x && x < x2[i]) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                if (previous != -1) {
                    minXGap = Math.min(minXGap, x - previous);
                }
                previous = x;
            }
        }

        // Find the smallest gap between valid horizontal creases.
        previous = -1;

        for (int y = py1; y <= py2; y++) {
            boolean valid = true;

            for (int i = 0; i < n; i++) {
                if (y1[i] < y && y < y2[i]) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                if (previous != -1) {
                    minYGap = Math.min(minYGap, y - previous);
                }
                previous = y;
            }
        }

        int answer = minXGap * minYGap;

        System.out.println(answer);

        sc.close();
    }
}

