package com.haunt.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public class Levels {

    private final HauntGame game;

    private final LevelInfo[] levels;

    public Levels(HauntGame game) {
        this.game = game;
        levels = new LevelInfo[] { new LevelInfo("Level1.csv", 20, 0),
                new LevelInfo("Level2.csv", 10, 5),
                new LevelInfo("LevelVase.csv", 10, 5),
                new LevelInfo("LevelSpike.csv", 10, 5),
                new LevelInfo("LevelPyramid.csv", 10, 3),
                new LevelInfo("LevelSpikeRedux.csv", 10, 3),
                new LevelInfo("LevelSawSilo.csv", 10, 3),
        };
        setLevel(0);
    }

    public void setLevel(int i) {
        if (i >= levels.length || i < 0)
            Gdx.app.log("TODO", "main menu");
        else
            game.setLevel(new Level(levels[i], this, i));
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5))
            setLevel(4);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6))
            setLevel(5);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7))
            setLevel(6);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8))
            setLevel(7);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9))
            setLevel(8);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0))
            setLevel(9);
    }

    public static class LevelInfo {
        public final String levelName;
        public final float beginningTime, timeIncrement;

        protected LevelInfo(String levelName, float beginningTime, float timeIncrement) {
            this.levelName = levelName;
            this.beginningTime = beginningTime;
            this.timeIncrement = timeIncrement;

        }
    }
}
