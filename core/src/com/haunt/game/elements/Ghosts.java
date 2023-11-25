package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
            if (g.isMoving())
                g.render(sb);
        sb.setColor(Color.WHITE);
    }

    public void resetTimer() {
        for (Ghost g : ghosts)
            if (g.isMoving())
                g.reset();
    }

    public void create() {
        for (Ghost g : ghosts)
            g.create();

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

    public static class Ghost extends Element {

        private float timePassed;

        private final List<Vector2> posData;
        private final List<Float> timeData;
        private final List<Boolean> facingData;

        private List<Vector2> curPos;
        private List<Float> curTime;
        private List<Boolean> curFacing;

        public Ghost(List<Vector2> pos, List<Float> time, List<Boolean> facing) {
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

            updateLoc(curPos.get(0));
        }

        protected void update() {
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
            return "assets/environment/ghost.png";
        }

    }

}
