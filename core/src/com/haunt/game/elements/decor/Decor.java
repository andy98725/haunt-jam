package com.haunt.game.elements.decor;

import com.badlogic.gdx.math.Rectangle;
import com.haunt.game.elements.Entity;

public abstract class Decor extends Entity {

    public Decor() {
        this.removeOnRestart = false;
    }

    private static final Rectangle shape = new Rectangle(0, 0, 0, 0), drawShape = new Rectangle(0, 0, 1, 1);

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected Rectangle drawShape() {
        return drawShape;
    }

}
