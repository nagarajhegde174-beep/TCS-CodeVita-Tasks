
import java.util.*;

public class Main {

    static char[][] applyGravity(char[][] box) {
        int m = box.length;
        int n = box[0].length;

        for (int col = 0; col < n; col++) {
            int bottom = m - 1;

            for (int row = m - 1; row >= 0; row--) {
                if (box[row][col] == '*') {
                    box[row][col] = '.';
                    box[bottom][col] = '*';
                    bottom--;
                }
            }
        }
        return box;
    }

    static char[][] rotateRight(char[][] box) {
        int m = box.length;
        int n = box[0].length;

        char[][] rotated = new char[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][m - 1 - i] = box[i][j];
            }
        }
        return rotated;
    }

    static char[][] rotateLeft(char[][] box) {
        int m = box.length;
        int n = box[0].length;

        char[][] rotated = new char[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rotated[n - 1 - j][i] = box[i][j];
            }
        }
        return rotated;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int m = sc.nextInt();
        int n = sc.nextInt();

        char[][] box = new char[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                box[i][j] = sc.next().charAt(0);
            }
        }

        int k = sc.nextInt();

        box = applyGravity(box);

        for (int i = 0; i < k; i++) {
            String direction = sc.next();

            if (direction.equals("right")) {
                box = rotateRight(box);
            } else {
                box = rotateLeft(box);
            }

            box = applyGravity(box);
        }

        for (char[] row : box) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(row[j]);
            }
            System.out.println();
        }

        sc.close();
    }
}
