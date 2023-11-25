package com.haunt.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain {

    public static final float ZOOM = 1.5f;
    private final int[][] levelMap;

    public Terrain(int[][] map) {
        this.levelMap = map;
    }

    public void render(SpriteBatch sb, Camera cam) {
        for (int i = 0; i < levelMap[0].length; i++)
            for (int j = 0; j < levelMap.length; j++)
                if (tileAt(i, j) != null)
                    sb.draw(sprite(i, j), i, j, 1, 1);
    }

    public int mapWid() {
        return levelMap[0].length;
    }

    public int mapHei() {
        return levelMap.length;
    }

    public Tile tileAt(float x, float y) {
        int xx = (int) x, yy = (int) y;

        if (yy < 0)
            // return Tile.KILL;
            return null;
        if (xx < 0 || xx >= levelMap[0].length || yy >= levelMap.length)
            // return Tile.SOLID;
            return null;

        switch (levelMap[levelMap.length - 1 - yy][xx]) {
            default:
            case 0:
                return Tile.EMPTY;
            case 1:
                return Tile.SOLID;
            case 2:
                return Tile.KILL;
        }

    }

    private final HashMap<Tile, TextureRegion[][]> spr = new HashMap<Tile, TextureRegion[][]>();

    public void create() {
        spr.put(Tile.EMPTY, new TextureRegion(new Texture("environment/bg.png")).split(64, 64));
        spr.put(Tile.SOLID, new TextureRegion(new Texture("environment/Terrain_Wood_TileSet.png")).split(32, 32));
        spr.put(Tile.KILL, new TextureRegion(new Texture("environment/kill.png")).split(32, 32));
    }

    public void dispose() {
        for (TextureRegion[][] t : spr.values())
            t[0][0].getTexture().dispose();
    }

    private TextureRegion sprite(float x, float y) {
        Tile t = tileAt(x, y);
        TextureRegion[][] currentTileset = spr.get(t);
        int tileX = 0;
        int tileY = 0;

        boolean tileAbove = tileAt(x, y + 1) == t ? true : false;
        boolean tileBelow = tileAt(x, y - 1) == t ? true : false;
        boolean tileLeft = tileAt(x - 1, y) == t ? true : false;
        boolean tileRight = tileAt(x + 1, y) == t ? true : false;

        // Only do thing on actual tilesets
        if (currentTileset.length == 1 && currentTileset[0].length == 1) {
            return currentTileset[0][0];
        }

        if (tileAbove && !tileBelow) {
            if (tileLeft && tileRight) {
                tileX = 1;
                tileY = 2;
            } else if (tileLeft && !tileRight) {
                tileX = 2;
                tileY = 2;
            } else if (!tileLeft && tileRight) {
                tileX = 0;
                tileY = 2;
            } else {
                tileX = 4;
                tileY = 2;
            }
        } else if (!tileAbove && tileBelow) {
            if (tileLeft && tileRight) {
                tileX = 1;
                tileY = 0;
            } else if (tileLeft && !tileRight) {
                tileX = 2;
                tileY = 0;
            } else if (!tileLeft && tileRight) {
                tileX = 0;
                tileY = 0;
            } else {
                tileX = 4;
                tileY = 0;
            }
        } else if (tileAbove && tileBelow) {
            if (tileLeft && tileRight) {
                tileX = 1;
                tileY = 1;
            } else if (tileLeft && !tileRight) {
                tileX = 2;
                tileY = 1;
            } else if (!tileLeft && tileRight) {
                tileX = 0;
                tileY = 1;
            } else {
                tileX = 3;
                tileY = 0;
            }
        } else if (!tileAbove && !tileBelow) {
            if (tileLeft && tileRight) {
                tileX = 4;
                tileY = 1;
            } else if (tileLeft && !tileRight) {
                tileX = 5;
                tileY = 1;
            } else if (!tileLeft && tileRight) {
                tileX = 3;
                tileY = 1;
            } else {
                tileX = 3;
                tileY = 2;
            }
        }

        // new TextureRegion().split(32, 32);
        // TODO 9tile
        Gdx.app.log("wid", currentTileset.length + "");
        Gdx.app.log("hei", currentTileset[0].length + "");
        return currentTileset[tileY][tileX];
    }

}
