package com.haunt.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public class Levels {

    private final HauntGame game;

    private final Level[] levels;

    public Levels(HauntGame game) {
        this.game = game;
        levels = new Level[] {
                new Level("Level1.csv"),
                new Level("Level2.csv"),
                new Level("Level3.csv"),
                new Level("Level4.csv"),
        };
        this.game.setLevel(levels[0]);
    }

    public void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            this.game.setLevel(levels[0]);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            this.game.setLevel(levels[1]);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            this.game.setLevel(levels[2]);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
            this.game.setLevel(levels[3]);
    }
}
