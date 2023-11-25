package com.haunt.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Ghosts;
import com.haunt.game.elements.Jar;
import com.ui.Timer;

public class Level {
    private static final Random r = new Random();

    public final Character character = new Character(this);
    public final Jar goal = new Jar();
    public final Ghosts ghosts = new Ghosts();
    public final Terrain terrain;
    public final Timer timer = new Timer(15);
    private Camera cam;

    private Vector2 startLoc;
    private int goalIndex;
    private Vector2[] endLocs;

    public Level(String filename) {
        String filedata = Gdx.files.local("assets/levels/" + filename).readString();

        String[] rows = filedata.split("\n");
        int cols = rows[0].split(",").length;

        Tile[][] tileData = new Tile[cols][rows.length];
        List<Vector2> jarLocs = new ArrayList<Vector2>();
        List<Integer> jarIDs = new ArrayList<Integer>();

        for (int j = 0; j < rows.length; j++) {
            String[] data = rows[rows.length - 1 - j].split(",");
            for (int i = 0; i < data.length; i++) {
                int t = Integer.parseInt(data[i].trim());
                if (t == 1)
                    tileData[i][j] = Tile.SOLID;
                else if (t == 5) {
                    tileData[i][j] = Tile.EMPTY;
                    this.startLoc = new Vector2(i, j);
                } else if (t >= 10) {
                    tileData[i][j] = Tile.EMPTY;
                    jarLocs.add(new Vector2(i, j));
                    jarIDs.add(t - 10);
                } else if (t < 0)
                    tileData[i][j] = Tile.EMPTY;
                else
                    throw new RuntimeException("Unidentified tile data " + t);

            }
        }

        Vector2[] jars = new Vector2[jarIDs.size()];
        for (int i = 0; i < jars.length; i++)
            jars[jarIDs.get(i)] = jarLocs.get(i);

        this.endLocs = jars;
        this.terrain = new Terrain(tileData);

        reset();
    }

    public void playerDies() {
        Gdx.app.log("YOU LOSE", ":(");
        reset();
    }

    private void reset() {
        goalIndex = 0;

        character.init(startLoc);
        goal.updateLoc(endLocs[goalIndex % endLocs.length]);
        ghosts.reset();
        timer.resetTimer();
    }

    public void reachGoal() {
        goalIndex++;
        if (goalIndex >= endLocs.length) {
            Gdx.app.log("YOU WIN", "!!!");

            do {
                goal.updateLoc(new Vector2(r.nextInt(terrain.mapWid()), r.nextInt(terrain.mapHei())));
            } while (terrain.tileAt(goal.loc.x, goal.loc.y) != Tile.EMPTY);

        } else
            goal.updateLoc(endLocs[goalIndex % endLocs.length]);
        character.reachGoal();

        // Create ghost from tracked positions
        ghosts.addGhost(character.makeGhost());
        ghosts.resetTimer();
        timer.resetTimer();

    }

    public void update() {
        character.update();
        this.cam.position.set(character.loc.x, character.loc.y, 0);
        this.cam.update();

        ghosts.update();

        if (!character.invincible() && ghosts.ghostCollided(character.pos))
            playerDies();

        else if (character.pos.overlaps(goal.pos))
            reachGoal();

        else if (timer.update())
            playerDies();

    }

    public void render(SpriteBatch sb) {
        // Game map
        sb.setProjectionMatrix(cam.combined);
        if (HauntGame.DEBUG)
            HauntGame.debugRenderer.setProjectionMatrix(cam.combined);

        terrain.render(sb, cam);
        ghosts.render(sb);
        goal.render(sb);
        character.render(sb);

        // UI
        sb.setProjectionMatrix(
                sb.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        if (HauntGame.DEBUG)
            HauntGame.debugRenderer.setProjectionMatrix(
                    HauntGame.debugRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
                            Gdx.graphics.getHeight()));

        timer.render(sb);
    }

    public void create() {
        this.cam = new OrthographicCamera();
        terrain.create();
        character.create();
        ghosts.create();
        goal.create();
        timer.create();
    }

    public void dispose() {
        terrain.dispose();
        character.dispose();
        ghosts.dispose();
        goal.dispose();
        timer.dispose();
    }

    public void resize(int width, int height) {
        if (cam != null) {
            cam.viewportWidth = 32f / Terrain.ZOOM;
            cam.viewportHeight = 32f * height / width / Terrain.ZOOM;
            cam.update();
        }
    }
}
