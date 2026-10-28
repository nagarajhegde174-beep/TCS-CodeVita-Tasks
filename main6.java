
import java.io.*;
import java.util.*;

public class main6 {

    static final double EPS = 1e-8;

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Stick {
        Point a, b;
        List<Point> points = new ArrayList<>();

        Stick(Point a, Point b) {
            this.a = a;
            this.b = b;
            points.add(a);
            points.add(b);
        }
    }

    static class Edge {
        int to, id;
        double length;

        Edge(int to, int id, double length) {
            this.to = to;
            this.id = id;
            this.length = length;
        }
    }

    static List<Stick> sticks = new ArrayList<>();
    static List<Point> nodes = new ArrayList<>();
    static List<List<Edge>> graph = new ArrayList<>();
    static Map<String, Integer> nodeMap = new HashMap<>();

    static int[] parent, parentEdge;
    static boolean[] visited;
    static List<Integer> cycle;
    static boolean found;

    static double cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y)
                - (b.y - a.y) * (c.x - a.x);
    }

    static boolean onSegment(Point p, Point a, Point b) {
        return Math.abs(cross(a, b, p)) < EPS
                && p.x >= Math.min(a.x, b.x) - EPS
                && p.x <= Math.max(a.x, b.x) + EPS
                && p.y >= Math.min(a.y, b.y) - EPS
                && p.y <= Math.max(a.y, b.y) + EPS;
    }

    static String key(Point p) {
        double x = Math.round(p.x * 100.0) / 100.0;
        double y = Math.round(p.y * 100.0) / 100.0;
        return String.format(Locale.US, "%.2f,%.2f", x, y);
    }

    static Point normalize(Point p) {
        return new Point(
                Math.round(p.x * 100.0) / 100.0,
                Math.round(p.y * 100.0) / 100.0
        );
    }

    static void addIntersection(Stick a, Stick b) {
        Point p = a.a, q = b.a;
        Point r = new Point(a.b.x - a.a.x, a.b.y - a.a.y);
        Point s = new Point(b.b.x - b.a.x, b.b.y - b.a.y);

        double den = r.x * s.y - r.y * s.x;
        double qpx = q.x - p.x;
        double qpy = q.y - p.y;

        if (Math.abs(den) < EPS) {
            // Parallel lines: include shared endpoints if present.
            Point[] candidates = {a.a, a.b, b.a, b.b};

            for (Point c : candidates) {
                if (onSegment(c, a.a, a.b)
                        && onSegment(c, b.a, b.b)) {
                    Point z = normalize(c);
                    a.points.add(z);
                    b.points.add(z);
                }
            }
            return;
        }

        double t = (qpx * s.y - qpy * s.x) / den;
        double u = (qpx * r.y - qpy * r.x) / den;

        if (t >= -EPS && t <= 1 + EPS
                && u >= -EPS && u <= 1 + EPS) {
            Point z = normalize(new Point(
                    p.x + t * r.x,
                    p.y + t * r.y
            ));

            a.points.add(z);
            b.points.add(z);
        }
    }

    static int getNode(Point p) {
        p = normalize(p);
        String k = key(p);

        if (nodeMap.containsKey(k)) {
            return nodeMap.get(k);
        }

        int id = nodes.size();
        nodes.add(p);
        graph.add(new ArrayList<>());
        nodeMap.put(k, id);
        return id;
    }

    static double distance(Point a, Point b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    static void addEdge(int u, int v, double len, int id) {
        graph.get(u).add(new Edge(v, id, len));
        graph.get(v).add(new Edge(u, id, len));
    }

    static boolean dfs(int u, int incomingEdge) {
        visited[u] = true;

        for (Edge e : graph.get(u)) {
            if (e.id == incomingEdge) continue;

            if (!visited[e.to]) {
                parent[e.to] = u;
                parentEdge[e.to] = e.id;

                if (dfs(e.to, e.id)) return true;
            } else if (isAncestor(e.to, u)) {
                cycle = new ArrayList<>();

                int cur = u;
                cycle.add(cur);

                while (cur != e.to) {
                    cur = parent[cur];
                    cycle.add(cur);
                }

                Collections.reverse(cycle);
                return true;
            }
        }

        return false;
    }

    static boolean isAncestor(int a, int b) {
        int cur = b;

        while (cur != -1) {
            if (cur == a) return true;
            cur = parent[cur];
        }

        return false;
    }

    static double cycleEdgeLength(int u, int v) {
        for (Edge e : graph.get(u)) {
            if (e.to == v) return e.length;
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in)
        );

        int n = Integer.parseInt(br.readLine().trim());
        double totalLength = 0;

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            Point a = new Point(
                    Double.parseDouble(st.nextToken()),
                    Double.parseDouble(st.nextToken())
            );

            Point b = new Point(
                    Double.parseDouble(st.nextToken()),
                    Double.parseDouble(st.nextToken())
            );

            sticks.add(new Stick(a, b));
            totalLength += distance(a, b);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                addIntersection(sticks.get(i), sticks.get(j));
            }
        }

        int edgeId = 0;

        for (Stick s : sticks) {
            Point a = s.a, b = s.b;

            s.points.sort(Comparator.comparingDouble(
                    p -> (p.x - a.x) * (b.x - a.x)
                            + (p.y - a.y) * (b.y - a.y)
            ));

            List<Point> unique = new ArrayList<>();

            for (Point p : s.points) {
                if (unique.isEmpty()
                        || distance(unique.get(unique.size() - 1), p) > EPS) {
                    unique.add(p);
                }
            }

            for (int i = 0; i + 1 < unique.size(); i++) {
                Point p = unique.get(i);
                Point q = unique.get(i + 1);

                int u = getNode(p);
                int v = getNode(q);

                if (u != v) {
                    addEdge(u, v, distance(p, q), edgeId++);
                }
            }
        }

        int size = nodes.size();
        visited = new boolean[size];
        parent = new int[size];
        parentEdge = new int[size];

        Arrays.fill(parent, -1);
        Arrays.fill(parentEdge, -1);

        for (int i = 0; i < size && !found; i++) {
            if (!visited[i] && dfs(i, -1)) {
                found = true;
            }
        }

        if (!found || cycle == null || cycle.size() < 3) {
            System.out.println("Abandoned");
            return;
        }

        double polygonArea = 0;
        double perimeter = 0;
        int m = cycle.size();

        for (int i = 0; i < m; i++) {
            Point p = nodes.get(cycle.get(i));
            Point q = nodes.get(cycle.get((i + 1) % m));

            polygonArea += p.x * q.y - q.x * p.y;
            perimeter += distance(p, q);
        }

        polygonArea = Math.abs(polygonArea) / 2.0;

        double remaining = Math.max(0, totalLength - perimeter);
        double computerArea = (remaining / 4.0) * (remaining / 4.0);

        if (polygonArea >= computerArea - EPS) {
            System.out.println("Kalyan");
        } else {
            System.out.println("Computer");
        }
    }
}

