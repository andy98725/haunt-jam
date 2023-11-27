package com.haunt.game.elements.hazards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Saw extends Hazard {

    public Saw(Vector2 loc) {
        updateLoc(loc);
        this.animated = true;
    }

    // TODO test
    private static final Rectangle hitbox = new Rectangle(0.2f, 0.2f, 0.6f, 0.6f);

    @Override
    protected Rectangle shape() {
        return hitbox;
    }

    private static TextureRegion tex;

    private static TextureRegion tex() {
        if (tex == null)
            tex = new TextureRegion(new Texture("assets/environment/saw.png"));
        return tex;
    }

    @Override
    protected TextureRegion spr() {
        return tex();
    }
}
