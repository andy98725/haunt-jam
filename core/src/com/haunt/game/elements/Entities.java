package com.haunt.game.elements;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entities {

    public final List<Element> entities = new ArrayList<Element>();

    public Entities() {

    }

    public void add(Element e) {
        e.create();
        entities.add(e);
    }

    public void remove(Element e) {
        entities.remove(e);
        e.dispose();
    }

    public void render(SpriteBatch sb) {
        for (Element g : entities)
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
        for (Element g : entities)
            g.create();
    }

    public void dispose() {
        for (Element g : entities)
            g.dispose();
    }

    public void clear() {
        for (Element g : entities)
            g.dispose();
        entities.clear();
    }
}
