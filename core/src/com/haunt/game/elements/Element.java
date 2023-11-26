package com.haunt.game.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.HauntGame;
import com.haunt.game.Level;

public abstract class Element {
    protected boolean facingLeft;

    protected abstract Rectangle shape();

    protected abstract Rectangle drawShape();

    protected abstract String spriteLoc();

    // Returns should be removed
    public boolean update() {
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

    protected TextureRegion sprite() {
        return spr;
    }

    public void create() {
        spr = new TextureRegion(new Texture(spriteLoc()));
    }

    public void dispose() {
        spr.getTexture().dispose();
    }
}
