package byow.WorldObjects;

import byow.Core.PerlinNoise;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Clearing extends Room {

    public Clearing(int x0, int y0, Random s) {
        super(x0, y0);
        build(s, 80, 8, 10);
    }

    private void build(Random s, int area, int minLength, int maxLength) {
        int width = s.nextInt(maxLength - minLength + 1) + minLength;
        int height = (int) Math.ceil((double) area / width);

        double[][] heightMap = PerlinNoise.generatePerlinNoise(width + 1, height + 1, 1, 0.005F, s);

        for (int i = 0; i < width + 1; i++) {
            for (int j = 0; j < height + 1; j++) {
                int x = getX() + i;
                int y = getY() + j;
                if (i == 0 || i == width || j == 0 || j == height) {
                    boundary.add(new WorldTile(x, y, Tileset.LIGHT_DIRT));
                }
                if (s.nextInt(3) > 1) {
                    tiles.add(new WorldTile(x, y, Tileset.LIGHT_DIRT));
                }
            }
        }
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                if (s.nextInt(4) > 1) {
                    int x = getX() + i;
                    int y = getY() + j;
                    tiles.add(new WorldTile(x, y, Tileset.DIRT));
                }
            }
        }
        for (int i = 2; i < width - 2; i++) {
            for (int j = 1; j < height - 2; j++) {
                int x = getX() + i;
                int y = getY() + j;
                tiles.add(new WorldTile(x, y, Tileset.DIRT));
            }
        }
        for (int i = 2; i < width - 1; i++) {
            for (int j = 2; j < height - 1; j++) {
                if (s.nextInt(5) > 1) {
                    int x = getX() + i;
                    int y = getY() + j;
                    tiles.add(new WorldTile(x, y, Tileset.DARK_DIRT));
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
                    if (!(neighbour instanceof Temple)) {
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
}

