package com.haunt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class HauntGame extends ApplicationAdapter {
	private SpriteBatch batch;

	private Level level = new Level(new int[][] {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1 },
	});

	@Override
	public void create() {
		batch = new SpriteBatch();
		level.create();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		level.render(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		level.dispose();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		level.resize(width, height);
	}
}
