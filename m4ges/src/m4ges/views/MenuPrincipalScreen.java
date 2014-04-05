package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import java.io.IOException;
import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Effect;
import m4ges.models.Sauvegarde;
import m4ges.models.Vague;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Joueur;
import m4ges.models.classes.Necromancien;
import m4ges.models.monster.Monstre;
import m4ges.util.AudioManager;
import m4ges.util.Constants;
import m4ges.util.GamePreferences;
import reseau.UnicastClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class MenuPrincipalScreen extends AbstractScreen {
	/**
	 * {@link Stage}
	 */
	private Stage stage;

	/**
	 * {@link AtlasRegion} to get the logo
	 */
	private Image imgTitle;

	// menu
	/**
	 * bouton pour heberger une partie
	 */
	//	private Button btnMenuHost;
	/**
	 * bouton pour join une partie
	 */
	private Button btnMenuPlay;
	/**
	 * bouton pour afficher options
	 */
	private Button btnMenuOptions;
	/**
	 * bouton pour afficher les tests
	 */
	//private Button btnMenuTest;
	/**
	 * bouton pour afficher le dico
	 */
	private Button btnMenuDico;
	// options
	/**
	 * fenetre des options
	 */
	private Window winOptions;
	/**
	 * bouton pour save les options
	 */
	private TextButton btnWinOptSave;
	/**
	 * bouton pour annuler les modifs
	 */
	private TextButton btnWinOptCancel;
	/**
	 * checkbox pour le son
	 */
	private CheckBox chkSound;
	/**
	 * slider pour volume son
	 */
	private Slider sldSound;
	/**
	 * checkbox pour music
	 */
	private CheckBox chkMusic;
	/**
	 * slider pour volume music
	 */
	private Slider sldMusic;
	/**
	 * checkbox pour voir fps
	 */
	private CheckBox chkShowFpsCounter;
//	private CheckBox chkUseMonochromeShader;

	// server setup
	/**
	 * fenetre pour creation serveur
	 */
	private Window winServer;
	/**
	 * input pour nom serveur
	 */
	private TextField tfServerName;

	// test fenetre
	private Window winTest;

	/**
	 * 
	 * @param myGame
	 */
	public MenuPrincipalScreen(MyGame myGame) {
		super(myGame);
		
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, true);
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);

	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);
		float w = Constants.VIEWPORT_GUI_WIDTH;
		float h =Constants.VIEWPORT_GUI_HEIGHT;

		TextureAtlas atlas = MyGame.manager.get("ui/loading.pack",
				TextureAtlas.class);



		stage.clear();
		// Background initialisation
		stage.addActor(buildBackgroundLayer());
		Stack stack = new Stack();
		stage.addActor(stack);

		stack.setSize(w, h);
		stack.add(buildTitleLayer(atlas));
		stack.add(buildControlLayer(atlas));
		stage.addActor(buildServerSetup(atlas));
		stage.addActor(buildTestsSetup(atlas));
		stage.addActor(buildOptionsWindowLayer());

		AudioManager.instance.play(Gdx.audio.newMusic(Gdx.files
				.internal("sound/CloudTopLoops.mp3")));

	}

	/**
	 * Construction de la fenetre de creation du server
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildTestsSetup(TextureAtlas atlas) {
		winTest = new Window("Test", skin);
		final TextButton testMulti = new TextButton("testMulti", skin);
		final TextButton testMacAddress = new TextButton("testMac", skin);
		final TextButton close = new TextButton("X", skin);
		winTest.getButtonTable().add(close).height(winTest.getPadTop());

		winTest.add(testMulti);
		winTest.row();
		winTest.add(testMacAddress);
		winTest.row();
		winTest.pack();
		winTest.setPosition(
				(float) (Constants.VIEWPORT_GUI_WIDTH / 2 - winServer.getWidth()),
				Constants.VIEWPORT_GUI_HEIGHT / 2);

		close.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				winTest.remove();
				showMenuButtons(true);
			}
		});
		testMulti.addListener(new ChangeListener() {
			public void changed(ChangeEvent arg0, Actor arg1) {
				Effect brulure = Effect.selectEffectFromEffectID(Effect.COMBUSTION);
				Effect gele = Effect.selectEffectFromEffectID(Effect.GEL);
				gele.setPosition(20, 10);
				stage.addActor(brulure);
				stage.addActor(gele);

			}
		});
		testMacAddress.addListener(new ChangeListener() {
			public void changed(ChangeEvent arg0, Actor arg1) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						Joueur a = new Aquamancien();
						a.setName("tot");
						Joueur b = new Necromancien();
						b.setName("aze");
						ArrayList<Joueur> l = new ArrayList<Joueur>();
						l.add(b);
						l.add(a);
						Sauvegarde s = new Sauvegarde(l, 2);
						s.sauvegarder();
						ArrayList<String> saves = Sauvegarde.getAllSave();
						for (String save : saves) {
//							System.err.println(save);
						}
						Sauvegarde u = Sauvegarde.charger(saves.get(0));	
					}
				}).start();
			}
		});
		showTestWindow(false, false);
		return winTest;
	}

	/**
	 * Construction de la fenetre de creation du server
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildServerSetup(TextureAtlas atlas) {
		winServer = new Window("Host a game", skin);
		Label lblServerName = new Label("Serveur name", skin);
		tfServerName = new TextField("", skin);
		final TextButton valider = new TextButton("Valider", skin);
		winServer.add(lblServerName);
		winServer.add(tfServerName);
		winServer.row();
		winServer.add(valider);
		winServer.row();
		winServer.pack();
		winServer.setPosition(
				(float) (Constants.VIEWPORT_GUI_WIDTH / 2 - winServer.getWidth()),
				Constants.VIEWPORT_GUI_HEIGHT / 2);

		valider.addListener(new ChangeListener() {
			public void changed(ChangeEvent arg0, Actor arg1) {
				if (tfServerName.getText().length() > 0) {

					game.androidUI.showAlertBox("Server", "Serveur created",
							"Ok", stage);
					winServer.remove();
					showMenuButtons(true);
				} else {
					game.androidUI.showAlertBox("Server",
							"Error : server's name invalid", "Ok", stage);
				}
			}
		});
		showServerWindow(false, false);
		return winServer;
	}

	/**
	 * creation de l'image du titre
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildTitleLayer(TextureAtlas atlas) {
		Table t = new Table();
		imgTitle = new Image(atlas.findRegion("TitleM4ges"));
		imgTitle.setSize((float) (Constants.VIEWPORT_GUI_WIDTH*0.5), (float) (Constants.VIEWPORT_GUI_HEIGHT*0.35));
		imgTitle.setPosition(Constants.VIEWPORT_GUI_WIDTH / 3 - imgTitle.getWidth(), (float) (Constants.VIEWPORT_GUI_HEIGHT /2 - imgTitle.getHeight()));
		imgTitle.addAction(sequence(Actions.fadeOut(0.0001f),Actions.fadeIn(3f)));
		imgTitle.pack();
		t.add(imgTitle).width((float) (Constants.VIEWPORT_GUI_WIDTH*0.5)).height((float) (Constants.VIEWPORT_GUI_HEIGHT*0.35));
		t.top();
		t.pack();
		return t;
	}

	/**
	 * creation des differents bouton
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildControlLayer(TextureAtlas atlas) {
		Table layer = new Table();

		TextureRegion image = new TextureRegion(
				atlas.findRegion("magic_button2"));
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(image);
		style.font = skin.getFont("default-font");

		// buttons with style
		//		btnMenuHost = buildBtnMenuHost(style);
		btnMenuPlay = buildBtnMenuPlay(style);
		btnMenuDico = buildBtnMenuDico(style);
		btnMenuOptions = buildBtnMenuOption(style);
		//btnMenuTest = buildBtnMenuTest(style);

		layer.add(btnMenuPlay);
		layer.row();
		layer.add(btnMenuDico);
		layer.row();
		layer.add(btnMenuOptions);
		layer.row();
		//layer.add(btnMenuTest).left();
		layer.left();
		layer.padLeft((float) (Constants.VIEWPORT_GUI_WIDTH) / 2
				- image.getRegionWidth() / 2);
		layer.padTop((float) (Constants.VIEWPORT_GUI_HEIGHT) / 2 - layer.getHeight());

		return layer;
	}

	/**
	 * construit le bouton pour jour
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuPlay(TextButtonStyle style) {
		TextButton tbPlay = new TextButton("Créer un perso", style);
		tbPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				goToNewCharacter();
			}
		});
		tbPlay.setPosition((float) (Constants.VIEWPORT_GUI_WIDTH / 2.5),
				Constants.VIEWPORT_GUI_HEIGHT / 6);

		return tbPlay;
	}

	/**
	 * construit le bouton pour affiché l'encyclopedie
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuDico(TextButtonStyle style) {
		TextButton tbDico = new TextButton("Voir l'encyclopedie", style);
		tbDico.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				goToDico();
			}
		});
		tbDico.setPosition((float) (Constants.VIEWPORT_GUI_WIDTH / 2.5),
				Constants.VIEWPORT_GUI_HEIGHT / 6);

		return tbDico;
	}

	/**
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuOption(TextButtonStyle style) {
		TextButton tbOption = new TextButton("Options", style);
		tbOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				loadSettings();
				showMenuButtons(false);
				showOptionsWindow(true, true);
			}
		});
		return tbOption;
	}

	/**
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuTest(TextButtonStyle style) {
		TextButton tbOption = new TextButton("Test", style);
		tbOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showMenuButtons(false);
				showTestWindow(true, true);
			}
		});
		return tbOption;
	}


	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		Image scrollingImage = new Image(atlas.findRegion("Scroll_forest"));
		scrollingImage.setHeight(Constants.VIEWPORT_GUI_HEIGHT);
		scrollingImage.setPosition(0, 0);
		RepeatAction ra = new RepeatAction();
		ra.setAction(sequence(moveTo(0, 0), moveBy((int)(-scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear),
				moveBy((int)(scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear)));
		ra.setCount(RepeatAction.FOREVER);
		scrollingImage.addAction(ra);

		return scrollingImage;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildOptWinAudioSettings() {
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skin, "default-font", Color.ORANGE))
		.colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skin);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skin));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skin);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skin);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skin));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skin);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildOptWinDebug() {
		Table tbl = new Table();
		// + Title: "Debug"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", skin, "default-font", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skin);
		tbl.add(new Label("Show FPS Counter", skin));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		// + Checkbox, "Use Monochrome Shader" label
//		chkUseMonochromeShader = new CheckBox("", skin);
		tbl.add(new Label("Use Monochrome Shader", skin));
//		tbl.add(chkUseMonochromeShader);
		tbl.row();
		return tbl;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildOptWinButtons() {
		Table tbl = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skin);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skin);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", skin);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skin);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildOptionsWindowLayer() {
		winOptions = new Window("Options", skin);
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide options window by default
		showOptionsWindow(false, false);
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to bottom right corner
		winOptions.setPosition(
				Constants.VIEWPORT_GUI_WIDTH / 2 - winOptions.getWidth() - 50, 50);
		return winOptions;
	}

	/**
	 * 
	 */
	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}

	/**
	 * 
	 */
	private void onCancelClicked() {
		showMenuButtons(true);
		showOptionsWindow(false, true);
		AudioManager.instance.onSettingsUpdated();
	}

	/**
	 * 
	 */
	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
