package com.haunt.game;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain {

    public static final float TILE_SIZE = 2;
    private final int[][] levelMap;

    public Terrain(int[][] map) {
        this.levelMap = map;
    }

    public void render(SpriteBatch sb, Camera cam) {
        // float lX = cam.position.x - cam.viewportWidth / 2, rX = cam.position.x +
        // cam.viewportWidth / 2;
        // float tY = cam.position.y - cam.viewportHeight / 2, bY = cam.position.y +
        // cam.viewportHeight / 2;
        // for (float x = lX - TILE_SIZE; x < rX + TILE_SIZE; x += TILE_SIZE)
        // for (float y = tY - TILE_SIZE; y < bY + TILE_SIZE; y += TILE_SIZE)
        // if (tileAt(x, y) != null)
        // sb.draw(sprite(x, y), x, y, TILE_SIZE, TILE_SIZE);

        // spriteBatch draws upside down
        float botY = (levelMap.length - 1) * TILE_SIZE;
        for (int i = 0; i < levelMap.length; i++)
            for (int j = 0; j < levelMap[0].length; j++)
                if (tileAt(i * TILE_SIZE, j * TILE_SIZE) != null)
                    sb.draw(sprite(i * TILE_SIZE, j * TILE_SIZE), i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE,
                            TILE_SIZE);
    }

    public Tile tileAt(float x, float y) {
        x /= TILE_SIZE;
        y /= TILE_SIZE;
        int xx = (int) x, yy = (int) y;

        if (yy < 0)
            // return Tile.KILL;
            return null;
        if (xx < 0 || xx >= levelMap[0].length || yy >= levelMap.length)
            // return Tile.SOLID;
            return null;

        switch (levelMap[yy][xx]) {
            default:
            case 0:
                return Tile.EMPTY;
            case 1:
                return Tile.SOLID;
            case 2:
                return Tile.KILL;
        }

    }

    public static enum Tile {
        EMPTY, SOLID, KILL
    }

    private final HashMap<Tile, TextureRegion> spr = new HashMap<Tile, TextureRegion>();

    public void create() {
        spr.put(Tile.EMPTY, new TextureRegion(new Texture("environment/bg.png")));
        spr.put(Tile.SOLID, new TextureRegion(new Texture("environment/solid.png")));
        spr.put(Tile.KILL, new TextureRegion(new Texture("environment/kill.png")));
    }

    public void dispose() {
        for (TextureRegion t : spr.values())
            t.getTexture().dispose();
    }

    private TextureRegion sprite(float x, float y) {
        Tile t = tileAt(x, y);
        // TODO 9tile

        return spr.get(t);
    }

}
