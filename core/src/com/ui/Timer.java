package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Timer {

    private final float timeSet;
    private final float timeIncrement;
    private float timeLeft;

    public Timer(float initialTime, float increment) {
        this.timeSet = initialTime;
        this.timeIncrement = increment;
        resetTimer();
    }

    public void resetTimer() {
        timeLeft = timeSet;
    }

    public void incrementTimer() {
        timeLeft += timeIncrement;
    }

    // Returns if the player ran out of time
    public boolean update() {
        timeLeft -= Gdx.graphics.getDeltaTime();

        return timeLeft <= 0;
    }

    public void render(SpriteBatch sb) {
        float sprX = Gdx.graphics.getWidth() / 2 - timerSpr.getRegionWidth() / 2;
        float sprY = Gdx.graphics.getHeight() - timerSpr.getRegionHeight() - 24;

        sb.draw(timerSpr, sprX, sprY);

        font.setColor(getColor());
        font.draw(sb, getText(), sprX,
                Gdx.graphics.getHeight() - timerSpr.getRegionHeight() / 2 - 24);

    }

    TextureRegion timerSpr;
    BitmapFont font;

    public void create() {
        timerSpr = new TextureRegion(new Texture("assets/ui/timer.png"));
        font = new BitmapFont();
    }

    public void dispose() {
        timerSpr.getTexture().dispose();
        font.dispose();
    }

    private Color getColor() {
        if (timeLeft < 5)
            return Color.RED;
        return Color.RED.cpy().lerp(Color.GREEN, (timeLeft - 5) / (timeSet - 5));
    }

    private String getText() {
        if (timeLeft >= 5)
            return Integer.toString((int) timeLeft);
        return Float.toString(timeLeft).substring(0, 3);
    }
}
