package byow.WorldObjects;

import byow.TileEngine.Tileset;

import java.util.*;

public class Temple extends Room {

    public Temple(int x0, int y0, Random s) {
        super(x0, y0);
        build(s, 48, 5, 8);
    }

    private void build(Random s, int area, int minLength, int maxLength) {
        int width = s.nextInt(maxLength - minLength + 1) + minLength;
        int height = (int) Math.ceil((double) area / width);

        for (int i = 0; i < width + 1; i++) {
            for (int j = 0; j < height + 1; j++) {

                if (i == 0 || i == width || j == 0 || j == height) {
                    WorldTile tile = new WorldTile(getX() + i, getY() + j, Tileset.WALL);
                    tiles.add(tile);
                    if ((i != 0 || (j != 0 && j != height)) && (i != width || (j != 0 && j != height))) {
                        boundary.add(tile);
                    }
                } else {
                    tiles.add(new WorldTile(getX() + i, getY() + j, Tileset.FLOOR));
                }
            }
        }

        for (int i = 1; i < width; i++) {
            WorldTile tile1 = new WorldTile(getX() + i, getY() + 1, Tileset.NOTHING);
            wallBoundary.add(tile1);
            WorldTile tile2 = new WorldTile(getX() + i, getY() + height - 1, Tileset.NOTHING);
            wallBoundary.add(tile2);
        }

        for (int j = 1; j < height; j++) {
            WorldTile tile1 = new WorldTile(getX() + 1, getY() + j, Tileset.NOTHING);
            wallBoundary.add(tile1);
            WorldTile tile2 = new WorldTile(getX() + width - 1, getY() + j, Tileset.NOTHING);
            wallBoundary.add(tile2);
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
                }
            }
        }

        if (!intersection.isEmpty()) {
            return true;
        }

        return false;
    }
}
