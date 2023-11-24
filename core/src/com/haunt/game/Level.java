package com.haunt.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Ghosts;
import com.haunt.game.elements.Goal;

public class Level {

    private final Character character = new Character();
    private final Goal goal = new Goal();
    private final Ghosts ghosts = new Ghosts();
    private final Terrain terrain;
    private Camera cam;

    private int jamI;
    private Vector2[] startLocs = new Vector2[] { new Vector2(5.5f, 5) };
    private Vector2[] endLocs = new Vector2[] { new Vector2(14.5f, 6.5f) };

    public Level(int[][] map, boolean upsideDown) {
        this.terrain = new Terrain(map, upsideDown);

        jamI = 0;
        character.init(startLocs[jamI % startLocs.length]);
        goal.setLoc(endLocs[jamI % startLocs.length]);

    }

    public void reachGoal() {
        jamI++;
        character.init(startLocs[jamI % startLocs.length]);
        goal.setLoc(endLocs[jamI % startLocs.length]);
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
