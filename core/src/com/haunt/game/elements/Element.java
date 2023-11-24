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

    protected abstract Rectangle drawShape();

    protected abstract String spriteLoc();

    public Vector2 loc;
    public Rectangle pos, drawPos;

    public void updateLoc(Vector2 loc) {
        this.loc = loc;
        this.pos = new Rectangle(shape());
        pos.setX(pos.getX() + loc.x);
        pos.setY(pos.getY() + loc.y);
        this.drawPos = new Rectangle(drawShape());
        drawPos.setX(drawPos.getX() + loc.x);
        drawPos.setY(drawPos.getY() + loc.y);
    }

    public void render(SpriteBatch sb) {
        if (facingLeft)
            sb.draw(sprite(), (drawPos.x + drawPos.width) * Terrain.TILE_SIZE, drawPos.y * Terrain.TILE_SIZE,
                    -drawPos.width * Terrain.TILE_SIZE, drawPos.height * Terrain.TILE_SIZE);
        else
            sb.draw(sprite(), drawPos.x * Terrain.TILE_SIZE, drawPos.y * Terrain.TILE_SIZE,
                    drawPos.width * Terrain.TILE_SIZE, drawPos.height * Terrain.TILE_SIZE);
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
