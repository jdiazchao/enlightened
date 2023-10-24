package byow.WorldObjects;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import javax.crypto.spec.PSource;
import java.util.*;

public class Path {
    public static void drawPath(TETile[][] world, List<WorldTile> tiles, int pad) {
        for (int k = 0; k < tiles.size(); k++) {
            WorldTile tile = tiles.get(k);
            int x = tile.getX();
            int y = tile.getY();
            int low_bound = (pad - 1) / 2;
            int top_bound = pad / 2;
            for (int i = -low_bound; i < top_bound + 1; i++) {
                for (int j = -low_bound; j < top_bound + 1; j++) {
                    if (x + i < 0 || x + i>= world.length || y + j < 0 || y + j>= world[0].length) {
                        continue;
                    }
                    if (i > -low_bound && i < top_bound && j > -low_bound && j < top_bound) {
                        world[x + i][y + j] = Tileset.FLOOR;
                    } else if(!world[x + i][y + j].equals(Tileset.FLOOR)) {
                        world[x + i][y + j] = Tileset.WALL;
                    }
                }
            }
        }
    }
}
