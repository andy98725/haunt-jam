package com.haunt.game.elements.hazards;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spike extends Hazard {

    public Spike(Vector2 loc) {
        updateLoc(loc);
        animated = true;
    }

    // TODO test
    private static final Rectangle hitbox = new Rectangle(0, 0, 1, 0.25f);

    @Override
    protected Rectangle shape() {
        return hitbox;
    }

    @Override
    protected String spriteLoc() {
        return "assets/environment/spikes.png";
    }

}
