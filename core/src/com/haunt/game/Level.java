package com.haunt.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Ghosts;
import com.haunt.game.elements.Goal;

public class Level {

    public final Character character = new Character();
    public final Goal goal = new Goal();
    public final Ghosts ghosts = new Ghosts();
    public final Terrain terrain;
    private Camera cam;

    private int goalIndex;
    private Vector2[] startLocs = new Vector2[] { new Vector2(5.5f, 5) };
    private Vector2[] endLocs = new Vector2[] { new Vector2(14.5f, 6.5f) };

    public Level(int[][] map) {
        this.terrain = new Terrain(map);

        goalIndex = 0;
        restart();

    }

    public void restart() {
        character.init(startLocs[goalIndex % startLocs.length]);
        goal.updateLoc(endLocs[goalIndex % startLocs.length]);
    }

    public void reachGoal() {
        goalIndex++;
        character.init(startLocs[goalIndex % startLocs.length]);
        goal.updateLoc(endLocs[goalIndex % startLocs.length]);
    }

    public void update() {
        character.update(this);

        this.cam.position.set(character.loc.x * Terrain.TILE_SIZE, character.loc.y * Terrain.TILE_SIZE, 0);
        this.cam.update();
    }

    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        terrain.render(sb, cam);
        ghosts.render(sb);
        goal.render(sb);
        character.render(sb);
    }

    public void create() {
        this.cam = new OrthographicCamera(8, 6);
        this.cam.position.set(character.loc.x, character.loc.y, 0);
        this.cam.update();

        terrain.create();
        character.create();
        ghosts.create();
        goal.create();
    }

    public void dispose() {
        terrain.dispose();
        character.dispose();
        ghosts.dispose();
        goal.dispose();
    }

    public void resize(int width, int height) {
        if (cam != null) {
            cam.viewportWidth = 30f;
            cam.viewportHeight = 30f * height / width;
            cam.update();
        }
    }
}
