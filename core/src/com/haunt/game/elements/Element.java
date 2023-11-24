package com.haunt.game.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Terrain;

public abstract class Element {
    protected boolean facingLeft;

    protected abstract Rectangle shape();

    protected abstract String spriteLoc();

    public Vector2 loc;
    private Rectangle pos;

    public void setLoc(Vector2 loc) {
        this.loc = loc;
        this.pos = new Rectangle(shape());
        pos.x += loc.x;
        pos.y += loc.y;
    }

    public Rectangle position() {
        return pos;
    }

    public void render(SpriteBatch sb) {
        if (facingLeft) // TODO
            sb.draw(sprite(), pos.x * Terrain.TILE_SIZE, pos.y * Terrain.TILE_SIZE, pos.width * Terrain.TILE_SIZE,
                    pos.height * Terrain.TILE_SIZE);
        else
            sb.draw(sprite(), pos.x * Terrain.TILE_SIZE, pos.y * Terrain.TILE_SIZE, pos.width * Terrain.TILE_SIZE,
                    pos.height * Terrain.TILE_SIZE);
    }

    private TextureRegion spr;

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
