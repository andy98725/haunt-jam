package com.haunt.game.elements;

import com.badlogic.gdx.math.Rectangle;

public class Goal extends Element {
    public static final Rectangle shape = new Rectangle(-0.55f, -0.5f, 1f, 1f);

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected String spriteLoc() {
        return "environment/goal.png";
    }

}
