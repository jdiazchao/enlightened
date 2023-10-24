package byow.WorldObjects;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    private int x0;
    private int y0;
    public List<WorldTile> boundary;

    public List<WorldTile> wallBoundary;
    public List<WorldTile> tiles;

    public Room(int x0, int y0) {
        this.x0 = x0;
        this.y0 = y0;
        this.tiles = new ArrayList<>();
        this.boundary = new ArrayList<>();
        this.wallBoundary = new ArrayList<>();
    }

     //Return x origin
    public int getX() {
        return x0;
    }

     //Return y origin
    public int getY() {
        return y0;
    }

    private List<WorldTile> inBoundBoundary(int w, int h) {
        List<WorldTile> tiles = new ArrayList<>();
        for (WorldTile tile : wallBoundary) {
            if (tile.inBounds(w, h)) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public WorldTile randomEntrance(Random random, int w, int h) {
        List<WorldTile> tiles = inBoundBoundary(w, h);
        int randIdx = random.nextInt(0, tiles.size());
        return tiles.get(randIdx);
    }

    public void drawOn(TETile[][] canvas, List<TETile> noOverwriteTiles) {
        for (WorldTile tile : tiles) {
            int x = tile.getX();
            int y = tile.getY();
            if (x >= 0 && x < canvas.length && y >= 0 && y < canvas[0].length) {
                if (!noOverwriteTiles.contains(canvas[x][y])) {
                    canvas[x][y] = tile.getTile();
                }
            }
        }

        for (WorldTile tile : wallBoundary) {
            int x = tile.getX();
            int y = tile.getY();
            if (x >= 0 && x < canvas.length && y >= 0 && y < canvas[0].length) {
                if (!noOverwriteTiles.contains(canvas[x][y])) {
                    canvas[x][y] = tile.getTile();
                }
            }
        }

        //Uncomment for testing
        /*if (x0 >= 0 && x0 < canvas.length && y0 >= 0 && y0 < canvas[0].length) {
            canvas[x0][y0] = Tileset.NOTHING;
        }*/
    }

    public boolean overlapWith(Room neighbour) {
        if (this instanceof Temple) {
            // Call the overlap() method on the Temple object
            return ((Temple) this).overlapWith(neighbour);
        } else if (this instanceof Clearing) {
            return ((Clearing) this).overlapWith(neighbour);
        } else {
            return false;
        }
    }

    public boolean outOfBounds(int height, int width, int pad) {
        for (WorldTile tile : boundary) {
            if (tile.getY() < pad || tile.getY() > height - pad) {
                return true;
            }
            if (tile.getX() < pad - 1 || tile.getX() > width - pad) {
                return true;
            }
        }
        return false;
    }
}
