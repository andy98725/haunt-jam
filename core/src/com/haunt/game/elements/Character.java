package com.haunt.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Terrain;

public class Character extends Element {

    public enum State {
        IDLE, WALK, JUMP, DASH, WALLSLIDE
    }

    public static final Rectangle shape = new Rectangle(-0.5f, 0, 1, 2);
    public static final Rectangle drawShape = new Rectangle(-1, 0, 2, 2);

    private State state;
    private Vector2 vel;

    public void init(Vector2 startLoc) {
        this.state = State.IDLE;
        this.vel = Vector2.Zero;
        updateLoc(startLoc);
    }

    private static final float X_VELOCITY = 3, Y_VELOCITY = 5;

    public void update(Level level) {

        float xvel = (Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0);
        float yvel = (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0);

        if (xvel < 0)
            facingLeft = true;
        else if (xvel > 0)
            facingLeft = false;

        xvel *= X_VELOCITY * Gdx.graphics.getDeltaTime();
        yvel *= Y_VELOCITY * Gdx.graphics.getDeltaTime();

        // TODO manipulate velocities in state (add accel)

        // // Apply collision
        if (xvel > 0) {
            float maxX = pos.x + pos.width + xvel;
            Terrain.Tile botHit = level.terrain.tileAt(maxX, pos.getY());
            Terrain.Tile topHit = level.terrain.tileAt(maxX, pos.getY() + pos.getHeight());
            if (botHit == Terrain.Tile.KILL || botHit == Terrain.Tile.KILL)
                level.restart();
            else if (botHit == Terrain.Tile.SOLID || botHit == Terrain.Tile.SOLID) {
                float newMaxX = maxX - (maxX % Terrain.TILE_SIZE);
                xvel += newMaxX - maxX;
            }

        }

        Vector2 newLoc = new Vector2(loc.x + xvel, loc.y + yvel);

        // TODO physics, collision here

        this.updateLoc(newLoc);
    }

    @Override
    protected Rectangle shape() {
        return shape;
    }

    @Override
    protected Rectangle drawShape() {
        return drawShape;
    }

    @Override
    protected String spriteLoc() {
        return "player/idle.png";
    }
}
