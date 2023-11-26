package com.haunt.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public class Levels {

    private final HauntGame game;

    private final String[] levels;

    public Levels(HauntGame game) {
        this.game = game;
        levels = new String[] { "Level1.csv", "Level2.csv", "Level3.csv", "Level4.csv" };
        setLevel(0);
    }

    private void setLevel(int i) {
        game.setLevel(new Level(levels[i]));
    }

    public void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            setLevel(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            setLevel(1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
            setLevel(2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
            setLevel(3);
    }
}
