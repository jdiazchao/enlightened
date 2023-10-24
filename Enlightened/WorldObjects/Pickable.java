package byow.WorldObjects;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Pickable {

    public WorldTile location;

    public Pickable() {
        this.location = new WorldTile(0, 0, Tileset.CLEAR);
    }
}
