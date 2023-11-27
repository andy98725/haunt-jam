package com.haunt.game.elements.hazards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spike extends Hazard {

    public Spike(Vector2 loc) {
        updateLoc(loc);
        animated = true;
    }

    private static final Rectangle hitbox = new Rectangle(0.1f, 0, 0.8f, 0.25f);

    @Override
    protected Rectangle shape() {
        return hitbox;
    }

    private static TextureRegion tex;

    private static TextureRegion tex() {
        if (tex == null)
            tex = new TextureRegion(new Texture("assets/environment/spikes.png"));
        return tex;
    }

    @Override
    protected TextureRegion spr() {
        return tex();
    }

}
