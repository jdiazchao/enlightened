package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class InputScreen extends DisplayScreen {
    private String header;
    public InputScreen(String header) {
        this.header = header;
    }

    @Override
    public void initialize() {
        initialize("");
    }

    public void initialize(String defaultInput) {
        super.initialize();
        updateInput(defaultInput);
    }

    public void updateInput(String input) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);

        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height * 3 / 5, header);

        int size = 20;
        Font fontSmall = new Font("Monaco", Font.TRUETYPE_FONT, size);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2, input);

        StdDraw.show();
    }
}
