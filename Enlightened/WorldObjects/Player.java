package byow.WorldObjects;

import byow.Core.Engine;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    public WorldTile position;
    public WorldTile prevPosition;
    private TETile[][] interactable;
    private TETile[][] world;
    private TETile appearance;

    private TETile standingOn;

    public Player(TETile[][] interactable, TETile[][] world, TETile appearance) {
        this.interactable = interactable;
        this.world = world;
        this.appearance = appearance;

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    this.prevPosition = new WorldTile(i, j, Tileset.NOTHING);
                    this.position = new WorldTile(i, j, Tileset.NOTHING);
                    this.standingOn = Tileset.FLOOR;
                }
            }
        }

        updateWorld();
    }

    public  void move(char command) {
        switch (command) {
            case 'w': moveIn(Direction.UP); break;
            case 's': moveIn(Direction.DOWN); break;
            case 'd': moveIn(Direction.RIGHT); break;
            case 'a': moveIn(Direction.LEFT); break;
        }
    }

    private void moveIn(Direction direction) {
        int newX = position.getX();
        int newY = position.getY();

        switch (direction) {
            case UP: newY++; break;
            case DOWN: newY--; break;
            case RIGHT: newX++; break;
            case LEFT: newX--; break;
        }

        if (walkable(newX, newY)) {
            this.prevPosition = new WorldTile(position.getX(), position.getY(), Tileset.CLEAR);
            this.position = new WorldTile(newX, newY, appearance);
        }

        updateWorld();
    }

    public enum Direction {
        UP, DOWN, RIGHT, LEFT
    }

    private boolean walkable(int x, int y) {
        if (x >= 0 && x < world.length) {
            if (y >= 0 && y < world[0].length) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateWorld() {

        int prevX = prevPosition.getX();
        int prevY = prevPosition.getY();
        interactable[prevX][prevY] = Tileset.CLEAR;

        int x = position.getX();
        int y = position.getY();
        interactable[x][y] = appearance;
    }
}
