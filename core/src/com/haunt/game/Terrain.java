package com.haunt.game;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.haunt.game.menu.Levels.LevelInfo;

public class Terrain {

    public static final float ZOOM = 1.5f;
    private final Tile[][] levelMap;
    private LevelInfo level;

    public Terrain(Tile[][] map, LevelInfo info) {
        this.levelMap = map;
        this.level = info;

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == Tile.MATCH) {
                    if (i == 0 || j == 0 || i == map.length - 1 || j == map[i].length - 1)
                        map[i][j] = null;
                    else if (map[i - 1][j] == null || map[i - 1][j] == Tile.BG)
                        map[i][j] = map[i - 1][j];
                    else if (map[i + 1][j] == null || map[i + 1][j] == Tile.BG)
                        map[i][j] = map[i + 1][j];
                    else if (map[i][j - 1] == null || map[i][j - 1] == Tile.BG)
                        map[i][j] = map[i][j - 1];
                    else if (map[i][j + 1] == null || map[i][j + 1] == Tile.BG)
                        map[i][j] = map[i][j + 1];
                    else
                        map[i][j] = null;
                }
            }
    }

    public void render(SpriteBatch sb, Camera cam) {
        for (int i = 0; i < levelMap.length; i++)
            for (int j = 0; j < levelMap[0].length; j++)
                if (drawTileAt(i, j) != null) {
                    Tile t = drawTileAt(i, j);
                    if (t.bgTile() != t && t.bgTile() != null)
                        sb.draw(spr.get(t.bgTile())[0][0], i, j, 1, 1);

                    sb.draw(sprite(i, j), i, j, 1, 1);
                }
    }

    public int mapWid() {
        return levelMap.length;
    }

    public int mapHei() {
        return levelMap[0].length;
    }

    public Tile drawTileAt(float x, float y) {
        int xx = (int) x, yy = (int) y;

        if (yy < 0)
            // return Tile.KILL;
            return null;
        if (xx < 0 || xx >= levelMap.length || yy >= levelMap[0].length)
            // return Tile.SOLID;
            return null;

        return levelMap[xx][yy];
    }

    public Tile collisionTileAt(float x, float y) {
        int xx = (int) x, yy = (int) y;

        if (yy < 0)
            // return Tile.KILL;
            return null;
        if (xx < 0 || xx >= levelMap.length || yy >= levelMap[0].length)
            // return Tile.SOLID;
            return null;

        if (levelMap[xx][yy] == null)
            return null;
        return levelMap[xx][yy].collisionTile();
    }

    private final HashMap<Tile, TextureRegion[][]> spr = new HashMap<Tile, TextureRegion[][]>();

    public void create() {

        spr.put(Tile.BG, new TextureRegion(new Texture("assets/environment/bg.png")).split(64, 64));

        if (level.bonusLevel) // Set Bonus Level Terrain Sprites
        {
            spr.put(Tile.SOLID,
                    new TextureRegion(new Texture("assets/environment/Terrain_Bonus_TileSet.png")).split(32, 32));
        } else // Set Regular Level Terrain Sprites
        {
            spr.put(Tile.SOLID,
                    new TextureRegion(new Texture("assets/environment/Terrain_Wood_TileSet.png")).split(32, 32));
        }

        spr.put(Tile.FALLTHROUGH,
                new TextureRegion(new Texture("assets/environment/SemisolidPlatform.png")).split(32, 32));
    }

    public void dispose() {
        for (TextureRegion[][] t : spr.values())
            t[0][0].getTexture().dispose();
    }

    private TextureRegion sprite(float x, float y) {
        Tile t = drawTileAt(x, y).collisionTile();
        TextureRegion[][] currentTileset = spr.get(t);
        int tileX = 0;
        int tileY = 0;

        boolean tileAbove = drawTileAt(x, y + 1) == t ? true : false;
        boolean tileBelow = drawTileAt(x, y - 1) == t ? true : false;
        boolean tileLeft = drawTileAt(x - 1, y) == t ? true : false;
        boolean tileRight = drawTileAt(x + 1, y) == t ? true : false;

        // Only do thing on actual tilesets

        switch (t) {
            default:
                return currentTileset[0][0];
            case SOLID:
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
                break;

            case FALLTHROUGH:
                if (!tileLeft && tileRight)
                    tileX = 0;
                if (tileLeft && tileRight)
                    tileX = 1;
                if (tileLeft && !tileRight)
                    tileX = 2;
                if (!tileLeft && !tileRight)
                    tileX = 3;
                break;
        }

        return currentTileset[tileY][tileX];
    }

}
