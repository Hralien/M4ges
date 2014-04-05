package m4ges.views;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Citation;
import m4ges.models.Effect;
import m4ges.models.LoadingBar;
import m4ges.models.Skill;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


/**
 * @author Mats Svensson
 */
public class LoadingScreen extends AbstractScreen {

	private Stage stage;

	private Image logo;
	private Image loadingFrame;
	private Image loadingBarHidden;
	private Image screenBg;
	private Image loadingBg;

	private float startX, endX;
	private float percent;

	private Actor loadingBar;

	private BitmapFont font;


	public LoadingScreen(MyGame game) {
		super(game);
		this.font = new BitmapFont(Gdx.files.internal("data/roboto-16.fnt"), false);
		Citation.buildList();

	}

	@Override
	public void show() {
		// Tell the manager to load assets for the loading screen
		MyGame.manager.load("ui/loading.pack", TextureAtlas.class);

		// Wait until they are finished loading
		MyGame.manager.finishLoading();

		// Initialize the stage where we will place everything
		stage = new Stage();

		TextureAtlas atlas = MyGame.manager.get("ui/loading.pack", TextureAtlas.class);
		// Grab the regions from the atlas and create some images
		logo = new Image(atlas.findRegion("TitleM4ges"));
		logo.setSize((float)(Gdx.graphics.getWidth()*.35),(float)( Gdx.graphics.getHeight()*.25));
		loadingFrame = new Image(atlas.findRegion("loading-frame"));
		loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
		screenBg = new Image(atlas.findRegion("screen-bg"));
		loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

		// Add the loading bar animation
		Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
		anim.setPlayMode(Animation.LOOP_REVERSED);
		loadingBar = new LoadingBar(anim);

		// Or if you only need a static bar, you can do
		// loadingBar = new Image(atlas.findRegion("loading-bar1"));

		Label lb_info = new Label("M4ges - IUT NANCY Charlemagne - 2013/2014", skin);

		
		// Add all the actors to the stage
		stage.addActor(screenBg);
		stage.addActor(loadingBar);
		stage.addActor(loadingBg);
		stage.addActor(loadingBarHidden);
		stage.addActor(loadingFrame);
		stage.addActor(logo);
		stage.addActor(lb_info);
		
		// Add everything to be loaded, for instance:
		MyGame.manager.load("effects/skill.pack", TextureAtlas.class);
		MyGame.manager.load("ui/battleui.pack", TextureAtlas.class);
		MyGame.manager.load("ui/maps.pack", TextureAtlas.class);
		MyGame.manager.load("character/personnage.pack", TextureAtlas.class);
		MyGame.manager.load("ui/scroll.pack", TextureAtlas.class);
		MyGame.manager.load("items/item.pack", TextureAtlas.class);

	}

	@Override
	public void resize(int width, int height) {
		// Set our screen to always be XXX x 480 in size
		width = (int) (Constants.VIEWPORT_GUI_HEIGHT * width / height);
		height = (int) Constants.VIEWPORT_GUI_HEIGHT;
		stage.setViewport(width , height, false);

		// Make the background fill the screen
		screenBg.setSize(width, height);

		// Place the logo in the middle of the screen and 100 px up
		logo.setX((width - logo.getWidth()) / 2);
		logo.setY((height - logo.getHeight()) / 2 + 100);

		// Place the loading frame in the middle of the screen
		loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
		loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 3);

		// Place the loading bar at the same spot as the frame, adjusted a few px
		loadingBar.setX(loadingFrame.getX() + 15);
		loadingBar.setY(loadingFrame.getY() + 5);

		// Place the image that will hide the bar on top of the bar, adjusted a few px
		loadingBarHidden.setX(loadingBar.getX() + 35);
		loadingBarHidden.setY(loadingBar.getY() - 3);
		// The start position and how far to move the hidden loading bar
		startX = loadingBarHidden.getX();
		endX = 440;

		// The rest of the hidden bar
		loadingBg.setSize(450, 50);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		super.render(delta);

		if (MyGame.manager.update()) { // Load some, will return true if done loading
			Skill.Initialisation();
			Effect.Initialisation();
			//            if (Gdx.input.isTouched()) { // If the screen is touched after the game is done loading, go to the main menu screen
			Gdx.input.vibrate(100);
			super.game.changeScreen(MyGame.MENUSCREEN);
			//            }
		}

		// Interpolate the percentage to make it more smooth
		percent = Interpolation.linear.apply(percent, MyGame.manager.getProgress(), 0.1f);

		// Update positions (and size) to match the percentage
		loadingBarHidden.setX(startX + endX * percent);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setWidth(450 - 450 * percent);
		loadingBg.invalidate();


		// Show the loading screen
		stage.act();
		stage.draw();

		//draw tips
		Gdx.gl10.glDisable(GL10.GL_ALPHA_TEST);
	}

	@Override
	public void hide() {
		// Dispose the loading assets as we no longer need them
		//        MyGame.manager.unload("ui/loading.pack");
	}

	@Override
	public void dispose() {
		stage.dispose();
		batch.dispose();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
}
