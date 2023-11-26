package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Tile;
import com.haunt.game.Util;

public class Jar extends Element {
    public static final Rectangle shape = new Rectangle(0, 0, 1f, 1f);
    public static final Rectangle explodeShape = new Rectangle(-0.5f, -0.5f, 2f, 2f);

    private final boolean isReal;
    private boolean exploding;
    private float explodingTimer;

    // Random location
    public Jar(Level level) {
        isReal = true;

        // Random map location
        do {
            updateLoc(
                    new Vector2(Util.random.nextInt(level.terrain.mapWid()),
                            Util.random.nextInt(level.terrain.mapHei())));
        } while (level.terrain.tileAt(loc.x, loc.y) != Tile.EMPTY);
    }

    public Jar(Vector2[] endLocs, int i) {
        isReal = i == endLocs.length - 1;
        updateLoc(endLocs[i]);
    }

    @Override
    public boolean onCollision(Level l) {
        if (exploding)
            return false;

        if (isReal)
            l.entities.remove(this);
        else
            exploding = true;

        update();
        updateLoc(loc);
        l.reachGoal(this);
        return true;
    }

    @Override
    public boolean update() {
        if (!exploding)
            return false;

        explodingTimer += Gdx.graphics.getDeltaTime();
        this.spr = explodingSpr().getKeyFrame(explodingTimer);
        return explodingTimer > explodingSpr.getAnimationDuration();

    }

    private static Animation<TextureRegion> explodingSpr;

    private static Animation<TextureRegion> explodingSpr() {
        if (explodingSpr == null) {
            TextureRegion[] explosionStrip = new TextureRegion(new Texture("assets/player/fakeExplode.png"))
                    .split(64, 64)[0];
            explodingSpr = new Animation<TextureRegion>(0.1f, explosionStrip);
        }
        return explodingSpr;
    }

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected Rectangle drawShape() {
        return exploding ? explodeShape : shape;
    }

    @Override
    protected String spriteLoc() {
        return isReal ? "assets/player/goal.png" : "assets/player/fake.png";
    }
}
