package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Level;
import com.haunt.game.Tile;
import com.haunt.game.ui.Timer;

public class Character extends Entity {
    public static final Rectangle shape = new Rectangle(-0.4f, 0, 0.8f, 1.8f);
    public static final Rectangle drawShape = new Rectangle(-1, 0, 2, 2);

    private final Level level;
    private final Timer t;
    private State state = State.IDLE;
    private Vector2 vel = new Vector2(), accel = new Vector2(), velocityCap = new Vector2();

    public Character(Level level, Timer t) {
        this.level = level;
        this.t = t;
        this.animated = true;
    }

    public enum State {
        IDLE, WALK, JUMP, WALLSLIDE,
        WIN, LOSE
    }

    public void init(Vector2 startLoc) {
        this.state = State.IDLE;
        this.vel = new Vector2();
        this.accel = new Vector2();
        updateLoc(startLoc);

        positions = new ArrayList<Vector2>();
        times = new ArrayList<Float>();
        facing = new ArrayList<Boolean>();
    }

    private static final float COYOTE_TIME = 0.12f, WJ_COYOTE_TIME = 0.2f;
    private static final float WALLJUMP_SPEED = 9f, WALLJUMP_GRAV = 10f;
    private float coyoteTime, walljumpCoyoteTime;
    private boolean walljumpCoyoteDirection;

    public boolean update() {
        super.update();
        if (walljumpCoyoteTime > 0)
            walljumpCoyoteTime -= Gdx.graphics.getDeltaTime();
        if (coyoteTime > 0)
            coyoteTime -= Gdx.graphics.getDeltaTime();

        int xMov = (Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0) + (Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0);
        int yMov = (Gdx.input.isKeyPressed(Input.Keys.W) ? 1 : 0);

        if (state != State.WALLSLIDE) {
            if (xMov < 0)
                facingLeft = true;
            else if (xMov > 0)
                facingLeft = false;
        }

        updateState(state, xMov, yMov);

        // Done this way because of math
        if (velocityCap.x >= 0)
            vel.x = MathUtils.clamp(vel.x, -velocityCap.x, velocityCap.x);
        if (velocityCap.y >= 0)
            vel.y = MathUtils.clamp(vel.y, -velocityCap.y, velocityCap.y);
        applyVelocity(vel.x / 2, vel.y / 2);

        vel.x += accel.x * Gdx.graphics.getDeltaTime();
        vel.y += accel.y * Gdx.graphics.getDeltaTime();
        if (velocityCap.x >= 0)
            vel.x = MathUtils.clamp(vel.x, -velocityCap.x, velocityCap.x);
        if (velocityCap.y >= 0)
            vel.y = MathUtils.clamp(vel.y, -velocityCap.y, velocityCap.y);
        applyVelocity(vel.x / 2, vel.y / 2);

        // save for ghost
        if (t.timerStarted && !level.isPaused) {
            positions.add(this.loc);
            times.add(Gdx.graphics.getDeltaTime());
            facing.add(facingLeft);
        }

        // Delay state change until end of animations
        if (state == State.LOSE && animationTime > 0.2f + loseAnim.getAnimationDuration())
            level.restart();
        if (state == State.WIN && animationTime > 0.2f + winAnim.getAnimationDuration())
            level.nextLevel();

        return false;
    }

