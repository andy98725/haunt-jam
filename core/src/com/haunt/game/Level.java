package com.haunt.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Entities;
import com.haunt.game.elements.Jar;
import com.haunt.game.elements.hazards.Ghost;
import com.haunt.game.elements.hazards.Saw;
import com.haunt.game.elements.hazards.Spike;
import com.haunt.game.menu.Levels;
import com.haunt.game.ui.Timer;

public class Level {
    private final Levels levelMenu;
    public final int levelID;

    public final Character character;
    public final List<Jar> goals = new ArrayList<Jar>();
    public final Entities entities = new Entities();
    public final Terrain terrain;
    public final Timer timer = new Timer(15, 5);
    private Camera cam;

    private Vector2 startLoc;
    private int goalIndex;
    private Vector2[] endLocs;

    public Level(String filename, Levels levelMenu, int levelID) {
        this.levelMenu = levelMenu;
        this.levelID = levelID;

        character = new Character(this, timer);
        String filedata = Gdx.files.local("assets/levels/" + filename).readString();

        String[] rows = filedata.split("\n");
        int cols = rows[0].split(",").length;

        Tile[][] tileData = new Tile[cols][rows.length];
        List<Vector2> jarLocs = new ArrayList<Vector2>();
        List<Integer> jarIDs = new ArrayList<Integer>();

        for (int j = 0; j < rows.length; j++) {
            String[] data = rows[rows.length - 1 - j].split(",");
            for (int i = 0; i < data.length; i++) {
                int id = Integer.parseInt(data[i].trim());
                boolean matched = false;
                for (Tile t : Tile.values())
                    if (t.mapID == id) {
                        matched = true;
                        if (tileData[i][j] == null)
                            tileData[i][j] = t;

                        if (t == Tile.D_DOOR || t == Tile.D_CLOCK)
                            tileData[i][j + 1] = t;
                        break;
                    }
                if (matched)
                    continue;

                switch (id) {
                    case 3:
                        tileData[i][j] = Tile.EMPTY;
                        this.entities.add(new Saw(new Vector2(i, j)));
                        break;
                    case 4:
                        tileData[i][j] = Tile.EMPTY;
                        this.entities.add(new Spike(new Vector2(i, j)));
                        break;
                    case 5:
                        tileData[i][j] = Tile.EMPTY;
                        this.startLoc = new Vector2(i, j);
                        break;
                    case 6:
                        tileData[i][j] = null;
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        tileData[i][j] = Tile.EMPTY;
                        jarLocs.add(new Vector2(i, j));
                        jarIDs.add(id - 10);
                        break;
                    default:
                        throw new RuntimeException("Unidentified tile data: " + id);
                }

            }
        }

        if (this.startLoc == null)
            throw new RuntimeException("level " + filename + " has no start");
        if (jarIDs.size() == 0)
            throw new RuntimeException("level " + filename + " has no jars");

        Vector2[] jars = new Vector2[jarIDs.size()];
        for (int i = 0; i < jars.length; i++)
            jars[jarIDs.get(i)] = jarLocs.get(i);

        this.endLocs = jars;
        this.terrain = new Terrain(tileData);

        restart();
    }

    public void playerDies() {
        character.die();
        isPaused = true;
    }

    public void restart() {
        goalIndex = 0;

        character.init(startLoc);
        entities.clear();
        entities.add(new Jar(endLocs, 0));
        timer.resetTimer();
    }

    public void nextLevel() {
        levelMenu.setLevel(levelID + 1);

    }

    private static final boolean ENDLESS_MODE = false;
    public boolean isPaused = false;

    public void reachGoal(Jar j) {
        goalIndex++;
        if (goalIndex >= endLocs.length && !ENDLESS_MODE) {
            character.reachGoal(true);
            isPaused = true;
        } else {
            character.reachGoal(false);
            timer.incrementTimer();
            entities.add(new Ghost(character));

            if (goalIndex >= endLocs.length)
                // Random location
                entities.add(new Jar(this));
            else
                entities.add(new Jar(endLocs, goalIndex));
        }

    }

    public void update() {

        character.update();
        this.cam.position.set(character.loc.x, character.loc.y, 0);
        this.cam.update();

        if (isPaused)
            return;

        entities.update();

        for (int i = 0; i < entities.entities.size(); i++)
            if (entities.entities.get(i).collided(character.pos))
                if (entities.entities.get(i).onCollision(this)) {
                    break;
                }

        if (timer.update())
            playerDies();

    }

    public void render(SpriteBatch sb) {
        // Game map
        sb.setProjectionMatrix(cam.combined);
        if (HauntGame.DEBUG)
            HauntGame.debugRenderer.setProjectionMatrix(cam.combined);

        terrain.render(sb, cam);
        entities.render(sb);
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
        entities.create();
        timer.create();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose() {
        terrain.dispose();
        character.dispose();
        entities.dispose();
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
