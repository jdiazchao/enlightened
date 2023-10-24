package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldObjects.Player;
import edu.princeton.cs.algs4.In;

public class Lighting {

    private Intensity intensity;
    private Player player;

    private int width;
    private int height;

    public Lighting(Player player, int width, int height) {
        this.intensity = Intensity.FULL;
        this.player = player;
        this.width = width;
        this.height = height;
    }

    public void lightUp(TETile[][] graphics) {
        TETile[][] lighting = new TETile[width][height];

        int reach = level();
        int[][] circle = midpointCircle(reach);

        if (!intensity.equals(Intensity.FULL)) {
            for (int i = 0; i < lighting.length; i++) {
                for (int j = 0; j < lighting[i].length; j++) {
                    lighting[i][j] = Tileset.NOTHING;
                }
            }

            for (int i = 0; i < circle.length; i++) {
                for (int j = 0; j < circle[i].length; j++) {
                    int x = player.position.getX() + i - reach;
                    int y = player.position.getY() + j - reach;
                    if (circle[i][j] > 0) {
                        if (x >= 0 && x < lighting.length) {
                            if (y >= 0 && y < lighting[i].length) {
                                lighting[x][y] = Tileset.CLEAR;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < lighting.length; i++) {
                for (int j = 0; j < lighting[i].length; j++) {
                    lighting[i][j] = Tileset.CLEAR;
                }
            }
        }

        for (int i = 0; i < lighting.length; i++) {
            for (int j = 0; j < lighting[i].length; j++) {
                if (!lighting[i][j].equals(Tileset.CLEAR)) {
                    graphics[i][j] = Tileset.NOTHING;
                }
            }
        }
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

    public void adjust(char command) {
        switch (command) {
            case 'o': intensity = higher(); break;
            case 'i': intensity = lower(); break;
            default: break;
        }
    }

    public Intensity higher() {
        switch (intensity) {
            case NONE: return Intensity.LOW;
            case LOW: return Intensity.MEDIUM;
            case MEDIUM, FULL: return Intensity.FULL;
        }
        return Intensity.FULL;
    }

    public Intensity lower() {
        switch (intensity) {
            case NONE, LOW: return Intensity.NONE;
            case MEDIUM: return Intensity.LOW;
            case FULL: return Intensity.MEDIUM;
        }
        return Intensity.NONE;
    }

    public int level() {
        switch (intensity) {
            case NONE: return 0;
            case LOW: return 4;
            case MEDIUM: return 8;
            case FULL: return 20;
        }

        return Math.max(width, height);
    }

    enum Intensity {
        NONE, LOW, MEDIUM, FULL
    }
}
