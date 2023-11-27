package com.haunt.game.elements.decor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door extends Decor {

    public Door(Vector2 loc) {
        updateLoc(loc);
    }

    private static final Rectangle drawShape = new Rectangle(0, 0, 1, 2);

    @Override
    public Rectangle drawShape() {
        return drawShape;
    }

    private static TextureRegion tex;

    private static TextureRegion tex() {
        if (tex == null)
            tex = new TextureRegion(new Texture("assets/environment/bg/door.png"));
        return tex;
    }

    @Override
    protected TextureRegion spr() {
        return tex();
    }

}
