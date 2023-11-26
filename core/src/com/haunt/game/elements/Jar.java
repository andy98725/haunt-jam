package com.haunt.game.elements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Tile;
import com.haunt.game.Util;

public class Jar extends Entity {
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

        l.reachGoal(this);

        if (isReal)
            l.entities.remove(this);
        else
            exploding = true;

        this.animated = true;
        updateSprite();
        updateLoc(loc);
        return true;
    }

    @Override
    public boolean update() {
        if (!exploding)
            return super.update();

        if (this.animationTime > this.animSpr.getAnimationDuration())
            return true;
        return super.update();
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
        return isReal ? "assets/player/goal.png"
                : exploding ? "assets/player/fakeExplode.png" : "assets/player/fake.png";
    }
}
