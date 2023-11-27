package com.haunt.game;

public enum Tile {
    EMPTY(0, -1), SOLID(2, 1), FALLTHROUGH(1, 2),
    // Decor
    D_DRAWER(-1, 20), D_VASE(-1, 21), D_WINDOW(-1, 22), D_DOOR(-1, 23), D_CLOCK(-1, 24),
    // Util
    MATCH(-1, -2);

    public final int priority, mapID;

    private Tile(int priority, int mapID) {
        this.priority = priority;
        this.mapID = mapID;
    }

    public Tile bgTile() {
        return collisionTile();
    }

    public Tile collisionTile() {
        switch (this) {
            case D_DRAWER:
            case D_VASE:
            case D_WINDOW:
            case EMPTY:
            default:
                return Tile.EMPTY;
            case SOLID:
                return Tile.SOLID;

        }
    }

}