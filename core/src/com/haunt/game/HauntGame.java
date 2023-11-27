package com.haunt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
	private String winTime, winDeaths;
	private float gameTime = 0;
	public int gameDeaths = 0;
	private BitmapFont font;
	private GlyphLayout timeTxt, deathTxt;

	public void win() {
		winScreen = new TextureRegion(new Texture("assets/ui/win.png"));
		font = new BitmapFont(Gdx.files.local("assets/ui/winFont.fnt"),
				new TextureRegion(new Texture("assets/ui/winFont.png")));

		winTime = Util.formatTime(gameTime);
		timeTxt = new GlyphLayout(font, winTime);
		winDeaths = Integer.toString(gameDeaths) + " death" + (gameDeaths != 1 ? "s" : "");
		deathTxt = new GlyphLayout(font, winDeaths);
	}

	public void setLevel(Level l) {
		if (level != null)
			level.dispose();
		level = l;
		level.create();
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
			float txt2CY = y + hei * 0.8f;
			font.setColor(Color.GREEN);
			font.draw(batch, winTime, txtCX - timeTxt.width / 2, txtCY - timeTxt.height / 2);
			font.setColor(gameDeaths == 0 ? Color.YELLOW : Color.RED);
			font.draw(batch, winDeaths, txtCX - deathTxt.width / 2, txt2CY - timeTxt.height / 2);
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
