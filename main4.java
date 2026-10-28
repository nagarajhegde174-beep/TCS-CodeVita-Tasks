
import java.util.*;

public class main4 {

    static class Slide {
        int x1, y1, x2, y2;

        Slide(int a, int b, int c, int d) {
            x1 = a;
            y1 = b;
            x2 = c;
            y2 = d;
        }

        boolean contains(int x, int y) {
            return x >= Math.min(x1, x2)
                    && x <= Math.max(x1, x2)
                    && y >= Math.min(y1, y2)
                    && y <= Math.max(y1, y2)
                    && Math.abs(x - x1) == Math.abs(y - y1)
                    && Math.abs(x - x2) == Math.abs(y - y2);
        }

        int[] downhill(int x, int y) {
            int lowY = Math.min(y1, y2);

            if (y <= lowY) {
                return null;
            }

            int lowX = (y1 < y2) ? x1 : x2;

            return new int[] {
                    x + Integer.signum(lowX - x),
                    y - 1
            };
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        Slide[] slides = new Slide[n];

        for (int i = 0; i < n; i++) {
            slides[i] = new Slide(
                    sc.nextInt(), sc.nextInt(),
                    sc.nextInt(), sc.nextInt()
            );
        }

        int x = sc.nextInt();
        int y = sc.nextInt();
        long energy = sc.nextLong();

        while (y > 0) {
            List<Slide> touching = new ArrayList<>();

            for (Slide s : slides) {
                if (s.contains(x, y)) {
                    touching.add(s);
                }
            }

            List<int[]> moves = new ArrayList<>();

            for (Slide s : touching) {
                int[] next = s.downhill(x, y);

                if (next != null) {
                    boolean duplicate = false;

                    for (int[] p : moves) {
                        if (p[0] == next[0] && p[1] == next[1]) {
                            duplicate = true;
                            break;
                        }
                    }

                    if (!duplicate) {
                        moves.add(next);
                    }
                }
            }

            if (touching.size() >= 2) {
                long cost = (long) x * y;

                if (energy < cost) {
                    break;
                }

                energy -= cost;
            }

            if (!moves.isEmpty()) {
                if (energy < 1) {
                    break;
                }

                int[] next = moves.get(0);
                x = next[0];
                y = next[1];
                energy--;
                continue;
            }

            // No downhill movement: fall vertically.
            int landingY = 0;

            for (Slide s : slides) {
                int minX = Math.min(s.x1, s.x2);
                int maxX = Math.max(s.x1, s.x2);

                if (x < minX || x > maxX) {
                    continue;
                }

                int slideY = s.y1
                        + Integer.signum(s.x2 - s.x1) * (x - s.x1);

                if (slideY < y && slideY > landingY) {
                    landingY = slideY;
                }
            }

            y = landingY;
        }

        System.out.println(x + " " + y);
        sc.close();
    }
}

