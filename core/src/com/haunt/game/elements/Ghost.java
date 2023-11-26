package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Util;

public class Ghost extends Element {

    private float timePassed, totTimePassed;

    private final Character follow;

    private int curIndex;

    public Ghost(Character c) {
        this.follow = c;

        reset();
    }

    @Override
    public boolean onCollision(Level l) {
        if (!follow.invincible()) {
            l.playerDies();
            return true;
        }

        return false;
    }

    public boolean isMoving() {
        return curIndex < follow.positions.size() - 1;
    }

    protected void reset() {
        curIndex = 0;
        timePassed = 0;
        this.totTimePassed = Util.random.nextFloat();

        updateLoc(follow.positions.get(0));
    }

    private static final Color transparent = new Color(1, 1, 1, 0.5f);

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(transparent);
        super.render(sb);
        sb.setColor(Color.WHITE);

    }

    @Override
    public boolean update() {
        totTimePassed += Gdx.graphics.getDeltaTime();
        if (!isMoving())
            return false;

        timePassed += Gdx.graphics.getDeltaTime();

        while (curIndex < follow.positions.size() - 1 && follow.times.get(curIndex) < timePassed) {
            timePassed -= follow.times.get(curIndex);
            curIndex++;
        }
        if (!isMoving()) {
            updateLoc(follow.positions.get(curIndex));
            totTimePassed = 0;
            return false;
        }

        Vector2 prevPos = follow.positions.get(curIndex), nextPos = follow.positions.get(curIndex + 1);

        updateLoc(prevPos.lerp(nextPos, timePassed / follow.times.get(curIndex)));
        this.facingLeft = follow.facing.get(curIndex);
        return false;
    }

    public static final Rectangle shape = new Rectangle(-0.4f, 0, 0.8f, 1.6f);
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
        throw new RuntimeException("done by Ghosts class");
    }

    @Override
    protected TextureRegion sprite() {
        return ghostAnim.getKeyFrame(totTimePassed, true);
    }

    protected static Animation<TextureRegion> ghostAnim;

    @Override
    public void create() {
        if (ghostAnim == null) {
            TextureRegion[] ghostSprs = new TextureRegion(new Texture("assets/player/ghost.png")).split(64, 64)[0];
            ghostAnim = new Animation<TextureRegion>(0.1f, ghostSprs);
        }
    }

    @Override
    public void dispose() {
    }
}
