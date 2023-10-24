package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import byow.TileEngine.Tileset;
import byow.WorldObjects.Player;

import java.io.*;


public class Engine {
    TERenderer ter;
    TETile[][] world;

    TETile[][] interactable;
    Player player;
    Lighting lighting;
    MenuScreen menuScreen;
    InputScreen seedInputScreen;
    boolean displayScreen;
    String hudText;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int MENU = 0;
    private static final int SEEDINPUT = 1;
    private static final int GAMEPLAY = 2;
    private static final int QUIT = 3;
    private static char[] validMenuCommands = {'n', 'l', 'q'};
    private static char[] validSeedCommands = {'s', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] validGamePlayCommands = {'w', 'a', 's', 'd', ':', 'q', 'i', 'o'};


    public Engine() {
        menuScreen = new MenuScreen();
        seedInputScreen = new InputScreen("Input Seed (S to Save)");
        ter = new TERenderer();
        displayScreen = false;
        hudText = "";
        this.interactable = new TETile[WIDTH][HEIGHT];
    }

    private String getPrevWorldCommands() throws IOException {
        FileReader fr = new FileReader("save-file.txt");
        StringBuilder prevWorld = new StringBuilder();
        int i;
        while ((i = fr.read()) != -1) {
            char c = (char) i;
            if (c == ':') {
                break;
            }
            prevWorld.append(c);
        }
        return prevWorld.toString();
    }

