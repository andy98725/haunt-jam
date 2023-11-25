package com.haunt.game;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Terrain {

    public static final float ZOOM = 1.5f;
    private final Tile[][] levelMap;

    public Terrain(Tile[][] map) {
        this.levelMap = map;
    }

    public void render(SpriteBatch sb, Camera cam) {
        for (int i = 0; i < levelMap.length; i++)
            for (int j = 0; j < levelMap[0].length; j++)
                if (tileAt(i, j) != null)
                    sb.draw(sprite(i, j), i, j, 1, 1);
    }

    public int mapWid() {
        return levelMap.length;
    }

    public int mapHei() {
        return levelMap[0].length;
    }

    public Tile tileAt(float x, float y) {
        int xx = (int) x, yy = (int) y;

        if (yy < 0)
            // return Tile.KILL;
            return null;
        if (xx < 0 || xx >= levelMap.length || yy >= levelMap[0].length)
            // return Tile.SOLID;
            return null;

        return levelMap[xx][yy];
    }

    private final HashMap<Tile, TextureRegion> spr = new HashMap<Tile, TextureRegion>();

    public void create() {
        spr.put(Tile.EMPTY, new TextureRegion(new Texture("assets/environment/bg.png")));
        spr.put(Tile.SOLID, new TextureRegion(new Texture("assets/environment/solid.png")));
        spr.put(Tile.KILL, new TextureRegion(new Texture("assets/environment/kill.png")));
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
