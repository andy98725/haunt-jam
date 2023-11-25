package com.haunt.game;

public enum Tile {
    EMPTY(0, -1), SOLID(1, 1),
    // Decor
    D_DRAWER(-1, 20), D_VASE(-1, 21), D_WINDOW(-1, 22);

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
                return Tile.EMPTY;
            default:
            case SOLID:
                return Tile.SOLID;

        }
    }

}