package com.haunt.game.elements.hazards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;

public class Ghost extends Hazard {

    private float timePassed;

    private final Character follow;

    private int curIndex;

    public Ghost(Character c) {
        this.follow = c;
        this.animated = true;
        this.removeOnRestart = true;

        reset();
    }

    public boolean isMoving() {
        return curIndex < follow.positions.size() - 1;
    }

    protected void reset() {
        curIndex = 0;
        timePassed = 0;
        this.animationTime = 0;

        updateLoc(follow.positions.get(0));
    }

    private static final Color transparent = new Color(1, 1, 1, 0.5f);

    private static final float fadeTime = 0.4f;

    @Override
    public void render(SpriteBatch sb) {
        if (animationTime < fadeTime)
            sb.setColor(transparent.cpy().mul(animationTime / fadeTime));
        else
            sb.setColor(transparent);
        super.render(sb);
        sb.setColor(Color.WHITE);

    }

    @Override
    public boolean update() {
        if (!isMoving())
            return super.update();

        timePassed += Gdx.graphics.getDeltaTime();

        while (curIndex < follow.positions.size() - 1 && follow.times.get(curIndex) < timePassed) {
            timePassed -= follow.times.get(curIndex);
            curIndex++;
        }
        if (!isMoving()) {
            updateLoc(follow.positions.get(curIndex));
            return super.update();
        }

        Vector2 prevPos = follow.positions.get(curIndex), nextPos = follow.positions.get(curIndex + 1);

        updateLoc(prevPos.lerp(nextPos, timePassed / follow.times.get(curIndex)));
        this.facingLeft = follow.facing.get(curIndex);

        return super.update();
    }

    public static final Rectangle shape = new Rectangle(-0.4f, 0.2f, 0.8f, 1f);
    public static final Rectangle drawShape = new Rectangle(-1, 0, 2, 2);

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected Rectangle drawShape() {
        return drawShape;
    }

    @Override
    protected String spriteLoc() {
        return "assets/player/ghost.png";
    }

}
