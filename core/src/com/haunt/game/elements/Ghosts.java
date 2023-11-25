package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.Util;

public class Ghosts {

    public final List<Ghost> ghosts = new ArrayList<Ghost>();

    public void addGhost(Ghost g) {
        g.create();
        ghosts.add(g);
    }

    public void update() {
        for (Ghost g : ghosts)
            g.update();
    }

    private static final Color transparent = new Color(1, 1, 1, 0.5f);

    public void render(SpriteBatch sb) {
        sb.setColor(transparent);
        for (Ghost g : ghosts)
            g.render(sb);
        sb.setColor(Color.WHITE);
    }

    public void resetTimer() {
        for (Ghost g : ghosts)
            g.reset();
    }

    public void create() {
        for (Ghost g : ghosts)
            g.create();

        if (ghostAnim == null) {
            TextureRegion[] ghostSprs = new TextureRegion(new Texture("assets/player/ghost.png")).split(64, 64)[0];
            ghostAnim = new Animation<TextureRegion>(0.1f, ghostSprs);
        }
    }

    public void dispose() {
        for (Ghost g : ghosts)
            g.dispose();
    }

    public void reset() {
        for (Ghost g : ghosts)
            g.dispose();
        ghosts.clear();
    }

    public boolean ghostCollided(Rectangle bounds) {
        for (Ghost g : ghosts)
            if (g.isMoving() && g.pos.overlaps(bounds))
                return true;

        return false;
    }

    protected static Animation<TextureRegion> ghostAnim;

    public static class Ghost extends Element {

        private float timePassed, totTimePassed;

        private final Jar.FakeJar jar;
        private final List<Vector2> posData;
        private final List<Float> timeData;
        private final List<Boolean> facingData;

        private List<Vector2> curPos;
        private List<Float> curTime;
        private List<Boolean> curFacing;

        public Ghost(List<Vector2> pos, List<Float> time, List<Boolean> facing, Jar.FakeJar jar) {
            this.jar = jar;
            this.posData = pos;
            this.timeData = time;
            this.facingData = facing;

            reset();
        }

        public boolean isMoving() {
            return curPos.size() > 1;
        }

        protected void reset() {
            curPos = new ArrayList<Vector2>(posData);
            curTime = new ArrayList<Float>(timeData);
            curFacing = new ArrayList<Boolean>(facingData);
            timePassed = 0;
            this.totTimePassed = Util.random.nextFloat();

            updateLoc(curPos.get(0));
            jar.unexplode();
        }

        @Override
        public void render(SpriteBatch sb) {
            if (curPos.size() > 1)
                super.render(sb);
            else
                jar.explode(totTimePassed);

            jar.render(sb);
        }

        protected void update() {
            totTimePassed += Gdx.graphics.getDeltaTime();
            if (curPos.size() <= 1)
                return;

            timePassed += Gdx.graphics.getDeltaTime();

            while (curPos.size() > 1 && curTime.get(0) < timePassed) {
                timePassed -= curTime.remove(0);
                curPos.remove(0);
                curFacing.remove(0);
            }
            if (curPos.size() == 1) {
                updateLoc(curPos.get(0));
                totTimePassed = 0;
                return;
            }

            Vector2 prevPos = curPos.get(0), nextPos = curPos.get(1);

            updateLoc(prevPos.lerp(nextPos, timePassed / curTime.get(0)));
            this.facingLeft = curFacing.get(0);
        }

        public static final Rectangle shape = new Rectangle(-0.5f, 0, 1, 2);
        public static final Rectangle drawShape = new Rectangle(-1, 0, 2, 2);

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
            throw new RuntimeException("done by Ghosts class");
        }

        @Override
        public void create() {
            jar.create();
        }

        @Override
        public void dispose() {
            jar.dispose();
        }

        @Override
        protected TextureRegion sprite() {
            return Ghosts.ghostAnim.getKeyFrame(totTimePassed, true);
        }

    }

}
