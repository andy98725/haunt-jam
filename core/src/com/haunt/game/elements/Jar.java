package com.haunt.game.elements;

import com.badlogic.gdx.math.Rectangle;

public class Jar extends Element {
    public static final Rectangle shape = new Rectangle(0, 0, 1f, 1f);

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected Rectangle drawShape() {
        return shape;
    }

    @Override
    protected String spriteLoc() {
        return "environment/goal.png";
    }

}
