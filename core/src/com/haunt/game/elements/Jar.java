package com.haunt.game.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
        return "assets/player/goal.png";
    }

    public static class FakeJar extends Element {
        public static final Rectangle shape = new Rectangle(0, 0, 1f, 1f);
        public static final Rectangle explodeShape = new Rectangle(-0.5f, -0.5f, 2f, 2f);

        public FakeJar(Vector2 pos) {
            this.updateLoc(pos);
        }

        @Override
        protected Rectangle shape() {
            return shape;
        }

        @Override
        protected Rectangle drawShape() {
            return exploding ? explodeShape : shape;
        }

        @Override
        protected String spriteLoc() {
            return "assets/player/fake.png";
        }

        private boolean exploding;

        public void explode(float time) {
            if (!exploding) {
                dispose();
                exploding = true;
                updateLoc(loc);
            }

            this.spr = getExplosion(time);
        }

        public void unexplode() {
            if (exploding) {
                exploding = false;
                // remake original jar sprite
                create();
                updateLoc(loc);
            }
        }

        @Override
        public void dispose() {
            if (!exploding)
                super.dispose();
        }

        private static Animation<TextureRegion> explosion;

        private static TextureRegion getExplosion(float time) {
            if (explosion == null) {
                TextureRegion[] explosionStrip = new TextureRegion(new Texture("assets/player/fakeExplode.png"))
                        .split(64, 64)[0];
                explosion = new Animation<TextureRegion>(0.1f, explosionStrip);
            }

            return explosion.getKeyFrame(time);

        }
    }

}
