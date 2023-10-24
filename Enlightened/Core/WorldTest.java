package byow.Core;

import byow.TileEngine.TETile;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;

public class WorldTest {
    @Test
    public void sameWorldTest() {
        Engine engine = new Engine();
        TETile[][] world1  = engine.interactWithInputString("n5197880843569031643s");
        TETile[][] world2  = engine.interactWithInputString("n455857754086099036s");
        int sameTiles = 0;
        for (int i = 0; i < world1.length; i++) {
            for (int j = 0; j < world1[0].length; j++) {
                String desc1 = world1[i][j].description();
                String desc2 = world2[i][j].description();
                if (desc1.equals(desc2)) {
                    sameTiles++;
                }
            }
        }
        assertThat(sameTiles != world1.length * world1[0].length);
    }
}
