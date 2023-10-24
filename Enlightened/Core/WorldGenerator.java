package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldObjects.Path;
import byow.WorldObjects.PathFinder;
import byow.WorldObjects.Room;
import byow.WorldObjects.WorldTile;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class WorldGenerator {
    private class RoomPathComparator implements Comparator<Room> {
        @Override
        public int compare(Room o1, Room o2) {
            return Integer.compare(o1.getX(), o2.getX());
        }
    }
    private static final int PAD = 3;
    private TETile[][] world;
    private Chunk chunkGenerator;
    private Random random;
    public WorldGenerator(int width, int height, long seed) {
        random = new Random(seed);
        world = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        chunkGenerator = new Chunk(width, height, random, PAD);
    }

    public TETile[][] generate() {
        chunkGenerator.build(0, 0);
        chunkGenerator.drawOn(world);
        List<Room> rooms = chunkGenerator.getPlayableRooms();

        Collections.shuffle(rooms, random);
        //Collections.sort(rooms, new RoomPathComparator());
        generatePaths(rooms);
        return world;
    }

    private List<WorldTile> connectPath(WorldTile start, WorldTile end, List<Room> rooms) {
        int xStart = start.getX();
        int yStart = start.getY();
        int xEnd = end.getX();
        int yEnd = end.getY();
        List<WorldTile> pathTiles = PathFinder.findPath(world, rooms, xStart, yStart, xEnd, yEnd, PAD / 2, random);
        return pathTiles;
    }
    private void generatePaths(List<Room> rooms) {
        List<TETile> obstacles = List.of(Tileset.LIGHT_LAKE);

        int w = world.length;
        int h = world[0].length;
        List<WorldTile> pathTurnTiles = new ArrayList<>();
        for (int i = 0; i < rooms.size() - 1; i++) {
            WorldTile start = rooms.get(i).randomEntrance(random, w, h);
            WorldTile end = rooms.get(i + 1).randomEntrance(random, w, h);
            List<Room> allButStartEnd = new ArrayList<>();
            for (int j = 0; j < rooms.size(); j++) {
                if (j != i && j != i + 1) {
                    allButStartEnd.add(rooms.get(j));
                }
            }
            List<WorldTile> pathTiles = connectPath(start, end, allButStartEnd);
            Path.drawPath(world, pathTiles, PAD);
            int idx = pathTiles.size() / 2;
            pathTurnTiles.add(pathTiles.get(idx));
        }

        for (int i = 0; i < pathTurnTiles.size() - 1; i++) {
            WorldTile start = pathTurnTiles.get(i);
            WorldTile end = pathTurnTiles.get(i + 1);
            List<Room> allButStartEnd = new ArrayList<>();
            //avoids rooms connecting next two paths
            for (int j = 0; j < rooms.size(); j++) {
                if (j - i < 0 || j - i > 2) {
                    allButStartEnd.add(rooms.get(j));
                }
            }
            List<WorldTile> pathTiles = connectPath(start, end, allButStartEnd);
            Path.drawPath(world, pathTiles, PAD);
        }
    }
}
