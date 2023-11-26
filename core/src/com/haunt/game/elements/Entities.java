package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entities {

    public final List<Entity> entities = new ArrayList<Entity>();

    public Entities() {

    }

    public void add(Entity e) {
        e.create();
        entities.add(e);
    }

    public void remove(Entity e) {
        entities.remove(e);
        e.dispose();
    }

    public void render(SpriteBatch sb) {
        for (Entity g : entities)
            g.render(sb);
    }

    public void update() {
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i).update()) {
                entities.remove(i);
                i--;
            }
    }

    public void create() {
        for (Entity g : entities)
            g.create();
    }

    public void dispose() {
        for (Entity g : entities)
            g.dispose();
    }

    public void clear() {
        for (int i = 0; i < entities.size(); i++) {
            if (!entities.get(i).removeOnRestart)
                continue;

            entities.get(i).dispose();
            entities.remove(i);
            i--;
        }
    }
}
