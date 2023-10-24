package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile NOTHING = new TETile('a', Color.black, Color.black, "nothing");

    public static final TETile FLOOR = new TETile('b', Color.gray, new Color(145, 144, 144),
            "floor");
    public static final TETile WALL = new TETile('c', Color.gray, new Color(124, 124, 124),
            "wall");
    public static final TETile TREE = new TETile('d', Color.gray, new Color(69, 157, 69),
            "tree");
    public static final TETile DARK_TREE = new TETile('e', Color.gray, new Color(60, 143, 60),
            "tree");
    public static final TETile LIGHT_DIRT = new TETile('f', Color.gray, new Color(211, 163, 121),
            "dirt");
    public static final TETile DIRT = new TETile('g', Color.gray, new Color(192, 147, 104),
            "dirt");
    public static final TETile DARK_DIRT = new TETile('h', Color.gray, new Color(171, 123, 81),
            "dirt");
    public static final TETile LIGHT_LAKE = new TETile('i', Color.gray, new Color(67, 173, 245),
            "lake");
    public static final TETile LAKE = new TETile('j', Color.gray, new Color(58, 163, 234),
            "lake");
    public static final TETile DARK_LAKE = new TETile('k', Color.gray, new Color(51, 156, 227),
            "lake");
    public static final TETile DARK2_LAKE = new TETile('l', Color.gray, new Color(40, 142, 211),
            "lake");

    public static final TETile CLEAR = new TETile(' ', Color.gray, new Color(255, 255, 255, 0),
            "clear");
}


