package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public abstract class Entity {
    protected boolean facingLeft;

    protected boolean animated;
    protected float animationTime;
    protected boolean animationLoop = true;

    public boolean removeOnRestart = true;

    protected abstract Rectangle shape();

    protected abstract Rectangle drawShape();

    // Returns if should be removed
    public boolean update() {
        animationTime += Gdx.graphics.getDeltaTime();

        return false;
    }

    public boolean collided(Rectangle pos) {
        return this.pos.overlaps(pos);
    }

    // Returns used; 1 collision allowed per frame
    public boolean onCollision(Level l) {
        return false;
    }

    public Vector2 loc;
    public Rectangle pos, drawPos;

    public void updateLoc(Vector2 loc) {
        this.loc = loc;
        this.pos = new Rectangle(shape());
        pos.x += loc.x;
        pos.y += loc.y;
        this.drawPos = new Rectangle(drawShape());
        drawPos.x += loc.x;
        drawPos.y += loc.y;
    }

    public void render(SpriteBatch sb) {

        if (facingLeft)
            sb.draw(sprite(), (drawPos.x + drawPos.width), drawPos.y,
                    -drawPos.width, drawPos.height);
        else
            sb.draw(sprite(), drawPos.x, drawPos.y,
                    drawPos.width, drawPos.height);

        if (HauntGame.DEBUG)
            HauntGame.debugRenderer.rect(pos.x, pos.y, pos.width, pos.height);
    }

    protected TextureRegion spr;
    protected Animation<TextureRegion> animSpr;

    protected TextureRegion sprite() {
        if (animated)
            return animSpr.getKeyFrame(animationTime, animationLoop);

        return spr;
    }

    protected abstract TextureRegion spr();

    protected float frameTime = 0.1f;

    public void create() {
        if (!animated)
            spr = spr();
        else {
            TextureRegion base = spr();
            TextureRegion[] sprSheet = base.split(base.getRegionHeight(), base.getRegionHeight())[0];
            animSpr = new Animation<TextureRegion>(0.1f, sprSheet);

        }
    }

    public void dispose() {
    }

    protected void updateSprite() {
        dispose();
        create();
    }
}
