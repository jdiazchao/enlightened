package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class DisplayScreen {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    public int width;
    public int height;
    public DisplayScreen() {
        this.width = WIDTH;
        this.height = HEIGHT;
    }
    public void initialize() {
        StdDraw.setCanvasSize(width, height);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
    }
}