    private void updateState(State st, int xMov, int yMov) {
        this.state = st;
        switch (st) {
            case IDLE:
                velocityCap = new Vector2(0, 0);
                if (xMov != 0 || yMov != 0) {
                    this.animationTime = 0;
                    updateState(State.WALK, xMov, yMov);
                }

                // Check falling
                if (tileHitY(pos.y - err) == Tile.EMPTY) {
                    coyoteTime = COYOTE_TIME;
                    this.animationTime = 1;
                    updateState(State.JUMP, xMov, yMov);
                    break;
                }

                break;
            case WALK:
                velocityCap = new Vector2(8, 0);

                // Jump
                if (yMov > 0) {
                    vel.y = 12;// duplicated in coyote jump
                    this.animationTime = 0;
                    updateState(State.JUMP, xMov, 0);
                    break;
                }

                // Check falling
                if (tileHitY(pos.y - err) == Tile.EMPTY) {
                    coyoteTime = COYOTE_TIME;
                    this.animationTime = 1;
                    updateState(State.JUMP, xMov, yMov);
                    break;
                }

                // Instant stop on other press
                if (xMov * vel.x < 0) {
                    // vel.x = 0;
                    accel.x = 50f * xMov;
                } else if (xMov == 0) {
                    if (Math.abs(vel.x) < 0.5)
                        vel.x = 0;
                    accel = new Vector2(-vel.x * 12, 0);
                } else {
                    accel = new Vector2(12f * xMov, 0);
                }
                if (xMov == 0 && yMov == 0 && Math.abs(vel.x) <= err && accel.x == 0)
                    updateState(State.IDLE, 0, 0);

                break;

            case JUMP:
                velocityCap = new Vector2(8, -1);
                boolean fullhop = yMov > 0 && vel.y > 0;

                if (xMov * vel.x < 0) {
                    accel.x = 25f * xMov;
                } else {
                    accel.x = 8 * xMov;
                }
                accel.y = fullhop ? -16 : -24;

                if (coyoteTime > 0 && yMov > 0) {
                    this.coyoteTime = 0;
                    this.animationTime = 0;
                    vel.y = 10;
                    break;
                }
                if (walljumpCoyoteTime > 0 && Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    this.walljumpCoyoteTime = 0;
                    this.animationTime = 0;

                    vel.y = Math.max(WALLJUMP_SPEED, vel.y);
                    vel.x = 8 * (walljumpCoyoteDirection ? -1 : 1);
                    break;
                }

                if (vel.y <= 0 && tileHitY(pos.y - err) != Tile.EMPTY) {
                    updateState(State.WALK, xMov, yMov);
                    break;
                }
                if (((vel.x < 0 && xMov == 0) || xMov < 0) && tileHitX(pos.x - err) == Tile.SOLID) {
                    vel.y *= 0.8;
                    updateState(State.WALLSLIDE, 0, yMov);
                    break;
                }
                if (((vel.x > 0 && xMov == 0) || xMov > 0) && tileHitX(pos.x + pos.width + err) == Tile.SOLID) {
                    vel.y *= 0.8;
                    updateState(State.WALLSLIDE, xMov, yMov);
                    break;
                }

                break;
            case WALLSLIDE:
                velocityCap = new Vector2(8, -1);
                accel.y = -WALLJUMP_GRAV;

                // Hit floor
                if (vel.y <= 0 && tileHitY(pos.y - err) != Tile.EMPTY) {
                    updateState(State.WALK, xMov, yMov);
                    break;
                }

                // Direction facing
                if (tileHitX(pos.x - err) == Tile.SOLID)
                    facingLeft = false;
                else if (tileHitX(pos.x + pos.width + err) == Tile.SOLID)
                    facingLeft = true;
                else {
                    walljumpCoyoteTime = WJ_COYOTE_TIME;
                    walljumpCoyoteDirection = facingLeft;
                    this.animationTime = 1;
                    updateState(State.JUMP, xMov, yMov);
                    break;
                }

                // Fall off
                if ((xMov < 0 && facingLeft) || (xMov > 0 && !facingLeft)) {
                    this.animationTime = 1;
                    walljumpCoyoteTime = WJ_COYOTE_TIME;
                    walljumpCoyoteDirection = facingLeft;
                    updateState(State.JUMP, xMov, yMov);
                }

                // Walljump
                if (yMov > 0 && Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    vel.y = Math.max(WALLJUMP_SPEED, vel.y);
                    vel.x = 8 * (facingLeft ? -1 : 1);
                    this.animationTime = 0;
                    updateState(State.JUMP, 0, yMov);
                    break;
                }
                break;
            case LOSE:
            case WIN:
                velocityCap = new Vector2(-1, -1);
                this.accel = new Vector2(-vel.x * 32, -32);
                if (Math.abs(vel.x) < 2)
                    vel.x = 0;

                if (Math.abs(vel.x) > err || Math.abs(vel.y) > err)
                    this.animationTime = 0;
                break;
            default:
                break;
        }
    }

