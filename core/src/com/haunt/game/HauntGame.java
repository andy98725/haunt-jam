package com.haunt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

	private Music music;

	@Override
	public void create() {
		levelSelect = new Levels(this);
		batch = new SpriteBatch();

		music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/chamber-of-secrets.wav"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();

		if (DEBUG)
			debugRenderer = new ShapeRenderer();
	}

	private TextureRegion winScreen = null;
	private String winTime;
	private float gameTime = 0;
	private BitmapFont font;
	private GlyphLayout txt;

	public void win() {
		winScreen = new TextureRegion(new Texture("assets/ui/win.png"));

		winTime = Util.formatTime(gameTime);
		font = new BitmapFont(Gdx.files.local("assets/ui/winFont.fnt"),
				new TextureRegion(new Texture("assets/ui/winFont.png")));
		txt = new GlyphLayout(font, winTime);

	}

	public void setLevel(Level l) {
		if (level != null)
			level.dispose();
		level = l;
		level.create();
		win();
	}

	@Override
	public void render() {
		if (winScreen != null) {
			batch.setProjectionMatrix(
					batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
			ScreenUtils.clear(0, 0, 0, 1);

			float wid = Math.min(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight() * winScreen.getRegionWidth() / winScreen.getRegionHeight());
			float hei = wid * winScreen.getRegionHeight() / winScreen.getRegionWidth();
			float x = Gdx.graphics.getWidth() / 2 - wid / 2;
			float y = Gdx.graphics.getHeight() / 2 - hei / 2;

			batch.begin();
			batch.draw(winScreen, x, y, wid, hei);

			float txtCX = x + wid * 0.725f;
			float txtCY = y + hei * 0.9f;
			font.draw(batch, winTime, txtCX - txt.width / 2, txtCY - txt.height / 2);
			batch.end();
			return;
		}
		gameTime += Gdx.graphics.getDeltaTime();

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
