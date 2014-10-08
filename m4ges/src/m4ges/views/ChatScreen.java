package m4ges.views;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.util.Constants;
import reseau.TCPClient;
import reseau.UnicastClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;

/**
 * lancement du chat apres saisie du pseudo et de l'adresse
 * 
 * @author Florian
 * 
 */
public class ChatScreen extends AbstractScreen {


	private Stage stage;


	public ChatScreen(MyGame myGame) {
		super(myGame);
		this.batch = new SpriteBatch();
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT, false);
	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);
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

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(buildBackgroundLayer());

		// bouton de validation
		final TextButton validation = new TextButton("se connecter", skin);
		validation.pad(5);
		//bouton pour signaler qu'on est pret a debuter la partie
		final TextButton pret = new TextButton("Prêt", skin);
		pret.pad(5);
		pret.setColor(Color.RED);

		// recuperation des dimensions de l'ecran
		float width = Constants.VIEWPORT_GUI_WIDTH;
		//float height = Constants.VIEWPORT_GUI_HEIGHT;

		// window.debug();
		final Window window = new Window("Connexion", skin);
		window.getButtonTable().pad(5);
		window.setPosition(width * 0, 200);
		window.defaults().pad(20, 20, 20, 20);
		window.row();
		window.add(validation);
		window.row();
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);
		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				ChatWindow cw = new ChatWindow(game);
				final TCPClient uc = new TCPClient(game);
				uc.chatWindow = cw;
				
				game.setMC(uc);

//				try {
//					uc.lancerClient();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				window.removeActor(validation);
				window.add(pret);
				window.row();
				/*
				 * J'ai mit un listener dans un autre
				 * c'est degueu, si tu veux changer vas y
				 * mais faut ajouter UnicastClient en param du coup
				 */
				pret.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						pret.setColor(Color.GREEN);
						uc.estPret();
					}
				});
				
				stage.addActor(cw.getWindow());
//				
			}
		});


	}
	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		Image scrollingImage = new Image(atlas.findRegion("Scroll_balcon"));
		scrollingImage.setPosition(0, 0);
		scrollingImage.setHeight(Constants.VIEWPORT_GUI_HEIGHT);
		RepeatAction ra = new RepeatAction();
		ra.setAction(sequence(moveTo(0, 0), moveBy((int)(-scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear),
				moveBy((int)(scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear)));
		ra.setCount(RepeatAction.FOREVER);
		scrollingImage.addAction(ra);
		return scrollingImage;
	}
	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

}
