package m4ges.views;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.cinematiques.Message;
import m4ges.models.cinematiques.MessageAccessor;
import m4ges.models.cinematiques.SpriteAccessor;
import m4ges.models.cinematiques.Valkyrie;
import m4ges.models.cinematiques.ValkyrieAccessor;
import m4ges.util.Constants;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * 
 * @author Florian
 * 
 */
public class FinalScreen extends AbstractScreen {


	String ipClient;
	Stage stage;
	TweenManager tweenManager;
	
	private Sprite sprite;
	private Message dialog;
	private BitmapFont font;

	public FinalScreen(MyGame myGame) {
		super(myGame);
		this.stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
		this.tweenManager = new TweenManager();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();

	}

	@Override
	public void render(float delta) {
		super.render( delta );
		
		tweenManager.update(delta);
		
		stage.act(delta);
		stage.draw();


		batch.begin();
		sprite.draw(batch);
		font.draw(batch,  dialog.getContenu(), dialog.getX(), dialog.getY());

		batch.end();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		sprite = new Valkyrie().getSprite();
		this.font = new BitmapFont(Gdx.files.internal("data/wolf.fnt"), false);

		dialog = new Message("Bravo");
		
		stage.addActor(buildBackgroundLayer());
//		stage.addActor(buildValkyrieLayer());
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(Valkyrie.class, new ValkyrieAccessor());
		Tween.registerAccessor(Message.class, new MessageAccessor());
		Tween.call(windCallback).start(tweenManager);
	}
	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		Image scrollingImage = new Image(atlas.findRegion("Scroll_chato"));
		scrollingImage.setHeight(Gdx.graphics.getHeight());
		scrollingImage.setPosition(0, 0);
		RepeatAction ra = new RepeatAction();
		ra.setAction(sequence(moveTo(0, 0), moveBy((int)(-scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear),
				moveBy((int)(scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear)));
		ra.setCount(RepeatAction.FOREVER);
		scrollingImage.addAction(ra);
		return scrollingImage;
	}

	
	private final TweenCallback windCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {

			Timeline.createSequence()
		    .push(Tween.set(dialog, ValkyrieAccessor.POSITION_XY).target(-100,400)) // First, set all objects to their initial positions
//		    .pushPause(2000)      // Wait 1 second
		    .push(Tween.to(sprite, ValkyrieAccessor.POSITION_XY, 4.0f).target(Constants.VIEWPORT_GUI_WIDTH-sprite.getWidth(), 0))  // Move the objects around, one after the other
			.push(Tween.to(dialog, MessageAccessor.POSITION_XY, 4.0f).target(200, 400))
		    .start(tweenManager);    // and finally start it!
			
//			System.err.println("fini");
		}
	};
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
