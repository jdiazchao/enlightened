package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class MenuScreen extends DisplayScreen{
    @Override
    public void initialize() {
        super.initialize();
        StdDraw.setPenColor(Color.WHITE);

        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height * 2 / 3, "CS61B: THE GAME");

        int size = 18;
        double step = size * 1.5;
        Font fontSmall = new Font("Monaco", Font.TRUETYPE_FONT, size);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height * 1 / 3, "New Game (N)");
        StdDraw.text(width / 2, height * 1 / 3 + step, "Load Game (L)");
        StdDraw.text(width / 2, height * 1 / 3 + step * 2, "Quit (Q)");

        StdDraw.show();
    }
}
