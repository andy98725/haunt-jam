package com.haunt.game;

public enum Tile {
    EMPTY(0), SOLID(1), KILL(2);

    public final int priority;

    private Tile(int priority) {
        this.priority = priority;
    }

}