//		chkUseMonochromeShader.setChecked(prefs.useMonochromeShader);
	}

	/**
	 * 
	 */
	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
//		prefs.useMonochromeShader = chkUseMonochromeShader.isChecked();
		prefs.save();
	}

	/**
	 * gere l'affichage des boutons du menu principal
	 * 
	 * @param visible
	 */
	private void showMenuButtons(boolean visible) {
		float moveDuration = 1.0f;
		Interpolation moveEasing = Interpolation.swing;
		float delayOptionsButton = 0.25f;

		float moveX = 500 * (visible ? -1 : 1);
		float moveY = 0 * (visible ? -1 : 1);
		final Touchable touchEnabled = visible ? Touchable.enabled
				: Touchable.disabled;
		//		btnMenuHost.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
		btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
		btnMenuDico.addAction(sequence(delay(delayOptionsButton),
				moveBy(moveX, moveY, moveDuration, moveEasing)));
		btnMenuOptions.addAction(sequence(delay(delayOptionsButton * 2),
				moveBy(moveX, moveY, moveDuration, moveEasing)));
//		btnMenuTest.addAction(sequence(delay(delayOptionsButton * 3),moveBy(moveX, moveY, moveDuration, moveEasing)));

		SequenceAction seq = sequence();
		if (visible)
			seq.addAction(delay(delayOptionsButton + moveDuration));
		seq.addAction(run(new Runnable() {
			public void run() {
				//				btnMenuHost.setTouchable(touchEnabled);
				btnMenuPlay.setTouchable(touchEnabled);
				btnMenuDico.setTouchable(touchEnabled);
				btnMenuOptions.setTouchable(touchEnabled);
//				btnMenuTest.setTouchable(touchEnabled);
			}
		}));
		stage.addAction(seq);
	}

	/**
	 * affiche la fenetre d'option
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showOptionsWindow(boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled
				: Touchable.disabled;
		winOptions.addAction(sequence(touchable(touchEnabled),
				alpha(alphaTo, duration)));
	}

	/**
	 * affiche la fenetre de config du serv
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showServerWindow(boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled
				: Touchable.disabled;
		winServer.addAction(sequence(touchable(touchEnabled),
				alpha(alphaTo, duration)));
	}

	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showTestWindow(boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled
				: Touchable.disabled;
		winTest.addAction(sequence(touchable(touchEnabled),
				alpha(alphaTo, duration)));
	}

	/**
	 * 
	 */
	private void goToNewCharacter() {
		GamePreferences prefs = GamePreferences.instance;
		if (!prefs.suc_creerUnePartie) {
			game.androidUI
			.showToast(
					"[succes]: vous avez débloqué le succes creer une partie",
					5, stage);
			prefs.suc_creerUnePartie = true;
			prefs.save();
		}
		AudioManager.instance.stopMusic();
		super.game.changeScreen(MyGame.NEWCHARACTERSCREEN);
	}

	/**
	 * 
	 */
	private void goToDico() {
		AudioManager.instance.stopMusic();
		super.game.changeScreen(MyGame.DICOSCREEN);
	}

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
