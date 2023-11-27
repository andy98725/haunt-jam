package com.haunt.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.haunt.game.elements.Character;
import com.haunt.game.elements.Entities;
import com.haunt.game.elements.Jar;
import com.haunt.game.elements.decor.Clock;
import com.haunt.game.elements.decor.Door;
import com.haunt.game.elements.decor.Drawer;
import com.haunt.game.elements.decor.Vase;
import com.haunt.game.elements.decor.Window;
import com.haunt.game.elements.hazards.Ghost;
import com.haunt.game.elements.hazards.Saw;
import com.haunt.game.elements.hazards.Spike;
import com.haunt.game.menu.Levels;
import com.haunt.game.ui.Timer;

public class Level {
    private final Levels levelMenu;
    private final HauntGame game;
    public final int levelID;

    public final Character character;
    public final List<Jar> goals = new ArrayList<Jar>();
    public final Entities entities = new Entities();
    public final Terrain terrain;
    public final Timer timer;
    private Camera cam;

    private Vector2 startLoc;
    private int goalIndex;
    private Vector2[] endLocs;

    public Level(Levels.LevelInfo info, int levelID, HauntGame game, Levels levelMenu) {
        this.game = game;
        this.timer = new Timer(info.beginningTime, info.timeIncrement);
        this.levelMenu = levelMenu;
        this.levelID = levelID;

        character = new Character(this, timer);
        String filedata = Gdx.files.local("assets/levels/" + info.levelName).readString();

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
                        break;
                    }
                if (matched)
                    continue;

                switch (id) {
                    case 3:
                        entities.add(new Saw(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 4:
                        entities.add(new Spike(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 5:
                        this.startLoc = new Vector2(i + 0.5f, j);
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 6:
                        // tileData[i][j] = null;
                        break;
                    case 20:
                        entities.add(new Drawer(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 21:
                        entities.add(new Vase(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 22:
                        entities.add(new Window(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 23:
                        entities.add(new Door(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 24:
                        entities.add(new Clock(new Vector2(i, j)));
                        tileData[i][j] = Tile.MATCH;
                        break;
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                        tileData[i][j] = Tile.MATCH;
                        jarLocs.add(new Vector2(i, j));
                        jarIDs.add(id - 100);
                        break;
                    default:
                        throw new RuntimeException("Unidentified tile data: " + id);
                }

            }
        }

        if (this.startLoc == null)
            throw new RuntimeException("level " + info.levelName + " has no start");
        if (jarIDs.size() == 0)
            throw new RuntimeException("level " + info.levelName + " has no jars");

        Vector2[] jars = new Vector2[jarIDs.size()];
        for (int i = 0; i < jars.length; i++)
            jars[jarIDs.get(i)] = jarLocs.get(i);

        this.endLocs = jars;
        this.terrain = new Terrain(tileData);

        restart(false);
    }

    public void playerDies() {
        character.die();
        isPaused = true;
    }

    public void restart(boolean addDeath) {
        if (addDeath)
            game.gameDeaths++;

        goalIndex = 0;
        isPaused = false;

        character.init(startLoc);
        entities.clear();
        entities.add(new Jar(endLocs, 0));
        timer.resetTimer();
    }

    public void nextLevel() {
        timer.printTime(levelID);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            restart(true);

        character.update();

        float x = character.loc.x, y = character.loc.y;
        if (terrain.mapWid() > cam.viewportWidth)
            x = MathUtils.clamp(x, cam.viewportWidth / 2, terrain.mapWid() - cam.viewportWidth / 2);
        if (terrain.mapHei() > cam.viewportHeight)
            y = MathUtils.clamp(y, cam.viewportHeight / 2, terrain.mapHei() - cam.viewportHeight / 2);
        else
            y = Math.max(y, cam.viewportHeight / 2);
        this.cam.position.set(x, y, 0);
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
