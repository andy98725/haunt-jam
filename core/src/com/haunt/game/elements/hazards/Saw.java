package com.haunt.game.elements.hazards;

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

    @Override
    protected String spriteLoc() {
        return "assets/environment/saw.png";
    }

}
