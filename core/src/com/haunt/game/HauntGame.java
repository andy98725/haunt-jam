package com.haunt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.haunt.game.menu.Levels;

public class HauntGame extends ApplicationAdapter {

	public static final boolean DEBUG = false;

	public static ShapeRenderer debugRenderer;
	private SpriteBatch batch;

	private Level level;
	private Levels levelSelect;

	@Override
	public void create() {
		levelSelect = new Levels(this);
		batch = new SpriteBatch();

		if (DEBUG)
			debugRenderer = new ShapeRenderer();
	}

	public void setLevel(Level l) {
		if (level != null)
			level.dispose();
		level = l;
		level.create();
	}

	@Override
	public void render() {
		levelSelect.input();

		level.update();
		ScreenUtils.clear(0, 0, 0, 1);

		if (DEBUG)
			debugRenderer.begin(ShapeType.Line);
		batch.begin();
		level.render(batch);
		batch.end();
		if (DEBUG)
			debugRenderer.end();
	}

	@Override
	public void dispose() {
		level.dispose();
		batch.dispose();
		if (DEBUG)
			debugRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		level.resize(width, height);
	}
}
