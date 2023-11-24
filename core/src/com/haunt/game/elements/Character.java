package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private static final float X_VELOCITY = 3, Y_VELOCITY = 5;

    public void update(Level level) {

        float xvel = (Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0);
        float yvel = (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0);

        if (xvel < 0)
            facingLeft = true;
        else if (xvel > 0)
            facingLeft = false;

        xvel *= X_VELOCITY * Gdx.graphics.getDeltaTime();
        yvel *= Y_VELOCITY * Gdx.graphics.getDeltaTime();

        Vector2 newLoc = new Vector2(loc.x + xvel, loc.y + yvel);
        // TODO physics, collision here

        this.setLoc(newLoc);
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
