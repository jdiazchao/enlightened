package byow.WorldObjects;

import byow.TileEngine.TETile;

import java.util.*;

public class PathFinder {
    private static class Vertex{
        public int x;
        public int y;
        public double distTo;
        public Vertex edgeTo;
        public boolean marked;
        public Vertex(int x, int y) {
            this(x, y, Integer.MAX_VALUE);
        }
        public Vertex(int x, int y, int distTo) {
            this.x = x;
            this.y = y;
            this.distTo = distTo;
            edgeTo = null;
            marked = false;
        }
    }

    private static class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex o1, Vertex o2) {
            return Double.compare(o1.distTo, o2.distTo);
        }
    }
    private static boolean inbounds(int x, int y, int w, int h, int pad) {
        return (x >= pad && x < w - 1 - pad && y >= pad && y < h - 1 - pad);
    }

    public static List<WorldTile> findPath(TETile[][] world, List<Room> rooms, int xStart, int yStart, int xEnd, int yEnd, int pad, Random random) {
        int w = world.length;
        int h = world[0].length;
        Vertex[][]worldVertices = new Vertex[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                worldVertices[i][j] = new Vertex(i, j);
            }
        }
        for (Room room : rooms) {
            for (WorldTile tile : room.tiles) {
                worldVertices[tile.getX()][tile.getY()].marked = true;
            }
        }

        PriorityQueue<Vertex> pq = new PriorityQueue(new VertexComparator());

        Vertex startPos = worldVertices[xStart][yStart];
        startPos.distTo = 0;
        Vertex endPos = worldVertices[xEnd][yEnd];
        startPos.marked = false;
        endPos.marked = false;

        pq.add(startPos);
        while (!pq.isEmpty()) {
            Vertex p = pq.remove();
            p.marked = true;
            if (p.equals(endPos)) {
                break;
            }
            List<Vertex> adj = new ArrayList<>();
            if (inbounds(p.x + 1, p.y, w, h, pad)) {
                adj.add(worldVertices[p.x + 1][p.y]);
            }
            if (inbounds(p.x - 1, p.y, w, h, pad)) {
                adj.add(worldVertices[p.x - 1][p.y]);
            }
            if (inbounds(p.x, p.y + 1, w, h, pad)) {
                adj.add(worldVertices[p.x][p.y + 1]);
            }
            if (inbounds(p.x, p.y - 1, w, h, pad)) {
                adj.add(worldVertices[p.x][p.y - 1]);
            }
            for (int i = 0; i < adj.size(); i++) {
                Vertex q = adj.get(i);
                if (q.marked) {
                    continue; //there is already a path in this area, override if empty pq (break through walls)
                }

                double heuristic = 0;
                double weight = Math.pow(q.x - p.x, 2) + Math.pow(q.y - p.y, 2);
                double tot = p.distTo + weight + heuristic;
                if (tot < q.distTo) {
                    //changing priority in the queue
                    pq.remove(q);
                    q.distTo = tot;
                    q.edgeTo = p;
                    pq.add(q);
                }
            }
        }

        Vertex curr = endPos;
        List<WorldTile> res = new ArrayList<>();

        while (curr != null) {
            WorldTile tile = new WorldTile(curr.x, curr.y);
            res.add(tile);
            curr = curr.edgeTo;
        }

        return res;
    }
}
