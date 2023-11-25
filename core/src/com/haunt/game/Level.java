package com.haunt.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Ghosts;
import com.haunt.game.elements.Goal;

public class Level {

    public final Character character = new Character(this);
    public final Goal goal = new Goal();
    public final Ghosts ghosts = new Ghosts();
    public final Terrain terrain;
    private Camera cam;

    private Vector2 startLoc;
    private int goalIndex;
    private Vector2[] endLocs;

    public Level(int[][] map, Vector2 startLoc, Vector2[] jarLocs) {
        this.terrain = new Terrain(map);

        this.startLoc = startLoc;
        this.endLocs = jarLocs;

        restart();

        goal.updateLoc(new Vector2(startLoc.x + 2, startLoc.y));
    }

    public void restart() {
        goalIndex = 0;

        character.init(startLoc);
        goal.updateLoc(endLocs[goalIndex % endLocs.length]);
        ghosts.reset();

    }

    private static final Random r = new Random();

    public void reachGoal() {
        goalIndex++;
        goal.updateLoc(endLocs[goalIndex % endLocs.length]);
        character.reachGoal();

        // Create ghost from tracked positions
        ghosts.addGhost(character.makeGhost());
        ghosts.resetTimer();

        // TEMP randomize location
        do {
            goal.updateLoc(new Vector2(0.5f + r.nextInt(terrain.mapWid()), 0.5f + r.nextInt(terrain.mapHei())));
        } while (terrain.tileAt(goal.loc.x, goal.loc.y) != Tile.EMPTY);
    }

    public void update() {
        character.update();
        this.cam.position.set(character.loc.x, character.loc.y, 0);
        this.cam.update();

        ghosts.update();

        if (!character.invincible() && ghosts.ghostCollided(character.pos))
            restart();

        if (character.pos.overlaps(goal.pos))
            reachGoal();

    }

    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        if (HauntGame.DEBUG)
            HauntGame.debugRenderer.setProjectionMatrix(cam.combined);

        terrain.render(sb, cam);
        ghosts.render(sb);
        goal.render(sb);
        character.render(sb);
    }

    public void create() {
        this.cam = new OrthographicCamera();
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
            cam.viewportWidth = 32f / Terrain.ZOOM;
            cam.viewportHeight = 32f * height / width / Terrain.ZOOM;
            cam.update();
        }
    }
}
