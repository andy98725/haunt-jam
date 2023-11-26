package com.haunt.game.elements.hazards;

import com.badlogic.gdx.math.Rectangle;
import com.haunt.game.Level;
import com.haunt.game.elements.Entity;

public abstract class Hazard extends Entity {

    private static final Rectangle draw = new Rectangle(0, 0, 1, 1);

    public Hazard() {
        this.removeOnDie = false;
    }

    @Override
    protected Rectangle drawShape() {
        return draw;
    }

    @Override
    public boolean onCollision(Level l) {
        if (!l.character.invincible()) {
            l.playerDies();
            return true;
        }

        return false;
    }
}
