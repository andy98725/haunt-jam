package com.haunt.game.elements.hazards;

import com.badlogic.gdx.math.Rectangle;
import com.haunt.game.Level;
import com.haunt.game.elements.Entity;

public abstract class Hazard extends Entity {

    private static final Rectangle draw = new Rectangle(0, 0, 1, 1);

    public Hazard() {
        this.removeOnRestart = false;
    }

    @Override
    protected Rectangle drawShape() {
        return draw;
    }

    @Override
    public boolean onCollision(Level l) {
        l.playerDies();
        return true;
    }
}
