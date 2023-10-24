package byow.WorldObjects;

import byow.TileEngine.Tileset;

import java.util.*;

public class Lake extends Room {

    public Lake(int x0, int y0, Random s) {
        super(x0, y0);
        build(s, s.nextInt(3, 6));
    }

    private void build(Random s, int radius) {
        int[][] circle = midpointCircle(radius);

        for (int i = 0; i < circle.length; i++) {
            for (int j = 0; j < circle[i].length; j++) {
                int x = getX() + i;
                int y = getY() + j;
                boundary.add(new WorldTile(x, y, Tileset.LIGHT_LAKE));
                if (circle[i][j] == 1) {
                    tiles.add(new WorldTile(x, y, Tileset.LIGHT_LAKE));
                }
                if (circle[i][j] == 2) {
                    tiles.add(new WorldTile(x, y, Tileset.LAKE));
                }
                if (circle[i][j] == 3) {
                    tiles.add(new WorldTile(x, y, Tileset.DARK_LAKE));
                }
                if (circle[i][j] > 3) {
                    tiles.add(new WorldTile(x, y, Tileset.DARK2_LAKE));
                }
            }
        }
    }

    public boolean overlapWith(Room neighbour) {
        List<WorldTile> intersection = new ArrayList<>();

        for (WorldTile tile : boundary) {
            for (WorldTile neighbourTile : neighbour.boundary) {
                int x1 = tile.getX();
                int y1 = tile.getY();
                int x2 = neighbourTile.getX();
                int y2 = neighbourTile.getY();
                if (x1 == x2 && y1 == y2) {
                    intersection.add(tile);
                    if (!(neighbour instanceof Lake)) {
                        return true;
                    }
                }
            }
        }

        if (neighbour instanceof Clearing) {
            combineWith(neighbour, intersection);
        }

        return false;
    }

    public void combineWith(Room neighbour, List<WorldTile> intersection) {
    }

    private int[][] midpointCircle(int r) {
        int[][] grid = new int[2*r+1][2*r+1];
        int x = 0;
        int y = r;
        int d = 1 - r;

        while (x <= y) {
            for (int i = -y; i <= y; i++) {
                int dist = (int) Math.sqrt(x*x + i*i);
                if (dist <= r) {
                    grid[x+r][i+r] = r - dist + 1;
                    grid[-x+r][i+r] = r - dist + 1;
                    grid[i+r][x+r] = r - dist + 1;
                    grid[i+r][-x+r] = r - dist + 1;
                }
            }

            if (d < 0) {
                d += 2*x + 3;
            } else {
                d += 2*(x-y) + 5;
                y--;
            }
            x++;
        }

        return grid;
    }
}

