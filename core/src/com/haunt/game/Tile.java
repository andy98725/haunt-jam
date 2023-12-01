package com.haunt.game;

public enum Tile {
    BG(0, -1), SOLID(2, 1), BONUS(2, 26),
    FALLTHROUGH(1, 2), FALLTHROUGH_EMPTY(1, 25),
    // // Decor
    // D_DRAWER(-1, 20), D_VASE(-1, 21), D_WINDOW(-1, 22), D_DOOR(-1, 23),
    // D_CLOCK(-1, 24),
    // Util
    MATCH(-1, -2);

    public final int priority, mapID;

    private Tile(int priority, int mapID) {
        this.priority = priority;
        this.mapID = mapID;
    }

    public Tile collisionTile() {
        switch (this) {
            default:
                return this;
            case FALLTHROUGH_EMPTY:
                return FALLTHROUGH;
            case BONUS:
                return SOLID;
        }
    }

    public Tile bgTile() {
        switch (this) {
            default:
            case FALLTHROUGH_EMPTY:
                return null;
            case FALLTHROUGH:
                return BG;
        }

    }

}