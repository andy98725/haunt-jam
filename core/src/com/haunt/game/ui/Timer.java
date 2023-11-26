package com.haunt.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Timer {

    private final float timeSet;
    private final float timeIncrement;
    private float timeLeft, rawTime;
    public boolean timerStarted;

    public Timer(float initialTime, float increment) {
        this.timeSet = initialTime + 0.5f;
        this.timeIncrement = increment;
        resetTimer();
    }

    public void printTime(int levelNum) {
        Gdx.app.log("Level " + (levelNum + 1), Float.toString(rawTime) + " seconds");
    }

    public void resetTimer() {
        this.rawTime = 0;
        timeLeft = timeSet;
        lastTick = (int) timeLeft;
        timerStarted = false;
    }

    public void incrementTimer() {
        timeLeft += timeIncrement;
    }

    // Returns if the player ran out of time
    public boolean update() {
        if (!timerStarted) {
            int xMov = (Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0);
            int yMov = (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0);
            if (xMov != 0 || yMov != 0)
                timerStarted = true;
            else
                return false;
        }

        rawTime += Gdx.graphics.getDeltaTime();
        timeLeft -= Gdx.graphics.getDeltaTime();
        if (lastTick != (int) timeLeft) {
            float minVolume = timeSet * 3 / 4, maxVolume = timeSet * 1 / 4;
            lastTick = (int) timeLeft;
            playTickNoise(1 - MathUtils.clamp((lastTick - maxVolume) / (minVolume - maxVolume), 0, 1));
        }
        if (timeLeft <= 0)
            tickFinal.play();

        return timeLeft <= 0;
    }

    private int lastTick;

    private void playTickNoise(float volume) {
        if (volume <= 0)
            return;

        if (volume >= 1)
            tickLoud.play();
        else
            tick.play(volume);
    }

    private static final Rectangle size = new Rectangle(0, 0, 64, 64);

    public void render(SpriteBatch sb) {
        float cx = Gdx.graphics.getWidth() / 2, cy = Gdx.graphics.getHeight() - 32;

        sb.draw(timerSpr, cx - size.width / 2, cy - size.height / 2, size.width, size.height);

        txt.setText(font, getText());
        font.setColor(getColor());
        font.draw(sb, getText(), cx - txt.width / 2, cy - txt.height / 2 + 12);

    }

    private TextureRegion timerSpr;
    private BitmapFont font;
    private GlyphLayout txt;
    private Sound tick, tickLoud, tickFinal;

    public void create() {
        timerSpr = new TextureRegion(new Texture("assets/ui/timer.png"));
        font = new BitmapFont(Gdx.files.local("assets/ui/timerFont.fnt"),
                new TextureRegion(new Texture("assets/ui/timerFont.png")));
        txt = new GlyphLayout(font, getText());
        tick = Gdx.audio.newSound(Gdx.files.internal("assets/sound/tick.mp3"));
        tickLoud = Gdx.audio.newSound(Gdx.files.internal("assets/sound/tickLoud.mp3"));
        tickFinal = Gdx.audio.newSound(Gdx.files.internal("assets/sound/tickFinal.mp3"));
    }

    public void dispose() {
        timerSpr.getTexture().dispose();
        font.dispose();
        tick.dispose();
        tickLoud.dispose();
        tickFinal.dispose();
    }

    private Color getColor() {
        float redTime = timeSet / 3;
        float time = timeLeft > redTime ? (int) timeLeft : timeLeft;
        if (time < redTime)
            return Color.RED;
        // There's a math error here, but I like the result
        return Color.RED.cpy().lerp(Color.GREEN, MathUtils.clamp((time - redTime) / redTime, 0, 1));
    }

    private String getText() {
        if (timeLeft >= 5)
            return Integer.toString((int) timeLeft);
        if (timeLeft <= 0)
            return "0";
        return Float.toString(timeLeft).substring(0, 3);
    }
}