    private void applyVelocity(float xvel, float yvel) {
        xvel *= Gdx.graphics.getDeltaTime();
        yvel *= Gdx.graphics.getDeltaTime();

        // Horizontal collision first
        if (xvel > 0) { // right
            float checkX = pos.x + pos.width + xvel;
            Tile hit = tileHitX(checkX);

            if (hit == Tile.SOLID) {
                float newMaxX = checkX - (checkX % 1);
                xvel = Math.max(err, xvel + newMaxX - checkX);
                this.vel.x = xvel;
            }
        } else if (xvel < 0) { // left
            float checkX = pos.x + xvel;
            Tile hit = tileHitX(checkX);

            if (hit == Tile.SOLID) {
                float newMinX = checkX + 1 - (checkX % 1);
                xvel = Math.min(-err, xvel + newMinX - checkX);
                this.vel.x = xvel;
            }
        }
        if (xvel != err && xvel != -err)
            this.updateLoc(new Vector2(loc.x + xvel, loc.y));

        // Vertical collision next
        if (yvel > 0) { // up
            float checkY = pos.y + pos.height + yvel;
            Tile hit = tileHitY(checkY);

            if (hit == Tile.SOLID) {
                float newMaxY = checkY - (checkY % 1);
                yvel = Math.max(err, yvel + newMaxY - checkY);
                this.vel.y = yvel;
            }
        } else if (yvel < 0) { // down
            float checkY = pos.y + yvel;
            Tile hit = tileHitY(checkY);

            if (hit == Tile.SOLID) {
                float newMinY = checkY + 1 - (checkY % 1);
                yvel = Math.min(-err, yvel + newMinY - checkY);
                this.vel.y = yvel; // Not done for collision checking reasons
            }
        }
        if (yvel != err && yvel != -err)
            this.updateLoc(new Vector2(loc.x, loc.y + yvel));
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

        if (hit == Tile.FALLTHROUGH) {
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                return Tile.EMPTY;
            if (newY > pos.y)
                return Tile.EMPTY;

            // Only block fallthrough on top
            if ((int) (pos.y + err) == (int) newY)
                return Tile.EMPTY;

            return Tile.SOLID;
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

    public void reachGoal(boolean win) {
        if (win) {
            state = State.WIN;
            this.animationTime = 0;
        }
    }

    public void die() {
        state = State.LOSE;
        this.animationTime = 0;
    }

    @Override
    protected TextureRegion sprite() {
        switch (state) {
            case IDLE:
                return idleAnim.getKeyFrame(this.animationTime, true);
            case WALK:
                return walkingAnim.getKeyFrame(this.animationTime, true);
            case JUMP:
                if (vel.y > -6)
                    return jumpingAnim.getKeyFrame(this.animationTime, false);
                else
                    return fallingAnim.getKeyFrame(this.animationTime, true);
            case WALLSLIDE:
                return wallslideAnim.getKeyFrame(this.animationTime, true);
            case WIN:
                return winAnim.getKeyFrame(this.animationTime, false);
            case LOSE:
                return loseAnim.getKeyFrame(this.animationTime, false);
            default:
                throw new RuntimeException("Unknown state " + state);

        }

    }

    private Animation<TextureRegion> idleAnim, walkingAnim, jumpingAnim, fallingAnim, wallslideAnim, winAnim, loseAnim;

    @Override
    public void create() {
        TextureRegion[][] spritesheet = new TextureRegion(new Texture("assets/player/anims.png")).split(64, 64);

        idleAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[1], 12));
        walkingAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[0], 6));
        jumpingAnim = new Animation<TextureRegion>(frameTime / 2, subregion(spritesheet[3], 3));
        fallingAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[4], 1));
        wallslideAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[2], 1));
        winAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[5], 25));
        loseAnim = new Animation<TextureRegion>(frameTime, subregion(spritesheet[6], 16));
    }

    private TextureRegion[] subregion(TextureRegion[] strip, int size) {
        TextureRegion[] ret = new TextureRegion[size];
        for (int i = 0; i < ret.length; i++)
            ret[i] = strip[i];
        return ret;
    }

    @Override
    public void dispose() {
        idleAnim.getKeyFrame(0).getTexture().dispose();
        walkingAnim.getKeyFrame(0).getTexture().dispose();
        jumpingAnim.getKeyFrame(0).getTexture().dispose();
        fallingAnim.getKeyFrame(0).getTexture().dispose();
        wallslideAnim.getKeyFrame(0).getTexture().dispose();
        winAnim.getKeyFrame(0).getTexture().dispose();
        loseAnim.getKeyFrame(0).getTexture().dispose();
    }

    @Override
    protected String spriteLoc() {
        throw new RuntimeException("Unused");
    }
}
