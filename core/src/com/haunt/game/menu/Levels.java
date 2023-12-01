package com.haunt.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public class Levels {

    private final HauntGame game;

    private final LevelInfo[] levels, bonusLevels;

    public Levels(HauntGame game) {
        this.game = game;
        levels = new LevelInfo[] { new LevelInfo("Level1.csv", 12, 0),
                new LevelInfo("Level2.csv", 8, 5),
                new LevelInfo("LevelVase.csv", 8, 2),
                new LevelInfo("LevelSpike.csv", 6, 4),
                new LevelInfo("LevelTowerSaws.csv", 8, 0),
                new LevelInfo("LevelLabrynth.csv", 6, 4),
                new LevelInfo("LevelCircuit.csv", 7, 3),
                new LevelInfo("LevelStar.csv", 8, 2),
                new LevelInfo("LevelWeave.csv", 7, 3),
                new LevelInfo("LevelSpikeRedux.csv", 6, 3),
                new LevelInfo("LevelInfinity.csv", 4, 1),
                new LevelInfo("LevelSawSilo.csv", 6, 3),
                new LevelInfo("LevelPyramid.csv", 4, 4),
                new LevelInfo("LevelDonut.csv", 1.5f, 1.5f),
                new LevelInfo("LevelCross.csv", 4, 5),
                new LevelInfo("LevelTowerFall.csv", 3, 2),
                new LevelInfo("LevelLabrynthRedux.csv", 5, 5),
                new LevelInfo("LevelWiggly.csv", 6, 3),
                new LevelInfo("LevelWeaveRedux.csv", 4, 2.5f),
                new LevelInfo("LevelCircle.csv", 4, 2),
                new LevelInfo("LevelTowerRedux.csv", 2, 1),
                new LevelInfo("LevelDoubleBack.csv", 6, 3.5f),
                new LevelInfo("LevelInfinityRedux.csv", 4, 2),
                new LevelInfo("LevelCrossRedux.csv", 3, 4),
        };
        bonusLevels = new LevelInfo[] { new LevelInfo("bonus/Intro.csv", 4, 3f, true),
                new LevelInfo("bonus/Void.csv", 1, 1.75f, true),
        };
        setLevel(0);
    }

    public void setLevel(int i) {
        if (i >= levels.length || i < 0)
            game.win();
        else
            game.setLevel(new Level(levels[i], i, game, this));
    }

    public void setBonusLevel(int i) {
        if (i >= bonusLevels.length || i < 0)
            game.win();
        else
            game.setLevel(new Level(bonusLevels[i], i, game, this));
    }

    private static final int[] numkeys = new int[] { Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3,
            Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6,
            Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9,
            Input.Keys.NUM_0 };

    public void input() {
        // if (!HauntGame.DEBUG)
        // return;

        for (int i = 0; i < numkeys.length; i++)
            if (Gdx.input.isKeyJustPressed(numkeys[i])) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
                    setBonusLevel(i);
                else
                    setLevel(i);
            }
    }

    public static class LevelInfo {
        public final String levelName;
        public final float beginningTime, timeIncrement;
        public final boolean bonusLevel;

        protected LevelInfo(String levelName, float beginningTime, float timeIncrement) {
            this.levelName = levelName;
            this.beginningTime = beginningTime;
            this.timeIncrement = timeIncrement;
            this.bonusLevel = false;
        }

        protected LevelInfo(String levelName, float beginningTime, float timeIncrement, boolean bonus) {
            this.levelName = levelName;
            this.beginningTime = beginningTime;
            this.timeIncrement = timeIncrement;
            this.bonusLevel = bonus;
        }
    }
}
