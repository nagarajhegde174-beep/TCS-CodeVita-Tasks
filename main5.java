
import java.io.*;
import java.util.*;

public class main5 {

    static class Segment {
        int start, end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    static List<Segment> merge(List<Segment> list) {
        list.sort(Comparator.comparingInt(s -> s.start));

        List<Segment> result = new ArrayList<>();

        for (Segment s : list) {
            if (result.isEmpty()
                    || s.start > result.get(result.size() - 1).end) {
                result.add(new Segment(s.start, s.end));
            } else {
                Segment last = result.get(result.size() - 1);
                last.end = Math.max(last.end, s.end);
            }
        }

        return result;
    }

    static boolean covered(List<Segment> list, int start, int end) {
        if (list == null) return false;

        for (Segment s : list) {
            if (s.start <= start && s.end >= end) return true;
            if (s.start > start) return false;
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());

        Map<Integer, List<Segment>> vertical = new HashMap<>();
        Map<Integer, List<Segment>> horizontal = new HashMap<>();

        TreeSet<Integer> xSet = new TreeSet<>();
        TreeSet<Integer> ySet = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());

            if (x1 == x2) {
                vertical.computeIfAbsent(x1, k -> new ArrayList<>())
                        .add(new Segment(Math.min(y1, y2), Math.max(y1, y2)));
                xSet.add(x1);
            } else if (y1 == y2) {
                horizontal.computeIfAbsent(y1, k -> new ArrayList<>())
                        .add(new Segment(Math.min(x1, x2), Math.max(x1, x2)));
                ySet.add(y1);
            }
        }

        for (int x : xSet) vertical.put(x, merge(vertical.get(x)));
        for (int y : ySet) horizontal.put(y, merge(horizontal.get(y)));

        List<Integer> xs = new ArrayList<>(xSet);
        List<Integer> ys = new ArrayList<>(ySet);

        long count = 0;

        for (int i = 0; i < xs.size(); i++) {
            int x1 = xs.get(i);

            for (int j = i + 1; j < xs.size(); j++) {
                int x2 = xs.get(j);

                for (int a = 0; a < ys.size(); a++) {
                    int y1 = ys.get(a);

                    if (!covered(vertical.get(x1), y1, y1 + 1)
                            || !covered(vertical.get(x2), y1, y1 + 1)
                            || !covered(horizontal.get(y1), x1, x2)) {
                        continue;
                    }

                    for (int b = a + 1; b < ys.size(); b++) {
                        int y2 = ys.get(b);

                        if (covered(vertical.get(x1), y1, y2)
                                && covered(vertical.get(x2), y1, y2)
                                && covered(horizontal.get(y2), x1, x2)) {
                            count++;
                        }
                    }
                }
            }
        }

        System.out.println(count);
    }
}

