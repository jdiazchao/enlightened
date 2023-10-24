package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldObjects.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.io.*;
import java.util.*;

public class Chunk {
    private List<Room> rooms;
    private int width, height;
    private Random random;
    private int pad;

    public Chunk(int width, int height, Random random, int pad) {
        this.rooms = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.random = random;
        this.pad = pad;
    }

    public List<Room> getPlayableRooms() {
        List<Room> playableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room instanceof Temple) {
                playableRooms.add(room);
            }
        }
        return playableRooms;
    }

    /**
     * @param Cx0 x location of chunk
     * @param Cy0 y location of chunk
     * @return grid defining rooms in the chunk
     */

    public void build(int Cx0, int Cy0) {

        Random chunkSource = random; //new Random(source + cantorPair(Cx0, Cy0)); (Non-infinite generation)

        int step = 10;
        for (int i = 0; i < width; i += step) {
            for (int j = 0; j < height; j += step) {
                int x0 =  chunkSource.nextInt(i, i + step);
                int y0 =  chunkSource.nextInt(j, j + step);

                Temple newTemple = new Temple(x0, y0, chunkSource);

                if (newTemple.outOfBounds(height, width, pad)) {
                    continue;
                }

                Iterator<Room> iterator = rooms.iterator();
                boolean overlap = false;

                while (iterator.hasNext()) {
                    Room neighbour = iterator.next();
                    if (newTemple.overlapWith(neighbour)) {
                        overlap = true;
                    }
                }
                if (!overlap) {
                    rooms.add(newTemple);
                }
            }
        }

        for (int i = 0; i < 40; i++) {

            int type = chunkSource.nextInt(7);

            int x0 =  chunkSource.nextInt(0, width);
            int y0 =  chunkSource.nextInt(0, height);

            Room newRoom;

            if (type < 2) {
                newRoom = new Temple(x0, y0, chunkSource);
            } else if (type < 7) {
                newRoom = new Lake(x0, y0, chunkSource);
            } else {
                newRoom = new Clearing(x0, y0, chunkSource);
            }

            if (newRoom.outOfBounds(height, width, pad)) {
                continue;
            }

            Iterator<Room> iterator = rooms.iterator();
            boolean overlap = false;

            while (iterator.hasNext()) {
                Room neighbour = iterator.next();
                if (newRoom.overlapWith(neighbour)) {
                    overlap = true;
                }
                if (newRoom.outOfBounds(height, width, pad)) {
                    overlap = true;
                }
            }

            if (!overlap) {
                rooms.add(newRoom);
            }
        }
    }

    private static long cantorPair(int a, int b) {
        return (long) (0.5 * (a + b) * (a + b + 1) + b);
    }

    public void drawOn(TETile[][] canvas) {
        double[][] heightMap = PerlinNoise.generatePerlinNoise(width, height, 4, 0.1F, random);

        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                canvas[i][j] = new TETile(' ', Color.gray, new Color(60, (int) (143 + 50 * heightMap[i][j]), 60),
                        "tree");
            }
        }

        for (Room room : rooms) {
            if (room instanceof Clearing) {
                room.drawOn(canvas, List.of(Tileset.FLOOR));
            }
        }

        for (Room room : rooms) {
            if (room instanceof Lake) {
                room.drawOn(canvas, List.of(Tileset.FLOOR));
            }
        }

        for (Room room : rooms) {
            if (room instanceof Temple) {
                room.drawOn(canvas, List.of(Tileset.FLOOR));
            }
        }

    }
}