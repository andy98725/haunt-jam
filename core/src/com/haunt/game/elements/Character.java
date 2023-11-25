package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Tile;

public class Character extends Element {
    public static final Rectangle shape = new Rectangle(-0.5f, 0, 1, 2);
    public static final Rectangle drawShape = new Rectangle(-1, 0, 2, 2);

    private final Level level;
    private State state;
    private Vector2 vel;

    public Character(Level level) {
        this.level = level;
    }

    public enum State {
        IDLE, WALK, JUMP, DASH, WALLSLIDE
    }

    public void init(Vector2 startLoc) {
        this.state = State.IDLE;
        this.vel = Vector2.Zero;
        updateLoc(startLoc);

        positions = new ArrayList<Vector2>();
        times = new ArrayList<Float>();
        facing = new ArrayList<Boolean>();
    }

    public void reachGoal() {
        this.iframes = 1;
    }

    public boolean invincible() {
        return iframes > 0;
    }

    private float iframes = 0;

    private static final float X_VELOCITY = 3, Y_VELOCITY = 5;

    public void update() {
        if (iframes > 0)
            iframes -= Gdx.graphics.getDeltaTime();

        float xvel = (Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0);
        float yvel = (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0);

        if (xvel < 0)
            facingLeft = true;
        else if (xvel > 0)
            facingLeft = false;
        // TODO manipulate velocities in state (add accel)

        xvel *= X_VELOCITY * Gdx.graphics.getDeltaTime();
        yvel *= Y_VELOCITY * Gdx.graphics.getDeltaTime();

        // Horizontal collision first

        if (xvel > 0) { // right
            float checkX = pos.x + pos.width + xvel;
            Tile hit = tileHitX(checkX);

            if (hit == Tile.KILL)
                level.playerDies();
            else if (hit == Tile.SOLID || hit == Tile.SOLID) {
                float newMaxX = checkX - (checkX % 1);
                xvel = Math.max(0, xvel + newMaxX - checkX);
            }
        } else if (xvel < 0) { // left
            float checkX = pos.x + xvel;
            Tile hit = tileHitX(checkX);

            if (hit == Tile.KILL)
                level.playerDies();
            else if (hit == Tile.SOLID || hit == Tile.SOLID) {
                float newMinX = checkX + 1 - (checkX % 1);
                xvel = Math.min(0, xvel + newMinX - checkX);
            }
        }
        this.updateLoc(new Vector2(loc.x + xvel, loc.y));

        // Vertical collision next

        if (yvel > 0) { // up
            float checkY = pos.y + pos.height + yvel;
            Tile hit = tileHitY(checkY);

            if (hit == Tile.KILL)
                level.playerDies();
            else if (hit == Tile.SOLID || hit == Tile.SOLID) {
                float newMaxY = checkY - (checkY % 1);
                yvel = Math.max(0, yvel + newMaxY - checkY);
            }
        } else if (yvel < 0) { // down
            float checkY = pos.y + yvel;
            Tile hit = tileHitY(checkY);

            if (hit == Tile.KILL)
                level.playerDies();
            else if (hit == Tile.SOLID || hit == Tile.SOLID) {
                float newMinY = checkY + 1 - (checkY % 1);
                yvel = Math.min(0, yvel + newMinY - checkY);
            }
        }
        this.updateLoc(new Vector2(loc.x, loc.y + yvel));

        // save for ghost
        positions.add(this.loc);
        times.add(Gdx.graphics.getDeltaTime());
        facing.add(facingLeft);
    }

    private static final float err = 0.01f;

    private Tile tileHitX(float newX) {
        Tile hit = Tile.EMPTY;
        for (float y = pos.getY() + err; y <= pos.getY() + pos.getHeight() - err; y += 0.5f - err) {
            Tile hitPos = level.terrain.tileAt(newX, y);
            if (hitPos != null && hitPos.priority > hit.priority)
                hit = hitPos;
        }
        return hit;
    }

    private Tile tileHitY(float newY) {
        Tile hit = Tile.EMPTY;
        for (float x = pos.getX() + err; x <= pos.getX() + pos.getWidth() - err; x += 0.5f - err) {
            Tile hitPos = level.terrain.tileAt(x, newY);
            if (hitPos != null && hitPos.priority > hit.priority)
                hit = hitPos;
        }
        return hit;
    }

    public List<Vector2> positions = new ArrayList<Vector2>();
    public List<Float> times = new ArrayList<Float>();
    public List<Boolean> facing = new ArrayList<Boolean>();

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
        return "assets/player/idle.png";
    }

    private static Color invincibleCol = new Color(1, 1, 1, 0.75f);

    @Override
    public void render(SpriteBatch sb) {
        if (invincible())
            sb.setColor(invincibleCol);
        super.render(sb);
        if (invincible())
            sb.setColor(Color.WHITE);
    }

    public Ghosts.Ghost makeGhost() {
        Ghosts.Ghost g = new Ghosts.Ghost(positions, times, facing);

        positions = new ArrayList<Vector2>();
        times = new ArrayList<Float>();
        facing = new ArrayList<Boolean>();

        return g;
    }
}