    private void loadGame(String input) {
        long seed = getSeed(input, 1);
        WorldGenerator worldGen = new WorldGenerator(WIDTH, HEIGHT, seed);
        world = worldGen.generate();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                interactable[i][j] = Tileset.CLEAR;
            }
        }
        this.player = new Player(interactable, world, Tileset.DIRT);
        this.lighting = new Lighting(player, WIDTH, HEIGHT);
        if (displayScreen) {
            ter.initialize(WIDTH, HEIGHT);
            renderGraphics();
        }
    }

    private int loadPrevGame(StringBuilder inputSoFar) throws IOException {
        inputSoFar.setLength(0);
        String prevWorldCommands = getPrevWorldCommands();
        inputSoFar.append(prevWorldCommands);
        if (prevWorldCommands.length() == 0) {
            System.out.println("No previous game saved. Enter a seed.");
            return MENU;
        }
        boolean prev = displayScreen;
        displayScreen = false;
        processInput(false, prevWorldCommands);
        displayScreen = prev;
        if (displayScreen) {
            ter.initialize(WIDTH, HEIGHT);
            renderGraphics();
        }
        return GAMEPLAY;
    }

    private int goToSeedInput(StringBuilder inputSoFar, char command) {
        inputSoFar.append(command);
        if (displayScreen) {
            seedInputScreen.initialize();
        }
        return SEEDINPUT;
    }

    private int quitGame() {
        return QUIT;
    }

    private boolean contains(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) {
                return true;
            }
        }
        return false;
    }

    private int processMenuInput(StringBuilder inputSoFar, char command) throws IOException {
        if (!contains(validMenuCommands, command)) {
            return MENU;
        }
        return switch (command) {
            case 'l' -> loadPrevGame(inputSoFar);
            case 'n' -> goToSeedInput(inputSoFar, command);
            case 'q' -> quitGame();
            default -> MENU;
        };
    }

    private int processSeedInput(StringBuilder inputSoFar, char command) {
        if (!contains(validSeedCommands, command)) {
            return SEEDINPUT;
        }
        inputSoFar.append(command);
        if (command == 's') {
            loadGame(inputSoFar.toString());
            if (displayScreen) {
                ter.initialize(WIDTH, HEIGHT);
            }
            return GAMEPLAY;
        }
        if (displayScreen) {
            seedInputScreen.updateInput(inputSoFar.substring(1)); //substring not include 'n'
        }
        return SEEDINPUT;
    }

    private void saveGame(String gameInput) {
        FileWriter writer;
        try {
            writer = new FileWriter("save-file.txt", false);
            writer.append(gameInput);
            writer.close();
        } catch (IOException e) {
            //Catch silently
        }
    }

    private int processGamePlayInput(StringBuilder inputSoFar, char command) {
        if (!contains(validGamePlayCommands, command)) {
            return GAMEPLAY;
        }
        System.out.println(command);
        int last = inputSoFar.length() - 1;
        if (last >= 0 && inputSoFar.charAt(last) == ':') {
            inputSoFar.deleteCharAt(last);
            if (command == 'q') {
                System.out.println("QUIT GAME");
                saveGame(inputSoFar.toString());
                inputSoFar.setLength(0);
                if (displayScreen) {
                    menuScreen.initialize();
                }
                return MENU;
            }
        }
        if (command != 'q') {
            inputSoFar.append(command);
        }

        player.move(command);
        lighting.adjust(command);

        if (displayScreen) {
            renderGraphics();
        }

        return GAMEPLAY;
    }

    /**
     *
     * Method for processing user input
     *
     * @param keyboard - input type of keyboard if true, string input if false
     * @source https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
     */
    private void processInput(boolean keyboard, String input) throws IOException {
        int gameStage = MENU;

        InputSource inputSource;
        if (keyboard) {
            inputSource = new KeyboardInputSource();
        } else {
            inputSource = new StringInputDevice(input);
        }

        StringBuilder inputSoFar = new StringBuilder();

        if (displayScreen) {
            menuScreen.initialize();
        }

        while (gameStage != QUIT && inputSource.possibleNextInput()) {
            char c = Character.toLowerCase(inputSource.getNextKey());
            gameStage = switch (gameStage) {
                case MENU -> processMenuInput(inputSoFar, c);
                case SEEDINPUT -> processSeedInput(inputSoFar, c);
                default -> processGamePlayInput(inputSoFar, c);
            };
            while (gameStage == GAMEPLAY && displayScreen && !StdDraw.hasNextKeyTyped()) {
                processMouseInput();
            }
        }

        if (gameStage == QUIT) {
            System.exit(0);
        }
    }

    private void processMouseInput() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY() + ter.getyOffset();
        int i = (int) Math.floor(x * world.length / WIDTH);
        int j = (int) Math.floor(y * world[0].length / HEIGHT);

        if (i >= 0 && i < world.length && j >= 0 && j < world[0].length) {
            TETile tile = world[i][j];
            hudText = tile.description();
            renderGraphics();
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayScreen = true;
        try {
            processInput(true, "");
        } catch (IOException e) {
            //catch input exception
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many input types.

        displayScreen = false;
        try {
            processInput(false, input);
        } catch (IOException e) {
            //catch exception
        }

        return renderGraphics();
    }

    /**
     * Method used to get the integer seed from the string input of the program
     *
     * @param input the input string as the seed of the world
     * @param defaultSeed the default seed if the input string is invalid
     * @return the integer seed evaluated by the input string
     */
    private long getSeed(String input, long defaultSeed) {
        int first = input.toLowerCase().indexOf('n');
        int last = input.toLowerCase().indexOf('s');
        try {
            String s = input.substring(first + 1, last);
            return Long.parseLong(s);
        } catch (StringIndexOutOfBoundsException e) {
            //returns default seed
        } catch (NumberFormatException e) {
            //returns default seed
        }
        return defaultSeed;
    }

    /**
     *
     * @param width bounding width of world
     * @param height bounding height of world
     * @param seed seed used for world generation
     * @return list of rooms in the world
     */

    /**
     *:
     * Method used to test world rendering
     *
     */

    private TETile[][] renderGraphics() {
        TETile[][] graphics = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                graphics[i][j] = world[i][j];
            }
        }

        for (int i = 0; i < interactable.length; i++) {
            for (int j = 0; j < interactable[i].length; j++) {
                if (!interactable[i][j].equals(Tileset.CLEAR)) {
                    graphics[i][j] = interactable[i][j];
                }
            }
        }
        lighting.lightUp(graphics);
        if (displayScreen) {
            ter.renderFrame(graphics, hudText);
        }
        return graphics;
    }
}
