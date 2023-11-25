package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

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
        float sprY = Gdx.graphics.getHeight() - 36 - timerSpr.getRegionHeight() / 2;
        sb.draw(timerSpr, sprX, sprY);

        txt.setText(font, getText());
        float txtX = Gdx.graphics.getWidth() / 2 - txt.width / 2;
        float txtY = Gdx.graphics.getHeight() - 36 - txt.height / 2 + 8;
        font.setColor(getColor());
        font.draw(sb, getText(), txtX, txtY);

    }

    TextureRegion timerSpr;
    BitmapFont font;
    GlyphLayout txt;

    public void create() {
        timerSpr = new TextureRegion(new Texture("assets/ui/timer.png"));
        font = new BitmapFont();
        txt = new GlyphLayout(font, getText());
    }

    public void dispose() {
        timerSpr.getTexture().dispose();
        font.dispose();
    }

    private Color getColor() {
        float redTime = timeSet / 3;
        if (timeLeft < redTime)
            return Color.RED;
        // There's a math error here, but I like the result
        return Color.RED.cpy().lerp(Color.GREEN, MathUtils.clamp((timeLeft - redTime) / redTime, 0, 1));
    }

    private String getText() {
        if (timeLeft >= 5)
            return Integer.toString((int) timeLeft);
        return Float.toString(timeLeft).substring(0, 3);
    }
}
