package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;

public class Character extends Element {

    public enum State {
        IDLE, WALK, JUMP, DASH, WALLSLIDE
    }

    public static final Rectangle shape = new Rectangle(-0.5f, 0, 1, 1);

    private State state;
    private Vector2 vel;

    public void init(Vector2 startLoc) {
        this.state = State.IDLE;
        this.vel = Vector2.Zero;
        setLoc(startLoc);
    }

    public void update(Level level) {

        // Update position
        Vector2 newpos = new Vector2(loc.x + vel.x * Gdx.graphics.getDeltaTime(),
                loc.y + vel.y * Gdx.graphics.getDeltaTime());

        // TODO

    }

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected String spriteLoc() {
        return "player/idle.png";
    }
}
