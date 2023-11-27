package com.haunt.game.elements.decor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Drawer extends Decor {

    public Drawer(Vector2 loc) {
        updateLoc(loc);
    }

    private static TextureRegion tex;

    private static TextureRegion tex() {
        if (tex == null)
            tex = new TextureRegion(new Texture("assets/environment/bg/drawer.png"));
        return tex;
    }

    @Override
    protected TextureRegion spr() {
        return tex();
    }

}
