package byow.WorldObjects;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldTile {
    private int x;
    private int y;
    private TETile tile;
    public WorldTile(int x, int y) {
        this(x, y, Tileset.NOTHING);
    }
    public WorldTile(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean inBounds(int w, int h) {
        return getX() < w && getY() < h && getX() >= 0 && getY() >= 0;
    }

    public TETile getTile() {
        return tile;
    }
}