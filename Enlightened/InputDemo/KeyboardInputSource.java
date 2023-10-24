package byow.InputDemo;

/**
 * Created by hug.
 */
import edu.princeton.cs.algs4.StdDraw;

public class KeyboardInputSource implements InputSource {
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
